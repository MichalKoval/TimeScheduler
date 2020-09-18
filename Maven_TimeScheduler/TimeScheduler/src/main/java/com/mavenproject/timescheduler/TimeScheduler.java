/**
 * TimeScheduler, @author Michal Koval, Zapoctovy program z predmetu Java, MFF UK Praha
 * 
 * Program na najdenie co najlepsieho caseveho rozvrhu pre aktivity studenta.
 * Medzi aktivity studenta patri:
 *  - Skolska aktivita(trieda SchoolActivity)
 *  - Sportova aktivita(trieda SportActivity)
 *  - Mimocasova(volno) aktivita(trieda FreeTime)
 *  - Doba nerusenia(spanok, obed, ...) (trieda DoNotDisturb). Je to takztiez druh aktivity.
 * Vsetky spomenute aktivity su odvodene od tried: Activity, alebo DefaultActivity.
 * 
 * Hlavnu cast programu tvori dialog (alebo menu, trieda ConsoleDialog) v konzole,
 * ktory interaguje z uzivatelom pomocou prikazov:
 *      - navigacnych - exit, naspat, zrusit, preskocit, dokoncit
 *      - prikazov pre volbu polozky v menu, pomocou cisel
 *      - alebo vlastny vstup uzivatela - zadany text, casovy udaj, ciselny udaj ...
 * Pre zaradenie aktivit do casoveho rozvrhu posluzi trieda ScheduleActivities,
 * ktora za vhodnych podmienok roztriedy novopridane aktivity.
 * 
 * Moznosti ako pridat nove subory je bud pomocou dialogu v konzole, alebo priamo
 * pridanim aktivit do suboru. Program nasledne rozlisi novopridane aktivity a pokusi sa
 * ich zaradit do rozvrhu.
 * 
 * Pre uchovanie dat pocat behu programu posluzia triedy ActivitiesData a SettingsData.
 * Postaraju sa o nacitanie alebo ulozenie dat z, do suboru. Data v subore su ulozene
 * v JSON formate. Pre serializaciu a deserializaciu JSON je pouzita
 * externa kniznica Jackson. Je obsiahnuta v dependencies, a v pom.xml subore.
 * Serializuju sa hodnoty, ktore nie su null. Zakladne nastavenia potrebne pre beh programu
 * su v subore 'settings'.
 * Vzor konfiguracneho suboru:        
 *   {
 *     "fileName" : "settings",
 *     "timeZone" : 1,
 *     "notificationEnabled" : false,
 *     "activitiesDataInFileName" : "scheduler_input",
 *     "activitiesDataOutFileName" : "scheduler_output"
 *   }
 *
 * Pre spracovanie dat je pouzita trieda ActivitiesManager, ktora aktualizuje zmeny
 * v datach aktivit, ako aj poskytuje predspracovane zoznamy aktivit pre dalsie spracovanie
 * triedou ScheduleActivities. Triedy ActivitiesManager a ScheduleActivities medzi sebou
 * navzajom spolupracuju cez urcite volania funkci, kde sa navzajom informuju ci doslo zmene.
 * 
 * Nakoniec je tu implementacia notifikacie o najblizsom konani sa aktivity,
 * ktora sa neobisla bez pouzitia grafickeho rozhrania java.awt.* .
 * Notifikacie je podporovana len pre operacny system Windows 10.
 * V notifikaciach budu zobrazovane len tieto typy aktivit: skolska aktivita, sportova aktivita a volna aktivita.
 * Aktivity nerusenia nebudu zobrazovane v notifikacii.
 * 
 * Program pozostava z viacerych balickov:
 *  @see com.mavenproject.timescheduler - obsahuje triedy pre spracovanie dat
 *  @see com.mavenproject.timescheduler.data - sustreduje triedy pre ulozenie dat
 *  @see com.mavenproject.timescheduler.dialogs - obsahuje vsetky potrebne triedy dialogov
 *  @see com.mavenproject.timescheduler.models - obsahuje triedy reprezentujuce jednotlive druhy aktivit
 *  @see com.mavenproject.timescheduler.services - obsahuje generator skusobnych dat s aktivitami
 * 
 */
package com.mavenproject.timescheduler;

import com.mavenproject.timescheduler.data.ActivitiesData;
import com.mavenproject.timescheduler.data.SettingsData;
import com.mavenproject.timescheduler.dialogs.ConsoleDialog;
import com.mavenproject.timescheduler.dialogs.ConsoleDialogBody_Intro;
import com.mavenproject.timescheduler.services.ActivitiesDataGenerator;

/**
 * Hlavna trieda programu, kde su vytvorene zakladne instancie pre chod programu.
 * 
 */
public class TimeScheduler {
    private static final String TIMESCHEDULER_SETTINGS_FILE_NAME = "settings";
    
    /**
     * Metoda predgeneruje nejake data pomocou sluzby ActivitiesDataGenerator.generate() a ulozi ich docasneho suboru 'generated_data'
     */
    public static void generateSomeTestData() {
        ActivitiesData activitiesData = ActivitiesDataGenerator.generate();
        activitiesData.saveToFile("generated_data"); 
    }
    
    /**
     * Metoda prednastavi a spusti hlavnu cast programu
     */
    public static void startProgram() {
        //Nacitame zakladne nastavenia programu z pevne daneho suboru TIMESCHEDULER_SETTINGS_FILE_NAME
        SettingsData settingsData = new SettingsData();
        if (!settingsData.loadFromFile(TIMESCHEDULER_SETTINGS_FILE_NAME)) {
            System.err.println("Dodajte prosim konfiguracny subor. Prednastavene meno konfiguracneho suboru je: '" + TIMESCHEDULER_SETTINGS_FILE_NAME + "'");
            return;
        }
        System.out.println("Konfiguracne data boli nacitane zo suboru: " + TIMESCHEDULER_SETTINGS_FILE_NAME);
        
        
        //Nacitame si data aktivit zo suboru pre nasledne spracovanie
        ActivitiesData activitiesData = new ActivitiesData();
        if (!activitiesData.loadFromFile(settingsData.getActivitiesDataInFileName())) {
            System.err.println("Dodajte prosim subor s aktivitami pre ich rozvrhnutie. Pociatocne meno suboru s aktivitami je nastavene na: '" + settingsData.getActivitiesDataInFileName() + "'");
            return;
        }
        System.out.println("Data s aktivitami pre rozvrhnutie boli nacitanie zo suboru: " + settingsData.getActivitiesDataInFileName());
        
        //Pouzijeme ActivityManager pre spracovanie dat s aktivitami
        ActivitiesManager activitiesManager = new ActivitiesManager(activitiesData, settingsData);
        
        //Pouzijeme SchudeleActivities pre zaradenie potencionalnych novych aktivit do rozvrhu
        ScheduleActivities scheduler = new ScheduleActivities(activitiesManager);
        
        //Spojime ActivityManager so ScheduleActivities
        activitiesManager.setScheduler(scheduler);

        
        //Vytvorime interaktivny konzolovy dialog, pre komunikaciu s uzivatelom
        ConsoleDialog consoleDialog = new ConsoleDialog(settingsData, activitiesManager);
        consoleDialog.setWidth(90);
        
        consoleDialog.addToBody(
            new ConsoleDialogBody_Intro(
                consoleDialog.getSettingsData(),
                consoleDialog.getActivitiesManager()
            )
        );
        
        //Vytvorime instanciu NotificationHandler pre spravu notifikacii
        NotificationHandler notificationHandler = new NotificationHandler(activitiesManager, settingsData);
        activitiesManager.setNotificationHandler(notificationHandler);
        
        //Pokusime sa rozvrhnut aktivity nacitane zo suboru preto aby mohli fungovat notifikacie
        String errorMsg;
        // Ak sa aktivity podarilo rozvrhnut, TryToSchedule aktivuje notifikacie, ak su dovolene
        // Aktivity musia byt povolene v nastaveniach
        if (settingsData.isNotificationEnabled()) {
            if ((errorMsg = scheduler.TryToSchedule()) != null) {
                System.out.println(errorMsg);
            }
        }
        //Zobrazime uvodne konzolove okno ConsoleDialogBody_Intro
        consoleDialog.printDialog();
    }
    
    public static void main(String[] args) {
        
        //Pre debug: Predgenerujeme si nejake data
        //generateSomeTestData();

        
        //Spustime TimeSchuler
        startProgram();
        
        
    }
}
