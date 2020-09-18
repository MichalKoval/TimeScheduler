package com.mavenproject.timescheduler.data;

/**
 * Abstraktna trieda reprezentuje zakladne funkcie pre spravu suborov. Z tejto tiedy su dalej odvodene triedy: ActivitiesData a SettingsData
 */
public abstract class Data {
    private String fileName = null;
    
    /**
     * Zakladny konstruktor
     */
    public Data() {
    }
    /**
     * Konstruktor nastavi meno suboru
     * @param fileName meno suboru pre spracovanie dat
     */
    public Data(String fileName) {
        this.fileName = fileName;
    }
    
    /**
     * Metoda vrati meno suboru, z ktoreho su nacitane data
     * @return Retazec s menom suboru
     */
    public String getFileName() {
        return fileName;
    }
    /**
     * Metoda nastavi meno subory pre nacitanie
     * @param fileName Retazec s menom suboru
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    /**
     * Metoda sa pokusi nacitat subor z uz vopred nastavenim menom suboru
     * 
     * @throws Exception Ak meno suboru nebolo nastavene vopred bude vyhodena vynimka o nenastavenom mene suboru.
     */
    public void loadFromFile() throws Exception {
        if (fileName == null || fileName.isEmpty()) {
            throw new Exception("ERROR: Prosim nastavte meno suboru pre nacitanie dat.");
        }
        
        loadFromFile(fileName);
    }
    
    /**
     * Abstraktna metoda reprezentujuca nacitanie dat zo suboru s vopred zadanym menom
     * @param fileName Retazec s menom suboru
     * @return False, ak sa nepodarilo otvorit subor
     */
    public abstract boolean loadFromFile(String fileName);
    
    /**
     * Metoda sa pokusi ulozit data do suboru z uz vopred nastavenim menom suboru
     * 
     * @throws Exception Ak meno suboru nebolo nastavene vopred bude vyhodena vynimka o nenastavenom mene suboru.
     */
    public void saveToFile() throws Exception {
        if (fileName == null || fileName.isEmpty()) {
            throw new Exception("ERROR: Prosim nastavte meno suboru pre ulozenie dat.");
        }
        
        saveToFile(fileName);
    }
    
    /**
     * Abstraktna metoda reprezentujuca ulozenie dat do suboru s vopred zadanym menom
     * @param fileName Retazec s menom suboru
     */
    public abstract void saveToFile(String fileName);
}
