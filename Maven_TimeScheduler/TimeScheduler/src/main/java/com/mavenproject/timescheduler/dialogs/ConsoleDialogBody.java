package com.mavenproject.timescheduler.dialogs;

import java.util.List;

/**
 * Trieda reprezentuje elementy, ktore budu vygenerovane a zobrazene v tele dialogu. Je implementaciou intefacu IConsoleDialogBody.
 * Kazde subzobrazenie v dialogu je zlozene prave z intancii ConsoleDialogBody.
 */
public class ConsoleDialogBody implements IConsoleDialogBody {
    private String title = null;
    private ConsoleUserInput_Navigation userInput_Nagination = null;
    private ConsoleUserInput_Question userInput_Question = null;
    private List<ConsoleDialogElement> consoleDialogElements = null;
    
    /**
     * Zakladny konstruktor
     */
    public ConsoleDialogBody() {
    }
    /**
     * Konstruktor nastavuje nadpis v tele dialogu, typicky nadpis pre nejake subzobrazenie.
     * @param title Retazec s nadpisom
     */
    public ConsoleDialogBody(String title) {
        this.title = title;
    }
    public ConsoleDialogBody(
            String title,
            ConsoleUserInput_Navigation userInputNagination,
            ConsoleUserInput_Question userInputQuestion
    ) {
        this.title = title;
        this.userInput_Nagination = userInputNagination;
        this.userInput_Question = userInputQuestion;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle() {
        return title;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public ConsoleUserInput_Navigation getUserInputNavigation() {
        return userInput_Nagination;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public ConsoleUserInput_Question getUserInputQuestion() {
        return userInput_Question;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasExitOption() {
        return this.userInput_Nagination.isExit();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasBackOption() {
        return this.userInput_Nagination.isBack();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasCancelOption() {
        return this.userInput_Nagination.isCancel();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasSkipOption() {
        return this.userInput_Nagination.isSkip();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasFinishOption() {
        return this.userInput_Nagination.isFinish();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public List<ConsoleDialogElement> getExtraDialogElements() {
        if (consoleDialogElements != null && consoleDialogElements.size() > 0) {
            return consoleDialogElements;
        }
        
        return null;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setTitle(String title) {
        this.title = title;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void setUserInputQuestion(ConsoleUserInput_Question question) {
        this.userInput_Question = question;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void addConsoleDialogElements(List<ConsoleDialogElement> consoleDialogElements) {
        this.consoleDialogElements = consoleDialogElements;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void enableExitOption(boolean b) {
        this.userInput_Nagination.setExit(b);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void enableBackOption(boolean b) {
        this.userInput_Nagination.setBack(b);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void enableCancelOption(boolean b) {
        this.userInput_Nagination.setCancel(b);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void enableSkipOption(boolean b) {
        this.userInput_Nagination.setSkip(b);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void enableFinishOption(boolean b) {
        this.userInput_Nagination.setFinish(b);
    }
}
