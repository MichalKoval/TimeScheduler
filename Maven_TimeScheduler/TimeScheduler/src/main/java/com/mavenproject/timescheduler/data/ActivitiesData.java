package com.mavenproject.timescheduler.data;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mavenproject.timescheduler.models.Activity;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Trieda reprezentuje datovy model zoznamu aktivit (rozneho typu) a obsahuje metody pre rozparsovanie dat zo suboru alebo ulozenie dat do suboru v JSON formate.
 * Je odvodena od triedy Data, ktora implementuje zakladne funkcie pre nastavenie mena suboru, a za akych podmienok pojde data nacitat.
 * 
 */
public class ActivitiesData extends Data {
    private ObjectMapper objectMapper;
    private List<Activity> activities;
    
    /*
     * Referencia typu, aky ocakavame v JSON subore   
     */
    private TypeReference<List<Activity>> listOfActivitiesTypeReference;
    
    /**
     * Metoda prednastavi ObjectMapper pre naslednu serializaciu alebo deserializaciu dat (datum, cas, casova zona)
     */
    private void preSetObjectMapper() {
        objectMapper = new ObjectMapper();
        
        //Nastavime format datumu a casu pri serializovani do JSON suboru (UTC datetime format) pomocou modulu dodavanemu k Jackson ObjectMapper
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        //Pretty print (iba pre debug)
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        
        //Nastavime, aky typ ma ObjectMapper ocakavat v JSON subore
        listOfActivitiesTypeReference = new TypeReference<List<Activity>>() {};
    }
    
    // Constructors -----------------------------------------
    /**
     * Zakladny konstruktor. Prednastavuje Serializovanie a deserializovanie pre JSON subor.
     */
    public ActivitiesData() {
        preSetObjectMapper();
        activities = new ArrayList<>();
    }
    /**
     * Konstruktor prednastavuje Serializovanie a deserializovanie pre JSON subor a nacita JSON subor na zaklade mena suboru.
     * 
     * @param fileName Meno suboru pre spracovanie dat
     */
    public ActivitiesData(String fileName) {
        super(fileName);
        preSetObjectMapper();
        loadFromFile(fileName);
    }
    
    /**
     * Metoda prida novu aktivitu do dat
     * @param activity Aktivita na pridanie
     */
    public void add(Activity activity) {
        this.activities.add(activity);
    }
    /**
     * Metoda odoberie zadanu aktivitu z dat
     * @param activity Aktivita na odobratie
     */
    public void remove(Activity activity) {
        this.activities.remove(activity);
    }
    /**
     * Metoda odoberie urcite aktivity na zaklade predikatu.
     * @param filter predikat
     * @return True ak sa podarilo odobrat aktivity na zaklade kriteria v predikate.
     */
    public boolean removeIf(Predicate filter) {
        return this.activities.removeIf(filter);
    }
    
    // Metody pre spracovanie dat z a do suboru
    /**
     * Metoda nacita JSON subor a deserializuje ho na list objektov(konkretne list roznych aktivit)
     * @param fileName Meno vstupneho suboru s aktivitami
     * @return False ak sa nepodarilo otvorit subor
     */
    @Override
    public boolean loadFromFile(String fileName) {
        try(InputStream activitiesJSONInputStream = new FileInputStream(fileName)) {
            
            activities = objectMapper
                    .readerFor(listOfActivitiesTypeReference)
                    .readValue(activitiesJSONInputStream);
            
            // Poznamename si meno suboru z ktoreho sme citali data
            setFileName(fileName);
            
            return true;
        } catch (IOException ex) {
            
            System.err.println("Nepodarilo sa otvorit subor s datami o aktivitach.");
            System.err.println(ex.getMessage());
            
            return false;
        }
    }
    
    /**
     * Metoda ulozi(serializuje) datovu reprezentaciu aktivit do JSON suboru
     * @param fileName Meno vystupneho suboru
     */
    @Override
    public void saveToFile(String fileName) {
        try {
            
            objectMapper
                    .writerFor(listOfActivitiesTypeReference)
                    .writeValue(new File(fileName), activities);
            
            // Poznamename si meno suboru do ktoreho sme ukladali data
            setFileName(fileName);
        } catch (IOException ex) {
            Logger.getLogger(ActivitiesData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Getters ----------------------------------------------
    /**
     * Metoda vrati vsetky aktivity v datach
     * @return List vsetkych aktivit
     */
    public List<Activity> getActivities() {
        return activities;
    }

    // Setters ----------------------------------------------
    /**
     * Metoda nastavi nove aktivity popripade nahradi stare aktivity.
     * @param activities List aktivit na nastavenie
     */
    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }
}