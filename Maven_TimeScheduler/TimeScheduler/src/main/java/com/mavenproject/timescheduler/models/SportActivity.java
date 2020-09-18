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
 * Trieda reprezentuje sportovy aktivitu studenta. Vychadza z triedy 'DefaultActivity' a doplnuje popis druhu sportovej aktivity
 * 
 * @author Michal
 */
@JsonInclude(Include.NON_NULL)
@JsonTypeName("sportActivity")
public class SportActivity extends DefaultActivity {
    
    private SportActivity.SportActivities sportActivityType = SportActivities.ANOTHER;

    
    
    // Enums ------------------------------------------------
    /**
     * Enum reprezentujuci typy sportovych aktivit, je mozne ich prekonvertovat do string reprezentacie, pomocou pravenej metody toString().
     */
    public static enum SportActivities {
        ARCHERY("Lukostrelba"),
        AQUATIC_AND_PADDLE("Voda a vodne bicyklovanie"),
        BASKETBALL("Basketbal"),
        BAT_AND_BALL("Lopta a siet"),
        BOARD("Board"),
        CLIMBING("Horolezectvo"),
        CYCLING("Jazda na bicykli"),
        COMBAT("Zapasenie"),
        CUE("Kulecnik"),
        EQUINE("Jazdectvo"),
        FISHING("Rybarcenie"),
        FOOTBALL("Futbal"),
        GOLF("Golf"),
        GYMNASTICS("Gymnastika"),
        HANDBALL("Hadzana"),
        ICE("Hra na lade"),
        ORIENTEERING("Orienteering"),
        RACQUET("Raketove sporty"),
        RUNNING("Behanie"),
        SAILING("Plachtenie"),
        SKIING("Lyzovanie"),
        SHOOTING("Strelba"),
        STICK_AND_BALL("Palica a lopta"),
        WALKING("Chodza"),
        ANOTHER("Ine");
        
        private final String description;
        
        private SportActivities(final String description) {        
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
    public SportActivity() {}
    /**
     * Konstruktor nastavuje typ sportovej aktivity.
     * Takztiez nastavuje vlastnosti z abstraktnej super triedy Activity, ako: zaciatok konania aktivity a dlzka trvania sportovej aktivity.
     * @param sportActivityType Enum s typom sportovej aktivity.
     * @param startDT Zaciatok konania sportovej aktivity.
     * @param duration Dlzka trvania sportovej aktivity.
     */
    public SportActivity(SportActivities sportActivityType, LocalDateTime startDT, long duration) {
        super(startDT, duration);
        this.sportActivityType = sportActivityType;
    }
    /**
     * Konstruktor nastavuje typ sportovej aktivity.
     * Taktiez nastavuje vlastnosti z abstraktnej super triedy DefaultActivity, ako: kratky popis sportovej aktivity.
     * A dalej nastavuje vlastnosti z abstraktnej super triedy Activity, ako: zaciatok konania aktivity a dlzka trvania sportovej aktivity.
     * @param sportActivityType Enum s typom sportovej aktivity.
     * @param description Kratky popis sportovej aktivity
     * @param startDT Zaciatok konania sportovej aktivity.
     * @param duration Dlzka trvania sportovej aktivity.
     */
    public SportActivity(SportActivities sportActivityType, String description, LocalDateTime startDT, long duration) {
        super(description, startDT, duration);
        this.sportActivityType = sportActivityType;
    }
    /**
     * Konstruktor nastavuje typ sportovej aktivity.
     * Taktiez nastavuje vlastnosti z abstraktnej super triedy DefaultActivity, ako: miesto konania a kratky popis sportovej aktivity.
     * A dalej nastavuje vlastnosti z abstraktnej super triedy Activity, ako: zaciatok konania aktivity a dlzka trvania sportovej aktivity.
     * @param sportActivityType Enum s typom sportovej aktivity.
     * @param description Kratky popis sportovej aktivity
     * @param location Miesto konania sportovej aktivity.
     * @param startDT Zaciatok konania sportovej aktivity.
     * @param duration Dlzka trvania sportovej aktivity.
     */
    public SportActivity(SportActivities sportActivityType, String description, String location, LocalDateTime startDT, long duration) {
        super(description, startDT, duration, location);
        this.sportActivityType = sportActivityType;
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
        String title = "Sportova aktivita";
        List<String> descript = new ArrayList<>();
        if (getSportActivityType() != null) { descript.add("Typ: " + getSportActivityType().toString()); }
        if (getDescription() != null) { descript.add("Popis: " + getDescription()); }
        if (getLocation() != null) { descript.add("Miesto: " + getLocation()); }
        if (getStartDT() != null) { descript.add("Zaciatok: " + getStartDT().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))); }
        if (getEndDT() != null) { descript.add("Koniec: " + getEndDT().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))); }
        
        return new ActivityStringRepresentation(title, descript);
    }
    /**
     * Metoda vrati typ sportovej aktivity.
     * @return Enum s typom sportovej aktivity.
     */
    public SportActivities getSportActivityType() {
        return this.sportActivityType;
    }

    // Setters ----------------------------------------------
    /**
     * Metoda nastavi typ sportovej aktivity.
     * @param sa Enum s typom sportovej aktivity.
     */
    public void setSportActivityType(SportActivities sa) {
        this.sportActivityType = sa;
    }
}