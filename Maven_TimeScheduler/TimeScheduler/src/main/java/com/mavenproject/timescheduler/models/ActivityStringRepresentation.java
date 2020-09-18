/*
 * 
 */
package com.mavenproject.timescheduler.models;

import java.util.List;

/**
 * Trieda reprezentuje textovy popis activity, jej zakladny nazov, a podrobnejsie informacie.
 * Pouzite pri zobrazeni informacii o aktivite v notifikacii, alebo v console dialogu.
 * 
 */
public class ActivityStringRepresentation {
    public String title;
    public List<String> description;
    
    /**
     * Konstruktor nastavujucu nazov aktivity a jej textovy popis
     * @param title Nazov aktivity
     * @param description Textovy popis aktivity (List retazcov)
     */
    public ActivityStringRepresentation(String title, List<String> description) {
        this.title = title;
        this.description = description;
    }

    /**
     * Metoda vracia nazov aktivity
     * @return Retazec s menom aktivity
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * Metoda vracia List retazcov popisujucich aktivitu.
     * 
     * @return List retazcov obsahujucich informacie o aktivite.
     */
    public List<String> getDescription() {
        return this.description;
    }
}
