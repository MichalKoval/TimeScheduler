/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mavenproject.timescheduler.dialogs;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Interface popisujuci to, z coho bude pozostavat telo dialogu, ake elementy bude obsahovat.
 * Interface obsahuje aj defaultnu metodu, ktora generuje elementy dialogu.
 */
public interface IConsoleDialogBody {
    
    /**
     * Metoda vrati nadpis zobrazeny v subzobrazeni.
     * @return Textovy retazec reprezentujuci nadpis.
     */
    public String getTitle();
    /**
     * Metoda vrati flags, pre navigacne prikazy, ktore bude mozne uzivatel vramci menu zvolit.
     * @return Flags pre navigacne prikazy.
     */
    public ConsoleUserInput_Navigation getUserInputNavigation();
    /**
     * Metoda vrati triedu reprezentujucu otazku, ktora bude polozena uzivatelovi v menu/dialogu
     * @return Trieda s otazkou pre uzivatela na vstupe
     */
    public ConsoleUserInput_Question getUserInputQuestion();
    /**
     * Metoda vrati true, ak je v menu zobrazena moznost = Exit
     * @return True, ak bude zobrazeny prikaz = Exit
     */
    public boolean hasExitOption();
    /**
     * Metoda vrati true, ak je v menu zobrazena moznost = Naspat
     * @return True, ak bude zobrazeny prikaz = Naspat
     */
    public boolean hasBackOption();
    /**
     * Metoda vrati true, ak je v menu zobrazena moznost = Zrusit
     * @return True, ak bude zobrazeny prikaz = Zrusit
     */
    public boolean hasCancelOption();
    /**
     * Metoda vrati true, ak je v menu zobrazena moznost = Preskocit
     * @return True, ak bude zobrazeny prikaz = Preskocit
     */
    public boolean hasSkipOption();
    /**
     * Metoda vrati true, ak je v menu zobrazena moznost = Dokoncit
     * @return True, ak bude zobrazeny prikaz = Dokoncit
     */
    public boolean hasFinishOption();
    /**
     * Metoda vrati elementy, ktore boli pridane navyse do menu/dialogu.
     * @return List elementov, ktore boli pridane navyse.
     */
    public List<ConsoleDialogElement> getExtraDialogElements();
    
    /**
     * Metoda nastavi nadpis zobrazeny v subzobrazeni.
     * @param title Textovy retazec reprezentujuci nadpis.
     */
    public void setTitle(String title);
    /**
     * Metoda nastavi triedu reprezentujucu otazku, ktora bude polozena uzivatelovi v menu/dialogu
     * @param question Trieda s otazkou pre uzivatela na vstupe
     */
    public void setUserInputQuestion(ConsoleUserInput_Question question);
    /**
     * Metoda nastavi, ze chceme mat v menu zobrazeny navigacny prikaz = Exit
     * @param b, True ak chceme aby bol zobrazeny prikaz = Exit
     */
    public void enableExitOption(boolean b);
    /**
     * Metoda nastavi, ze chceme mat v menu zobrazeny navigacny prikaz = Naspat
     * @param b, True ak chceme aby bol zobrazeny prikaz = Naspat
     */
    public void enableBackOption(boolean b);
    /**
     * Metoda nastavi, ze chceme mat v menu zobrazeny navigacny prikaz = Zrusit
     * @param b, True ak chceme aby bol zobrazeny prikaz = Zrusit
     */
    public void enableCancelOption(boolean b);
    /**
     * Metoda nastavi, ze chceme mat v menu zobrazeny navigacny prikaz = Preskocit
     * @param b, True ak chceme aby bol zobrazeny prikaz = Preskocit
     */
    public void enableSkipOption(boolean b);
    /**
     * Metoda nastavi, ze chceme mat v menu zobrazeny navigacny prikaz = Dokoncit
     * @param b, True ak chceme aby bol zobrazeny prikaz = Dokoncit
     */
    public void enableFinishOption(boolean b);
    
    /**
     * Metoda vygeneruje dilcie elementy, ktore sa budu nasledne vykreslene v dialogu metody printDialog() v triede ConsoleDialog.
     * @return List dilcich elementov: prazdny riadok, oddelovac, textovy riadok.
     */
    public default List<ConsoleDialogElement> getConsoleDialogElements() {
        List<ConsoleDialogElement> consoleDialogElements = new ArrayList<>();
        
        try {
            consoleDialogElements.add(new ConsoleDialogElement(ConsoleDialog.DialogElementType.EMPTY_LINE));
            consoleDialogElements.add(new ConsoleDialogElement(ConsoleDialog.TextAlign.LEFT, getTitle()));
            consoleDialogElements.add(new ConsoleDialogElement(ConsoleDialog.DialogElementType.EMPTY_LINE));
            
            List<ConsoleDialogElement> extraDialogElements;
            if ((extraDialogElements = getExtraDialogElements()) != null) {
                consoleDialogElements.addAll(extraDialogElements);
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
     * Metoda prida pomedzi vygenerovane dilcie elementy dialogu este dalsie elementy.
     * Typicky ak treba do predpripraveneho menu este pridat nejake elementy obsahujuce informacie navyse.
     * Je to v situaciach, kedy je lepsie nemenit cele menu, ak chceme pridat nejaky riadok.
     * 
     * @param consoleDialogElements Zoznam elementov, ktore sa dodatocne pridaju do menu/dialogu
     */
    public default void addConsoleDialogElements(List<ConsoleDialogElement> consoleDialogElements) { }
}
