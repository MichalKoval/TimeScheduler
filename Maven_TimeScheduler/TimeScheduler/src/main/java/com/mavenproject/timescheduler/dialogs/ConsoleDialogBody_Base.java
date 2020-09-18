/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mavenproject.timescheduler.dialogs;

import java.util.List;

/**
 *
 * @author Michal
 */
public abstract class ConsoleDialogBody_Base {
        
    public ConsoleDialogBody_Base() { }
    
    /**
     * Metoda prednastavi predchadzajuce zobrazenie v dialogu.
     * @return Ak existuje predchadzajuca strana pre dane zobrazenie v dialogu, metoda vrati true. Inak false.
     */
    public abstract boolean setPrevious();
    /**
     * Metoda prednastavi nasledujuce zobrazenie v dialogu
     * @return Ak existuje nasledujuca strana pre dane zobrazenie v dialogu, metoda vrati true. Inak false.
     */
    public abstract boolean setNext();
    /**
     * Metoda vrati telo dialogu pre nasledne zobrazenie
     * @return Telo dialogu.
     */
    public abstract ConsoleDialogBody getCurr();
    /**
     * Metoda vrati submenu daneho menu pre nasledne zobrazenie dialogu
     * @return SubMenu pre telo dialogu.
     */
    public abstract ConsoleDialogBody_Base getSubMenuBase();
    
    /**
     * Metoda reprezetuje handler pre spracovanie vstupu od uzivatela typu: YES/NO Otazka
     * @param navigation Trieda reprezentuje jeden zo zvolenych naviganych prikazov (Exit, Naspat, Zrusit, Preskocit, Dokoncit), ak to bolo zvoleneme uzivatelom na vstupe
     * @param question Prednastavenie pre otazku
     * @return Instrukcia pre ConsoleDialog ako ma postupovat pri zobrazovani dialogu uzivatelovi. Jedna z moznych instrukcii: EXIT, REPAINT, ADD_SUBMENU, REMOVE_SUBMENU, NOPAINT.
     */
    public abstract ConsoleDialog.Instructions handleYesNo(ConsoleUserInput_NavigationSelect navigation, ConsoleUserInput_Question question);
    /**
     * Metoda reprezetuje handler pre spracovanie vstupu od uzivatela typu: Vyber moznost z menu
     * @param navigation Trieda reprezentuje jeden zo zvolenych naviganych prikazov (Exit, Naspat, Zrusit, Preskocit, Dokoncit), ak to bolo zvoleneme uzivatelom na vstupe
     * @param question Prednastavenie pre otazku
     * @return Instrukcia pre ConsoleDialog ako ma postupovat pri zobrazovani dialogu uzivatelovi. Jedna z moznych instrukcii: EXIT, REPAINT, ADD_SUBMENU, REMOVE_SUBMENU, NOPAINT.
     */
    public abstract ConsoleDialog.Instructions handleChooseOption(ConsoleUserInput_NavigationSelect navigation, ConsoleUserInput_Question question);
    /**
     * Metoda reprezetuje handler pre spracovanie vstupu od uzivatela typu: Ciselny udaj, typicka long hodnota (napr.: dlzka trvania udalosti v minutach, ...)
     * @param navigation Trieda reprezentuje jeden zo zvolenych naviganych prikazov (Exit, Naspat, Zrusit, Preskocit, Dokoncit), ak to bolo zvoleneme uzivatelom na vstupe
     * @param question Prednastavenie pre otazku
     * @return Instrukcia pre ConsoleDialog ako ma postupovat pri zobrazovani dialogu uzivatelovi. Jedna z moznych instrukcii: EXIT, REPAINT, ADD_SUBMENU, REMOVE_SUBMENU, NOPAINT.
     */
    public abstract ConsoleDialog.Instructions handleNumber(ConsoleUserInput_NavigationSelect navigation, ConsoleUserInput_Question question);
    /**
     * Metoda reprezetuje handler pre spracovanie vstupu od uzivatela typu: Textovy vstup
     * @param navigation Trieda reprezentuje jeden zo zvolenych naviganych prikazov (Exit, Naspat, Zrusit, Preskocit, Dokoncit), ak to bolo zvoleneme uzivatelom na vstupe
     * @param question Prednastavenie pre otazku
     * @return Instrukcia pre ConsoleDialog ako ma postupovat pri zobrazovani dialogu uzivatelovi. Jedna z moznych instrukcii: EXIT, REPAINT, ADD_SUBMENU, REMOVE_SUBMENU, NOPAINT.
     */
    public abstract ConsoleDialog.Instructions handleText(ConsoleUserInput_NavigationSelect navigation, ConsoleUserInput_Question question);
    /**
     * Metoda reprezetuje handler pre spracovanie vstupu od uzivatela typu: Casovy udaj vo formate (HH:mm)
     * @param navigation Trieda reprezentuje jeden zo zvolenych naviganych prikazov (Exit, Naspat, Zrusit, Preskocit, Dokoncit), ak to bolo zvoleneme uzivatelom na vstupe
     * @param question Prednastavenie pre otazku
     * @return Instrukcia pre ConsoleDialog ako ma postupovat pri zobrazovani dialogu uzivatelovi. Jedna z moznych instrukcii: EXIT, REPAINT, ADD_SUBMENU, REMOVE_SUBMENU, NOPAINT.
     */
    public abstract ConsoleDialog.Instructions handleTime(ConsoleUserInput_NavigationSelect navigation, ConsoleUserInput_Question question);
    /**
     * Metoda reprezetuje handler pre spracovanie vstupu od uzivatela typu: Casovy udaj vo formate (yyyy-MM-dd HH:mm)
     * @param navigation Trieda reprezentuje jeden zo zvolenych naviganych prikazov (Exit, Naspat, Zrusit, Preskocit, Dokoncit), ak to bolo zvoleneme uzivatelom na vstupe
     * @param question Prednastavenie pre otazku
     * @return Instrukcia pre ConsoleDialog ako ma postupovat pri zobrazovani dialogu uzivatelovi. Jedna z moznych instrukcii: EXIT, REPAINT, ADD_SUBMENU, REMOVE_SUBMENU, NOPAINT.
     */
    public abstract ConsoleDialog.Instructions handleDateTime(ConsoleUserInput_NavigationSelect navigation, ConsoleUserInput_Question question);
    
    
    /**
     * Metoda vrati instrukciu, ktora definuje ako sa ma zachovat ConsoleDialog, ci ma pridat menu alebo odobrat, prekreslit menu, alebo ukoncit program, ...
     * 
     * @param navigation Trieda reprezentuje jeden zo zvolenych naviganych prikazov (Exit, Naspat, Zrusit, Preskocit, Dokoncit), ak to bolo zvoleneme uzivatelom na vstupe
     * @return Instrukcia pre ConsoleDialog ako ma postupovat pri zobrazovani dialogu uzivatelovi. Jedna z moznych instrukcii: EXIT, REPAINT, ADD_SUBMENU, REMOVE_SUBMENU, NOPAINT.
     */
    public abstract ConsoleDialog.Instructions handleNavigationToInstruction(ConsoleUserInput_NavigationSelect navigation);
    
    /**
     * Metoda spracuje vstup od uzivatela. Typ vstupu (Volba v menu, zadany datum a cas, zadany textovy vstup, zadane cislo) je definovany vopred
     * @return Instrukcia pre ConsoleDialog ako ma postupovat pri zobrazovani dialogu uzivatelovi
     */
    public abstract ConsoleDialog.Instructions manageUserAnswer();
}
