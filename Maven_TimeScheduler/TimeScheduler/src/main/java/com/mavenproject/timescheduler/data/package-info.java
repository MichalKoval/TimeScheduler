/**
 * Ulohou tried v tomto baliku je overit, ci je mozne data nacitat zo suborov alebo ulozit do suborov.
 * Data su reprezentovane v JSON strukture.
 * Triedy obsahuju ObjectMapper z dependencies Jackson, ktory sa stara o vhodne serializovanie a deserializovanie JSON suborov.
 * 'ActivitiesData' sa postara o nacitanie dat a aktivitami a ich udrziavanie v datovej strukture.
 * 'SettingsData' sa postara o nacitanie konfiguracnych dat pre nasledny spravny chod programu.
 * 
 * 'ActivitiesData' a 'SettingsData' su naslednu pouzivane triedami z balicka @see com.mavenproject.timescheduler
 * 
 * @see com.mavenproject.timescheduler.data.Data - zakladna trieda od ktorej su odvodene triedy: ActivitiesData a SettingsData
 * @see com.mavenproject.timescheduler.data.ActivitiesData - Trieda, ktorej ulohou je data o aktivitach nacitat zo suboru a udrzovatich ich v datovej strukture.
 * @see com.mavenproject.timescheduler.data.SettingsData - Trieda, ktorej ulohou je nacitat konfiguracne data.
 */
package com.mavenproject.timescheduler.data;
