package com.mavenproject.timescheduler.dialogs;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Trieda reprezentuje menu pre vyber poloziek. Pozostava z nadpisu, poliziek a otazky, ktora bude polozena uzivatelovi.
 * Otazkou rozumieme typ textoveho vstupu, aky od uzivatela ocakavame. (y/s; ciselny vstup, textovy vstup, vyber moznosti, casovy udaj).
 * @see com.mavenproject.timescheduler.dialogs.ConsoleUserInput_Question
 */
public class ConsoleDialogBodyChooseMenu extends ConsoleDialogBody {
    private String menuTitle = null;
    private List<String> menuOptions = null;   
    
    /**
     * Zakladny konstrktor
     */
    public ConsoleDialogBodyChooseMenu() {
    
    }
    /**
     * Konstruktor nastavi: Nadpis menu pre vyber poloziek
     * @param menuTitle Nadpis menu
     */
    public ConsoleDialogBodyChooseMenu(String menuTitle) {
        super(menuTitle);
        this.menuTitle = menuTitle;
    }
    /**
     * Konstruktor nastavi: Nadpis menu a List poloziek pre vyber
     * @param menuTitle Nadpis menu
     * @param menuOptions List poloziek pre vyber
     */
    public ConsoleDialogBodyChooseMenu(String menuTitle, List<String> menuOptions) {
        super(menuTitle);
        this.menuOptions = menuOptions;
    }
    /**
     * Konstruktor nastavi: Nadpis menu, navigacne prikazy a typ otazky, ktory bude polozeny uzivatelovi.
     * @param menuTitle Nadpis v menu == popis hodnoty aku vyzadujeme.
     * @param userInputNagination Navigacny prikaz, ak bol uzivatelom zvoleny
     * @param userInputQuestion Typ otazky polozeny uzivatelovi
     */
    public ConsoleDialogBodyChooseMenu(
            String menuTitle,
            ConsoleUserInput_Navigation userInputNagination,
            ConsoleUserInput_Question userInputQuestion
    ) {
        super(menuTitle, userInputNagination, userInputQuestion);
    }
    /**
     * Konstruktor nastavi: Nadpis menu, list poloziek pre vyber, navigacne prikazy a typ otazky, ktory bude polozeny uzivatelovi.
     * @param menuTitle Nadpis v menu == popis hodnoty aku vyzadujeme.
     * @param menuOptions List poloziek pre vyber
     * @param userInputNagination Navigacny prikaz, ak bol uzivatelom zvoleny
     * @param userInputQuestion Typ otazky polozeny uzivatelovi
     */
    public ConsoleDialogBodyChooseMenu(
            String menuTitle,
            List<String> menuOptions,
            ConsoleUserInput_Navigation userInputNagination,
            ConsoleUserInput_Question userInputQuestion
    ) {
        super(menuTitle, userInputNagination, userInputQuestion);
        this.menuOptions = menuOptions;
    } 
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<ConsoleDialogElement> getConsoleDialogElements() {
        List<ConsoleDialogElement> consoleDialogElements = new ArrayList<>();
        
        try {
            consoleDialogElements.add(new ConsoleDialogElement(ConsoleDialog.DialogElementType.EMPTY_LINE));
            consoleDialogElements.add(new ConsoleDialogElement(ConsoleDialog.TextAlign.LEFT, getTitle()));
            
            if (menuOptions != null) {
                for (int i = 0; i < menuOptions.size(); i++) {
                    consoleDialogElements.add(new ConsoleDialogElement(ConsoleDialog.TextAlign.LEFT, " " + (i + 1) + ") " + menuOptions.get(i)));
                }

                consoleDialogElements.add(new ConsoleDialogElement(ConsoleDialog.DialogElementType.EMPTY_LINE));
            }
            
            if (hasExitOption()) { consoleDialogElements.add(new ConsoleDialogElement(ConsoleDialog.TextAlign.LEFT, " e) Exit")); }
            if (hasBackOption()) { consoleDialogElements.add(new ConsoleDialogElement(ConsoleDialog.TextAlign.LEFT, " b) Naspat")); }
            if (hasCancelOption()) { consoleDialogElements.add(new ConsoleDialogElement(ConsoleDialog.TextAlign.LEFT, " c) Zrusit")); }
            if (hasSkipOption()) { consoleDialogElements.add(new ConsoleDialogElement(ConsoleDialog.TextAlign.LEFT, " s) Preskocit")); }
            if (hasFinishOption()) { consoleDialogElements.add(new ConsoleDialogElement(ConsoleDialog.TextAlign.LEFT, " f) Dokoncit")); }
            
            consoleDialogElements.add(new ConsoleDialogElement(ConsoleDialog.DialogElementType.EMPTY_LINE));
            
            
        } catch (NullPointerException | InvalidParameterException ex) {
            Logger.getLogger(ConsoleDialogBodyChooseMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return consoleDialogElements;
    }
    
    /**
     * Metoda nastavi zoznam poloziek, ktore chcem mat v menu s moznostou vyberu.
     * Napr.: 
     * <p>Vyberte moznost:</p>
     * <p> 1) Zobrazit aktivity</p>
     * <p> 2) Pridat aktivitu</p>
     * <p> 3) Nastavenia</p>
     * @param menuOptions List retazcov s polozkami
     */
    public void setMenuOptions(List<String> menuOptions) {
        this.menuOptions = menuOptions;
    }
}
