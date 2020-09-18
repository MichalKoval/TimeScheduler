/**
 * Balicek obsahuje triedy reprezentujuce datove modely jednotlivych aktivit.
 * Zakladnou abstraktnou triedou je 'Activity', ktora obsahuje zaciatok aktivity, jej trvanie a deadline.
 * Od tejto triedy je odvodena abstraktna trieda 'DefaultActivity', ktora doplnuje meno, kratky popis a miesto konania aktivity.
 * 
 * Od triedy 'DefaultActivity' su odvodene triedy 'SchoolActivity', 'SportSctivity' a 'FreeTime', ktore doplnuju dalsie vlastnosti specificke pre dane aktivity
 * Od triedy 'Activity' je odvodena trieda DoNotDisturb, ktora popisuje v aky cas Doba nerusenia zacina, a ako casto sa opakuje: denne, pondelok, utorok, .. a o aku druh ide: spanok, obed, ...
 * 
 * Vsetko tieto triedy s aktivitami su pouzite jednak pocas behu programu na rozvrhnutie do rozvrhu a jednak su vzorovymi modelmi
 * pre serializaciu a deserializaciu z a do suboru. Vyhodou je, ze vsetky vyssie spomenute triedy vychadzaju z 'Activity' a je mozne ich davat do spolocnych datovych struktur,
 * kedze zdielaju rovnaku super class.
 * 
 * Triedy ActivityStringRepresentation, ActivityWithArtificialDeadline a ActivityWithArtificialStartTimeAndLateness su pouzite pri rozvrhovani aktivit, ako docasne datove struktury. @see com.mavenproject.timescheduler
 */
package com.mavenproject.timescheduler.models;
