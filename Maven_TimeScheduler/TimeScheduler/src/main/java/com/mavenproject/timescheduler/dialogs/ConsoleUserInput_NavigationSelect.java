/*
 * 
 */
package com.mavenproject.timescheduler.dialogs;

/**
 * Trieda reprezentuje navigacny prikaz, ktory bol zvoleny uzivatelom v menu/dialogu. Nasledne sa podla tejto volby spracuju instrukcie pre dalsie zobrazenie v ConsoleDialog().
 * <p>Ak bol zvolene Exit = chceme korektne ukoncit program.</p>
 * <p>Ak bol zvolene Naspat = chceme sa vrati o jedno zobrazenie v menu dozadu.</p>
 * <p>Ak bol zvolene Zrusit = chceme zrusit dane zobrazenie a vratit sa do povodneho menu.</p>
 * <p>Ak bol zvolene Preskocit = chceme prekocit na nasledujuce zobrazenie.</p>
 * <p>Ak bol zvolene Dokoncit = typicky ak sa ukoncilo nejake zobrazenie a bola uzivatelovi zobrazena sprava, ci prebehlo spravne. Po zvoleni Dokoncit sa vratime naspat do povodneho menu.</p>
 * 
 */
public class ConsoleUserInput_NavigationSelect {
    private ConsoleUserInput_Navigation.Navigations consoleUserInput_Navigation;
    private boolean requireNavigationSelection = false;
    private ConsoleUserInput_Navigation requiredNavigations = null;
    
    /**
     * Zakladny konstruktor
     */
    public ConsoleUserInput_NavigationSelect() {
    }
    /**
     * Konstruktor nastavi zvoleny navigacny prikaz.
     * @param consoleUserInput_Navigation Enum s typom navigacneho prikazu.
     */
    public ConsoleUserInput_NavigationSelect(ConsoleUserInput_Navigation.Navigations consoleUserInput_Navigation) {
        this.consoleUserInput_Navigation = consoleUserInput_Navigation;
    }
    
    /**
     * Metoda nastavi zvoleny navigacny prikaz.
     * @param consoleUserInput_Navigation Enum s typom navigacneho prikazu.
     */
    public void setNavigation(ConsoleUserInput_Navigation.Navigations consoleUserInput_Navigation) {
        this.consoleUserInput_Navigation = consoleUserInput_Navigation;
    }
    
    /**
     * Metoda vrati zvoleny navigacny prikaz.
     * @return Enum s typom navigacneho prikazu.
     */
    public ConsoleUserInput_Navigation.Navigations getNavigation() {
        return consoleUserInput_Navigation;
    }

    /**
     * Metoda vrati True, ak pozadujeme, aby uzivatel vybral spravne odpoved na vyber navigacneho prikazu. (Exit, Naspat, Zrusit, Preskocit, Dokoncit).
     * @return True, ak su pozadovane navigacne prikazy.
     */
    public boolean isRequireNavigationSelection() {
        return requireNavigationSelection;
    }
    
    /**
     * Metoda nastavi, aby uzivatel vybral spravne odpoved na vyber navigacneho prikazu. (Exit, Naspat, Zrusit, Preskocit, Dokoncit).
     * @param requireNavigationSelection True, ak pozadujeme, aby uzivatel zvolil jeden z navigacnych prikazov.
     */
    public void setRequireNavigationSelection(boolean requireNavigationSelection) {
        this.requireNavigationSelection = requireNavigationSelection;
    }
    
    /**
     * Metoda vrati flags, pre navigacne prikazy, ktore su povolene zadat uzivatelom.
     * @return Navigacne prikazy.
     */
    public ConsoleUserInput_Navigation getRequiredNavigations() {
        return requiredNavigations;
    }
    
    /**
     * Metoda nastavi flags, pre navigacne prikazy, ktore su povolene zadat uzivatelom.
     * @param requiredNavigations Flags na navigacne prikazy, z ktorych si uzivatel budem moct vybrat.
     */
    public void setRequiredNavigations(ConsoleUserInput_Navigation requiredNavigations) {
        this.requiredNavigations = requiredNavigations;
    }
    
    
}
