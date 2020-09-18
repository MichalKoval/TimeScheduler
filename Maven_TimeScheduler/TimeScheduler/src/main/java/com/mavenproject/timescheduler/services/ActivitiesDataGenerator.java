/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mavenproject.timescheduler.services;

import com.mavenproject.timescheduler.data.ActivitiesData;
import com.mavenproject.timescheduler.models.DoNotDisturb;
import com.mavenproject.timescheduler.models.FreeTime;
import com.mavenproject.timescheduler.models.SchoolActivity;
import com.mavenproject.timescheduler.models.SportActivity;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

/**
 * Generovanie dat pre otestovanie programu
 * @author Michal
 */
public class ActivitiesDataGenerator {
    
    /**
     * Metoda predgeneruje ukazkove data pre urcity semester zacinajuci dnom X po dobu N tyzdnov
     * Metodu je nasledne mozne upravit podla potrieb.
     * @return ActivitiesData
     */
    public static ActivitiesData generate() {
        ActivitiesData activitiesData = new ActivitiesData();
        
        //LocalDateTime pre casy zaciatkov predmetov
        LocalDateTime[] ldt = new LocalDateTime[9];
        ldt[0] = LocalDateTime.of(2017,Month.FEBRUARY,27,8,0);
        ldt[1] = LocalDateTime.of(2017,Month.FEBRUARY,27,11,20);
        ldt[2] = LocalDateTime.of(2017,Month.FEBRUARY,27,14,40);
        ldt[3] = LocalDateTime.of(2017,Month.FEBRUARY,28,11,20);
        ldt[4] = LocalDateTime.of(2017,Month.MARCH,1,8,0);
        ldt[5] = LocalDateTime.of(2017,Month.MARCH,1,13,0);
        ldt[6] = LocalDateTime.of(2017,Month.MARCH,1,16,20);
        ldt[7] = LocalDateTime.of(2017,Month.MARCH,3,8,0);
        ldt[8] = LocalDateTime.of(2017,Month.MARCH,3,11,20);
        
        
        //------------Skolske aktivity s pevne urcenym casom-------------------------------------------------------
        for (int i = 0; i < 13; i++) {
            activitiesData.add(
                new SchoolActivity(
                    "Technologie XML",
                    "Holubová Irena, doc. RNDr., NPRG036",
                    "S3",
                    SchoolActivity.SchoolActivities.LECTURE,
                    ldt[0],
                    90L
                )
            );
            
            //localDateTime = localDateTime.plusHours();
            activitiesData.add(
                new SchoolActivity(
                    "Databázové systémy",
                    "Lokoč Jakub, RNDr., Ph.D., NDBI025",
                    "S7",
                    SchoolActivity.SchoolActivities.PRACTICAL,
                    ldt[1],
                    90L
                )
            );
            
            activitiesData.add(
                new SchoolActivity(
                    "Optimalizační metody",
                    "Dvořák Pavel, Mgr., NOPT048",
                    "S10",
                    SchoolActivity.SchoolActivities.PRACTICAL,
                    ldt[2],
                    90L
                )
            );
            
            activitiesData.add(
                new SchoolActivity(
                    "Tělesná výchova IV",
                    "Teplý Jiří, Mgr., NTVY017",
                    "KTV",
                    SchoolActivity.SchoolActivities.PRACTICAL,
                    ldt[3],
                    45L
                )
            );
            
            activitiesData.add(
                new SchoolActivity(
                    "Automaty a gramatiky",
                    "Majerech Vladan, Mgr., NTIN071",
                    "S10",
                    SchoolActivity.SchoolActivities.PRACTICAL,
                    ldt[4],
                    90L
                )
            );
            
            activitiesData.add(
                new SchoolActivity(
                    "Optimalizační metody",
                    "Martin Loebl, prof. RNDr., CSc., NOPT048",
                    "S5",
                    SchoolActivity.SchoolActivities.LECTURE,
                    ldt[5],
                    90L
                )
            );
            
            activitiesData.add(
                new SchoolActivity(
                    "Automaty a gramatiky",
                    "Vomlelová Marta, Mgr., Ph.D., NTIN071",
                    "S5",
                    SchoolActivity.SchoolActivities.LECTURE,
                    ldt[6],
                    90L
                )
            );
            
            activitiesData.add(
                new SchoolActivity(
                    "Technologie XML",
                    "Hemlich Jiří, RNDr., NPRG036",
                    "SW2",
                    SchoolActivity.SchoolActivities.PRACTICAL,
                    ldt[7],
                    90L
                )
            );
            
            activitiesData.add(
                new SchoolActivity(
                    "Databázové systémy",
                    "Kopecký Michal, RNDr., Ph.D., NDBI025",
                    "S5",
                    SchoolActivity.SchoolActivities.LECTURE,
                    ldt[8],
                    90L
                )
            );
            
            // Posunieme sa na dalsi tyzden
            for (int j = 0; j < 9; j++) {
                ldt[j] = ldt[j].plusDays(7);
            }
        }
        
        //--------------Dalsie aktivity(s konkretnym vymedzenym casom, DoNotDisturb, volny cas, atd..)-----------------
        
        //Spanok opakovany denne
        activitiesData.add(
                new DoNotDisturb(
                        DoNotDisturb.Events.SLEEP,
                        DoNotDisturb.Repeat.DAILY,
                        LocalTime.of(22, 0),
                        480 //Dlzka spanku v minutach 8 hodin == 480 min
                )
        );
        
        //Obed
        activitiesData.add(
                new DoNotDisturb(
                        DoNotDisturb.Events.LUNCH,
                        DoNotDisturb.Repeat.WEDNESDAY,
                        LocalTime.of(11, 30),
                        30 //Dlzka obedu 30 min
                )
        );
        
        activitiesData.add(
                new DoNotDisturb(
                        DoNotDisturb.Events.LUNCH,
                        DoNotDisturb.Repeat.THURSDAY,
                        LocalTime.of(11, 30),
                        30 //Dlzka obedu 30 min
                )
        );
        
        //--------------Aktivity, ktore treba rozvrhnut do rozvrhu--------------------------------
        
        activitiesData.add(
            new SchoolActivity(
                "Java, zápočtový program",
                "Vypracovanie zápočtového programu na tému 'TimeScheduler'.",
                SchoolActivity.SchoolActivities.SEMESTRAL_WORK,
                87658,
                LocalDateTime.of(2018,Month.MAY,25,12,0)
            )
        );
        
        activitiesData.add(
            new SchoolActivity(
                "Téma r.p. a b.p.",
                "Dohodnúť tému ročníkového projektu a bakalárskej práce.",
                SchoolActivity.SchoolActivities.HOMEWORK,
                20160,
                LocalDateTime.of(2018,Month.MARCH,16,12,0)
            )
        );
        
        activitiesData.add(
            new SchoolActivity(
                "Ročníkový projekt",
                "Vypracovanie projektu na vopred dohodnutú tému.",
                SchoolActivity.SchoolActivities.SEMESTRAL_WORK,
                87658,
                LocalDateTime.of(2018,Month.MAY,31,12,0)
            )
        );
        
        activitiesData.add(
            new SchoolActivity(
                "Vypracovanie bakalárskej práce",
                "Vypracovanie bakalárskej práce nadväzujúcej na ročníkovy projekt.",
                SchoolActivity.SchoolActivities.BACHELOR_THESIS,
                262974,
                LocalDateTime.of(2018,Month.SEPTEMBER,15,0,0)
            )
        );
        
        activitiesData.add(
            new SportActivity(
                SportActivity.SportActivities.RACQUET,
                "Hra Tenis",
                "Kolej 17. listopadu, kurt pred Vltavou",
                LocalDateTime.of(2018, Month.JUNE, 5, 10, 0),
                60
            )
        );
        
        activitiesData.add(
            new SportActivity(
                SportActivity.SportActivities.RACQUET,
                "Hra Ping-Pong",
                "Kolej 17. listopadu",
                LocalDateTime.of(2018, Month.JUNE, 8, 18, 0),
                50
            )
        );
        
        activitiesData.add(
            new FreeTime(
                "Kino Cinemax",
                "Star Wars",
                LocalDateTime.of(2018, Month.JUNE, 9, 20, 0),
                90
            )
        );
        
        return activitiesData;
    }
}
