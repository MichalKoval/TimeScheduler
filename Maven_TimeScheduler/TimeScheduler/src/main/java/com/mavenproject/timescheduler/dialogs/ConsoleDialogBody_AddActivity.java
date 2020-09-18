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
import com.mavenproject.timescheduler.models.DoNotDisturb;
import com.mavenproject.timescheduler.models.FreeTime;
import com.mavenproject.timescheduler.models.SchoolActivity;
import com.mavenproject.timescheduler.models.SportActivity;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Michal
 */
public class ConsoleDialogBody_AddActivity extends ConsoleDialogBody_Base {
    
    private Activity activityToAdd = null;
    private ConsoleDialogBody activitiesChooseMenu;
    private ConsoleDialogBody addActivityProblem;
    private ConsoleDialogBody addActivitySuccess;
    private List<ConsoleDialogBody> schoolActivityDialogs;
    private List<ConsoleDialogBody> sportActivityDialogs;
    private List<ConsoleDialogBody> freetimeActivityDialogs;
    private List<ConsoleDialogBody> doNotDisturbDialogs;
    private int currIndexSchoolActivityDialogs = 0;
    private int currIndexSportActivityDialogs = 0;
    private int currIndexFreetimeActivityDialogs = 0;
    private int currIndexDoNotDisturbDialogs = 0;
    
    
    private SettingsData settingsData;
    private ActivitiesManager activitiesManager;
    
    private ActivityDialogType currActivityDialog = ActivityDialogType.CHOOSE_ACTIVITY_DIALOG;

    private enum ActivityDialogType {
        CHOOSE_ACTIVITY_DIALOG,
        SCHOOLACTIVITY_DIALOG,
        SPORTACTIVITY_DIALOG,
        FREETIMEACTIVITY_DIALOG,
        DONOTDISTURB_DIALOG,
        SUCCESS_DIALOG,
        ERROR_DIALOG
    }
    
    /**
     * Konstruktor prednastavi vsetky potrebne zobrazenia(tela dialogov) pre submenu pridavajuceho nove aktivity
     * @param settingsData Data s nastaveniami programu
     * @param activitiesManager Spravca dat s aktivitami
     */
    public ConsoleDialogBody_AddActivity(SettingsData settingsData, ActivitiesManager activitiesManager) {
        this.settingsData = settingsData;
        this.activitiesManager = activitiesManager;
        
        activitiesChooseMenu = new ConsoleDialogBodyChooseMenu(
            "Pridat aktivitu:",
            Arrays.asList(
                "Skolska aktivita",
                "Sportova aktivita",
                "Volny cas",
                "Doba nerusenia"
            ),
            new ConsoleUserInput_Navigation(false, false, true, false, false),
            new ConsoleUserInput_Question(false, false, 1, 4)
        );
        
        addActivitySuccess = new ConsoleDialogBodyInfo(
            "Aktivita bola zaradena do rozvrhu.",
            new ConsoleUserInput_Navigation(false, false, false, false, true),
            new ConsoleUserInput_Question(false, true, "moznost Dokoncit (f)")
        );
        
        addActivityProblem = new ConsoleDialogBodyInfo(
            "Aktivitu sa nepodarilo zaradit do rozvrhu.",
            new ConsoleUserInput_Navigation(false, true, false, false, false),
            new ConsoleUserInput_Question(false, true, "moznost Naspat (s)")
        ); 
        
        schoolActivityDialogs = Arrays.asList(
            new ConsoleDialogBody[] {
                new ConsoleDialogBodyChooseMenu(
                    "Zvolte druh skolskej aktivity:",
                    // Vlozime List retazcov reprezentujuci rozne typy skolskych aktivit
                    Arrays.asList(SchoolActivity.SchoolActivities.values()).stream()
                        .map(SchoolActivity.SchoolActivities::toString)
                        .collect(Collectors.toList()),
                    new ConsoleUserInput_Navigation(false, false, true, false, false),
                    new ConsoleUserInput_Question(false, false, 1, 9)
                ),
                new ConsoleDialogBodyAskForValue(
                    "Zadajte meno skolskej aktivity.",
                    new ConsoleUserInput_Navigation(false, true, true, false, false),
                    new ConsoleUserInput_Question(false, false, "meno skolskej aktivity")
                ),
                //Optional == true
                new ConsoleDialogBodyAskForValue(
                    "Zadajte kratky popis skolskej aktivity.",
                    new ConsoleUserInput_Navigation(false,true, true, true, false),
                    new ConsoleUserInput_Question(true, false, "popis skolskej aktivity")
                ),
                //Optional == true
                new ConsoleDialogBodyAskForValue(
                    "Zadajte miesto konania skolskej aktivity.",
                    new ConsoleUserInput_Navigation(false, true, true, true, false),
                    new ConsoleUserInput_Question(true, false, "miesto konania skolskej aktivity")
                ),
                //Optional == true
                new ConsoleDialogBodyAskForValue(
                    "Zadajte datum a cas skolskej aktivity. (Format: yyyy-MM-dd hh:mm)",
                    new ConsoleUserInput_Navigation(false, true, true, true, false),
                    new ConsoleUserInput_Question(true, false, "datum a cas skolskej aktivity", ConsoleUserInput.QuestionType.DATETIME)
                ),
                new ConsoleDialogBodyAskForValue(
                    "Zadajte dlzku trvania skolskej aktivity. (v minutach)",
                    new ConsoleUserInput_Navigation(false, true, true, false, false),
                    new ConsoleUserInput_Question(false, false, "dlzku trvania skolskej aktivity", ConsoleUserInput.QuestionType.NUMBER)
                ),
                //Optional
                new ConsoleDialogBodyAskForValue(
                    "Zadajte deadline splnenia skolskej aktivity. (Format: yyyy-MM-dd HH:mm)",
                    new ConsoleUserInput_Navigation(false, true, true, true, false),
                    new ConsoleUserInput_Question(true, false, "deadline splnenia skolskej aktivity", ConsoleUserInput.QuestionType.DATETIME)
                )
            }
        );
        
        sportActivityDialogs = Arrays.asList(
            new ConsoleDialogBody[] {
                new ConsoleDialogBodyChooseMenu(
                    "Zvolte druh sportovej aktivity:",
                    // Vlozime List retazcov reprezentujuci rozne typy sportovych aktivit
                    Arrays.asList(SportActivity.SportActivities.values()).stream()
                        .map(SportActivity.SportActivities::toString)
                        .collect(Collectors.toList()),
                    new ConsoleUserInput_Navigation(false, false, true, false, false),
                    new ConsoleUserInput_Question(false, false, 1, 25)
                ),
                //Optional == true
                new ConsoleDialogBodyAskForValue(
                    "Zadajte kratky popis sportovej aktivity.",
                    new ConsoleUserInput_Navigation(false, true, true, true, false),
                    new ConsoleUserInput_Question(true, false, "popis sportovej aktivity")
                ),
                //Optional == true
                new ConsoleDialogBodyAskForValue(
                    "Zadajte miesto konania sportovej aktivity.",
                    new ConsoleUserInput_Navigation(false, true, true, true, false),
                    new ConsoleUserInput_Question(true, false, "miesto konania sportovej aktivity")
                ),
                //Optional == true
                new ConsoleDialogBodyAskForValue(
                    "Zadajte datum a cas sportovej aktivity. (Format: yyyy-MM-dd hh:mm)",
                    new ConsoleUserInput_Navigation(false, true, true, true, false),
                    new ConsoleUserInput_Question(true, false,"datum a cas sportovej aktivity", ConsoleUserInput.QuestionType.DATETIME)
                ),
                new ConsoleDialogBodyAskForValue(
                    "Zadajte dlzku trvania sportovej aktivity. (v minutach)",
                    new ConsoleUserInput_Navigation(false, true, true, false, false),
                    new ConsoleUserInput_Question(false, false, "dlzku trvania sportovej aktivity", ConsoleUserInput.QuestionType.NUMBER)
                )
            }
        );
        
        freetimeActivityDialogs = Arrays.asList(
            new ConsoleDialogBody[] {
                new ConsoleDialogBodyAskForValue(
                    "Zadajte meno mimocasovej aktivity.",
                    new ConsoleUserInput_Navigation(false, false, true, false, false),
                    new ConsoleUserInput_Question(false, false, "meno mimocasovej aktivity")
                ),
                //Optional
                new ConsoleDialogBodyAskForValue(
                    "Zadajte miesto konania mimocasovej aktivity.",
                    new ConsoleUserInput_Navigation(false, true, true, true, false),
                    new ConsoleUserInput_Question(true, false, "miesto konania mimocasovej aktivity")
                ),
                new ConsoleDialogBodyAskForValue(
                    "Zadajte datum a cas pre zaciatok mimocasovej activity. (Format: yyyy-MM-dd HH:mm)",
                    new ConsoleUserInput_Navigation(false, true, true, false, false),
                    new ConsoleUserInput_Question(false, false, "datum a cas pre zaciatok mimocasovej activity", ConsoleUserInput.QuestionType.DATETIME)
                ),
                new ConsoleDialogBodyAskForValue(
                    "Zadajte dlzku trvania mimocasovej activity. (v minutach)",
                    new ConsoleUserInput_Navigation(false, true, true, false, false),
                    new ConsoleUserInput_Question(false, false, "dlzku trvania mimocasovej activity", ConsoleUserInput.QuestionType.NUMBER)
                )
            }
        );
        
        doNotDisturbDialogs = Arrays.asList(
            new ConsoleDialogBody[] {
                new ConsoleDialogBodyChooseMenu(
                    "Zvolte druh nerusenia:",
                    // Vlozime List retazcov reprezentujuci rozne typy aktivit nerusenia
                    Arrays.asList(DoNotDisturb.Events.values()).stream()
                        .map(DoNotDisturb.Events::toString)
                        .collect(Collectors.toList()),
                    new ConsoleUserInput_Navigation(false, false, true, false, false),
                    new ConsoleUserInput_Question(false, false, 1, 5)
                ),
                new ConsoleDialogBodyAskForValue(
                    "Zadajte typicky cas zaciatku, kedy si nezelate byt ruseny. (Format: HH:mm)",
                    new ConsoleUserInput_Navigation(false, true, true, false, false),
                    new ConsoleUserInput_Question(false, false, "cas zaciatku", ConsoleUserInput.QuestionType.TIME)
                ),
                
                new ConsoleDialogBodyAskForValue(
                    "Zadajte dlzku trvania nerusenia. (v minutach)",
                    new ConsoleUserInput_Navigation(false, true, true, false, false),
                    new ConsoleUserInput_Question(false, false, "dlzku trvania nerusenia", ConsoleUserInput.QuestionType.NUMBER)
                ),
                new ConsoleDialogBodyChooseMenu(
                    "Ako casto pocas tyzdna si nezelate byt ruseny pre zvoleny casovy usek:",
                    // Vlozime List retazcov reprezentujuci rozne typy opakovani aktivit nerusenia
                    Arrays.asList(DoNotDisturb.Repeat.values()).stream()
                        .map(DoNotDisturb.Repeat::toString)
                        .collect(Collectors.toList()),
                    new ConsoleUserInput_Navigation(false, false, true, false, false),
                    new ConsoleUserInput_Question(false, false, 1, 8)
                )
            }
        );
        
        
    }
    
    private boolean checkRangeLeft(int startIndex, int endIndex, int currIndex) {
        if (currIndex > startIndex && currIndex < endIndex) {
            return true;
        }
        
        return false;
    }
    
    private boolean checkRangeRight(int startIndex, int endIndex, int currIndex) {
        if (currIndex >= startIndex && currIndex < (endIndex - 1)) {
            return true;
        }
        
        return false;
    }
    
    private String tryAddToActivities(Activity a) {
        return activitiesManager.tryAddActivity(a);
    }
    
    private void handleSuccessDialogInput() {
        
    }
    
    private void manageEndOfDialog() {
        currIndexSchoolActivityDialogs = 0;
        currIndexSportActivityDialogs = 0;
        currIndexFreetimeActivityDialogs = 0;
        currIndexDoNotDisturbDialogs = 0;
        String activityDescription = null;
        ActivityStringRepresentation asr = null;
        
        String errorMsg;
        
        if ((errorMsg = tryAddToActivities(activityToAdd)) == null) {
            switch (currActivityDialog) {
                case SCHOOLACTIVITY_DIALOG: 
                    asr = ((SchoolActivity)activityToAdd).generateStringRepresentation(settingsData.getTimeZone());
                    addActivitySuccess.setTitle(addActivitySuccess.getTitle() + asr.getTitle()); break;
                case SPORTACTIVITY_DIALOG:
                    asr = ((SportActivity)activityToAdd).generateStringRepresentation(settingsData.getTimeZone());
                    addActivitySuccess.setTitle(addActivitySuccess.getTitle() + asr.getTitle()); break;
                case FREETIMEACTIVITY_DIALOG: 
                    asr = ((FreeTime)activityToAdd).generateStringRepresentation(settingsData.getTimeZone());
                    addActivitySuccess.setTitle(addActivitySuccess.getTitle() + asr.getTitle()); break;
                case DONOTDISTURB_DIALOG:
                    asr = ((DoNotDisturb)activityToAdd).generateStringRepresentation(settingsData.getTimeZone());
                    addActivitySuccess.setTitle(addActivitySuccess.getTitle() + asr.getTitle()); break;
            }
            
            currActivityDialog = ActivityDialogType.SUCCESS_DIALOG;
        } else {
            currActivityDialog = ActivityDialogType.ERROR_DIALOG;
            List<ConsoleDialogElement> errorDialogElements = new ArrayList<>();
            String[] errorMessages = errorMsg.split("\\r?\\n");
            
            for (String errorMessage : errorMessages) {
                errorDialogElements.add(new ConsoleDialogElement(ConsoleDialog.TextAlign.LEFT, errorMessage));
            }
            
            errorDialogElements.add(new ConsoleDialogElement(ConsoleDialog.DialogElementType.EMPTY_LINE));
            
            addActivityProblem.addConsoleDialogElements(errorDialogElements);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean setPrevious() {
        switch (currActivityDialog) {
            case CHOOSE_ACTIVITY_DIALOG: 
                return false;
            case SUCCESS_DIALOG:
                return false;
            case ERROR_DIALOG:
                if (checkRangeLeft(0, schoolActivityDialogs.size(), currIndexSchoolActivityDialogs)) {
                    currIndexSchoolActivityDialogs--;
                    return true;
                } else {
                    currIndexSchoolActivityDialogs = 0;
                    currActivityDialog = ActivityDialogType.CHOOSE_ACTIVITY_DIALOG;
                    return true;
                }
            case SCHOOLACTIVITY_DIALOG:
                if (checkRangeLeft(0, schoolActivityDialogs.size(), currIndexSchoolActivityDialogs)) {
                    currIndexSchoolActivityDialogs--;
                    return true;
                } else {
                    currIndexSchoolActivityDialogs = 0;
                    currActivityDialog = ActivityDialogType.CHOOSE_ACTIVITY_DIALOG;
                    return true;
                }
            case SPORTACTIVITY_DIALOG:
                if (checkRangeLeft(0, sportActivityDialogs.size(), currIndexSportActivityDialogs)) {
                    currIndexSportActivityDialogs--;
                    return true;
                } else { 
                    currIndexSportActivityDialogs = 0;
                    currActivityDialog = ActivityDialogType.CHOOSE_ACTIVITY_DIALOG;
                    return true;
                }
            case FREETIMEACTIVITY_DIALOG:
                if (checkRangeLeft(0, freetimeActivityDialogs.size(), currIndexFreetimeActivityDialogs)) {
                    currIndexFreetimeActivityDialogs--;
                    return true;
                } else { 
                    currIndexFreetimeActivityDialogs = 0;
                    currActivityDialog = ActivityDialogType.CHOOSE_ACTIVITY_DIALOG;
                    return true;
                }
            case DONOTDISTURB_DIALOG:
                if (checkRangeLeft(0, doNotDisturbDialogs.size(), currIndexDoNotDisturbDialogs)) {
                    currIndexDoNotDisturbDialogs--;
                } else { 
                    currIndexDoNotDisturbDialogs = 0;
                    currActivityDialog = ActivityDialogType.CHOOSE_ACTIVITY_DIALOG;
                    return true;
                }
        }
        
        return true;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean setNext() {
        switch (currActivityDialog) {
            case CHOOSE_ACTIVITY_DIALOG: 
                return false;
            case SUCCESS_DIALOG:
                currActivityDialog = ActivityDialogType.CHOOSE_ACTIVITY_DIALOG;
                return true;
            case ERROR_DIALOG:
                currActivityDialog = ActivityDialogType.CHOOSE_ACTIVITY_DIALOG;
                return true;
            case SCHOOLACTIVITY_DIALOG:
                if (checkRangeRight(0, schoolActivityDialogs.size(), currIndexSchoolActivityDialogs)) {
                    currIndexSchoolActivityDialogs++;
                    return true;
                } else {
                    manageEndOfDialog();
                    return true;
                }
            case SPORTACTIVITY_DIALOG:
                if (checkRangeRight(0, sportActivityDialogs.size(), currIndexSportActivityDialogs)) {
                    currIndexSportActivityDialogs++;
                    return true;
                } else { 
                    manageEndOfDialog();
                    return true;
                }
            case FREETIMEACTIVITY_DIALOG:
                if (checkRangeRight(0, freetimeActivityDialogs.size(), currIndexFreetimeActivityDialogs)) {
                    currIndexFreetimeActivityDialogs++;
                    return true;
                } else { 
                    manageEndOfDialog();
                    return true;
                }
            case DONOTDISTURB_DIALOG:
                if (checkRangeRight(0, doNotDisturbDialogs.size(), currIndexDoNotDisturbDialogs)) {
                    currIndexDoNotDisturbDialogs++;
                    return true;
                } else { 
                    manageEndOfDialog();
                    return true;
                }
        }
        return true;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public ConsoleDialogBody getCurr() {
        switch (currActivityDialog) {
            case CHOOSE_ACTIVITY_DIALOG: return activitiesChooseMenu;
            case SCHOOLACTIVITY_DIALOG: return schoolActivityDialogs.get(currIndexSchoolActivityDialogs);
            case SPORTACTIVITY_DIALOG: return sportActivityDialogs.get(currIndexSportActivityDialogs);
            case FREETIMEACTIVITY_DIALOG: return freetimeActivityDialogs.get(currIndexFreetimeActivityDialogs);
            case DONOTDISTURB_DIALOG: return doNotDisturbDialogs.get(currIndexDoNotDisturbDialogs);
            case SUCCESS_DIALOG: return addActivitySuccess;
            case ERROR_DIALOG: return addActivityProblem;
            default: return activitiesChooseMenu;
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
        Integer answerInteger = ConsoleUserInput.ChooseOption(navigation, question.preFillInputWith, question.startRange, question.endRange);
                
        //Zistime ci nebol zadany: exit, back, cancel, skip, finish
        if (answerInteger == null && navigation != null) {
            return handleNavigationToInstruction(navigation);
        }

        if (answerInteger != null) {
            //Ak sme v zakladnom menu pre pridanie aktivit, podla zvolenej aktvity sa vyberie rozsirena instancia aktivity(Skola, Sport, Volny cas, Nerusit)
            if (currActivityDialog == ActivityDialogType.CHOOSE_ACTIVITY_DIALOG) {
                
                switch (answerInteger) {
                    case 1: 
                        currActivityDialog = ActivityDialogType.SCHOOLACTIVITY_DIALOG;
                        activityToAdd = new SchoolActivity();
                        return ConsoleDialog.Instructions.REPAINT;
                    case 2:
                        currActivityDialog = ActivityDialogType.SPORTACTIVITY_DIALOG;
                        activityToAdd = new SportActivity();
                        return ConsoleDialog.Instructions.REPAINT;
                    case 3:
                        currActivityDialog = ActivityDialogType.FREETIMEACTIVITY_DIALOG;
                        activityToAdd = new FreeTime();
                        return ConsoleDialog.Instructions.REPAINT;
                    case 4:
                        currActivityDialog = ActivityDialogType.DONOTDISTURB_DIALOG;
                        activityToAdd = new DoNotDisturb();
                        return ConsoleDialog.Instructions.REPAINT;
                }
            }
            
            //Ak je jeden z vyplnacich dialogov prave otvoreny
            switch (currActivityDialog) {
                case SCHOOLACTIVITY_DIALOG: 
                    ((SchoolActivity)activityToAdd).setSchoolActivityType(SchoolActivity.SchoolActivities.values()[answerInteger - 1]);
                    return setNext() ? ConsoleDialog.Instructions.REPAINT : ConsoleDialog.Instructions.NOPAINT;
                    
                case SPORTACTIVITY_DIALOG: 
                    ((SportActivity)activityToAdd).setSportActivityType(SportActivity.SportActivities.values()[answerInteger - 1]);
                    return setNext() ? ConsoleDialog.Instructions.REPAINT : ConsoleDialog.Instructions.NOPAINT;
                    
                case DONOTDISTURB_DIALOG:
                    switch (currIndexDoNotDisturbDialogs) {
                        //Index dialogu v poradi ako idu dialogy pre vyber
                        case 0: 
                            ((DoNotDisturb)activityToAdd).setEvent(DoNotDisturb.Events.values()[answerInteger - 1]);
                            return setNext() ? ConsoleDialog.Instructions.REPAINT : ConsoleDialog.Instructions.NOPAINT;
                        //Index dialogu v poradi ako idu dialogy pre vyber
                        case 3:
                            ((DoNotDisturb)activityToAdd).setRepeat(DoNotDisturb.Repeat.values()[answerInteger - 1]);
                            return setNext() ? ConsoleDialog.Instructions.REPAINT : ConsoleDialog.Instructions.NOPAINT;
                    }
            }
        }

        return ConsoleDialog.Instructions.NOPAINT;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public ConsoleDialog.Instructions handleNumber(ConsoleUserInput_NavigationSelect navigation, ConsoleUserInput_Question question) {
        Integer answerInteger = ConsoleUserInput.Number(navigation, question.preFillInputWith);
                
        //Zistime ci nebol zadany: exit, back, cancel, skip, finish
        if (answerInteger == null && navigation != null) {
            return handleNavigationToInstruction(navigation);
        }

        if (answerInteger != null) {
            switch(currActivityDialog) {
                case SCHOOLACTIVITY_DIALOG:
                    if (currIndexSchoolActivityDialogs == 5) {
                        ((SchoolActivity)activityToAdd).setDuration(answerInteger);
                        return setNext() ? ConsoleDialog.Instructions.REPAINT : ConsoleDialog.Instructions.NOPAINT;
                    }
                case SPORTACTIVITY_DIALOG:
                    if (currIndexSportActivityDialogs == 4) {
                        ((SportActivity)activityToAdd).setDuration(answerInteger);
                        return setNext() ? ConsoleDialog.Instructions.REPAINT : ConsoleDialog.Instructions.NOPAINT;
                    }
                case FREETIMEACTIVITY_DIALOG:
                    if (currIndexFreetimeActivityDialogs == 3) {
                        ((FreeTime)activityToAdd).setDuration(answerInteger);
                        return setNext() ? ConsoleDialog.Instructions.REPAINT : ConsoleDialog.Instructions.NOPAINT;
                    }
                case DONOTDISTURB_DIALOG:
                    if (currIndexDoNotDisturbDialogs == 2) {
                        ((DoNotDisturb)activityToAdd).setDuration(answerInteger);
                        return setNext() ? ConsoleDialog.Instructions.REPAINT : ConsoleDialog.Instructions.NOPAINT;
                    }
            }
        }
        
        return ConsoleDialog.Instructions.NOPAINT;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public ConsoleDialog.Instructions handleText(ConsoleUserInput_NavigationSelect navigation, ConsoleUserInput_Question question) {
        String answerString;
        
        // Ak bude vstup viacznakovy v dialogu SUCCESS_DIALOG alebo ERROR_DIALOG, tak poziadame uzivatela a znovu zadanie moznosti
        while (true) {
            answerString = ConsoleUserInput.Text(navigation, question.preFillInputWith, question.errorMessage);

            //Zistime ci nebol zadany: exit, back, cancel, skip, finish
            if (answerString == null && navigation != null) {
                return handleNavigationToInstruction(navigation);
            }

            if (answerString != null) {
                switch(currActivityDialog) {
                    case SCHOOLACTIVITY_DIALOG:
                        switch (currIndexSchoolActivityDialogs) {
                            case 1: 
                                ((SchoolActivity)activityToAdd).setName(answerString);
                                return setNext() ? ConsoleDialog.Instructions.REPAINT : ConsoleDialog.Instructions.NOPAINT;
                            case 2:
                                ((SchoolActivity)activityToAdd).setDescription(answerString);
                                return setNext() ? ConsoleDialog.Instructions.REPAINT : ConsoleDialog.Instructions.NOPAINT;
                            case 3:
                                ((SchoolActivity)activityToAdd).setLocation(answerString);
                                return setNext() ? ConsoleDialog.Instructions.REPAINT : ConsoleDialog.Instructions.NOPAINT;
                        }
                    case SPORTACTIVITY_DIALOG:
                        switch (currIndexSportActivityDialogs) {
                            case 1:
                                ((SportActivity)activityToAdd).setDescription(answerString);
                                return setNext() ? ConsoleDialog.Instructions.REPAINT : ConsoleDialog.Instructions.NOPAINT;
                            case 2:
                                ((SportActivity)activityToAdd).setLocation(answerString);
                                return setNext() ? ConsoleDialog.Instructions.REPAINT : ConsoleDialog.Instructions.NOPAINT;
                        }
                    case FREETIMEACTIVITY_DIALOG:
                        switch (currIndexFreetimeActivityDialogs) {
                            case 0:
                                ((FreeTime)activityToAdd).setName(answerString);
                                return setNext() ? ConsoleDialog.Instructions.REPAINT : ConsoleDialog.Instructions.NOPAINT;
                            case 1:
                                ((FreeTime)activityToAdd).setLocation(answerString);
                                return setNext() ? ConsoleDialog.Instructions.REPAINT : ConsoleDialog.Instructions.NOPAINT;
                        }
                    case SUCCESS_DIALOG:
                        if (answerString.length() > 1) {
                            System.out.print("ERROR: Zadajte prosim moznost Dokoncit (f).\n> ");
                            continue;
                        }
                        currActivityDialog = ActivityDialogType.CHOOSE_ACTIVITY_DIALOG;
                        return setNext() ? ConsoleDialog.Instructions.REPAINT : ConsoleDialog.Instructions.NOPAINT;
                    case ERROR_DIALOG:
                        if (answerString.length() > 1) {
                            System.out.print("ERROR: Zadajte prosim moznost Naspat (b).\n> ");
                            continue;
                        }
                        currActivityDialog = ActivityDialogType.CHOOSE_ACTIVITY_DIALOG;
                        return setNext() ? ConsoleDialog.Instructions.REPAINT : ConsoleDialog.Instructions.NOPAINT;
                }
            }
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public ConsoleDialog.Instructions handleTime(ConsoleUserInput_NavigationSelect navigation, ConsoleUserInput_Question question) {
        LocalTime answerTime = ConsoleUserInput.Time(navigation, question.preFillInputWith);
                
        //Zistime ci nebol zadany: exit, back, cancel, skip, finish
        if (answerTime == null && navigation != null) {
            return handleNavigationToInstruction(navigation);
        }
        
        //Porovname to s aktualnym casom v UTC      
        if (answerTime != null) {
            if (currActivityDialog == ActivityDialogType.DONOTDISTURB_DIALOG &&
                currIndexDoNotDisturbDialogs == 1) {
                
                ((DoNotDisturb)activityToAdd).setStartT(answerTime.minusHours(settingsData.getTimeZone()));
                return setNext() ? ConsoleDialog.Instructions.REPAINT : ConsoleDialog.Instructions.NOPAINT;
            }
        
        }
        
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
            //Ak je jeden z vyplnacich dialogov prave otvoreny
            switch (currActivityDialog) {
                case SCHOOLACTIVITY_DIALOG: 
                    switch (currIndexSchoolActivityDialogs) {
                        case 4: 
                            ((SchoolActivity)activityToAdd).setStartDT(answerDateTime.minusHours(settingsData.getTimeZone()));
                            return setNext() ? ConsoleDialog.Instructions.REPAINT : ConsoleDialog.Instructions.NOPAINT;
                        case 6:
                            ((SchoolActivity)activityToAdd).setDeadline(answerDateTime.minusHours(settingsData.getTimeZone()));
                            return setNext() ? ConsoleDialog.Instructions.REPAINT : ConsoleDialog.Instructions.NOPAINT;
                    }
                    
                case SPORTACTIVITY_DIALOG: 
                    if (currIndexSportActivityDialogs == 3) {
                        ((SportActivity)activityToAdd).setStartDT(answerDateTime.minusHours(settingsData.getTimeZone()));
                        return setNext() ? ConsoleDialog.Instructions.REPAINT : ConsoleDialog.Instructions.NOPAINT;
                    }
                    return ConsoleDialog.Instructions.NOPAINT;
                    
                case FREETIMEACTIVITY_DIALOG:
                    if (currIndexFreetimeActivityDialogs == 2) {
                        ((FreeTime)activityToAdd).setStartDT(answerDateTime.minusHours(settingsData.getTimeZone()));
                        return setNext() ? ConsoleDialog.Instructions.REPAINT : ConsoleDialog.Instructions.NOPAINT;
                    }
                    return ConsoleDialog.Instructions.NOPAINT;
                    
                case DONOTDISTURB_DIALOG:
                    switch (currIndexDoNotDisturbDialogs) {
                        case 0: 
                    
                            return setNext() ? ConsoleDialog.Instructions.REPAINT : ConsoleDialog.Instructions.NOPAINT;
                        case 3:
                    
                            return setNext() ? ConsoleDialog.Instructions.REPAINT : ConsoleDialog.Instructions.NOPAINT;
                    }
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
            case YES_NO: return handleYesNo(userInput_NavigationSelected, userInput_Question);
            case CHOOSE_OPTION: return handleChooseOption(userInput_NavigationSelected, userInput_Question);
            case NUMBER: return handleNumber(userInput_NavigationSelected, userInput_Question);
            case TEXT: return handleText(userInput_NavigationSelected, userInput_Question);
            case TIME: return handleTime(userInput_NavigationSelected, userInput_Question);
            case DATETIME: return handleDateTime(userInput_NavigationSelected, userInput_Question);
            default: return ConsoleDialog.Instructions.NOPAINT;
        }
    }
}