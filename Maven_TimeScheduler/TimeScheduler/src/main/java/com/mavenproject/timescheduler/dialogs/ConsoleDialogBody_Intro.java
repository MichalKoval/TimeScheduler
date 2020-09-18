/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mavenproject.timescheduler.dialogs;

import com.mavenproject.timescheduler.ActivitiesManager;
import com.mavenproject.timescheduler.data.SettingsData;

/**
 *  Trieda reprezentuje uvodne zbrazenie dialogu pri spusteni programu
 */
public class ConsoleDialogBody_Intro extends ConsoleDialogBody_Base {
    private ConsoleDialogBody introAboutProgram;
    private SettingsData settingsData;
    private ActivitiesManager activitiesManager;
    
    /**
     * Konstruktor nastavi dialog zobrazeny pri spusteni programu, kde su uvodne slova,
     * @param settingsData Data s nastaveniami
     * @param activitiesManager Spravca dat s aktivitami
     */
    public ConsoleDialogBody_Intro(SettingsData settingsData, ActivitiesManager activitiesManager) {
        this.settingsData = settingsData;
        this.activitiesManager = activitiesManager;
        
        introAboutProgram = new ConsoleDialogBodyInfo(
            "Vitajte v aplikacii Time Scheduler!",
            new ConsoleUserInput_Navigation(true, false, false, true, false),
            new ConsoleUserInput_Question(false, true, "moznost Exit (e) alebo Preskocit (s)")
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
        return true;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ConsoleDialogBody getCurr() {
        return introAboutProgram;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public ConsoleDialogBody_Base getSubMenuBase() {
        return new ConsoleDialogBody_MainMenu(this.settingsData, this.activitiesManager);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ConsoleDialog.Instructions handleNavigationToInstruction(ConsoleUserInput_NavigationSelect navigation) {
        switch (navigation.getNavigation()) {
            case EXIT: return ConsoleDialog.Instructions.EXIT;
            case SKIP: return ConsoleDialog.Instructions.ADD_SUBMENU;
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
            System.out.print("ERROR: Zadajte prosim moznost Exit (e) alebo Preskocit (s).\n> ");
        }       
                
        //Zistime ci nebol zadany: exit, skip
        if (answerString == null && navigation != null) {
            return handleNavigationToInstruction(navigation);
        }
        
        if (true) {
            
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
        
        return handleText(userInput_NavigationSelected, userInput_Question);
    }
}
