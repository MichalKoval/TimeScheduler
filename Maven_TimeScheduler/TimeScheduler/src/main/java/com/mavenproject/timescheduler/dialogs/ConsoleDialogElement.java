/*
 * 
 */
package com.mavenproject.timescheduler.dialogs;

import java.security.InvalidParameterException;

/**
 * Trieda reprezentuje definiciu zakladneho elementu, z ktoreho sa nasledne skladaju zlozitejsie zobrazenia v menu/dialogu.
 * Rozlisujeme tri druhy elementov: Oddelovac, pradny riadok, a riadok s textom.
 */
public class ConsoleDialogElement{
    private final String errorNullPointerMsgElement = "INTERNAL ERROR: Typ  elementu je null.";
    private final String errorNullPointerMsgElementTypeText = "INTERNAL ERROR: Nie je mozne nastavit element typu TEXT, ak nebol zadany text a jeho pozicovanie. Pouzite iny konstruktor ConsoleDialogElement() na zhotovenie elementu.";
    public ConsoleDialog.DialogElementType dialogElementType = null;
    public ConsoleDialog.TextAlign textAlign = null;
    public String text = null;
    
    /**
     * Konstruktor nastavi typ elementu.
     * @param dialogElementType Typ elementu.
     * @throws NullPointerException Je vyhodena vynimka v pripade, ze je typ elementu textovy. Je potrebne dodat text a pozicovanie
     */
    public ConsoleDialogElement(ConsoleDialog.DialogElementType dialogElementType) throws NullPointerException {
        if (dialogElementType == null) {
            throw new NullPointerException(errorNullPointerMsgElement);
        }
        
        if (dialogElementType == dialogElementType.TEXT) {
            throw new InvalidParameterException(errorNullPointerMsgElementTypeText);
        }
        
        this.dialogElementType = dialogElementType;
        this.textAlign = null;
        this.text = null;
    }
    
    /**
     * Konstruktor nastavi textovy element a jeho pozicovanie.
     * @param textAlign Pozicovanie textu: vlavo, v strede, vpravo.
     * @param text Retazec s textom, ak je text prazdny, zmeni sa textovy element na pradny riadok.
     */
    public ConsoleDialogElement(ConsoleDialog.TextAlign textAlign, String text) {
                
        if (text == null || text.isEmpty()) {
            this.dialogElementType = ConsoleDialog.DialogElementType.EMPTY_LINE;
            this.textAlign = null;
            this.text = null;
        } else if (textAlign == null) {
            this.dialogElementType = ConsoleDialog.DialogElementType.TEXT;
            this.textAlign = ConsoleDialog.TextAlign.LEFT;
            this.text = text;
        } else {
            this.dialogElementType = ConsoleDialog.DialogElementType.TEXT;
            this.textAlign = textAlign;
            this.text = text;
        }
    }
    
    
    
}
