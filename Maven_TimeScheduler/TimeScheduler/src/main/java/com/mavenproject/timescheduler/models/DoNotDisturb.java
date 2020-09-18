/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mavenproject.timescheduler.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Trieda reprezentuje cas, kedy si student nezela byt vyrusovany. Vacsinou ide o typicke potreby cloveka (spanok, obed, vecera).
 * Vychadza z triedy 'Activity' a doplnuje o aky druh nerusene casu ide (spanok, obed, ...) a taktiez zaciatok a pocet opakovani daneho nerusuneho casu (denne, pondelok, utorok, ...)
 * Prakticky je to tiez aktivita vymedzujuca v rozvrhu cas, kedy nechce byt student ruseny.
 * Typicky casovy zaciatok aktivity nerusenia je reprezentovany pomocou LocalTime(Format HH:mm).
 * Nie je pouzite LocalDateTime ako v pripade startDT (Format: yyyy-MM-dd HH:mm) u inych druhov aktivit.
 * 
 */
@JsonInclude(Include.NON_NULL)
@JsonTypeName("doNotDisturb")
public class DoNotDisturb extends Activity {
    
    private Repeat repeat = null;
    private Events event = null;
    private LocalTime startT = null;
    
    // Enums ------------------------------------------------
    /**
     * Enum reprezentuje ako casto sa bude aktivita nerusenia opakovat: denne, pondelok, utorok, ..., nedela.
     * Je mozne ich prekonvertovat na string pomocou upravenej metody toString().
     */
    public static enum Repeat {
        DAILY("Kazdodenne"),
        MONDAY("Pondelok"),
        TUESDAY("Utorok"),
        WEDNESDAY("Streda"),
        THURSDAY("Stvrtok"),
        FRIDAY("Piatok"),
        SATURDAY("Sobota"),
        SUNDAY("Nedela");
        
        private final String description;
        
        private Repeat(final String description) {        
            this.description = description;
        }
        
        @Override
        public String toString() {
            return description;
        }
    }
    /**
     * Enum reprezentuje druhy nerusenia: spanok, ranajky, obed, veceda, alebo ine.
     * Je mozne ich prekonvertovat na string pomocou upravenej metody toString().
     * 
     */
    public static enum Events {
        SLEEP("Spanok"),
        BREAKFAST("Ranajky"),
        LUNCH("Obed"),
        DINNER("Vecera"),
        ANOTHER("Ine");
        
        private final String description;
        
        private Events(final String description) {        
            this.description = description;
        }
        
        @Override
        public String toString() {
            return description;
        }
    }
    
    // Constructors -----------------------------------------
    /**
     * Zakladny kontruktor pre aktivitu nerusenia.
     */
    public DoNotDisturb() {}
    /**
     * Konstruktor nastavuje druh, opakovanie a zaciatok aktivity nerusenia.
     * A taktiez nastavuje vlastnosti z abstraktnej super triedy Activity, ako: dlzka trvania aktivity.
     * @param doNotDisturbEvent Druh aktivity nerusenia.
     * @param doNotDisturbRepeat Opakocanie aktivity nerusenia.
     * @param startT Typicky casovy zaciatok (Format HH:mm) aktivity nerusenia. Nie je pouzite LocalDateTime ako v pripade startDT (Format: yyyy-MM-dd HH:mm)
     * @param duration Dlzka trvania aktivity nerusenia.
     */
    public DoNotDisturb(Events doNotDisturbEvent, Repeat doNotDisturbRepeat, LocalTime startT, long duration) {
        super(duration);
        this.startT = startT;
        this.repeat = doNotDisturbRepeat;
        this.event = doNotDisturbEvent;
    }
    
    // Getters ----------------------------------------------
    /**
     * {@inheritDoc}
     * 
     * Vlastnost nebude zahrnuta v JSON subore.
     */
    @Override
    @JsonIgnore
    public ActivityStringRepresentation generateStringRepresentation(int timeZone) {
        String title = "Doba nerusenia";
        List<String> descript = new ArrayList<>();
        if (getEvent() != null) { descript.add("Udalost: " + getEvent().toString()); }
        if (getStartDT() != null) { descript.add("Zaciatok: " + getStartDT().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))); }
        if (getEndDT() != null) { descript.add("Koniec: " + getEndDT().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))); }
        
        return new ActivityStringRepresentation(title, descript);
    }
    /**
     * Metoda vrati zaciatok konania aktivity (Format HH:mm).
     * @return LocalTime (Format HH:mm).
     */
    public LocalTime getStartT() {
        return this.startT;
    }
    /**
     * Metoda vrati ako casto si zelame opakovat aktivitu
     * @return Enum Repeat
     */
    public Repeat getRepeat() {
        return this.repeat;
    }
    /**
     * Metoda vrati druh aktivity nerusenia
     * @return Enum Events
     */
    public Events getEvent() {
        return this.event;
    }
    
    // Setters ----------------------------------------------
    /**
     * Metoda nastavi typicky zaciatok aktivity nerusenia (Format HH:mm).
     * @param st LocalTime (Format HH:mm)
     */
    public void setStartT(LocalTime st) {
        this.startT = st;
    }
    /**
     * Metoda nastavi, ako casto sa bude aktivita nerusenia opakovat: denne, pondelok, ..., nedela.
     * @param r Enum Repeat
     */
    public void setRepeat(Repeat r) {
        this.repeat = r;
    }
    /**
     * Metoda nastavi druh aktivity nerusenia.
     * @param t Enum Events
     */
    public void setEvent(Events t) {
        this.event = t;
    }
}