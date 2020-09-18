/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mavenproject.timescheduler.dialogs;

import com.mavenproject.timescheduler.ActivitiesManager;
import com.mavenproject.timescheduler.data.SettingsData;
import com.mavenproject.timescheduler.models.Activity;
import com.mavenproject.timescheduler.models.ActivityStringRepresentation;
import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Michal
 */
public class ConsoleDialogBody_ShowActivities extends ConsoleDialogBody_Base {
    private ConsoleDialogBody startTimeIntervalDialog;
    private ConsoleDialogBody endTimeIntervalDialog;
    private ConsoleDialogBody showActivitiesList;
    
    private SettingsData settingsData;
    private ActivitiesManager activitiesManager;
    
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private List<ConsoleDialogElement> consoleDialogElements;
    private List<Activity> selectedActivities;
    
    private ShowActivitiesMenuType currShowActivitiesMenuType = ShowActivitiesMenuType.STARTTIME_DIALOG;

    private enum ShowActivitiesMenuType {
        STARTTIME_DIALOG,
        ENDTIME_DIALOG,
        ACTIVITIES
    }
    
    /**
     * Metoda prednastavi zobrazenie pre zoznam aktivit v dialogu: Zobrazit aktivity
     * Zavola Scheduler, ak data este neboli rozvrhnute.
     */
    private void manageActivitiesList() {
        String errorMsg = null;
        //Pred tym nez zobrazime zoznam aktivit zo zadaneho intervalu, rozvrhneme aktivity, ak este rozvrhnute neboli.
        errorMsg = activitiesManager.getCurrScheduler().TryToSchedule();
        
        List<Activity> activities = activitiesManager.getAllActivitiesWithTimeRange(startDateTime, endDateTime);
        
        consoleDialogElements = new ArrayList<>();
        
        try {
            if (activities == null && errorMsg != null) {
                String[] errorMsgs = errorMsg.split("\\r?\\n");
                consoleDialogElements.add(new ConsoleDialogElement(ConsoleDialog.DialogElementType.EMPTY_LINE));
            
                for (String errorMessage : errorMsgs) {
                    consoleDialogElements.add(new ConsoleDialogElement(ConsoleDialog.TextAlign.LEFT, errorMessage));
                }
            } else if (activities != null) {
                if (activities.isEmpty()) {
                    consoleDialogElements.add(new ConsoleDialogElement(ConsoleDialog.TextAlign.LEFT, "Nenasli sa aktivity vyhovojuce zadanemu casovemu intervalu."));
                } else {
                    for (Activity activity : activities) {
                        ActivityStringRepresentation asr = activity.generateStringRepresentation(settingsData.getTimeZone());
                        consoleDialogElements.add(new ConsoleDialogElement(ConsoleDialog.TextAlign.LEFT, asr.title + ":"));

                        asr.description.forEach((string) -> {
                            consoleDialogElements.add(new ConsoleDialogElement(ConsoleDialog.TextAlign.LEFT, " " + string));
                        });
                        consoleDialogElements.add(new ConsoleDialogElement(ConsoleDialog.DialogElementType.DIVIDER));
                    }
                }
            }
            
            
        } catch (NullPointerException | InvalidParameterException ex) {
            Logger.getLogger(ConsoleDialogBodyChooseMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        showActivitiesList.addConsoleDialogElements(consoleDialogElements);
        currShowActivitiesMenuType = ShowActivitiesMenuType.ACTIVITIES;
    }
    
    public ConsoleDialogBody_ShowActivities(SettingsData settingsData, ActivitiesManager activitiesManager) {
        this.settingsData = settingsData;
        this.activitiesManager = activitiesManager;
        
        startTimeIntervalDialog = new ConsoleDialogBodyAskForValue(
            "Zadajte zaciatok casoveho useku. (Format: yyyy-MM-dd HH:mm)",
            new ConsoleUserInput_Navigation(false, false, true, false, false),
            new ConsoleUserInput_Question(false, false, "zaciatok casoveho useku", ConsoleUserInput.QuestionType.DATETIME)
        );
        
        endTimeIntervalDialog = new ConsoleDialogBodyAskForValue(
            "Zadajte koniec casoveho useku. (Format: yyyy-MM-dd HH:mm)",
            new ConsoleUserInput_Navigation(false, true, true, false, false),
            new ConsoleUserInput_Question(false, false,  "koniec casoveho useku", ConsoleUserInput.QuestionType.DATETIME)   
        );
        
        showActivitiesList = new ConsoleDialogBodyInfo(
            "Zoznam aktivit podla zadaneho casoveho intervalu:",
            new ConsoleUserInput_Navigation(false, true, false, false, true),
            new ConsoleUserInput_Question(false, true,  "prosim moznost Naspat (b) alebo Dokoncit (f)")
        );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean setPrevious() {
        switch (currShowActivitiesMenuType) {
            //Je to zaciatok dialogu pre zobrazenie aktivit, nema predchadzajucu stranu
            case STARTTIME_DIALOG: return false;
            //Predchadzajuca strana je STARTTIME_DIALOG
            case ENDTIME_DIALOG: currShowActivitiesMenuType = ShowActivitiesMenuType.STARTTIME_DIALOG; return true;
            //Predchadzajuca strana je ENDTIME_DIALOG
            case ACTIVITIES: currShowActivitiesMenuType = ShowActivitiesMenuType.ENDTIME_DIALOG; return true;
        }
        
        return true;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean setNext() {
        switch (currShowActivitiesMenuType) {
            //Je to zaciatok dialogu pre zobrazenie aktivit, ma nasledujucu stranu ENDTIME_DIALOG
            case STARTTIME_DIALOG: currShowActivitiesMenuType = ShowActivitiesMenuType.ENDTIME_DIALOG; return true;
            //Nasledujuca strana je ACTIVITIES, kde sa zobrazia aktivity zo zadaneho intervalu
            case ENDTIME_DIALOG: currShowActivitiesMenuType = ShowActivitiesMenuType.ACTIVITIES; manageActivitiesList(); return true;
            //Posledna strana dialogu, nema nasledujucu stranu
            case ACTIVITIES: return false;
        }
        
        return true;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public ConsoleDialogBody getCurr() {
        switch (currShowActivitiesMenuType) {
            case STARTTIME_DIALOG: return startTimeIntervalDialog;
            case ENDTIME_DIALOG: return endTimeIntervalDialog;
            case ACTIVITIES: return showActivitiesList;
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
        return ConsoleDialog.Instructions.NOPAINT;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public ConsoleDialog.Instructions handleChooseOption(ConsoleUserInput_NavigationSelect navigation, ConsoleUserInput_Question question) {
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
        String answerString;
                
        // Kym je vrateny retazec, dlhsi ako 1, nejde o navigacny prikaz
        while ((answerString = ConsoleUserInput.Text(navigation, question.preFillInputWith, question.errorMessage)) != null) {            
            System.out.print("ERROR: Zadajte prosim moznost Naspat (b) alebo Dokoncit (f).\n> ");
        }
        
        //Zistime ci nebol zadany: exit, back, cancel, skip, finish
        if (answerString == null && navigation != null) {
            return handleNavigationToInstruction(navigation);
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
        LocalDateTime answerDateTime = ConsoleUserInput.DateTime(navigation, question.preFillInputWith);
                
        //Zistime ci nebol zadany: exit, back, cancel, skip, finish
        if (answerDateTime == null && navigation != null) {
            return handleNavigationToInstruction(navigation);
        }
        
        //Porovname to s aktualnym casom v UTC      
        if (answerDateTime != null) {
            //Vsetky casove udaje porovnavame voci UTC
            switch (currShowActivitiesMenuType) {
                case STARTTIME_DIALOG: 
                    startDateTime = answerDateTime.minusHours(settingsData.getTimeZone());
                    return setNext() ? ConsoleDialog.Instructions.REPAINT : ConsoleDialog.Instructions.NOPAINT;
                
                case ENDTIME_DIALOG:
                    endDateTime = answerDateTime.minusHours(settingsData.getTimeZone());
                    return setNext() ? ConsoleDialog.Instructions.REPAINT : ConsoleDialog.Instructions.NOPAINT;
                
                case ACTIVITIES:
                    return ConsoleDialog.Instructions.REPAINT;
            }
        }
        
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
            case TEXT: return handleText(userInput_NavigationSelected, userInput_Question);
            case DATETIME: return handleDateTime(userInput_NavigationSelected, userInput_Question);
            default: return ConsoleDialog.Instructions.NOPAINT;
        }
    }
}
