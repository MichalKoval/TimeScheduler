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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Trieda reprezentuje skolsku aktivitu studenta. Vychadza z triedy 'DefaultActivity' a doplnuje popis druhu skolskej aktivity
 */
@JsonInclude(Include.NON_NULL)
@JsonTypeName("schoolActivity")
public class SchoolActivity extends DefaultActivity {
    
    private SchoolActivities schoolActivityType = SchoolActivities.ANOTHER;
    
    // Enums ------------------------------------------------
    /**
     * Enum reprezentujuci typy skolskych aktivit, je mozne ich prekonvertovat do string reprezentacie, pomocou pravenej metody toString().
     */
    public static enum SchoolActivities {
        LECTURE("Prednaska"),
        PRACTICAL("Cvicenie"),
        HOMEWORK("Domaca uloha"),
        TEST("Test"),
        EXAM("Pisomka"),
        SEMESTRAL_WORK("Semestralna praca"),
        BACHELOR_THESIS("Bakalarska praca"),
        MASTER_THESIS("Diplomova praca"),
        ANOTHER("Ine");
        
        private final String description;
        
        private SchoolActivities(final String description) {        
            this.description = description;
        }
        
        @Override
        public String toString() {
            return description;
        }
    }
    
    // Constructors -----------------------------------------
    /**
     * Zakladny konstruktor
     */
    public SchoolActivity() { } 
    /**
     * Konstruktor nastavuje vlastnosti z abstraktnej super triedy DefaultActivity, ako: meno a kratky popis skolskej aktivity.
     * @param name Meno skolskej aktivity.
     * @param description Kratky popis skolskej aktivity.
     */
    public SchoolActivity(String name, String description) {
        super(name, description);
    }
    /**
     * Konstruktor nastavuje typ skolskej aktivity.
     * Taktiez nastavuje vlastnosti z abstraktnej super triedy DefaultActivity, ako: meno a kratky popis skolskej aktivity.
     * @param schoolActivityType Enum s typom skolskej aktivity.
     * @param name Meno skolskej aktivity.
     * @param description Kratky popis skolskej aktivity.
     */
    public SchoolActivity(String name, String description, SchoolActivities schoolActivityType) {
        super(name, description);
        this.schoolActivityType = schoolActivityType;
    }
    /**
     * Konstruktor nastavuje typ skolskej aktivity.
     * Taktiez nastavuje vlastnosti z abstraktnej super triedy DefaultActivity, ako: meno a kratky popis skolskej aktivity.
     * A dalej nastavuje vlastnosti z abstraktnej super triedy Activity, ako: dlzka trvania skolskej aktivity a jej deadline.
     * @param schoolActivityType Enum s typom skolskej aktivity.
     * @param name Meno skolskej aktivity.
     * @param description Kratky popis skolskej aktivity.
     * @param duration Dlzka trvania skolskej aktivity.
     * @param deadline Deadline skolskej aktivity.
     */
    public SchoolActivity(String name, String description, SchoolActivities schoolActivityType, long duration, LocalDateTime deadline) {
        super(name, description, duration, deadline);
        this.schoolActivityType = schoolActivityType;
    }
    /**
     * Konstruktor nastavuje typ skolskej aktivity.
     * Taktiez nastavuje vlastnosti z abstraktnej super triedy DefaultActivity, ako: meno a kratky popis skolskej aktivity.
     * A dalej nastavuje vlastnosti z abstraktnej super triedy Activity, ako: zaciatok konania aktivity a dlzka trvania skolskej aktivity.
     * @param schoolActivityType Enum s typom skolskej aktivity.
     * @param name Meno skolskej aktivity.
     * @param description Kratky popis skolskej aktivity.
     * @param startDT Zaciatok konania skolskej aktivity.
     * @param duration Dlzka trvania skolskej aktivity.
     */
    public SchoolActivity(String name, String description, SchoolActivities schoolActivityType, LocalDateTime startDT, long duration) {
        super(name, description, startDT, duration);
        this.schoolActivityType = schoolActivityType;
    }
    /**
     * Konstruktor nastavuje typ skolskej aktivity.
     * Taktiez nastavuje vlastnosti z abstraktnej super triedy DefaultActivity, ako: meno a kratky popis skolskej aktivity.
     * A dalej nastavuje vlastnosti z abstraktnej super triedy Activity, ako: zaciatok konania aktivity, dlzka trvania skolskej aktivity a jej deadline.
     * @param schoolActivityType Enum s typom skolskej aktivity.
     * @param name Meno skolskej aktivity.
     * @param description Kratky popis skolskej aktivity.
     * @param startDT Zaciatok konania skolskej aktivity.
     * @param duration Dlzka trvania skolskej aktivity.
     * @param deadline Deadline skolskej aktivity.
     */
    public SchoolActivity(String name, String description, SchoolActivities schoolActivityType, LocalDateTime startDT, long duration, LocalDateTime deadline) {
        super(name, description, startDT, duration, deadline);
        this.schoolActivityType = schoolActivityType;
    }
    /**
     * Konstruktor nastavuje typ skolskej aktivity.
     * Taktiez nastavuje vlastnosti z abstraktnej super triedy DefaultActivity, ako: meno, miesto konania a kratky popis skolskej aktivity.
     * A dalej nastavuje vlastnosti z abstraktnej super triedy Activity, ako: zaciatok konania aktivity a dlzka trvania skolskej aktivity.
     * @param schoolActivityType Enum s typom skolskej aktivity.
     * @param name Meno skolskej aktivity.
     * @param description Kratky popis skolskej aktivity
     * @param location Miesto konania skolskej aktivity.
     * @param startDT Zaciatok konania skolskej aktivity.
     * @param duration Dlzka trvania skolskej aktivity.
     */
    public SchoolActivity(String name, String description, String location, SchoolActivities schoolActivityType, LocalDateTime startDT, long duration) {
        super(name, description, location, startDT, duration);
        this.schoolActivityType = schoolActivityType;
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
        String title = "Skolska aktivita";
        List<String> descript = new ArrayList<>();
        if (getSchoolActivityType() != null) { descript.add("Typ: " + getSchoolActivityType().toString()); }
        if (getName() != null) { descript.add("Meno: " + getName()); }
        if (getDescription() != null) { descript.add("Popis: " + getDescription()); }
        if (getLocation() != null) { descript.add("Miesto: " + getLocation()); }
        if (getStartDT() != null) { descript.add("Zaciatok: " + getStartDT().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))); }
        if (getEndDT() != null) { descript.add("Koniec: " + getEndDT().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))); }
        
        return new ActivityStringRepresentation(title, descript);
    }
    /**
     * Metoda vrati typ skolskej aktivity.
     * @return Enum s typom skolskej aktivity.
     */
    public SchoolActivities getSchoolActivityType() {
        return this.schoolActivityType;
    }    
    
    // Setters ----------------------------------------------
    /**
     * Metoda nastavi typ skolskej aktivity.
     * @param sa Enum s typom skolskej aktivity.
     */
    public void setSchoolActivityType(SchoolActivity.SchoolActivities sa) {
        this.schoolActivityType = sa;
    }
}


