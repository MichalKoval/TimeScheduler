package com.mavenproject.timescheduler.dialogs;

import com.mavenproject.timescheduler.ActivitiesManager;
import com.mavenproject.timescheduler.data.SettingsData;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.util.Stack;

/**
 * Trieda reprezentuje textovy dialog, ktory bude zobrazeny uzivatelovi v konzolovej aplikacii.
 * Obsahuje hlavicku, telo, a paticku. Telo moze pozostavat zo subzobrazeni( intro, menu z moznostami, zobrazenie pre pridanie aktivity ...)
 * @author Michal
 */
public class ConsoleDialog {
    private SettingsData settingsData;
    private ActivitiesManager activitiesManager;
    private int consoleWidth;
    private int borderWidth;
    private String header = "Time Scheduler";
    /**
     * Telo dialogu: Jednotlive druhy menu v dialogu su postupne pridavane alebo odoberane zo stacku, navrchu stacku sa nachadza menu prave zobrazene uzivatelovi v konzole.
     * Pod vrcholom stacku su postupne ostane menu v poradi, ako boli otvarane.
     * Menu je obecne myslene ako to, co sa zobrazi v tele dialogu.
     */
    private Stack<ConsoleDialogBody_Base> body = new Stack<>();
    private String footer = "Author: Michal Koval";
    
    //Instrukcie pre riadenie dialogu
    private Instructions dialogInstruction = null;
    
    /**
     * Enum obsahuje instrukcie pre riadenie dialogu: jeho ukoncenie, prekreslenie, pridanie alebo odobranie subzobrazeni
     * @see com.mavenproject.timescheduler.dialogs.ConsoleDialog#manageInstruction(com.mavenproject.timescheduler.dialogs.ConsoleDialog.Instructions)
     */
    public static enum Instructions {
        EXIT,
        REPAINT,
        NOPAINT,
        ADD_SUBMENU,
        REMOVE_SUBMENU
    }
    
    /**
     * Enum reprezentujuci osadenie textu: vlavo, v strede, vpravo.
     */
    public static enum TextAlign {
        LEFT,
        MIDDLE,
        RIGHT
    }
    
    /**
     * Enum reprezentujuci elementy dialogu. Kazdy dialog v menu pozostava z dielcich elementov: oddelovac, prazdny riadok, textovy riadok.
     */
    public static enum DialogElementType {
        DIVIDER,
        EMPTY_LINE,
        TEXT
    }
    
    /**
     * Stringbuilder pre generovanie textoveho menu.
     */
    private StringBuilder dialogToPrint;
    
    /**
     * Metoda predpripravi retazec reprezentujuci oddelovac v menu/dialogu.
     */
    private void printDivider() {
        for (int i = 0; i < (consoleWidth + 2); i++) {
            dialogToPrint.append('=');
        }
        dialogToPrint.append('\n');
    }
    /**
     * Metoda predpripravi retazec reprezentujuci prazdny riadok v menu/dialogu.
     */
    private void printEmptyLine() {
        int bordersWidth = 2*borderWidth;
        
        printLeftBorder();
        
        for (int i = 0; i < (consoleWidth - bordersWidth); i++) {
            dialogToPrint.append(' ');
        }
        
        printRightBorder();
    }
    /**
     * Metoda predpripravi retazec reprezentujuci lavy okraj v menu/dialogu.
     */
    private void printLeftBorder() {
        dialogToPrint.append('|');
        
        for (int i = 0; i < (borderWidth - 1); i++) {
            dialogToPrint.append(' ');
        }
    }  
    /**
     * Metoda predpripravi retazec reprezentujuci pravy okraj v menu/dialogu.
     */
    private void printRightBorder() {
        for (int i = 0; i < (borderWidth - 1); i++) {
            dialogToPrint.append(' ');
        }
        
        dialogToPrint.append('|');
        dialogToPrint.append('\n');
    }
    /**
     * Metoda predpripravi retazec reprezentujuci text v riadku napozicovany podla enumu TextAlign.
     */
    private void printText(String text, TextAlign textAlign) {
        int bordersWidth = 2*borderWidth;

        //Text potrebujeme vtesnat do urcitej sirky dialogu
        if (text.length() > (consoleWidth - bordersWidth)) {
            text = text.substring(0, consoleWidth - (bordersWidth + 5));
            text += "...";
        }
        
        int wspacesBefore = 0;
        int wspacesAfter = 0;
        //Vyratame si poziciu textu vramci riadka
        switch (textAlign) {
            case LEFT: 
                wspacesBefore = 1;
                wspacesAfter = consoleWidth - (bordersWidth + text.length() + wspacesBefore);
                break;
            case MIDDLE: 
                wspacesBefore = (consoleWidth - (bordersWidth + text.length())) / 2;
                wspacesBefore = (wspacesBefore < 1) ? 1 : wspacesBefore; 
                wspacesAfter = consoleWidth - (bordersWidth + text.length() + wspacesBefore);
                break;
            case RIGHT:
                wspacesAfter = 1;
                wspacesBefore = consoleWidth - (bordersWidth + text.length() + wspacesAfter);
                break;
        }
        
        /// pridame napozicovany text do stringbuilderu
        printLeftBorder();
        
        // Znaky medzery pred
        for (int i = 0; i < wspacesBefore; i++) {
            dialogToPrint.append(' ');
        }
        
        // Text medzi
        dialogToPrint.append(text);
        
        // Znaky medzery za
        for (int i = 0; i < wspacesAfter; i++) {
            dialogToPrint.append(' ');
        }
        
        printRightBorder();
    }
    /**
     * Metoda predpripravi zobrazenie pre sipku, ktora neskor bude uzivatelovi indikovat miesto kde ma zadat vstup.
     */
    private void printCommandArrow() {
        dialogToPrint.append('>');
        dialogToPrint.append(' ');
    }
    /**
     * Metoda predpripravi hlavicku dialogu
     */
    private void printHeader() {
        printDivider();
        printText(this.header, TextAlign.MIDDLE);
        printDivider();
    }
    /**
     * Metoda predpripravi telo dialogu. Telo dialogu bude vyplnene dialogovymi elementami: prazdy riadok, oddelovac, textovy riadok.
     */
    private void printBody() {
        ConsoleDialogBody_Base currentDialogBody_Base;
        ConsoleDialogBody currentDialogBody;
        
        if (body.size() > 0) {
            currentDialogBody_Base = body.peek();
            if (currentDialogBody_Base != null) {
               currentDialogBody = currentDialogBody_Base.getCurr();
                if (currentDialogBody != null) {
                    for (ConsoleDialogElement element : currentDialogBody.getConsoleDialogElements()) {
                        if (element != null) {
                            switch(element.dialogElementType) {
                                case DIVIDER: printDivider();
                                    break;
                                case EMPTY_LINE: printEmptyLine();
                                    break;
                                case TEXT: 
                                    if (element.text != null && !element.text.isEmpty() && element.textAlign != null) {
                                        printText(element.text, element.textAlign);
                                    } else {
                                        printText("ERROR: Prazdny textovy element!", TextAlign.LEFT);
                                    }
                            }
                        } else {
                            printText("ERROR: DialogElement == null !", TextAlign.MIDDLE);
                        }
                    }
                } else {
                    printText("ERROR: DialogBody == null !", TextAlign.MIDDLE);
                } 
            } else {
                printText("ERROR: DialogBody_Base == null !", TextAlign.MIDDLE);
            }
            
        }
        
        printDivider();
    }
    /**
     * Metoda predpripravy hlavicku dialogu.
     */    
    private void printFooter() {
        printText("author: Michal Koval", TextAlign.RIGHT);
        printDivider();
    }
    
    /**
     * Konstruktor nastavuje: StringBuilder pre generovanie textoveho dialogu, konfiguracne data a spravcu dat s aktivitami.
     * @param settingsData Konfiguracne data programu.
     * @param activitiesManager Spravca dat s aktivitami.
     */
    public ConsoleDialog(SettingsData settingsData, ActivitiesManager activitiesManager) {
        this.dialogToPrint = new StringBuilder();
        this.settingsData = settingsData;
        this.activitiesManager = activitiesManager;
    }
    
    /**
     * Metoda vrati konfiguracne data programu.
     * @return Konfiguracne data programu.
     */
    public SettingsData getSettingsData() {
        return settingsData;
    }
    /**
     * Metoda vrati spravcu dat s aktivitami.
     * @return KSpravca dat s aktivitami.
     */
    public ActivitiesManager getActivitiesManager() {
        return activitiesManager;
    }
    
    /**
     * Metoda nastavi sirku dialogu/menu v konzole, sirkou sa mysli pocet znakov.
     * @param width Sirka dialogu/menu.
     */
    public void setWidth(int width) {
        this.consoleWidth = (width - 2);
    }
    /**
     * Je mozne dodefinovat vlastny nazov hlavicky v dialogu/menu.
     * @param header Nazov hlavicku dialogu/menu
     */
    public void setHeader(String header) {
        this.header = header;
    }
    /**
     * Je mozne dodefinovat vlastnu paticku v dialogu/menu.
     * @param footer Text v paticke
     */
    public void setFooter(String footer) {
        this.footer = footer;
    }
    /**
     * Metoda prida subzobrazenie (vlastny funkcny celok pridany do tela dialogu), ktory vygeneruje dialogove elementy.
     * @param bodyDialog Subzobrazenie, ktore sa zobrazi v tele dialogu.
     */
    public void addToBody(ConsoleDialogBody_Base bodyDialog) {
        this.body.push(bodyDialog);
    }
    /**
     * Metoda odoberie subzobrazenie (vlastny funkcny celok pridany do tela dialogu), ktory vygeneruje dialogove elementy.
     */
    public void removeFromBody() {
        this.body.pop();
    }
       
    /**
     * Metoda vypise textovy dialog do konzoly
     */
    public void printDialog() {
        //TODO FIX: clear console screen, not working!!!
        //cls Windows not working, different version of windows
        //clear GNU/Linux working

        //Pred vypisanim vycistime string builder
        this.dialogToPrint.setLength(0);
        
        //Zavolame procedury pre vypisanie jednotlivych podsekcii
        printHeader();
        printBody();
        printFooter();
        printCommandArrow();
        
        //Vypiseme dialog
        System.out.print(dialogToPrint.toString());
        
        askUserForInput();
    }
    
    /** Metoda ziska odpoved zadanu uzivatelom a posunie ju dalej pre spracovanie
     *
     */
    private void askUserForInput() {
        ConsoleDialogBody_Base currentDialogBody_Base;
        ConsoleDialogBody currentDialogBody;
        
        if (body.size() > 0) {
            currentDialogBody_Base = body.peek(); 
            
            if (currentDialogBody_Base != null) {
                manageInstruction(currentDialogBody_Base.manageUserAnswer());
                
            } else {
                printText("ERROR: DialogBody_Base == null !", TextAlign.MIDDLE);
            }
        }
    }
    
    /**
     * Metoda spracuje instrukciu vygenerovanu na zaklade uzivatelovho vstupu.
     * Su to instruckie pre prekreslenie menu, pridanie noveho submenu, odobratie zodpovedaneho dialogu v menu.
     */
    private void manageInstruction(Instructions instruct) {
        switch (instruct) {
            case EXIT: manageExit(); break;
            case REPAINT: printDialog(); break;
            //case NOPAINT: askUserForInput(); break;
            case ADD_SUBMENU: addSubMenu(); break;
            case REMOVE_SUBMENU: removeSubMenu(); break;
        }
    }
    
    /**
     * Metoda sa pokusi korektne ukoncit beh programu. Uzivatel v menu zvolil moznost = Exit.
     * Korektnym ukoncenim sa mysli ulozenie konfiguracnych dat, dat s aktivitami ak boli pozmene. Plus ukoncenie osobitneho vlakna casovaca,
     * ktory sa stara o zobrazovanie notifikacii
     */
    private void manageExit() {
        System.out.println(" Ukladanie nastaveni a dat...");
        settingsData.saveToFile("settings");
        activitiesManager.saveActivities();
        //Pozastavime Timer notifikacie, aby slo aplikaciu korektne ukoncit
        activitiesManager.getNotificationHandler().stopNotifications();
        //Odoberieme ikonku pre notifikaciu zo System Tray, zistime, ci bol podporovany System Tray
        if (SystemTray.isSupported()) {
            // Ziskame objekt pre zobrazenie notifikacie
            SystemTray notification = SystemTray.getSystemTray();

            // Zistime, ci nasa aplikacia uz nema TrayIcon zobrazeny
            // Tato metoda vrati iba TrayIcons vytvorene tymto programom
            TrayIcon[] messages = notification.getTrayIcons();
            
            // Odoberieme vsetky ikonky zo system tray, ake boli vygenerovane, aby slo korektne ukoncit AWT Thread
            if (messages.length > 0) {
                for (TrayIcon message : messages) {
                    notification.remove(message);
                }
            }
        }
        System.out.println(" Exiting...");
    }
    
    /**
     * Metoda prida do menu novy druh subzobrazenia. (subzobrazenia: Prehlad aktivit, Pridanie aktivity, Nastavenia)
     */
    private void addSubMenu() {
        ConsoleDialogBody_Base currentDialogBody_Base = body.peek();
        if (currentDialogBody_Base != null) {
            ConsoleDialogBody_Base newDialogBodyToAdd_Base;
            
            if ((newDialogBodyToAdd_Base = currentDialogBody_Base.getSubMenuBase()) != null) {
                addToBody(newDialogBodyToAdd_Base);
                
                printDialog();
            }
        }
    }
    /**
     * Metoda odoberie z menu novy druh subzobrazenia. (subzobrazenia: Prehlad aktivit, Pridanie aktivity, Nastavenia)
     * Typicky, ak praca v subzobrazeni uz bola ukoncena, odoberieme subzobrazenie zo stacku zobrazeni
     */
    private void removeSubMenu() {
        ConsoleDialogBody_Base currentDialogBody_Base = body.peek();
        if (currentDialogBody_Base != null) {
            removeFromBody();
            
            printDialog();
        }
    }
}
