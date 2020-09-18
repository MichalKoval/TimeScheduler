/**
 * Balik obsahuje main class v triede 'TimeScheduler' pre spustenie programu.
 * Uvodny popis programu z hladiska funkcnosti programu je mozne najst v subore 'TimeScheduler.java', popripade v uzivatelskej dokumentacii.
 * 
 * V tomto balicku sa nachadzaju triedy, ktore data o aktivitach spracovavaju.
 * Asi najpodstatnejsou triedou balicku je 'ScheduleActivities'. 
 * Ta sa pokusi rozvrhnut data o aktivitach. Predspracovane data jej poskytne spravca
 * dat 'ActivitiesManager', ktory data rozdeli na viacere skupiny: aktivity s pevne urcenym deadlinom,
 * aktivity s periodou opakovania a aktivity, ktore este nie si zaradene do rozvrhu.
 * 
 * 'ActivityManager' sa postara aj o nasledne ulozenie dat pomocou triedy 'ActivitiesData' @see com.mavenproject.timescheduler.data
 * 
 * Trieda 'NotificationHandler' zabezpecuje zobrazovanie casovo najblizsich aktivit v notifikacii. Predpoklada, ze data o aktivitach su uz uspecne rozvrhnute.
 * Informacie o najblizsich aktivitach dostane pomocou triedy 'ActivityManager'
 * 
 * 
 * @see com.mavenproject.timescheduler.ActivitiesManager ActivitiesManager
 * @see com.mavenproject.timescheduler.ActivityNotification ActivityNotification
 * @see com.mavenproject.timescheduler.NotificationHandler NotificationHandler
 * @see com.mavenproject.timescheduler.ScheduleActivities ScheduleActivities
 * @see com.mavenproject.timescheduler.TimeScheduler TimeScheduler
 */
package com.mavenproject.timescheduler;
