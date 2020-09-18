/*
 * 
 */
package com.mavenproject.timescheduler.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.LocalDateTime;

/**
 * Abstraktna trieda reprezentuje obecnu aktivitu studenta odvodenu od abstraktnej triedy Activity.
 * Obsahuje meno aktivity, kratky popis aktivity, miesto konania aktivity a to co zdedila od abstraktnej tiedy Activity
 */
@JsonInclude(Include.NON_NULL)
@JsonTypeName("defaultActivity")
public abstract class DefaultActivity extends Activity {
    private String name = null;
    private String description = null;
    private String location = null;

    // Constructors -----------------------------------------
    /**
     * Hlavny konstruktor obecej aktivity.
     */
    public DefaultActivity() { }
    /**
     * Konstruktor nastavuje vlastnosti z abstraktnej super triedy Activity, ako: dlzka trvania a deadline aktivity.
     * @param duration Dlzka trvania aktivity.
     * @param deadline Deadline aktivity.
     */
    public DefaultActivity(long duration, LocalDateTime deadline) {
        super(duration, deadline);
    }    
    /**
     * Konstruktor nastavuje vlastnosti z abstraktnej super triedy Activity, ako: zaciatok konania aktivity a dlzka trvania aktivity.
     * @param startDT Zaciatok konania aktivity.
     * @param duration Dlzka trvania aktivity.
     */
    public DefaultActivity(LocalDateTime startDT, long duration) {
        super(startDT, duration);
    }
    /**
     * Konstruktor nastavuje vlastnosti z abstraktnej super triedy Activity, ako: zaciatok konania aktivity, dlzka trvania aktivity a deadline aktivity.
     * @param startDT Zaciatok konania aktivity.
     * @param duration Dlzka trvania aktivity.
     * @param deadline Deadline aktivity.
     */
    public DefaultActivity(LocalDateTime startDT, long duration, LocalDateTime deadline) {
        super(startDT, duration, deadline);
    }
    /**
     * Konstruktor nastavuje popis aktivity.
     * A taktiez nastavuje vlastnosti z abstraktnej super triedy Activity, ako: dlzka trvania aktivity a deadline aktivity.
     * @param description Popis aktivity.
     * @param duration Dlzka trvania aktivity.
     * @param deadline Deadline aktivity.
     */
    public DefaultActivity(String description, long duration, LocalDateTime deadline) {
        super(duration, deadline);
        this.description = description;
    }
    /**
     * Konstruktor nastavuje popis aktivity.
     * A taktiez nastavuje vlastnosti z abstraktnej super triedy Activity, ako: zaciatok konania aktivity a dlzka trvania aktivity.
     * @param description Popis aktivity.
     * @param startDT Zaciatok konania aktivity.
     * @param duration Dlzka trvania aktivity.
     */
    public DefaultActivity(String description, LocalDateTime startDT, long duration) {
        super(startDT, duration);
        this.description = description;
    }
    /**
     * Konstruktor nastavuje popis aktivity a miesto konania aktivity.
     * A taktiez nastavuje vlastnosti z abstraktnej super triedy Activity, ako: zaciatok konania aktivity a dlzka trvania aktivity.
     * @param description Popis aktivity.
     * @param location Popis miesta konania aktivity.
     * @param startDT Zaciatok konania aktivity.
     * @param duration Dlzka trvania aktivity.
     */
    public DefaultActivity(String description, LocalDateTime startDT, long duration, String location) {
        super(startDT, duration);
        this.description = description;
        this.location = location;
    }
    /**
     * Konstruktor nastavuje meno aktivity a popis aktivity.
     * @param name Meno aktivity.
     * @param description Popis aktivity.
     */
    public DefaultActivity(String name, String description) {
        this.name = name;
        this.description = description;
    }
    /**
     * Konstruktor nastavuje meno aktivity, popis aktivity a miesto konania aktivity.
     * @param name Meno aktivity.
     * @param description Popis aktivity.
     * @param location Popis miesta konania aktivity
     */
    public DefaultActivity(String name, String description, String location) {
        this.name = name;
        this.description = description;
    }
    /**
     * Konstruktor nastavuje meno aktivity a popis aktivity.
     * A taktiez nastavuje vlastnosti z abstraktnej super triedy Activity, ako: dlzku konania aktivity a deadline aktivity.
     * @param name Meno aktivity.
     * @param description Popis aktivity.
     * @param duration Dlzka trvania aktivity.
     * @param deadline Deadline aktivity.
     */
    public DefaultActivity(String name, String description, long duration, LocalDateTime deadline) {
        super(duration, deadline);
        this.name = name;
        this.description = description;
    }    
    /**
     * Konstruktor nastavuje meno aktivity a popis aktivity.
     * A taktiez nastavuje vlastnosti z abstraktnej super triedy Activity, ako: zaciatok konania aktivity a dlzku konania aktivity.
     * @param name Meno aktivity.
     * @param description Popis aktivity.
     * @param startDT Zaciatok konania aktivity.
     * @param duration Dlzka trvania aktivity.
     */
    public DefaultActivity(String name, String description, LocalDateTime startDT, long duration) {
        super(startDT, duration);
        this.name = name;
        this.description = description;
    }
    /**
     * Konstruktor nastavuje meno aktivity a popis aktivity.
     * A taktiez nastavuje vlastnosti z abstraktnej super triedy Activity, ako: zaciatok konania aktivity, dlzka trvania aktivity a deadline aktivity.
     * @param name Meno aktivity.
     * @param description Popis aktivity.
     * @param startDT Zaciatok konania aktivity.
     * @param duration Dlzka trvania aktivity.
     * @param deadline Deadline aktivity.
     */
    public DefaultActivity(String name, String description, LocalDateTime startDT, long duration, LocalDateTime deadline) {
        super(startDT, duration, deadline);
        this.name = name;
        this.description = description;
    }    
    /**
     * Konstruktor nastavuje meno aktivity, popis aktivity a miesto konania aktivity.
     * A taktiez nastavuje vlastnosti z abstraktnej super triedy Activity, ako: dlzku trvania aktivity a deadline aktivity.
     * @param name Meno aktivity.
     * @param description Popis aktivity.
     * @param location Miesto konania aktivity.
     * @param duration Dlzka trvania aktivity.     * 
     * @param deadline Deadline aktivity.
     */
    public DefaultActivity(String name, String description, String location, long duration, LocalDateTime deadline) {
        super(duration, deadline);
        this.name = name;
        this.description = description;
        this.location = location;
    }    
    /**
     * Konstruktor nastavuje meno aktivity, popis aktivity a miesto konania aktivity.
     * A taktiez nastavuje vlastnosti z abstraktnej super triedy Activity, ako: zaciatok konania aktivity a dlzku trvania aktivity.
     * @param name Meno aktivity.
     * @param description Popis aktivity.
     * @param location Miesto konania aktivity.
     * @param startDT Zaciatok konania aktivity.
     * @param duration Dlzka trvania aktivity.
     */
    public DefaultActivity(String name, String description, String location, LocalDateTime startDT, long duration) {
        super(startDT, duration);
        this.name = name;
        this.description = description;
        this.location = location;
    }
    /**
     * Konstruktor nastavuje meno aktivity, popis aktivity a miesto konania aktivity.
     * A taktiez nastavuje vlastnosti z abstraktnej super triedy Activity, ako: zaciatok konania aktivity, dlzku trvania aktivity a deadline aktivity.
     * @param name Meno aktivity.
     * @param description Popis aktivity.
     * @param location Miesto konania aktivity.
     * @param startDT Zaciatok konania aktivity.
     * @param duration Dlzka trvania aktivity.
     * @param deadline Deadline aktivity.
     */
    public DefaultActivity(String name, String description, String location, LocalDateTime startDT, long duration, LocalDateTime deadline) {
        super(startDT, duration, deadline);
        this.name = name;
        this.description = description;
        this.location = location;
    }
    
    // Getters ----------------------------------------------
    /**
     * Metoda vrati meno aktivity.
     * 
     * @return Meno aktivity
     */
    public String getName() {
        return this.name;
    }
    /**
     * Metoda vrati kratky popis aktivity.
     * 
     * @return Popis aktivity
     */
    public String getDescription() {
        return this.description;
    }
    /**
     * Metoda vrati popis miesta konania aktivity.
     * 
     * @return Miesto konania aktivity
     */
    public String getLocation() {
        return location;
    }
    
    
    // Setters ----------------------------------------------
    /**
     * Metoda nastavi meno aktivity.
     * @param name Meno aktivity.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Metoda kratky popis aktivity.
     * @param desc Popis aktivity.
     */
    public void setDescription(String desc) {
        this.description = desc;
    }
    /**
     * Metoda popis miesta konania aktivity.
     * @param location Popis miesta konania aktivity.
     */
    public void setLocation(String location) {
        this.location = location;
    }
    
}
