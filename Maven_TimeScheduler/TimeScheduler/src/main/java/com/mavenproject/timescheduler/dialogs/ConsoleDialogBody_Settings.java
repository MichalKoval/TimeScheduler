/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mavenproject.timescheduler.dialogs;

import com.mavenproject.timescheduler.ActivitiesManager;
import com.mavenproject.timescheduler.ActivityNotification;
import com.mavenproject.timescheduler.NotificationHandler;
import com.mavenproject.timescheduler.data.SettingsData;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Michal
 */
public class ConsoleDialogBody_Settings extends ConsoleDialogBody_Base {
    private ConsoleDialogBody settingsMenu;
    private ConsoleDialogBody notificationSettings;
    private ConsoleDialogBody timeZoneSettings;
    private ConsoleDialogBody fileNameSettings;
    private ConsoleDialogBody fileNameInSettings;
    private ConsoleDialogBody fileNameOutSettings;
    private ConsoleDialogBody fileNameInSettingsError;
    private ConsoleDialogBody fileNameOutSettingsError;
    private SettingsData settingsData;
    private ActivitiesManager activitiesManager;
    
    private SettingMenuType currSettingMenuType = SettingMenuType.MENU_SETTINGS;

    private enum SettingMenuType {
        MENU_SETTINGS,
        NOTOFICATION_SETTINGS,
        TIMEZONE_SETTINGS,
        FILENAME_SETTINGS,
        FILENAME_IN_SETTINGS,
        FILENAME_OUT_SETTINGS,
        FILENAME_IN_SETTINGS_ERROR,
        FILENAME_OUT_SETTINGS_ERROR
    }
    
    /**
     * Konstruktor prednastavi vsetky potrebne zobrazenia(tela dialogov) pre submenu zobrazujuceho nastavenia programu
     * @param activitiesManager Spravca dat s aktivitami
     * @param settingsData Data s nastaveniami programu
     */
    public ConsoleDialogBody_Settings(SettingsData settingsData, ActivitiesManager activitiesManager) {
        this.activitiesManager = activitiesManager;
        this.settingsData = settingsData;
        
        settingsMenu = new ConsoleDialogBodyChooseMenu(
            "Vyberte moznost:",
            Arrays.asList(
                "Nastavit notifikacie",
                "Nastavit casovu zonu",
                "Nastavit meno suboru"
            ),
            new ConsoleUserInput_Navigation(false, false, true, false, false),
            new ConsoleUserInput_Question(false, false, 1, 3)
        );
        
        //YES/NO otazka
        notificationSettings = new ConsoleDialogBodyAskForValue(
            "Chcete povolit notifikacie? (y/n)",
            new ConsoleUserInput_Navigation(false, true, true, false, false),
            new ConsoleUserInput_Question(false, false)
        );
        
        timeZoneSettings = new ConsoleDialogBodyChooseMenu(
            "Zadajte novu casovu zonu: {-11, ... -1, 0, 1, ... 14 }",
            new ConsoleUserInput_Navigation(false, true, true, false, false),
            new ConsoleUserInput_Question(false, false,  -11, 14)
        );
        
        fileNameSettings = new ConsoleDialogBodyChooseMenu(
            "Vyberte moznost:",
            Arrays.asList(
                "Nastavit meno vstupneho suboru",
                "Nastavit meno vystupneho suboru"
            ),
            new ConsoleUserInput_Navigation(false, true, true, false, false),
            new ConsoleUserInput_Question(false, false,  1, 2)
        );
        
        fileNameInSettings = new ConsoleDialogBodyAskForValue(
            "Zadajte meno vstupneho suboru pre nacitanie aktivit.",
            new ConsoleUserInput_Navigation(false, true, true, false, false),
            new ConsoleUserInput_Question(true, false,  "meno vstupneho suboru pre nacitanie aktivit")
        );
        
        fileNameOutSettings = new ConsoleDialogBodyAskForValue(
            "Zadajte meno vystupneho suboru pre ulozenie aktivit.",
            new ConsoleUserInput_Navigation(false, true, true, false, false),
            new ConsoleUserInput_Question(true, false,  "meno vystupneho suboru pre ulozenie aktivit")
        );
        
        fileNameInSettingsError = new ConsoleDialogBodyInfo(
            "Zadane meno nevyhovuje, prosim zvolte ine meno vstupneho suboru.",
            new ConsoleUserInput_Navigation(false, true, true, false, false),
            new ConsoleUserInput_Question(false, false,  "")
        );
        
        fileNameOutSettingsError = new ConsoleDialogBodyInfo(
            "Zadane meno nevyhovuje, prosim zvolte ine meno vystupneho suboru.",
            new ConsoleUserInput_Navigation(false, true, true, false, false),
            new ConsoleUserInput_Question(false, false,  "")
        );
        
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean setPrevious() {
        switch (currSettingMenuType) {
            //Je to menu pre vyber moznosti, nema predchadzajucu stranu
            case MENU_SETTINGS: return false;
            //Predchadzajuca strana je MENU_SETTINGS
            case NOTOFICATION_SETTINGS: currSettingMenuType = SettingMenuType.MENU_SETTINGS; return true;
            //Predchadzajuca strana je MENU_SETTINGS
            case TIMEZONE_SETTINGS: currSettingMenuType = SettingMenuType.MENU_SETTINGS; return true;
            //Predchadzajuca strana je MENU_SETTINGS
            case FILENAME_SETTINGS: currSettingMenuType = SettingMenuType.MENU_SETTINGS; return true;
            //Predchadzajuca strana je FILENAME_SETTINGS
            case FILENAME_IN_SETTINGS: currSettingMenuType = SettingMenuType.FILENAME_SETTINGS; return true;
            //Predchadzajuca strana je FILENAME_SETTINGS
            case FILENAME_OUT_SETTINGS: currSettingMenuType = SettingMenuType.FILENAME_SETTINGS; return true;
            //Predchadzajuca strana je FILENAME_IN_SETTINGS
            case FILENAME_IN_SETTINGS_ERROR: currSettingMenuType = SettingMenuType.FILENAME_SETTINGS; return true;
            //Predchadzajuca strana je FILENAME_OUT_SETTINGS
            case FILENAME_OUT_SETTINGS_ERROR: currSettingMenuType = SettingMenuType.FILENAME_SETTINGS; return true;
        }
        
        return true;
    }    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean setNext() {
        switch (currSettingMenuType) {
            //Je to menu pre vyber moznosti, nema nasledujucu stranu(treba ju zvolit)
            case MENU_SETTINGS: return false;
            //Nasledujuca strana neexistuje
            case NOTOFICATION_SETTINGS: return true;
            //Nasledujuca strana neexistuje
            case TIMEZONE_SETTINGS: return true;
            //Nasledujuca strana neexistuje
            case FILENAME_SETTINGS: return false;
            //Nasledujuca strana neexistuje
            case FILENAME_IN_SETTINGS: currSettingMenuType = SettingMenuType.FILENAME_IN_SETTINGS_ERROR; return true;
            //Nasledujuca strana neexistuje
            case FILENAME_OUT_SETTINGS: currSettingMenuType = SettingMenuType.FILENAME_OUT_SETTINGS_ERROR; return true;
            //Nasledujuca strana neexistuje
            case FILENAME_IN_SETTINGS_ERROR: return true;
            //Nasledujuca strana neexistuje
            case FILENAME_OUT_SETTINGS_ERROR: return true;
        }
        
        return true;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public ConsoleDialogBody getCurr() {
        switch (currSettingMenuType) {
            case MENU_SETTINGS: return settingsMenu;
            case NOTOFICATION_SETTINGS: return notificationSettings;
            case TIMEZONE_SETTINGS: return timeZoneSettings;
            case FILENAME_SETTINGS: return fileNameSettings;
            case FILENAME_IN_SETTINGS: return fileNameInSettings;
            case FILENAME_OUT_SETTINGS: return fileNameOutSettings;
            case FILENAME_IN_SETTINGS_ERROR: return fileNameInSettingsError;
            case FILENAME_OUT_SETTINGS_ERROR: return fileNameOutSettingsError;
            default: return null;
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public ConsoleDialogBody_Base getSubMenuBase() {
        return null;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ConsoleDialog.Instructions handleNavigationToInstruction(ConsoleUserInput_NavigationSelect navigation) {
        switch (navigation.getNavigation()) {
            case EXIT: return ConsoleDialog.Instructions.EXIT;
            case BACK: return setPrevious() ?  ConsoleDialog.Instructions.REPAINT : ConsoleDialog.Instructions.NOPAINT;
            case CANCEL: return ConsoleDialog.Instructions.REMOVE_SUBMENU;
            case SKIP: return setNext() ? ConsoleDialog.Instructions.REPAINT : ConsoleDialog.Instructions.NOPAINT;
            case FINISH: return ConsoleDialog.Instructions.REMOVE_SUBMENU;
            default: return ConsoleDialog.Instructions.NOPAINT;
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public ConsoleDialog.Instructions handleYesNo(ConsoleUserInput_NavigationSelect navigation, ConsoleUserInput_Question question) {
        Boolean answerBoolean = ConsoleUserInput.YesNoQuestion(navigation, question.preFillInputWith);
                
        //Zistime ci nebol zadany: exit, back, cancel, skip, finish
        if (answerBoolean == null && navigation != null) {
            return handleNavigationToInstruction(navigation);
        }
        try {
            //Ak bolo na otazku YES/NO odpovedane kladne
            //Ziskame spravcu notifikacii
            NotificationHandler currNotificationHandler = activitiesManager.getNotificationHandler();
            if (answerBoolean) {
                if (currSettingMenuType == SettingMenuType.NOTOFICATION_SETTINGS) {
                    //Zmenime stav notifikacii na zapnute
                    settingsData.setNotificationEnabled(true);
                    settingsData.saveToFile();
                    
                    if (currNotificationHandler.isNotificationSupported() && !currNotificationHandler.isNotificationRunning()) {
                        currNotificationHandler.startNotifications();
                    }
                    return setPrevious() ? ConsoleDialog.Instructions.REPAINT : ConsoleDialog.Instructions.NOPAINT;
                }
            } 
            //Ak bolo na otazku YES/NO odpovedane zaporne
            else {
                if (currSettingMenuType == SettingMenuType.NOTOFICATION_SETTINGS) {
                    settingsData.setNotificationEnabled(false);
                    settingsData.saveToFile();
                    if (currNotificationHandler.isNotificationRunning()) {
                        currNotificationHandler.stopNotifications();
                    }
                    return setPrevious() ? ConsoleDialog.Instructions.REPAINT : ConsoleDialog.Instructions.NOPAINT;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return ConsoleDialog.Instructions.NOPAINT;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public ConsoleDialog.Instructions handleChooseOption(ConsoleUserInput_NavigationSelect navigation, ConsoleUserInput_Question question) {
        Integer answerInteger = ConsoleUserInput.ChooseOption(navigation, question.preFillInputWith, question.startRange, question.endRange);
                
        //Zistime ci nebol zadany: exit, back, cancel, skip, finish
        if (answerInteger == null && navigation != null) {
            return handleNavigationToInstruction(navigation);
        }

        if (answerInteger != null) {
            //Ak ide o ciselnu hodnotu casovej zony z rozmedia intervalu {-11,...,14}.
            try {
                if (currSettingMenuType == SettingMenuType.TIMEZONE_SETTINGS) {
                    if (answerInteger >= -11 && answerInteger <= 14) {
                        settingsData.setTimeZone(answerInteger);
                        settingsData.saveToFile();
                    }
                    return setPrevious() ? ConsoleDialog.Instructions.REPAINT : ConsoleDialog.Instructions.NOPAINT;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            
            if (currSettingMenuType == SettingMenuType.FILENAME_SETTINGS) {
                switch (answerInteger) {
                    case 1: currSettingMenuType = SettingMenuType.FILENAME_IN_SETTINGS;
                        break;
                    case 2: currSettingMenuType = SettingMenuType.FILENAME_OUT_SETTINGS;
                        break;
                    default: return setPrevious() ? ConsoleDialog.Instructions.REPAINT : ConsoleDialog.Instructions.NOPAINT;
                }
                
                return ConsoleDialog.Instructions.REPAINT;
            }
            
            switch (answerInteger) {
                case 1: currSettingMenuType = SettingMenuType.NOTOFICATION_SETTINGS;
                    break;
                case 2: currSettingMenuType = SettingMenuType.TIMEZONE_SETTINGS;
                    break;
                case 3: currSettingMenuType = SettingMenuType.FILENAME_SETTINGS;
                    break;
                default: return setPrevious() ? ConsoleDialog.Instructions.REPAINT : ConsoleDialog.Instructions.NOPAINT;
            }

            return ConsoleDialog.Instructions.REPAINT;
        }

        return ConsoleDialog.Instructions.NOPAINT;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public ConsoleDialog.Instructions handleNumber(ConsoleUserInput_NavigationSelect navigation, ConsoleUserInput_Question question) {
        return ConsoleDialog.Instructions.NOPAINT;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public ConsoleDialog.Instructions handleText(ConsoleUserInput_NavigationSelect navigation, ConsoleUserInput_Question question) {
        String answerString = ConsoleUserInput.Text(navigation, question.preFillInputWith, question.errorMessage);
                
        //Zistime ci nebol zadany: exit, back, cancel, skip, finish
        if (answerString == null && navigation != null) {
            return handleNavigationToInstruction(navigation);
        }
        
        if (answerString != null) {
            try {
                switch (currSettingMenuType) {
                    case FILENAME_IN_SETTINGS:
                        if (settingsData.setActivitiesDataInFileName(answerString)) {
                            settingsData.saveToFile();
                            return setPrevious() ? ConsoleDialog.Instructions.REPAINT : ConsoleDialog.Instructions.NOPAINT;
                        } else {
                            //Zobrazime chybu, ze meno suboru nevyhovuje
                            return setNext() ? ConsoleDialog.Instructions.REPAINT : ConsoleDialog.Instructions.NOPAINT;
                        }
                        
                    case FILENAME_OUT_SETTINGS:
                        if (settingsData.setActivitiesDataOutFileName(answerString)) {
                            settingsData.saveToFile();
                            return setPrevious() ? ConsoleDialog.Instructions.REPAINT : ConsoleDialog.Instructions.NOPAINT;
                        } else {
                            //Zobrazime chybu, ze meno suboru nevyhovuje
                            return setNext() ? ConsoleDialog.Instructions.REPAINT : ConsoleDialog.Instructions.NOPAINT;
                        }
                    case FILENAME_IN_SETTINGS_ERROR:
                        if (settingsData.setActivitiesDataInFileName(answerString)) {
                            settingsData.saveToFile();
                            return setPrevious() ? ConsoleDialog.Instructions.REPAINT : ConsoleDialog.Instructions.NOPAINT;
                        } else {
                            //Zobrazime chybu, ze meno suboru nevyhovuje
                            return setNext() ? ConsoleDialog.Instructions.REPAINT : ConsoleDialog.Instructions.NOPAINT;
                        }
                        
                    case FILENAME_OUT_SETTINGS_ERROR:
                        if (settingsData.setActivitiesDataOutFileName(answerString)) {
                            settingsData.saveToFile();
                            return setPrevious() ? ConsoleDialog.Instructions.REPAINT : ConsoleDialog.Instructions.NOPAINT;
                        } else {
                            //Zobrazime chybu, ze meno suboru nevyhovuje
                            return setNext() ? ConsoleDialog.Instructions.REPAINT : ConsoleDialog.Instructions.NOPAINT;
                        }
                        
                } 
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            
            
        }
        
        return ConsoleDialog.Instructions.NOPAINT;
    }   
    /**
     * {@inheritDoc}
     */
    @Override
    public ConsoleDialog.Instructions handleTime(ConsoleUserInput_NavigationSelect navigation, ConsoleUserInput_Question question) {
        return ConsoleDialog.Instructions.NOPAINT;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public ConsoleDialog.Instructions handleDateTime(ConsoleUserInput_NavigationSelect navigation, ConsoleUserInput_Question question) {
//        LocalDateTime answerDateTime = ConsoleUserInput.DateTime(navigation, question.preFillInputWith);
//                
//        //Zistime ci nebol zadany: exit, back, cancel, skip, finish
//        if (answerDateTime == null && navigation != null) {
//            return handleNavigationToInstruction(navigation);
//        }
//        
//        //Porovname to s aktualnym casom v UTC      
//        if (answerDateTime != null) {
//            if (currSettingMenuType == SettingMenuType.DATEANDTIME_SETTINGS) {
//                //Vsetky casove udaje porovnavame voci UTC
//                LocalDateTime currDateTime = LocalDateTime.now(Clock.systemUTC());
//                answerDateTime = answerDateTime.minusHours(settingsData.getTimeZone());
//
//                settingsData.setTimeDifference(currDateTime.getMinute() - answerDateTime.getMinute());
//
//                currSettingMenuType = SettingMenuType.MENU_SETTINGS;
//                return ConsoleDialog.Instructions.REPAINT;
//            }
//
//            return ConsoleDialog.Instructions.NOPAINT;
//        }
        
        return ConsoleDialog.Instructions.NOPAINT;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public ConsoleDialog.Instructions manageUserAnswer() {
        ConsoleDialogBody currDialogBody = getCurr();
        ConsoleUserInput_Question userInput_Question = currDialogBody.getUserInputQuestion();
        ConsoleUserInput_NavigationSelect userInput_NavigationSelected = new ConsoleUserInput_NavigationSelect();
        userInput_NavigationSelected.setRequiredNavigations(currDialogBody.getUserInputNavigation());
        
        if (userInput_Question.requireNavigationSelection) {
            userInput_NavigationSelected.setRequireNavigationSelection(true);
        }
        
        switch (userInput_Question.type) {
            case YES_NO: return handleYesNo(userInput_NavigationSelected, userInput_Question);
            case CHOOSE_OPTION: return handleChooseOption(userInput_NavigationSelected, userInput_Question);
            case TEXT: return handleText(userInput_NavigationSelected, userInput_Question);
            case DATETIME: return handleDateTime(userInput_NavigationSelected, userInput_Question);
            default: return ConsoleDialog.Instructions.NOPAINT;
        }       
    }
}


    


