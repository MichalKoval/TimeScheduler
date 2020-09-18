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
 * Trieda reprezentuje volny cas studenta. Vychadza z triedy 'DefaultActivity' a casovo vymedzuje obdobie, kedy chce mas student volny cas (kino, stretnutie s priatelmi, ...).
 * Aktivita nepozaduje nastavenia deadlinu.
 */
@JsonInclude(Include.NON_NULL)
@JsonTypeName("freeTime")
public class FreeTime extends DefaultActivity {
    
    // Constructors -----------------------------------------
    /**
     * Hlavny konstruktor mimocasovej aktivity
     */
    public FreeTime() {}
    /**
     * Konstruktor nastavuje vlastnosti z abstraktnej super triedy DefaultActivity, ako: kratky popis mimocasovej aktivity.
     * A dalej nastavuje vlastnosti z abstraktnej super triedy Activity, ako: zaciatok konania aktivity a dlzka trvania aktivity.
     * @param description Kratky popis mimocasovej aktivity
     * @param startDT Zaciatok konania mimocasovej aktivity.
     * @param duration Dlzka trvania mimocasovej aktivity.
     */
    public FreeTime(String description, LocalDateTime startDT, long duration) {
        super(description, startDT, duration);
    }
    /**
     * Konstruktor nastavuje vlastnosti z abstraktnej super triedy DefaultActivity, ako: meno a kratky popis mimocasovej aktivity.
     * A dalej nastavuje vlastnosti z abstraktnej super triedy Activity, ako: zaciatok konania aktivity a dlzka trvania aktivity.
     * @param name Meno mimocasovej aktivity.
     * @param description Kratky popis mimocasovej aktivity
     * @param startDT Zaciatok konania mimocasovej aktivity.
     * @param duration Dlzka trvania mimocasovej aktivity.
     */
    public FreeTime(String name, String description, LocalDateTime startDT, long duration) {
        super(name, description, startDT, duration);
    }
    
    /**
     * {@inheritDoc}
     * 
     * Vlastnost nebude zahrnuta v JSON subore.
     */
    @Override
    @JsonIgnore
    public ActivityStringRepresentation generateStringRepresentation(int timeZone) {
        String title = "Volny cas";
        List<String> descript = new ArrayList<>();
        if (getName() != null) { descript.add("Meno: " + getName()); }
        if (getLocation() != null) { descript.add("Miesto: " + getLocation()); }
        if (getStartDT() != null) { descript.add("Zaciatok: " + getStartDT().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))); }
        if (getEndDT() != null) { descript.add("Koniec: " + getEndDT().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))); }
                
        return new ActivityStringRepresentation(title, descript);
    }
}
