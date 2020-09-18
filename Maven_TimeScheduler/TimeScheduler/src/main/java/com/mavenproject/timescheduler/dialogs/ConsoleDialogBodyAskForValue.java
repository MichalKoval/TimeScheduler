package com.mavenproject.timescheduler.dialogs;

/**
 * Trieda reprezentuje menu s popisom toho, aky udaj od uzivatela potrebuje na vstupe zadat a otazky.
 * Otazkou rozumieme typ textoveho vstupu, aky od uzivatela ocakavame. (y/s; ciselny vstup, textovy vstup, vyber moznosti, casovy udaj).
 * @see com.mavenproject.timescheduler.dialogs.ConsoleUserInput_Question
 */
public class ConsoleDialogBodyAskForValue extends ConsoleDialogBody {
    
    /**
     * Zakladny konstruktor
     */
    public ConsoleDialogBodyAskForValue() {
    
    }
    /**
     * Konstruktor nastavujuci typ otazky, ktory bude polozeny uzivatelovi,
     * @param questionTitle Typ otazky.
     */
    public ConsoleDialogBodyAskForValue(String questionTitle) {
        super(questionTitle);
    }
    /**
     * Konstruktor nastavi: Nadpis menu, navigacne prikazy a typ otazky, ktory bude polozeny uzivatelovi.
     * @param questionTitle Nadpis v menu == popis hodnoty aku vyzadujeme.
     * @param userInputNagination Navigacny prikaz, ak bol uzivatelom zvoleny.
     * @param userInputQuestion Typ otazky polozeny uzivatelovi.
     */
    public ConsoleDialogBodyAskForValue(
            String questionTitle,
            ConsoleUserInput_Navigation userInputNagination,
            ConsoleUserInput_Question userInputQuestion
    ) {
        super(questionTitle, userInputNagination, userInputQuestion);
    }
}
