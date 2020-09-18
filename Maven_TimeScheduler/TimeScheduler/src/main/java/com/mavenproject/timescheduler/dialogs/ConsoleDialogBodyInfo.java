package com.mavenproject.timescheduler.dialogs;

/**
 * Trieda reprezentuje menu z informaciou. Pozostava z kratkeho textu s informaciou a otazky, ktora bude polozena uzivatelovi.
 * Otazkou rozumieme typ textoveho vstupu, aky od uzivatela ocakavame. (y/s; ciselny vstup, textovy vstup, vyber moznosti, casovy udaj)
 * @see com.mavenproject.timescheduler.dialogs.ConsoleUserInput_Question
 */
public class ConsoleDialogBodyInfo extends ConsoleDialogBody {
    
    /**
     * Zakladny konstruktor
     */
    public ConsoleDialogBodyInfo() {
    
    }
    /**
     * Konstruktor nastavi: Nadpis menu == priamo textova informacia.
     * @param infoTitle Text s informaciou na zobrazenie.
     */
    public ConsoleDialogBodyInfo(String infoTitle) {
        super(infoTitle);
    }
    /**
     * Konstruktor nastavi: Nadpis menu == priamo textova informacia, navigacne prikazy a typ otazky, ktory bude polozeny uzivatelovi.
     * @param infoTitle Nadpis v menu == popis hodnoty aku vyzadujeme.
     * @param userInputNagination Navigacny prikaz, ak bol uzivatelom zvoleny
     * @param userInputQuestion Typ otazky polozeny uzivatelovi
     */
    public ConsoleDialogBodyInfo(
            String infoTitle,
            ConsoleUserInput_Navigation userInputNagination,
            ConsoleUserInput_Question userInputQuestion
    ) {
        super(infoTitle, userInputNagination, userInputQuestion);
    }
}
