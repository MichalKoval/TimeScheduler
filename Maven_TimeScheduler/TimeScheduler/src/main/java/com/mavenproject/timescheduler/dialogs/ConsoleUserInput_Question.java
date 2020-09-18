/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mavenproject.timescheduler.dialogs;

/**
 * Trieda reprezentuje typ otazky, aky bude polozeny uzivatelovi na zaklade dialogu alebo menu, v ktorom sa nachadza.
 * Pozadovana hodnota na vstupe moze byt povinna alebo nepovinna.
 * Popripade sa nastavi text spravy o chybem ktory sa ma zobrazit uzivatelovi, ake nezadal spravnu hodnotu.
 * Napr. aj je nespravne: Y/N, y/n, ..., ciselna hodnota z nejakeho intervalu, prazdny text, nespravny casovy udaj, atd, ...
 */
public class ConsoleUserInput_Question {
    public ConsoleUserInput.QuestionType type = null;
    public String preFillInputWith = null;
    public String errorMessage = null;
    public int startRange = 0;
    public int endRange = 0;
    public boolean optional = false;
    public boolean requireNavigationSelection = false;
    
    /**
     * Konstruktor nastavi otazku typu: YES/NO
     * @param optional True ak si zelame, aby zadana hodnota bola volitelna.
     * @param requireNavigationSelection True, ak pozadujeme, aby uzivatel vybral spravne odpoved na vyber navigacneho prikazu. (Exit, Naspat, Zrusit, Preskocit, Dokoncit)
     */
    public ConsoleUserInput_Question(boolean optional, boolean requireNavigationSelection) {
            this.type = ConsoleUserInput.QuestionType.YES_NO;
            this.optional = optional;
            this.requireNavigationSelection = requireNavigationSelection;
    }
    /**
     * Konstruktor nastavi otazku typu: textovy vstup.
     * @param optional True ak si zelame, aby zadana hodnota bola volitelna.
     * @param requireNavigationSelection True, ak pozadujeme, aby uzivatel vybral spravne odpoved na vyber navigacneho prikazu. (Exit, Naspat, Zrusit, Preskocit, Dokoncit)
     * @param errorMsg Retazec s chybou, ak bol na vstupe nekorektny text, napr. prazdny text.
     */
    public ConsoleUserInput_Question(boolean optional, boolean requireNavigationSelection, String errorMsg) {
            this.type = ConsoleUserInput.QuestionType.TEXT;
            this.optional = optional;
            this.requireNavigationSelection = requireNavigationSelection;
            this.errorMessage = errorMsg;
    }
    /**
     * Konstruktor nastavi otazku typu: vyberte polozku zo zadaneho intervalu poloziek.
     * @param optional True ak si zelame, aby zadana hodnota bola volitelna.
     * @param requireNavigationSelection True, ak pozadujeme, aby uzivatel vybral spravne odpoved na vyber navigacneho prikazu. (Exit, Naspat, Zrusit, Preskocit, Dokoncit)
     * @param startRange Zaciatok intervalu poloziek.
     * @param endRange Koniec intervalu poloziek.
     */
    public ConsoleUserInput_Question(boolean optional, boolean requireNavigationSelection, int startRange, int endRange) {
            this.type = ConsoleUserInput.QuestionType.CHOOSE_OPTION;
            this.optional = optional;
            this.requireNavigationSelection = requireNavigationSelection;
            this.startRange = startRange;
            this.endRange = endRange;
    }
    /**
     * Konstruktor nastavi otazku typu: zadajte casovy udaj, alebo datum a cas zaroven.
     * @param optional True ak si zelame, aby zadana hodnota bola volitelna.
     * @param requireNavigationSelection True, ak pozadujeme, aby uzivatel vybral spravne odpoved na vyber navigacneho prikazu. (Exit, Naspat, Zrusit, Preskocit, Dokoncit)
     * @param errorMsg Retazec s chybou, ak bol na vstupe nekorektny text, napr. zly casovy udaj, zly datum a cas zaroven.
     * @param questionType Typicke ide o dva typy otazok: zadajte cas (TIME), alebo zadajde datum a cas zaroven(DATETIME).
     */    
    public ConsoleUserInput_Question(boolean optional, boolean requireNavigationSelection, String errorMsg, ConsoleUserInput.QuestionType questionType) {
            this.type = questionType;
            this.optional = optional;
            this.requireNavigationSelection = requireNavigationSelection;
    }
}