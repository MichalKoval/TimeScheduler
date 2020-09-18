/*
 * 
 */
package com.mavenproject.timescheduler.models;

/**
 * Trieda reprezentuje umely cas zaciatku aktivity od pociatku casu 0. Pociatok sa vacsinou mysli od aktualneho casu, kedy program bezi
 */
public class ActivityWithArtificialStartTimeAndLateness {
    public Activity activity;
    
    /**
     * Umely cas zaciatku uz rozvrhnutej aktivity.
     */
    public long artificialStartTime;
    /**
     * Mozne oneskorenie po deadline.
     * Typicky lateness nesmie presiahnut viac ako 0 minut, inac bude aktivita povazovana za nezaradenu do rozvrhu. 
     */
    public long lateness;
    
    /**
     * Konstruktor nastavujuci aktivitu, ktora je alebo bude zaradena do rozvrhu, a umely zaciatok aktivity.
     * @param activity Aktivita, ktora je alebo bude zaradena do rozvrhu.
     * @param artificialStartTime Umely cas aktivity
     */
    public ActivityWithArtificialStartTimeAndLateness(Activity activity, long artificialStartTime) {
        this.activity = activity;
        this.artificialStartTime = artificialStartTime;
    }
    /**
     * Metoda vracia aktivitu, ktora je alebo bude zaradena do rozvrhu.
     * @return Aktivita
     */
    public Activity getActivity() {
        return activity;
    }
    /**
     * Metoda vracia umely casovy zaciatok uz rozvrhnutej aktivity.
     * @return Umely cacovy zaciatok v minutach od pociatku 0.
     */
    public long getArtificialStarTime() {
        return artificialStartTime;
    }
    /**
     * Metoda nastavi aktivitu, ktora je alebo bude zaradena do rozvrhu.
     * @param activity Aktivita
     */
    public void setActivity(Activity activity) {
        this.activity = activity;
    }
    /**
     * Metoda nastavuje umely casovy zaciatok uz rozvrhnutej aktivity
     * 
     * @param artificialDeadline Umely cacovy zaciatok v minutach od pociatku 0.
     */
    public void setArtificialStarTime(long artificialDeadline) {
        this.artificialStartTime = artificialDeadline;
    }
}
