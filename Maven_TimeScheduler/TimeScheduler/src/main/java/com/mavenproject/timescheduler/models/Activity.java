package com.mavenproject.timescheduler.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.LocalDateTime;

/**
 * Abstraktna trieda 'Activity' reprezentuje aktivitu studenta. Zaciatok, dlzku trvania a deadline aktivity.
 * Trieda obsajuje popis (anotacie) pre nasledne serializovanie a deserializovanie aktivity do JSON suboru.
 * 
 * Od tejto triedy budu odvodzovane specificke aktivity doplnene o dalsie informacie.
 */
@JsonInclude(Include.NON_NULL)
//@JsonRootName(value = "activity")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = As.PROPERTY)
@JsonSubTypes({
    @Type(value = DefaultActivity.class, name = "defaultActivity"),
    @Type(value = DoNotDisturb.class, name = "doNotDisturb"),
    @Type(value = FreeTime.class, name = "freeTime"),
    @Type(value = SchoolActivity.class, name = "schoolActivity"),
    @Type(value = SportActivity.class, name = "sportActivity")
})
@JsonTypeName("activity")
public abstract class Activity {
    /*
     * Privatne premenne udrzujuce infomacie o zaciatku, dlzke trvania a deadline aktivity
     */
    private LocalDateTime startDT = null;
    private long duration = 0L;
    private LocalDateTime deadline = null;
    
    // Constructors -----------------------------------------
    /**
     * Zakladny konstruktor abstraktnej triedy.
     */
    public Activity() { 
        this.startDT = null;
        this.duration = 0L;
        this.deadline = null;
    }
    /**
     * Konstruktor nastavuje dlzku trvania aktivity.
     * @param duration Dlzka trvania aktivity.
     */
    public Activity(long duration) { 
        this.duration = duration;
    }
    /**
     * Konstruktor nastavuje dlzku trvania aktivity a deadline.
     * 
     * @param duration Dlzka trvania v minutach.
     * @param deadline Deadline aktivity.
     */
    public Activity(long duration, LocalDateTime deadline) {
        this.duration = duration;
        this.deadline = deadline;
    }
    /**
     * Konstruktor nastavuje zaciatok aktivity a dlzku trvania.
     * 
     * @param startDT Zaciatok aktivity.
     * @param duration Dlzka trvania v minutach.
     */
    public Activity(LocalDateTime startDT, long duration) {
        this.startDT = startDT;
        this.duration = duration;
    }
    /**
     * Konstruktor nastavuje zaciatok aktivity, dlzku trvania aktivity a deadline.
     * 
     * @param startDT Zaciatok aktivity.
     * @param duration Dlzka trvania v minutach.
     * @param deadline Deadline aktivity.
     */
    public Activity(LocalDateTime startDT, long duration, LocalDateTime deadline) {
        this.startDT = startDT;
        this.duration = duration;
        this.deadline = deadline;
    }
    
    /**
     * Abstraktna metoda, ktora reprezentuje vygenerovany text popisujuci aktivitu.
     * Tato metoda sa pouziva pre zobrazenie informacii a aktvivite v menu alebo v notifikacii.
     * Pre spravne zobrazenie udajov potrebujeme casovu zonu uzivatela, kedze vsetky casove udaje su ulozene v UTC
     * 
     * @param timeZone Casova zona, podla ktorej sa zobrazia casove data
     * @return ActivityStringRepresentation je trieda obsahujuca textovy popois aktivity.
     */
    @JsonIgnore
    public abstract ActivityStringRepresentation generateStringRepresentation(int timeZone);
    
    // Getters ----------------------------------------------
    /**
     * Metoda zisti, ci ide o aktivitu s pevnym casom, alebo aktivitu, ktory treba rozvrhnut
     * 
     * Tato vlasnost nie je zahrnuta v serializovani alebo deserializovani do JSON - JsonIgnore
     * 
     * @return Ak je to false, aktivitu treba rozvrhnut do rozvrhu
     */
    @JsonIgnore
    public boolean isFixedTime() {
        return (this.startDT != null &&
                this.duration >= 0 &&
                this.deadline == null);
    }
    
    /**
     * Metoda vracia zaciatok konania aktivity.
     * @return Urcuju zaciatok aktivity, ak je to null potom aktivita nie je este rozvrhnuta.
     */
    public LocalDateTime getStartDT() {
        return this.startDT;
    }
    /**
     * Metoda vracia dlzku trvania aktivity v minutach.
     * @return Dlzka aktivity v minutach.
     */
    public long getDuration() {
        return this.duration;
    }
    /**
     * Metoda vracia ucas ukoncenia aktivity vyrataneho na zaklade
     * zaciatku aktivity (getStartDT()) a dlzky aktivity (getDuration() v minutach).
     * 
     * Tato vlasnost nie je zahrnuta v serializovani alebo deserializovani do JSON - JsonIgnore
     * 
     * @return Je cas ukoncenia aktivity.
     */
    @JsonIgnore
    public LocalDateTime getEndDT() {
        LocalDateTime tempLocalDT = LocalDateTime.of(
                this.startDT.getYear(),
                this.startDT.getMonth(),
                this.startDT.getDayOfMonth(),
                this.startDT.getHour(),
                this.startDT.getMinute());
        tempLocalDT = tempLocalDT.plusMinutes(duration);
        
        return tempLocalDT;
    }
    /**
     * Metoda vracia deadline splnenia aktivity
     * @return Deadline pre splnenie aktivity, ak je null aktivita nema specifikovany koniec
     */
    public LocalDateTime getDeadline() {
        return this.deadline;
    }
    
    // Setters ----------------------------------------------
    /**
     * Metoda nastavi zaciatok konania aktivity
     * @param sDT Zaciatok aktivity
     */
    public void setStartDT(LocalDateTime sDT) {
        this.startDT = sDT;
    }
    /**
     * Metoda nastavi dlzku trvania aktivity
     * @param d Dlzka trvania v minutach
     */
    public void setDuration(long d) {
        this.duration = d;
    }
    /**
     * Metoda nastavi deadline aktivity
     * @param dl Deadline aktivity
     */
    public void setDeadline(LocalDateTime dl) {
        this.deadline = dl;
    }
}