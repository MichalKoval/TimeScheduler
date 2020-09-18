/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mavenproject.timescheduler.dialogs;

/**
 * Trieda reprezentuje FLAGS, ktore urcuju ci bude mozne v menu/dialogu zvolit dany navigacny prikaz: Exit, Naspat, Zrusit, Preskocit, Dokoncit.
 */
public class ConsoleUserInput_Navigation {
    private boolean exit = false;
    private boolean back = false;
    private boolean cancel = false;
    private boolean skip = false;
    private boolean finish = false;
    
    /**
     * Enum reprezentujuci rozne navigacne prikazy, ktore moze uzivatel pri vstupe zvolit: Exit, Naspat, Zrusit, Preskocit, Dokoncit.
     */
    public enum Navigations {
        EXIT,
        BACK,
        CANCEL,
        SKIP,
        FINISH,
        NONE,
        REPEAT
    }
    
    /**
     * Konstruktor nastavi True/False na navigacne prikazy, ktore chceme, aby boli zobrazene uzivatelovi v menu, v pripade ze sa uzivatel bude chciet navigovat medzi jednotlivymi subzobrazeniami v dialogu.
     * @param isExit Bude mozne zvolit Exit.
     * @param isBack Bude mozne zvolit Naspat.
     * @param isCancel Bude mozne zvolit Zrusit.
     * @param isSkip Bude mozne zvolit Preskocit.
     * @param isFinish Bude mozne zvolit Dokoncit.
     */
    public ConsoleUserInput_Navigation(boolean isExit, boolean isBack, boolean isCancel, boolean isSkip, boolean isFinish) {
        exit = isExit;
        back = isBack;
        cancel = isCancel;
        skip = isSkip;
        finish = isFinish;
    }
    
    /**
     * Ci je v menu moznost zvolit Exit.
     * @return True/False
     */
    public boolean isExit() {
        return exit;
    }
    /**
     * Ci je v menu moznost zvolit Naspat.
     * @return True/False
     */
    public boolean isBack() {
        return back;
    }
    /**
     * Ci je v menu moznost zvolit Zrusit.
     * @return True/False
     */
    public boolean isCancel() {
        return cancel;
    }
    /**
     * Ci je v menu moznost zvolit Preskocit.
     * @return True/False
     */
    public boolean isSkip() {
        return skip;
    }
    /**
     * Ci je v menu moznost zvolit Dokoncit.
     * @return True/False
     */
    public boolean isFinish() {
        return finish;
    }
    
    
    /**
     * Metoda nastavi, aby bolo mozne v menu zvolit Exit.
     * @param isExit True, ak chceme aby bol navigacny prikaz Exit v menu
     */
    public void setExit(boolean isExit) {
        this.exit = isExit;
    }
    /**
     * Metoda nastavi, aby bolo mozne v menu zvolit Naspat.
     * @param isBack True, ak chceme aby bol navigacny prikaz Naspat v menu
     */
    public void setBack(boolean isBack) {
        this.back = isBack;
    }
    /**
     * Metoda nastavi, aby bolo mozne v menu zvolit Zrusit.
     * @param isCancel True, ak chceme aby bol navigacny prikaz Zrusit v menu
     */
    public void setCancel(boolean isCancel) {
        this.cancel = isCancel;
    }
    /**
     * Metoda nastavi, aby bolo mozne v menu zvolit Preskocit.
     * @param isSkip True, ak chceme aby bol navigacny prikaz Preskocit v menu
     */
    public void setSkip(boolean isSkip) {
        this.skip = isSkip;
    }
    /**
     * Metoda nastavi, aby bolo mozne v menu zvolit Dokoncit.
     * @param isFinish True, ak chceme aby bol navigacny prikaz Dokoncit v menu
     */
    public void setFinish(boolean isFinish) {
        this.finish = isFinish;
    }
}
