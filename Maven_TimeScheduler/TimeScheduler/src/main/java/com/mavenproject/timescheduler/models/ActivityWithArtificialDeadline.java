/*
 * 
 */
package com.mavenproject.timescheduler.models;

/**
 * Trieda reprezentuje dvojicu: Aktivita a jej umely deadline.
 * Umely deadline je vytvoreny ako: (povodny deadline aktivity) - ( DateTime.now(Clock.UTC) + (trvanie uz rozvrhnutych aktivit do povodneho deadlinu)).
 * Umely deadline je pouzity ako maximalna mozna dlzka v minutach, do ktorej ide vtesnat aktivitu od casove pociatku 0.
 */
public class ActivityWithArtificialDeadline {
    /**
     * Aktivita, ktora bude rozvrhnuta do rozvrhu.
     */
    public Activity activity;
    
    /**
     * Umely deadline aktivity = (povodny deadline aktivity) - ( DateTime.now(Clock.UTC) + (trvanie uz rozvrhnutych aktivit do povodneho deadlinu)).
     */
    public long artificialDeadline;
    
    /**
     * Konstruktor nastavujuci dvojicu aktivita a jej umely deadline
     * @param activity Aktivita
     * @param artificialDeadline Umely deadline aktivity 
     */
    public ActivityWithArtificialDeadline(Activity activity, long artificialDeadline) {
        this.activity = activity;
        this.artificialDeadline = artificialDeadline;
    }
    /**
     * Metoda vracia aktivity, ktora bude neskor rozvrhnuta do rozvrhu.
     * @return Aktivita na rozvrhnutie do rozvrhu.
     */
    public Activity getActivity() {
        return activity;
    }
    /**
     * Metoda vracia umely deadline aktivity.
     * @return Umely deadline aktivity.
     */
    public long getArtificialDeadline() {
        return artificialDeadline;
    }
    /**
     * Metoda nastavuje aktivitu pre nasledne rozvrhnutie.
     * 
     * @param activity Aktivita na rozvrhnutie.
     */
    public void setActivity(Activity activity) {
        this.activity = activity;
    }
    /**
     * Metoda nastavuje umely deadline aktivity pre nasledne rozvrhnutie.
     * @param artificialDeadline Umely deadline.
     */
    public void setArtificialDeadline(long artificialDeadline) {
        this.artificialDeadline = artificialDeadline;
    }
}
