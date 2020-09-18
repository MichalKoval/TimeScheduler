package com.mavenproject.timescheduler;


import com.mavenproject.timescheduler.models.Activity;
import com.mavenproject.timescheduler.models.ActivityWithArtificialDeadline;
import com.mavenproject.timescheduler.models.ActivityWithArtificialStartTimeAndLateness;
import com.mavenproject.timescheduler.models.DoNotDisturb;
import com.mavenproject.timescheduler.models.FreeTime;
import com.mavenproject.timescheduler.models.SchoolActivity;
import com.mavenproject.timescheduler.models.SportActivity;
import static java.lang.Long.max;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Trieda reprezentuje rozvrhovac aktivit, ktory sa na zaklade predlozenych dat
 * pokusi roztriedit nerozvrhnute aktivity medzi volne casove useky v rozvrhu, ak to samozrejme bude mozne.
 */
public class ScheduleActivities {
    
    /**
     * Podtrieda pouzita pre reprezentovanie volneho casoveho useku v rozvrhu, ktory pozostava z pociatocneho casu a dlzky trvania
     */
    private class StartDateTimeAndDuration {
        public LocalDateTime startDateTime = null;
        public long duration = 0L;

        public StartDateTimeAndDuration(LocalDateTime startDateTime, long duration) {
            this.startDateTime = startDateTime;
            this.duration = duration;
        }
    }
    
    private ActivitiesManager activitiesManager;
    private List<Activity> timeFixedActivities;
    private List<Activity> activitiesToSchedule;
    private List<ActivityWithArtificialDeadline> activitiesWithArtificialDeadlines;
    private List<ActivityWithArtificialStartTimeAndLateness> activitiesWithArtificialStartTimesAndLateness;
    //private List<Activity> activitiesToScheduleToExclude;
    private List<Activity> doNotDisturbActivities;
    
    /**
     * Pomocna metoda pre metody: sumDoNotDisturbZones_FromNowTillDeadline() a sumFixedActivities_FromNowTillDeadline()
     * Metoda vracia sucet casu, ktory zaberaju aktivity v danom casovom intervale, ratame aj aktivity v okoli (lavom a pravom) intervalu, ktore maju presah do intervalu. Ratame len ich presah.
     * 
     * @param starDateTime pociatocny cas intervalu
     * @param endDateTime koncovy cas intervalu
     * @param activities aktivity, v ktorych hladame danu sumu trvani aktivit vramci casvoveho intervalu
     * @return celkova suma trvani aktivit v presne zadanom intervale
     */
    private long sumDurationOfActivitiesInTimeInterval(LocalDateTime starDateTime, LocalDateTime endDateTime, List<Activity> activities) {
        //Najdeme activity v okoli lavej strany intervalu, a spocitame max presah do intervalu
        List<Activity> activitiesAroundLocalDateTimeNow = activities.stream()
                .filter(a -> a.getStartDT().isBefore(starDateTime) && a.getEndDT().isAfter(starDateTime))
                .collect(Collectors.toList());
        // Najdeme v nich najvacsi presah. Teda presah nejakej aktivity, ktory je najvacsi od aktualneho casu(== DateTime.now UTC)
        
        long maxFromTheBegining = activitiesAroundLocalDateTimeNow.stream()
                .collect(Collectors.summarizingLong(a -> Math.abs(TimeUnit.SECONDS.toMinutes(((Duration)Duration.between(a.getEndDT(), starDateTime)).getSeconds()))))
                .getMax();
        
        if (maxFromTheBegining == Long.MIN_VALUE) {
            maxFromTheBegining = 0;
        }
        
        // Potom najdeme aktivity, ktore zacinaju az po aktualnom case(== DateTime.now UTC), ale maju koniec pred deadlinom
        List<Activity> activitiesInBetweenInterval = activities.stream()
                .filter(a -> a.getStartDT().isAfter(starDateTime))
                .collect(Collectors.toList());
        // Scitame ich
        long activitiesInBetweenDuration = activitiesInBetweenInterval.stream()
                .mapToLong(Activity::getDuration)
                .sum();
        
        
        // Najdeme aktivity, ktore prebiehaju v okoli deadline
        List<Activity> activitiesAroundDeadline = activities.stream()
                .filter(a -> a.getStartDT().isBefore(endDateTime) && a.getEndDT().isAfter(endDateTime))
                .collect(Collectors.toList());
        // Najdeme v nich najvacsi zaporny presah. Teda aktivitu, ktora zacala najskor pred deadlinom ale pokracuje este po nom, a chceme vediet kolko max uplynulo z aktivity pred deadlinom
        long maxAtTheEnd = activitiesAroundDeadline.stream()
                .collect(Collectors.summarizingLong(a -> Math.abs(TimeUnit.SECONDS.toMinutes(
                        ((Duration)Duration.between(
                                endDateTime.plusMinutes(a.getDuration()),
                                a.getEndDT()
                            )
                        ).getSeconds()))
                    )
                )
                .getMax();
        
        if (maxAtTheEnd == Long.MIN_VALUE) {
            maxAtTheEnd = 0;
        }

        // Vratime sucet casu, ktory zaberaju aktivity v nadom casovo intervale, ratame aj aktivity v okoli (lavom a pravom) intervalu, ktore maju presah do intervalu
        return maxFromTheBegining + activitiesInBetweenDuration + maxAtTheEnd;
    }
    
    /**
     * Metoda vracia sumu casu, ktory zaberaju Aktivity nerusenia, od aktualneho casu (== DateTime.now UTC) po nejaky deadline
     * @param localDateTimeNow aktualny cas
     * @param deadline koniec casoveho intervalu v buducnosti
     * @return Metoda vracia sucet casu, ktory zaberaju aktivity nerusenia v danom casovom intervale, ratame aj aktivity v okoli (lavom a pravom) intervalu, ktore maju presah do intervalu. Ratame len ich presah.
     */
    private long sumDoNotDisturbZones_FromNowTillDeadline(LocalDateTime localDateTimeNow, LocalDateTime deadline) {
        //Budu vratene aj casove useky(aktivity) v okoli hranic intervalu, ktore mali nejaky presah do intervalu
        doNotDisturbActivities = activitiesManager.getDoNotDisturbActivities(localDateTimeNow, deadline);
        
        return sumDurationOfActivitiesInTimeInterval(localDateTimeNow, deadline, doNotDisturbActivities);
    }
    
    /**
     * Metoda vracia sumu casu, ktory zaberaju Aktivity pevne urcene v rozvrhu, od aktualneho casu (== DateTime.now UTC) po nejaky deadline
     * @param localDateTimeNow aktualny cas
     * @param deadline koniec casoveho intervalu v buducnosti
     * @return Metoda vracia sucet casu, ktory zaberaju aktivity v danom casovom intervale, ratame aj aktivity v okoli (lavom a pravom) intervalu, ktore maju presah do intervalu. Ratame len ich presah.
     */
    private long sumFixedActivities_FromNowTillDeadline(LocalDateTime localDateTimeNow, LocalDateTime deadline) {
        // Fixed aktivity su uz predzoradene vzostupne podla zaciatku aktivity
        
        return sumDurationOfActivitiesInTimeInterval(localDateTimeNow, deadline, timeFixedActivities);
    }
    
    /**
     * Metoda zisti, ci nie je medzi pevne zvolenymi aktivitami a aktivitami nerusenia nejaky casovy prienik.
     * Kedze aktivity nerusenia sa opakuju periodicky kazdy den, alebo tyzden, tak metoda skontroluje iba casovy usek
     * od aktualneho casu(== DateTime.now UTC) po poslednu pevne zvolenu aktivitu
     *
     * @return true ak sa nasiel prenik.
     */
    private boolean check_FixedActivities_And_DoNotDisturbZones_ForOverlap(LocalDateTime localDateTimeNow) {
        // Aktivity s pevne zvolenym casom su uz zoradene
        // Zistime casovy koniec poslednej aktivity v rozvrhu
        Activity lastActivity;
        if (timeFixedActivities == null || timeFixedActivities.isEmpty()) { return false; }
        if ((lastActivity = timeFixedActivities.get(timeFixedActivities.size() - 1)) == null) { return false; }
        
        LocalDateTime lastActivityEndDateTime = lastActivity.getEndDT();
        
        //Ak je casova hodnota poslednej najmladsej aktivity starsia ako aktualny cas, niet s cim porovnavat DoNotDisturb Aktivity a teda nedojde ku kolizii
        if (lastActivityEndDateTime.isBefore(localDateTimeNow)) { return false; }
        
        List<Activity> activitiesToCheck = new ArrayList<>();
        
        //DoNotDisturb aktivity, budu vratene aj casove useky(aktivity) v okoli hranic intervalu, ktore mali nejaky presah do intervalu
        activitiesToCheck.addAll(activitiesManager.getDoNotDisturbActivities(localDateTimeNow, lastActivityEndDateTime));

        //Najdeme activity v okoli lavej strany
        activitiesToCheck.addAll(timeFixedActivities.stream()
                .filter(a -> a.getStartDT().isBefore(localDateTimeNow) && a.getEndDT().isAfter(localDateTimeNow))
                .collect(Collectors.toList()));
        
        //Najdeme vsetky ostatne aktivity az po poslednu pevne zvolenu aktivitu v buducnosti
        activitiesToCheck.addAll(timeFixedActivities.stream()
                .filter(a -> a.getStartDT().isAfter(localDateTimeNow))
                .collect(Collectors.toList()));
        
        //Zoradime vsetky aktivity a zistime ci doslo ku kolizii
        activitiesToCheck.sort(Comparator.comparing(Activity::getStartDT));
        
        for (int i = 0; i < (activitiesToCheck.size() - 1); i++) {
            if (compareTwoActivitiesForOverlap(activitiesToCheck.get(i), activitiesToCheck.get(i + 1))) {
                // Nasiel sa prienik
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Metoda zisti ci je dvojica aktivit v casovom prieniku
     * @param a1 aktivita ocakavana ako casovo starsia
     * @param a2 aktivita ocakavana ako casovo novsia
     * @return true ak je a1 v prieniku z a2 
     */
    private boolean compareTwoActivitiesForOverlap(Activity a1, Activity a2) {
        LocalDateTime a1StartTime = a1.getStartDT();
        LocalDateTime a1EndTime = a1.getEndDT();
        
        LocalDateTime a2StartTime = a2.getStartDT();
        LocalDateTime a2EndTime = a2.getEndDT();
        
        return (a1StartTime.isBefore(a2EndTime) && a1EndTime.isAfter(a2StartTime));
    }
    
    /**
     * Metoda overi, ze aktivity na zaradenie neobsahuju:
     * - Aktivity, ktorych deadline konci pred aktualnym casom (== DateTime.now UTC)
     * - Aktivity, ktorych dlzka splnenia od aktualneho casu (== DateTime.now UTC) presahuje ich deadline
     * 
     * @param localDateTimeNow parameter obsahuju aktualny cas (== DateTime.now UTC)
     * @return boolean ak nenastala chyba == true
     */
    private boolean check_ActivitiesToSchedule_ForDeadlines(LocalDateTime localDateTimeNow) {
        //Zistime ci aktivity na zaradenie do rozvrhu nepresehaju celkovy dostupny cas na zaklade ich deadlinov
        
        List<Activity> activitiesNotFitting = new ArrayList<>();
        
        for (Activity activity : activitiesToSchedule) {
            //Prepokladame ze aktivity su tiez ulozene uz v UTC
            
            // Aktivita, ktorej deadline konci pred aktualnym casom == DateTime.now UTC
            if (activity.getDeadline().isBefore(localDateTimeNow)) {
                activitiesNotFitting.add(activity);
                continue;
            }
            
            // Aktivita, ktorej dlzka splnenia od aktualneho casu presahuje jej deadline
            if ( (localDateTimeNow.plusMinutes(activity.getDuration())).isAfter(activity.getDeadline())) {
                activitiesNotFitting.add(activity);
            }
        }
        
        return activitiesNotFitting.isEmpty();
            
    }
    
    /** 
     * Metoda zisti, ci je mozne kazdu z aktivit pridat do rozvrhu tak,
     * aby platilo: ((trvanie pridavanej aktvity) + (trvanie inych aktivit do deadlinu pridavanej aktivity)) je mensi alebo rovny ako (deadline pridavanej aktivity)
     * Ak to plati, metoda prida aktivity do docasneho rozvrhu vytvoreneho na zaklade dostupnych miest v povodnom rozvrhu.
     * 
     * @param localDateTimeNow hodnota aktualneho casu, kedy bude nova aktivita pridavana
     * @return true, ak je aktvity mozne vtesnat pomedzi ine do ich deadlinov
     */
    private boolean check_ActivitiesToSchedule_ToFitBetweenOtherActivitiesTillTheirDeadlines(LocalDateTime localDateTimeNow) {
        
        LocalDateTime activityDeadline;
        long activityDuration;
        activitiesWithArtificialDeadlines = new ArrayList<>();
        
        for (Activity activity : activitiesToSchedule) {
            activityDuration = activity.getDuration();
            activityDeadline = activity.getDeadline();
            
            //Rata sa presnostou na minuty
            long possibleAvailableTime = Math.abs(TimeUnit.SECONDS.toMinutes(((Duration)Duration.between(activityDeadline, localDateTimeNow)).getSeconds()));
            
            long availableTimeForActivity = possibleAvailableTime - 
                    (sumFixedActivities_FromNowTillDeadline(localDateTimeNow, activityDeadline) +
                     sumDoNotDisturbZones_FromNowTillDeadline(localDateTimeNow, activityDeadline));
            
            if (availableTimeForActivity < activityDuration) {
                activitiesWithArtificialDeadlines = null;
                return false;
            } else {
                //Pridame umelo vytvoreny deadline a aktivitu do zoznamu pre neskorsie rozvrhnutie
                activitiesWithArtificialDeadlines.add(new ActivityWithArtificialDeadline(activity, availableTimeForActivity));
            }
        }
        
        return true;
    }
    
    /**
     * Metoda nacita potrebne data za pomoci spravcu dat ActivitiesManager, pre nasledne spracovanie rozvrhovacom
     */
    private void initialize() {
        timeFixedActivities = activitiesManager.getFixedActivities();
        activitiesToSchedule = activitiesManager.getActivitiesToSchedule();
        activitiesWithArtificialDeadlines = null;
        activitiesWithArtificialStartTimesAndLateness = null;
    }
    
    /**
     * Zakladny konstruktor pre rozvrhovac aktivit
     */
    public ScheduleActivities() {
    }

    /**
     * Konstruktor nastavuje spravcu dat s aktivitami nacitanymi zo suboru, alebo zo vstupu uzivatela
     * @param activitiesManager Spravda dat
     */
    public ScheduleActivities(ActivitiesManager activitiesManager) {
        this.activitiesManager = activitiesManager;
        initialize();
    }

    /**
     * Metoda nastavi spravcu dat
     * @param activitiesManager Spravca dat
     */
    public void setActivitiesManager (ActivitiesManager activitiesManager) {
        this.activitiesManager = activitiesManager;
        initialize();
    }
    
    /**
     * Metoda sa pokusi roztriedit aktivity pomocou ich umelych deadlinov == umele casove useky, do ktorych sa musia vtesnat aktivity
     * Tieto umele casove useky spolu zdielaju vsetky aktivity.
     * Pociatocny cas je 0. Od casu 0 tu mame pre kazdu aktivitu ich umely cas na splnenie.
     * Tento umely cas mozu zdielat spolu s inym aktivitami ak to bude mozne.
     * 
     * @return list ziaciatkov rozvrhnutych aktivit
     */
    private boolean scheduleActivitiesBasedOnArtificialDeadlines() {
        // Zoradime umele deadliny aktivit vzostupne (d1 <= d2 <= d3 <= ... <= dn)
        activitiesWithArtificialDeadlines.sort(Comparator.comparing(ActivityWithArtificialDeadline::getArtificialDeadline));
        
        activitiesWithArtificialStartTimesAndLateness = new ArrayList<>();
        
        long previousActivityFinishTime = 0;
        
        boolean scheduled = true;
       
        // Algoritmus: Greedy Schedule for Minimizing Lateness
        
        for (int i = 0; i < activitiesWithArtificialDeadlines.size(); i++) {
            Activity activity = activitiesWithArtificialDeadlines.get(i).activity;
            long artificialDeadline  = activitiesWithArtificialDeadlines.get(i).artificialDeadline;
            
            activitiesWithArtificialStartTimesAndLateness.add(
                    // Je potrebne vytvorit novu kopiu danej aktivity, aby neostala referencia na tu staru.
                    // Ak by ostala referencia na strau zmenilo by to hodnoty strarych aktivit
                    // My ale chceme nove
                    new ActivityWithArtificialStartTimeAndLateness(createCopyOfActivity(activity), previousActivityFinishTime)
            );
            
            // Vyratame zaciatok dalsej aktivity
            previousActivityFinishTime = 
                    activitiesWithArtificialStartTimesAndLateness.get(i).artificialStartTime +
                    activity.getDuration();
            
            // Spocitame oneskorenie od umeleho deadlinu
            activitiesWithArtificialStartTimesAndLateness.get(i).lateness = max(0, previousActivityFinishTime - artificialDeadline);
            
            if (activitiesWithArtificialStartTimesAndLateness.get(i).lateness > 0) {
                scheduled = false;
                
                //Debug nechame cyklus dobehnut, chceme vidiet oneskorenie od deadlinov
            }
        }
        
        return scheduled;
        
    }
    
    /**
     * Metoda nastavi nove zaciatocne casy pre novorozvrhnute aktivity na zaklade aktualneho casu a noveho umeleho casu.
     * Presnejsie: acitivity.startDT = localDateTimeNow + artificialStartTime;
     * @param localDateTimeNow aktualny cas, ku ktoremu sa prirata umely cas
     */
    private void setStartDateTimesBasedOnScheduling(LocalDateTime localDateTimeNow) {
        
        activitiesWithArtificialStartTimesAndLateness.forEach((awastal) -> {
            awastal.activity.setStartDT(localDateTimeNow.plusMinutes(awastal.artificialStartTime));
        });
        
        
    }
    
    /**
     * Metoda rozkraja novo rozvrhnute aktivity medzi volne casove useky aktivit, ktore tam uz su
     * 
     * @param localDateTimeNow aktualny cas
     * @return vracia rozkrajane aktivity, teda vysledny List bude obsahovat viacere kopie aktivit,
     *         ale vramci jednej aktivity bude kazda z kopii bude po sebe hierachicky nasledovat. 
     */
    private List<Activity> sliceScheduledActivitiesToFitSchedule(LocalDateTime localDateTimeNow) {
        Deque<StartDateTimeAndDuration> startDateTimesAndDurations = getTimeLengthsBetween_Fixed_And_DoNotDisturbActivities(localDateTimeNow);
        
        List<Activity> slicedActivities = new ArrayList<>();
        
        for (ActivityWithArtificialStartTimeAndLateness awastal : activitiesWithArtificialStartTimesAndLateness) {
            long availableActivityDuration = awastal.activity.getDuration();
            
            while (availableActivityDuration > 0) {                
                //Vytvorime kopiu aktivity
                StartDateTimeAndDuration startDateTimeAndDuration = null;
                
                if (!startDateTimesAndDurations.isEmpty()) {
                    startDateTimeAndDuration = startDateTimesAndDurations.removeFirst();
                } else {
                    break;
                }
                
                //Zrusime deadline u danej aktivity, kedze je uz rozvrhnuta
                awastal.activity.setDeadline(null);
                Activity newCopiedActivity = createCopyOfActivity(awastal.activity);
                if (availableActivityDuration > startDateTimeAndDuration.duration) {
                    
                    newCopiedActivity.setStartDT(startDateTimeAndDuration.startDateTime);
                    newCopiedActivity.setDuration(startDateTimeAndDuration.duration);
                    availableActivityDuration -= startDateTimeAndDuration.duration;
                    
                    slicedActivities.add(newCopiedActivity);
                } else {
                    //Vieme, ze availableActivityDuration > 0, kedze sme v cykle
                    newCopiedActivity.setStartDT(startDateTimeAndDuration.startDateTime);
                    newCopiedActivity.setDuration(availableActivityDuration);
                    
                    //Zvysilo nam miesto, ktore vratime pre dalsie aktivity na krajanie
                    startDateTimeAndDuration.startDateTime = startDateTimeAndDuration.startDateTime.plusMinutes(availableActivityDuration);
                    startDateTimeAndDuration.duration -= availableActivityDuration;
                    startDateTimesAndDurations.addFirst(startDateTimeAndDuration);
                    
                    slicedActivities.add(newCopiedActivity);
                    break;
                }
            }
        }
        
        return slicedActivities;
    }
    
    /**
     * Metoda vyrata volne casove useky medzi aktivitami typu: aktivita s pevnym casom a aktivita nerusenia
     * Metodu ide pouzit az po overeni ze aktivity sa neprekryvaju, pomocou check_FixedActivities_And_DoNotDisturbZones_ForOverlap() metody
     * 
     * @param localDateTimeNow aktualny cas
     * @return vratime pole casovych dlzok, reprezentujucich volne casove useky, medzi rozvrhnutymi aktivitami 
     */
    private Deque<StartDateTimeAndDuration> getTimeLengthsBetween_Fixed_And_DoNotDisturbActivities(LocalDateTime localDateTimeNow) {
        //Potrebujeme ziskat maximalny deadline(tento krat nie umely deadline) novorozvrhnutych aktivit
        LocalDateTime latestScheduledActivityDeadline = localDateTimeNow;
        
        for (ActivityWithArtificialStartTimeAndLateness awastal : activitiesWithArtificialStartTimesAndLateness) {
            if (awastal.activity.getDeadline().isAfter(latestScheduledActivityDeadline)) {
                latestScheduledActivityDeadline = awastal.activity.getDeadline();
            }
        }
        
        //Musi byt final alebo effectively final, ked sa pouzije v lambda vyraze
        final LocalDateTime latestDeadline = latestScheduledActivityDeadline;
        
        // Ziskame DoNotDisturb aktivity po najvzdialenejsi deadline,
        // ak nie je novsi deadline ako aktualny cas, tak sa nevrati nic
        List<Activity> currentActivities = activitiesManager.getDoNotDisturbActivities(localDateTimeNow, latestScheduledActivityDeadline);
        
        // Pridame uz stale aktivity s pevnym casom
        //Najdeme activity v okoli lavej strany
        currentActivities.addAll(timeFixedActivities.stream()
                .filter(a -> a.getStartDT().isBefore(localDateTimeNow) && a.getEndDT().isAfter(localDateTimeNow))
                .collect(Collectors.toList()));
        
        //Najdeme vsetky ostatne aktivity az po poslednu pevne zvolenu aktivitu pred okolim latestScheduledActivityDeadline
        currentActivities.addAll(timeFixedActivities.stream()
                .filter(a -> a.getStartDT().isAfter(localDateTimeNow) && a.getStartDT().isBefore(latestDeadline))
                .collect(Collectors.toList()));
        
        // Najdeme aktivity, ktore prebiehaju v okoli latestScheduledActivityDeadline
        currentActivities.addAll(timeFixedActivities.stream()
                .filter(a -> a.getStartDT().isBefore(latestDeadline) && a.getEndDT().isAfter(latestDeadline))
                .collect(Collectors.toList()));
        
        //Zoradime vsetky aktivity 
        currentActivities.sort(Comparator.comparing(Activity::getStartDT));
        
        Deque<StartDateTimeAndDuration> startTimesAndDurations = new ArrayDeque<>(); 
        for (int i = 0; i < (currentActivities.size() - 1); i++) {
            Activity firstActivity = currentActivities.get(i);
            Activity secondActivity = currentActivities.get(i + 1);
            startTimesAndDurations.addLast(
                    new StartDateTimeAndDuration(
                            firstActivity.getEndDT(),
                            getTimeDifferenceBetweenTwoActivities(firstActivity, secondActivity)
                    )
            );
        }
        
        return startTimesAndDurations;
    }
    
    private long getTimeDifferenceBetweenTwoActivities(Activity a1, Activity a2) {
        return Math.abs(TimeUnit.SECONDS.toMinutes(((Duration)Duration.between(a1.getEndDT(), a2.getStartDT())).getSeconds()));
    }
    
    /**
     * Metoda vrati novu aktivitu s presne prednastavenymi hodnotami ako povodna aktivita
     * 
     * @return 
     */
    private Activity createCopyOfActivity(Activity activity) {
        Activity newActivity;
        
        if (activity instanceof SchoolActivity) {
            newActivity = new SchoolActivity();
            
            ((SchoolActivity)newActivity).setSchoolActivityType(((SchoolActivity)activity).getSchoolActivityType());
            ((SchoolActivity)newActivity).setName(((SchoolActivity)activity).getName());
            ((SchoolActivity)newActivity).setDescription(((SchoolActivity)activity).getDescription());
            ((SchoolActivity)newActivity).setLocation(((SchoolActivity)activity).getLocation());
            ((SchoolActivity)newActivity).setStartDT(((SchoolActivity)activity).getStartDT());
            ((SchoolActivity)newActivity).setDuration(((SchoolActivity)activity).getDuration());
            ((SchoolActivity)newActivity).setDeadline(((SchoolActivity)activity).getDeadline());
            
            return newActivity;
            
        } else if (activity instanceof SportActivity) {
            newActivity = new SportActivity();
            
            ((SportActivity)newActivity).setSportActivityType(((SportActivity)activity).getSportActivityType());
            ((SportActivity)newActivity).setName(((SportActivity)activity).getName());
            ((SportActivity)newActivity).setDescription(((SportActivity)activity).getDescription());
            ((SportActivity)newActivity).setLocation(((SportActivity)activity).getLocation());
            ((SportActivity)newActivity).setStartDT(((SportActivity)activity).getStartDT());
            ((SportActivity)newActivity).setDuration(((SportActivity)activity).getDuration());
            
            return newActivity;
            
        } else if (activity instanceof FreeTime) {
            newActivity = new FreeTime();
            
            ((FreeTime)newActivity).setName(((FreeTime)activity).getName());
            ((FreeTime)newActivity).setDescription(((FreeTime)activity).getDescription());
            ((FreeTime)newActivity).setLocation(((FreeTime)activity).getLocation());
            ((FreeTime)newActivity).setStartDT(((FreeTime)activity).getStartDT());
            ((FreeTime)newActivity).setDuration(((FreeTime)activity).getDuration());
            
            return newActivity;
            
        } else if (activity instanceof DoNotDisturb) {
            newActivity = new DoNotDisturb();
            
            ((DoNotDisturb)newActivity).setRepeat(((DoNotDisturb)activity).getRepeat());
            ((DoNotDisturb)newActivity).setEvent(((DoNotDisturb)activity).getEvent());
            ((DoNotDisturb)newActivity).setStartT(((DoNotDisturb)activity).getStartT());
            ((DoNotDisturb)newActivity).setDuration(((DoNotDisturb)activity).getDuration());
            
            return newActivity;
            
        } else {
            return null;
        }
    }
    
    /**
     * Metoda prida rozvrhnute aktivity medzi ostatne uz rozvrhnute
     * 
     * @param localDateTimeNow aktualny cas
     */
    private void addScheduledActivitiesToActivitiesData(LocalDateTime localDateTimeNow) {
        // Spravcovi dat oznamime, ze aktivity, ktore ideme pridat su uz rozvrhnute
        activitiesManager.setSchedulingWasSuccessful(true);
        
        // Predtym, ale este musime jednotlive activity nakrajat, aby vyplnili volne medzery v rozvrhu
        // Kedze sa nam ich podarilo rozvrhnut, bude nimi mozne vyplnit medzery
        activitiesManager.addScheduledActivities(sliceScheduledActivitiesToFitSchedule(localDateTimeNow));
    }
    
    /**
     * Metoda skontroluje ci data na rozvrhnutie nie su vsetky stare
     * 
     * @return true, ak su rozvrhnute data stare;
     */
    private boolean checkForOldData(LocalDateTime localDateTimeNow) {
        // Prepokladame ze aktivity s pevnym casom su zoradene vzostupne
        Activity lastActivity = timeFixedActivities.get(timeFixedActivities.size() - 1);
        LocalDateTime lastActivityEndDateTime = lastActivity.getEndDT();
        
        return (lastActivityEndDateTime.isBefore(localDateTimeNow));
    }
    
    /**
     * Metoda sa pokusi rozvrhnut(zahrnut) aktivity do rozvrhu.
     * Ak nastane niektora z moznych situacii:
     *  - prekryvanie sa aktivit,
     *  - nedostatok casu pre splnenie do deadlinu,
     *  - ziaden dalsi volny dostupny casovy usek,
     * bude vrateny textovy retazec s chybou.
     * 
     * @return Ak je string == null, rozvrhovanie prebehlo vporiadku, inac bude vratena chyba v retazci
     */
    public String TryToSchedule() {
        //Predchystame si data na spracovanie, ak sme predtym uz data spracovavali
        initialize();
        
        //Cas, kedy bol spusteny scheduling;
        LocalDateTime dateTimeNow = LocalDateTime.now(Clock.systemUTC());
        
        //Ak neexistuju ziadne aktivity pre rozvrhnutie, nebude pokracovat v algoritme, iba zistime ci sa pevne caovo zvolene aktivity neprekryvaju
        if (activitiesToSchedule.isEmpty()) {
            if (check_FixedActivities_And_DoNotDisturbZones_ForOverlap(dateTimeNow)) {
                return "Aktivity s pevne zvolenym datumom zaciatku a dlzkou\ntrvania sa prekryvaju. (Vratane aktivit nerusenia)";
            }
            
            return null;
        }
        
        if (!check_FixedActivities_And_DoNotDisturbZones_ForOverlap(dateTimeNow)) {
            
            // Zistime ci aktivitam uz nevyprsal deadline, popripade ci je mozne ich este zaradit do rozvrhu, aby sa stihol deadline danej aktivity
            if (check_ActivitiesToSchedule_ForDeadlines(dateTimeNow)) {
                
                // Skontrolujeme, ci je aktivity mozne vtesnat pomedzi aktivity s pevnym casom a aktivitami nerusenia na zaklade ich deadlinov
                // Resp. mame aktivitu, jej trvanie a deadline, a chceme vediet ci uz rozvrhnute aktivity neovplyvnia casovo trvanie pridavanej aktivity
                // Resp. pridavana aktivita ma svoje trvanie a to musi byt stihnute do deadlinu, co ak ale do deadlinu aktivity mame v rozvrhu aj nieco ine a cas na dopracovanie nezostane.
                // Resp. (trvanie aktvity) + (trvanie inych aktivit do rovnakeho deadlinu) < deadline
                if (check_ActivitiesToSchedule_ToFitBetweenOtherActivitiesTillTheirDeadlines(dateTimeNow)) {
                    
                    // Teraz je mozne pokusit sa aktivity rozvrhnut na zaklade ich deadlinov a trvani
                    // Deadliny aktivit v tomto pripade budu reprezentovane umelym deadlinom, teda iba casovou dlzkou v minutach dostupnou pre aktivitu
                    // Tieto umele casove dlzky porovname a na zaklade toho rozvrhneme aktivity
                    // Kazda aktivita teda bude mat svoje trvanie, a umely cas na splnenie
                    // Ak tieto umele casy pojde rozumne vyplnit bez prekryvania, tak mame aktivity rozvrhnute
                    
                    if (scheduleActivitiesBasedOnArtificialDeadlines()) {
                        // Nastavime nove ziaciatocne casy pre pridavane aktivity, na zaklade predratanych umelych hodnot
                        setStartDateTimesBasedOnScheduling(dateTimeNow);
                        addScheduledActivitiesToActivitiesData(dateTimeNow);
                        
                        // Aktualizujeme spravcu notifikacii, ak bola pozastavena
                        // Ziskame spravcu notifikacii
                        NotificationHandler currNotificationHandler = activitiesManager.getNotificationHandler();
                        if (currNotificationHandler.isNotificationSupported() && !currNotificationHandler.isNotificationRunning()) {
                            currNotificationHandler.startNotifications();
                        }
                        
                        // Podarilo sa, nevraciame ziadnu spravu o chybe
                        return null;
                    } else {
                        return "Aktivity nepojde rozumne rozvrhnut.";
                    }
                } else {
                    return "Pridane aktivity nie je mozne rozvrhnut\ndo jestvujuceho rozvrhu.";
                }
                
            } else
            {
                return "Niektore aktivity pre zaradenie do rozvrhu\nnestihnu splnit deadline na zaklade ich dlzky trvania.\nAlebo ide o aktivity, ktorych deadline uz vyprsal";
            }
        } else {
            return "Aktivity s pevne zvolenym datumom zaciatku a dlzkou\ntrvania sa prekryvaju. (Vratane aktivit nerusenia)";
        }
    }
}