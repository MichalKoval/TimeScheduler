/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mavenproject.timescheduler.dialogs;

import com.mavenproject.timescheduler.ActivitiesManager;
import com.mavenproject.timescheduler.data.SettingsData;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Michal
 */
public class ConsoleDialogBody_MainMenu extends ConsoleDialogBody_Base {
    private ConsoleDialogBody mainMenu;
    private SettingsData settingsData;
    private ActivitiesManager activitiesManager;
    
    private SubMenuType currSubMenuType = SubMenuType.NONE;

    private enum SubMenuType {
        NONE,
        SHOW_ACTIVITIES,
        ADD_ACTIVITY,
        SETTINGS
    }
    
    public ConsoleDialogBody_MainMenu(SettingsData settingsData, ActivitiesManager activitiesManager) {
        this.settingsData = settingsData;
        this.activitiesManager = activitiesManager;
        
        mainMenu = new ConsoleDialogBodyChooseMenu(
            "Vyberte moznost:",
            Arrays.asList(
                "Zobrazit aktivity",
                "Pridat aktivitu",
                "Nastavenia"
            ),
            new ConsoleUserInput_Navigation(true, false, false, false, false),
            new ConsoleUserInput_Question(false, false, 1, 3)
        );     
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean setPrevious() {
        return false;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean setNext() {
        return false;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public ConsoleDialogBody getCurr() {
        return mainMenu;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public ConsoleDialogBody_Base getSubMenuBase() {
        switch (currSubMenuType) {
            case SHOW_ACTIVITIES: return new ConsoleDialogBody_ShowActivities(this.settingsData, this.activitiesManager);
            case ADD_ACTIVITY: return new ConsoleDialogBody_AddActivity(this.settingsData, this.activitiesManager);
            case SETTINGS: return new ConsoleDialogBody_Settings(this.settingsData, this.activitiesManager);
            default: return null;
        }
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
        Integer answerInteger = ConsoleUserInput.ChooseOption(navigation, question.preFillInputWith, question.startRange, question.endRange);
                
        //Zistime ci nebol zadany: exit, back, cancel, skip, finish
        if (answerInteger == null && navigation != null) {
            return handleNavigationToInstruction(navigation);
        }

        if (answerInteger != null) {
            switch (answerInteger) {
                case 1: currSubMenuType = SubMenuType.SHOW_ACTIVITIES;
                    return ConsoleDialog.Instructions.ADD_SUBMENU;
                case 2: currSubMenuType = SubMenuType.ADD_ACTIVITY;
                    return ConsoleDialog.Instructions.ADD_SUBMENU;
                case 3: currSubMenuType = SubMenuType.SETTINGS;
                    return ConsoleDialog.Instructions.ADD_SUBMENU;
                default: return ConsoleDialog.Instructions.NOPAINT;
            }
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
            case CHOOSE_OPTION: return handleChooseOption(userInput_NavigationSelected, userInput_Question);
            default: return ConsoleDialog.Instructions.NOPAINT;
        }
    }
}
