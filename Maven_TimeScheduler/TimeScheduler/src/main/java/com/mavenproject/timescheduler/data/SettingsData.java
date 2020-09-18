package com.mavenproject.timescheduler.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Trieda reprezentuje datovy model nastaveni programu. Obsahuje metody pre nacitanie nastaveni zo suboru alebo ulozenie nastaveni do suboru v JSON formate.
 * Uzivatel ma moznost nastavit, ci chce mat zobrazene notifikacie(podporovana platforma pre notifikacie: Windows 10)
 * Uzivatel moze nastavit aj casovu zonu: integer cislo s posunom (-11, ..., 0, +1, +2, ... +11 ).
 */
public class SettingsData extends Data {
    private ObjectMapper objectMapper;
    private Integer timeZone = 1;
    private Boolean notificationEnabled = false;
    private String activitiesDataInFileName = null;
    private String activitiesDataOutFileName = null;
    /*
     * Referencie typov, ake ocakavame v JSON subore   
     */
    private TypeReference<Boolean> notificationEnabledTypeReference;
    private TypeReference<Integer> timeZoneTypeReference;
    private TypeReference<SettingsData> settingsDataTypeReference;
    
    /**
     * Metoda prednastavi ObjectMapper pre naslednu serializaciu alebo deserializaciu dat (data obsahuju programove nastavenia: nototifikacia, casovy posun)
     */
    private void preSetObjectMapper() {
        objectMapper = new ObjectMapper();
                
        //Pretty print pre lepsiu orientaciu medzi datami v subore
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        
        //Nastavime, aky typ ma ObjectMapper ocakavat v JSON subore
        timeZoneTypeReference = new TypeReference<Integer>() {};
        notificationEnabledTypeReference = new TypeReference<Boolean>() {};
        settingsDataTypeReference = new TypeReference<SettingsData>() {};
    }
    
    /**
     * Metoda overi ze neexistuje v zlozke programu meno s danym nazvom
     * @return True a take meno neexistuje
     */
    private boolean checkFileName(String file_name) {
        //Vytvorime list vsetkych suborov v zlozke
        String folderName = ".";
        File[] filesList = new File(folderName).listFiles();
        
        //Zistime ci dane meno uz neexistuje
        for (int i = 0; i < filesList.length; i++) {
            if (filesList[i].isFile()) {
                //Ziskame meno daneho suboru a porovname ho s activitiesDataInFileName
                String someFileName = filesList[i].getName();
                if (someFileName.equals(file_name)) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    /**
     * Zakladny konstruktor. Prednastavuje Serializovanie a deserializovanie pre JSON subor.
     */
    public SettingsData() {
        preSetObjectMapper();
    }
    
    /**
     * Metoda vrati cislo prave nastavenej casovej zony (-11, ..., -1, 0, 1, ..., +14)
     * @return int cislo z intervalu (-11, ..., -1, 0, 1, ..., +14)
     */
    public int getTimeZone() {
        return timeZone;
    }
    /**
     * Metoda vrati meno vstupneho suboru pre data aktivit
     * @return Retazec s menom vystupneho suboru
     */
    public String getActivitiesDataInFileName() {
        return activitiesDataInFileName;
    }
    /**
     * Metoda vrati meno vystupneho suboru pre data aktivit
     * @return Retazec s menom vystupneho suboru
     */
    public String getActivitiesDataOutFileName() {
        return activitiesDataOutFileName;
    }
    
    /**
     * Metoda vrati, ci je dovolene zobrazovat notifikaciu
     * @return true ak je zobrazenie notifikacie dovolene
     */
    public boolean isNotificationEnabled() {
        return notificationEnabled;
    }
    /**
     * Metoda na nastavenie zobrazenia notifikacie
     * @param notificationEnabled true alebo false na zaklade toho, ci chceme alebo nechceme povolit zobrazenie notifikacii
     */
    public void setNotificationEnabled(boolean notificationEnabled) {
        this.notificationEnabled = notificationEnabled;
    }
    /**
     * Metoda nastavi casovu zonu podla zadaneho cisla (-11, ..., -1, 0, 1, ..., +14)
     * @param timeZone int cislo z intervalu (-11, ..., -1, 0, 1, ..., +14)
     */
    public void setTimeZone(int timeZone) {
        this.timeZone = timeZone;
    }
    /**
     * Metoda nastavi meno vstupneho suboru pre data aktivit
     * @param activitiesDataInFileName Retazec s menom vystupneho suboru
     * @return True ak sa podari nastavit dane meno suboru, dane meno moze uz existovat
     */
    public boolean setActivitiesDataInFileName(String activitiesDataInFileName) {
        //Zistime ci daneho meno v zlozke programu existuje
        //Meno suboru nesmie kolidovat s meno suboru pre nastavenia programu
        if (!activitiesDataInFileName.equals(getFileName()) && !checkFileName(activitiesDataInFileName)) {
            this.activitiesDataInFileName = activitiesDataInFileName;
            return true;
        }
        
        return false;
    }
    /**
     * Metoda nastavi meno vystupneho suboru pre data aktivit
     * @param activitiesDataOutFileName Retazec s menom vystupneho suboru
     * @return True ak sa podari nastavit dane meno suboru, dane meno moze uz existovat
     */
    public boolean setActivitiesDataOutFileName(String activitiesDataOutFileName) {
        //Zistime ci daneho meno v zlozke programu neexistuje
        if (!activitiesDataInFileName.equals(getFileName()) && checkFileName(activitiesDataOutFileName)) {
            this.activitiesDataOutFileName = activitiesDataOutFileName;
            return true;
        }
        
        return false;
    }
    
    
    
    // Metody pre spracovanie dat z a do suboru, pre nacitanie ulozenych nastaveni programu
    /**
     * Metoda nacita JSON subor a deserializuje ho na object SettingsData
     * @param fileName Meno vstupneho suboru
     * @return False ak sa nepodarilo otvorit subor
     */
    @Override
    public boolean loadFromFile(String fileName) {
        try(InputStream settings = new FileInputStream(fileName)) {
            
            JsonNode array = objectMapper.readValue(settings, JsonNode.class);
                    
            timeZone = array.get("timeZone").asInt();
            notificationEnabled = array.get("notificationEnabled").asBoolean();
            activitiesDataInFileName = array.get("activitiesDataInFileName").asText();
            activitiesDataOutFileName = array.get("activitiesDataOutFileName").asText();
            
            // Poznamename si meno suboru z ktoreho sme nacitavali data
            setFileName(fileName);
            
            return true;
        } catch (IOException ex) {
            System.err.println("Nepodarilo sa otvorit konfiguracny subor programu.");
            System.err.println(ex.getMessage());
            
            return false;
        }
    }
    
    /**
     * Metoda ulozi(serializuje) datovu nastaveni do JSON suboru
     * @param fileName Meno vystupneho suboru
     */
    @Override
    public void saveToFile(String fileName) {
        try {
            objectMapper
                    .writerFor(settingsDataTypeReference)
                    .writeValue(new File(fileName), this);
            
            // Poznamename si meno suboru do ktoreho sme ukladali data
            setFileName(fileName);
            
        } catch (IOException ex) {
            Logger.getLogger(ActivitiesData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
