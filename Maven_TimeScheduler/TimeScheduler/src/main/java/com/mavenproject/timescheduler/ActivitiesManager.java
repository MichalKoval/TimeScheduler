package com.mavenproject.timescheduler;

import com.mavenproject.timescheduler.data.ActivitiesData;
import com.mavenproject.timescheduler.data.SettingsData;
import com.mavenproject.timescheduler.models.Activity;
import com.mavenproject.timescheduler.models.DoNotDisturb;
import com.mavenproject.timescheduler.models.FreeTime;
import com.mavenproject.timescheduler.models.SchoolActivity;
import com.mavenproject.timescheduler.models.SportActivity;
import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Trieda spracuvava data o aktivitach. Pracuje s datami nacitanymi zo suboru, ale aj s datami pridanymi uzivatelom
 * 
 */
public class ActivitiesManager {

    private ActivitiesData activitiesData;
    private SettingsData settingsData;
    private ScheduleActivities scheduler;
    private NotificationHandler notificationHandler;
    private boolean schedulingWasSuccessful = false;
    
    public ActivitiesManager() {
    }
    
    /**
     * Konstruktor nastavuje data s aktivitami na spracovanie a data s nastaveniami programu
     * @param activitiesData Data s aktivitami
     * @param settingsData Data s nastaveniami programu
     */
    public ActivitiesManager(ActivitiesData activitiesData, SettingsData settingsData) {
        this.activitiesData = activitiesData;
        this.settingsData = settingsData;
    }

    /**
     * Metoda nastavuje data s aktivitami.
     * @param activitiesData datova struktura pre reprezentovanie aktivit
     */
    public void setActivitiesData(ActivitiesData activitiesData) {
        this.activitiesData = activitiesData;
    }
    
    /**
     * Metoda nastavuje rozvrhovac pre data s aktivitami
     * @param scheduler Rozvrhovac aktivit
     */
    public void setScheduler(ScheduleActivities scheduler) {
        this.scheduler = scheduler;
    }

    /** Metoda nastavuje referenciu na zobrazovac notifikacii 
     * 
     * @param notificationHandler Zobrazovac notifikacii
     */
    public void setNotificationHandler(NotificationHandler notificationHandler) {
        this.notificationHandler = notificationHandler;
    }
    
    /**
     * Metoda vracia aktivity, ktore maju pevne urceny zaciatok a dlzku trvania, nemaju ziaden deadline.
     * Ide o aktivity typu: prednaska, cvicenie, telesna vychova, atd. ... (tieto activity su casovo pevne urcene z rozvrhu skoly)
     * Popripade kazde ine aktivity, kde cas bol vopred dohodnuty a nie je mozne vygenerovat ine vhodne terminy
     * Nie su tu zahrnute aktivity, kde si uzivatel nezela byt ruseni.
     * @return List aktivit s pevne urcenym casom (bez deadlinu), zoradene podla zaciatku vzostupne
     */
    public List<Activity> getFixedActivities() {
        return activitiesData.getActivities()
                    .stream()
                    .filter(a -> !(a instanceof DoNotDisturb) && a.isFixedTime())
                    .sorted(Comparator.comparing(Activity::getStartDT))
                    .collect(Collectors.toList());
    }
    
    /**
     * Metoda vracia aktivity, ktore budu neskor rozvrhnute do rozvrhu, podla deadlinov, trvania, atd ...
     * @return List aktivit, pre rozvrhnutie do rozvrhu. List je zoradeny podla deadlinov vzostupne.
     */
    public List<Activity> getActivitiesToSchedule() {
        return activitiesData.getActivities()
                    .stream()
                    .filter(a -> !(a instanceof DoNotDisturb) && !a.isFixedTime())
                    .sorted(Comparator.comparing(Activity::getDeadline))
                    .collect(Collectors.toList());
    }

    /**
     * Metoda vracia casove useky(aktivity) vramci casoveho intervalu, v ktorych si uzivatel nezela, aby boli pridelovane nejake aktivity.
     * Vacsinou ide o spanok alebo volny cas. Prakticky su to aktivity, ktore maju zaciatok a dlzku trvania, bez deadlinu
     * Budu vratene aj casove useky(aktivity) v okoli hranic intervalu, ktore mali nejaky presah do intervalu
     * 
     * @param startDatetime zaciatok casoveho intervalu
     * @param endDatetime koniec casoveho intervalu
     * 
     * @return List casovych usekov(teda aktivit), kedy uzivatel nechce byt ruseni vramci zadaneho casoveho intervalu. Vrati sa prazdny list ak ziadne niesu.
     */
    public List<Activity> getDoNotDisturbActivities(LocalDateTime startDatetime, LocalDateTime endDatetime) {
        //Prv vyhladame aktivity typu DoNotDisturb
        List<DoNotDisturb> doNoDisturbsZones = getDoNotDisturbZones();
        
        //Prekonvertujeme zoznam DoNotDisturb na zoznam aktivit
        List<Activity> newActivities = new ArrayList<>();
        List<LocalDateTime> dateTimeBeginnings;
        for (DoNotDisturb doNoDisturbsZone : doNoDisturbsZones) {
            if (doNoDisturbsZone.getRepeat() == DoNotDisturb.Repeat.DAILY) {
                dateTimeBeginnings = getDateTimesForDays(doNoDisturbsZone.getStartT(), startDatetime, endDatetime);
            } else {
                DayOfWeek dayOfWeek = null;
                switch (doNoDisturbsZone.getRepeat()) {
                    case MONDAY: dayOfWeek = DayOfWeek.MONDAY; break;
                    case TUESDAY: dayOfWeek = DayOfWeek.TUESDAY; break;
                    case WEDNESDAY: dayOfWeek = DayOfWeek.WEDNESDAY; break;
                    case THURSDAY: dayOfWeek = DayOfWeek.THURSDAY; break;
                    case FRIDAY: dayOfWeek = DayOfWeek.FRIDAY; break;
                    case SATURDAY: dayOfWeek = DayOfWeek.SATURDAY; break;
                    case SUNDAY: dayOfWeek = DayOfWeek.SUNDAY; break;
                }
                
                dateTimeBeginnings = getDateTimesForDays(doNoDisturbsZone.getStartT(), startDatetime, endDatetime, dayOfWeek);
            }
            
            DoNotDisturb doNotDisturbActivity;
            for (LocalDateTime dateTimeBeginning : dateTimeBeginnings) {
                doNotDisturbActivity = new DoNotDisturb();
                doNotDisturbActivity.setEvent(doNoDisturbsZone.getEvent());
                doNotDisturbActivity.setStartDT(dateTimeBeginning);
                doNotDisturbActivity.setDuration(doNoDisturbsZone.getDuration());
                newActivities.add(doNotDisturbActivity);
            }
        }
        
        return newActivities;
    }
    
    /**
     * Metoda vygeneruje datumy, casy a trvania pre aktivity, ktore sa opakuju kazdy tyzden v nejaky urcity den. (Napr. Kazda streda, obed o 11:45)
     * Je to vymedzene casovym intervalom, v ktorom chceme casove znamky vygenerovat
     * 
     * @param localTime kedy typicky v dany den aktivita zacina
     * @param startDatetime zaciatok intervalu
     * @param endDatetime koniec intervalu
     * @param dayOfWeek den, ktory sa opakuje
     * @return 
     */
    private List<LocalDateTime> getDateTimesForDays(LocalTime localTime, LocalDateTime startDatetime, LocalDateTime endDatetime, DayOfWeek dayOfWeek) {
        List<LocalDateTime> days = new ArrayList<>();
        Boolean dayOccured = false;
        
        startDatetime = LocalDateTime.of(LocalDate.of(startDatetime.getYear(), startDatetime.getMonth(), startDatetime.getDayOfMonth()), localTime);
        
        while (startDatetime.isBefore(endDatetime)){
            if ( startDatetime.getDayOfWeek() == dayOfWeek){
                days.add(startDatetime);
                dayOccured = true;
            }
            
            // Potrebujeme sa prv dostat k hladanemu dnu a potom budeme pridavat plus 1 tyzden
            if (dayOccured) {
                startDatetime = startDatetime.plusWeeks(1);
            } else {
                startDatetime = startDatetime.plusDays(1);
            }
        }
        
        return days;
    }
    
    /**
     * Metoda vygeneruje datumy, casy a trvania pre aktivity, ktore sa opakuju kazdy den. (Napr. doba spanku, doba obeda, ...)
     * Je to vymedzene casovym intervalom, v ktorom chceme casove znamky vygenerovat
     * 
     * @param localTime kedy typicky v dany den aktivita zacina
     * @param startDatetime zaciatok intervalu
     * @param endDatetime koniec intervalu
     * @param dayOfWeek den, ktory sa opakuje
     * @return 
     */
    private List<LocalDateTime> getDateTimesForDays(LocalTime localTime, LocalDateTime startDatetime, LocalDateTime endDatetime) {
        List<LocalDateTime> days = new ArrayList<>();
        
        startDatetime = LocalDateTime.of(LocalDate.of(startDatetime.getYear(), startDatetime.getMonth(), startDatetime.getDayOfMonth()), localTime);
        
        while (startDatetime.isBefore(endDatetime)){
            days.add(startDatetime);
            startDatetime = startDatetime.plusDays(1);
        }
        
        return days;
    }
    
    /**
     * Metoda vracia aktivity nerusenia
     * @return 
     */
    private List<DoNotDisturb> getDoNotDisturbZones() {
        return activitiesData.getActivities()
                    .stream()
                    .filter(a -> a instanceof DoNotDisturb)
                    .map(a -> (DoNotDisturb)a)
                    .collect(Collectors.toList());
    }
    
    /**
     * Metoda vrati referenciu na spravcu notifikacii
     * @return Spravca notifikacii
     */
    public NotificationHandler getNotificationHandler() {
        return notificationHandler;
    }
    
    /**
     * Metoda vrati referenciu na rozvrhovac
     * @return Referencia na rozvrhovac aktivit
     */
    public ScheduleActivities getCurrScheduler() {
        return this.scheduler;
    }

    /**
     * Metoda nahradi stare aktivity novymi
     * @param newActivities 
     */
    private void replaceOldActivities(List<Activity> newActivities) {
        this.activitiesData.setActivities(newActivities);
    }
    
    /**
     * Metoda nastavi, ci sa podarilo alebo nepodarilo rozvrhnut aktivity
     * @param schedulingWasSuccessful True ak sa podarilo rozvrhnut aktivity, inak False
     */
    public void setSchedulingWasSuccessful(boolean schedulingWasSuccessful) {
        this.schedulingWasSuccessful = schedulingWasSuccessful;
    }
    
    /**
     * Metoda aktualizuje povodne rozvrhnute aktivity s novorozvrhnutymi aktivitami
     * 
     * @param newActivitiesToAdd novorozvrhnute aktivity
     */
    public void addScheduledActivities(List<Activity> newActivitiesToAdd) {
        // Podarilo sa rozvrhnut aktualizujeme aktivity
        if (schedulingWasSuccessful) {
            // Ziskame uz rozvrhnute aktivity, pomedzi ktore pridame novo rozvrhnute
            List<Activity> activitiesToReplaceOldOnes = getFixedActivities();
                        
            activitiesToReplaceOldOnes.addAll(newActivitiesToAdd);
            
            // Zoradime vzostupne aktivity s pevnym casom
            activitiesToReplaceOldOnes.sort(Comparator.comparing(Activity::getStartDT));
            
            // Pridame este aktivity nerusenia
            activitiesToReplaceOldOnes.addAll(getDoNotDisturbZones());
            
            replaceOldActivities(activitiesToReplaceOldOnes);
            
            // Ulozime aktivity do suboru
            saveActivities();
        }
        
        // Pockame si na dalsie rozvrhovanie
        schedulingWasSuccessful = false;
    }
    
    /**
     * Metoda sa pokusi pridat novu aktivitu ziskanu zo vstupu od uzivatela.
     * Moze ist o aktivitu s pevne urcenym casom, kde metoda skuma kolizie,
     * ale aj o aktivitu s neurcenym presnym zaciatkom, ale s trvanim a deadlinom
     * 
     * @param activity aktivita, ktora sa bude prida a zaradi do rozvrhu, alebo nie
     * @return vracia spravu o chybe, ak sa aktivitu nepodari zaradit do rozvrhu.
     *         Inak vracia null;
     */
    public String tryAddActivity(Activity activity) {
        //Aktualny cas v UTC
        LocalDateTime localDateTimeNow = LocalDateTime.now(Clock.systemUTC());
        
        String errorMsg = null;
        
        if (activity instanceof SchoolActivity) {
            if ((errorMsg = checkSchoolActivityBounds((SchoolActivity) activity, localDateTimeNow)) != null) {
                return errorMsg;
            }
            
        } else if (activity instanceof SportActivity) {
            if ((errorMsg = checkSportActivityBounds((SportActivity) activity, localDateTimeNow)) != null) {
                return errorMsg;
            }
            
        } else if (activity instanceof FreeTime) {
            if ((errorMsg = checkFreeTimeActivityBounds((FreeTime) activity, localDateTimeNow)) != null) {
                return errorMsg;
            }
            
        } else if (activity instanceof DoNotDisturb) {
            if ((errorMsg = checkDoNotDisturbActivityBounds((DoNotDisturb) activity)) != null) {
                return errorMsg;
            }
            
        }
        
        this.activitiesData.add(activity);
        
        // Spustime scheduler, a dostaneme odpoved na to, ci sa podarilo zaradit novu aktivitu
        
        if ((errorMsg = scheduler.TryToSchedule()) != null) {
            this.activitiesData.getActivities().remove(activity);
        }
        return errorMsg; 
    }
    
    /**
     * Metoda overi udaje ulozene v skolskej aktivite, ci nie je nieco mimo medzu.
     * @param schoolActivity instancia skolskej aktivity pre overenie
     * @return true, ak je skolska aktivita vporiadku
     */
    private String checkSchoolActivityBounds(SchoolActivity schoolActivity, LocalDateTime localDateTimeNow) {
        //Skolska aktivita nesmie mat nastaveny zaciatok a zaroven deadline.
        //Kedze by to nedavalo zmysel, ak ma skolska aktivita nejaku dobu trvania.
        //Aj z hladiska rozvrhovania chceme rozlisit akvity, ktore su rozvrhnute s pevnym zaciatkom konania a dlzkou konania.
        //Alebo chceme aktivity, ktore maju nejaku dlzku trvania a deadline a chceme k tomu najst zaciatok a odobrat udaj o deadline.
        //Rozvrhnute aktivity nepotrebuju deadline, kdeze maju zaciatok a dlzku trvania
        if (schoolActivity.getStartDT() != null && schoolActivity.getDeadline() != null) {
            return "Skolska aktivita nesmie mat nastaveny casovy zaciatok a zaroven deadline, ak ma nejaku dlzku trvania.";
        }
        
        //Dlzka trvania aktivity, ktora nema casovy zaciatok nesmie presiahnut deadline
        if (schoolActivity.getStartDT() == null && schoolActivity.getDeadline() != null) {
            if (localDateTimeNow.plusMinutes(schoolActivity.getDuration()).isAfter(schoolActivity.getDeadline())) {
                return "Dlzka trvania aktivity aktualne presahuje jej deadline";
            }
        }
        
        //Aktivita musi mat nastaveny bud casovy zaciatok alebo deadline, ale nie oboje naraz
        //Ak nie je nastaveny ziaden z udajov, je tazke danu aktivitu niekde zaradit, ak mame iba dlzku jej trvania
        if (schoolActivity.getStartDT() == null && schoolActivity.getDeadline() == null) {
            return "Zadajte jednu z hodnot: Zaciatok konania aktivity, alebo deadline. Nie oboje naraz.";
        }
        
        //Dlzka trvania aktivity by mala byt viac ako nula minut
        if (schoolActivity.getDuration() < 1) {
            return "Zadana dlzka skolskej aktivity je nula minut.";
        }
        
        //Ak zadany cas zaciatku aktivity je stary
        if (schoolActivity.getStartDT() != null && schoolActivity.getStartDT().isBefore(localDateTimeNow)) {
            return "Cas zadany pre zaciatok skolskej aktivity je zastaraly.";
        }
        
        //Ak zadany cas deadlinu je stary
        if (schoolActivity.getDeadline() != null && schoolActivity.getDeadline().isBefore(localDateTimeNow)) {
            return "Cas zadany pre deadline skolskej aktivity je zastaraly.";
        }
        
        return null;
    }
    /**
     * Metoda overi udaje ulozene v sportovej aktivite, ci nie je nieco mimo medzu.
     * @param sportActivity instancia sportovej aktivity pre overenie
     * @param localDateTimeNow Aktualny cas.
     * @return true, ak je sportovej aktivita vporiadku
     */
    private String checkSportActivityBounds(SportActivity sportActivity, LocalDateTime localDateTimeNow) {
        //Nebol uvedeny casovy zaciatok spotovej aktivity aktivity
        if (sportActivity.getStartDT() == null) {
            return "Nebol zadany casovy zaciatok sportovej aktivity.";
        }

        //Dlzka trvania aktivity by mala byt viac ako nula minut
        if (sportActivity.getDuration() < 1) {
            return "Zadana dlzka sportovej aktivity je nula minut.";
        }
        
        //Ak zadany cas zaciatku aktivity je stary
        if (sportActivity.getStartDT() != null && sportActivity.getStartDT().isBefore(localDateTimeNow)) {
            return "Cas zadany pre zaciatok sportovej aktivity je zastaraly.";
        }
        
        return null;
    }
    /**
     * Metoda overi udaje ulozene vo volno casovej aktivite, ci nie je nieco mimo medzu.
     * @param freeTimeActivity instancia volno casovej aktivity pre overenie
     * @param localDateTimeNow Aktualny cas.
     * @return true, ak je volno casova aktivita vporiadku
     */
    private String checkFreeTimeActivityBounds(FreeTime freeTimeActivity, LocalDateTime localDateTimeNow) {
        //Nech je zadane aspon meno mimocasovej aktivity
        if (freeTimeActivity.getName() == null) {
            return "Nebolo zadane meno mimocasovej aktivity.";
        }
        
        //Nebol uvedeny casovy zaciatok mimozacovej aktivity
        if (freeTimeActivity.getStartDT() == null) {
            return "Nebol zadany casovy zaciatok mimocasovej aktivity.";
        }
        
        //Dlzka trvania aktivity by mala byt viac ako nula minut
        if (freeTimeActivity.getDuration() < 1) {
            return "Zadana dlzka mimocasovej aktivity je nula minut.";
        }
        
        //Ak zadany cas zaciatku aktivity je stary
        if (freeTimeActivity.getStartDT() != null && freeTimeActivity.getStartDT().isBefore(localDateTimeNow)) {
            return "Cas zadany pre zaciatok mimocasovej aktivity je zastaraly.";
        }
        
        return null;
    }
    /**
     * Metoda overi udaje ulozene v aktivite nerusenia, ci nie je nieco mimo medzu.
     * @param doNotDisturbActivity instancia aktivity nerusenia pre overenie
     * @return true, ak je aktivitenia nerusenia vporiadku
     */
    private String checkDoNotDisturbActivityBounds(DoNotDisturb doNotDisturbActivity) {
        if (doNotDisturbActivity.getEvent() == null) {
            return "Nebol zvoleny druh aktivity nerusenia.";
        }
        
        if (doNotDisturbActivity.getRepeat() == null) {
            return "Nebolo zvolene, ako casto sa ma aktivita nerusenia opakovat.";
        }
        
        if (doNotDisturbActivity.getStartT() == null) {
            return "Nebol zvoleny typicky cas zaciatku aktivity nerusenia.";
        }
        
        //Dlzka trvania aktivity nerusenia by mala byt viac ako nula minut
        if (doNotDisturbActivity.getDuration() < 1) {
            return "Zadana dlzka aktivity nerusenia je nula minut.";
        }
        
        return null;
    }
    
    /**
     * Metoda vrati cas zaciatku najblizsej moznej aktivity, ktora zacne po aktualnom case v buducnosti
     * @param localDateTimeNow Aktualny cas
     * @return Cas zaciatku najblizsej aktivity k localDateTimeNow v buducnosti.
     */
    public LocalDateTime findClosestActivityStartDateTimeFromNow(LocalDateTime localDateTimeNow) {
        //Najdeme casovo najblizsiu aktivitu
        Activity closestActivity;
        closestActivity = getFixedActivities().stream()
                .filter(a -> a.getStartDT().isAfter(localDateTimeNow))
                .findFirst()
                .orElse(null);
        
        return (closestActivity != null) ? closestActivity.getStartDT() : null;
    }
    
    /**
     * Metoda vrati aktivitu pre zadany pociatocny cas
     * @param startDateTime Cas zaciatku aktivity.
     * @return Aktivita, ktora ma rovnaky casovy zaciatok ako pozadovany cas. Ak aktivita zo zadanym casovym zaciatkom neexituje, metoda vrati null.
     */
    public Activity findActivityByDateTime(LocalDateTime startDateTime) {
        //Najdeme casovo najblizsiu aktivitu
        Activity foundActivity;
        foundActivity = getFixedActivities().stream()
                .filter(a -> a.getStartDT().isAfter(startDateTime))
                .findFirst()
                .orElse(null);  
        
        return (foundActivity != null) ? foundActivity : null;
    }
    
    /**
     * Metoda vrati vsetky aktivity, ktore su rozvrhnute v rozvrhu pre dany casovy interval
     * Tieto aktivity maju isFixedTime() flag. Metoda vracia aktivity aj na hraniciach intervalu.
     *  
     * @param lDtStart zaciatok casoveho intervalu
     * @param lDtEnd koniec casoveho intervalu
     * @return vracia prazdny list ak ziadne take aktivity neexistuju
     */
    public List<Activity> getAllActivitiesWithTimeRange(LocalDateTime lDtStart, LocalDateTime lDtEnd) {
        List<Activity> allActivities = new ArrayList<>();
        
        allActivities.addAll(activitiesData.getActivities()
                    .stream()
                    .filter(a -> a.isFixedTime() && a.getStartDT().isAfter(lDtStart) && a.getStartDT().isBefore(lDtEnd))
                    .sorted(Comparator.comparing(Activity::getStartDT))
                    .collect(Collectors.toList())
        );
        allActivities.addAll(getDoNotDisturbActivities(lDtStart, lDtEnd));
        
        allActivities.sort(Comparator.comparing(Activity::getStartDT));
        
        return allActivities;
        
    }
    
    /**
     * Metoda ulozi spracovane data naspat do suboru. Typicky pred korektnym ukoncenim programu.
     */
    public void saveActivities() {
        
        this.activitiesData.saveToFile(settingsData.getActivitiesDataOutFileName());
    }
}
