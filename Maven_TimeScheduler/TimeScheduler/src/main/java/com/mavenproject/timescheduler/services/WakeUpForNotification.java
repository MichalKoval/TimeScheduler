/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mavenproject.timescheduler.services;

import com.mavenproject.timescheduler.ActivitiesManager;
import com.mavenproject.timescheduler.ActivityNotification;
import com.mavenproject.timescheduler.NotificationHandler;
import com.mavenproject.timescheduler.data.SettingsData;
import com.mavenproject.timescheduler.models.Activity;
import java.awt.AWTException;
import java.net.MalformedURLException;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Deque;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Trieda definuje ulohu, ktora vykona zobrazenie notifikacie pre najblizsi nastaveny cas.
 */
public class WakeUpForNotification extends TimerTask {
    private ActivitiesManager activitiesManager = null;
    private SettingsData settingsData = null;
    private NotificationHandler notificationHandler = null;
    private Activity activityAboutToBeShownInNotification = null;
    private boolean taskFinished = false;
    
    public WakeUpForNotification(ActivitiesManager activitiesManager, SettingsData settingsData, NotificationHandler notificationHandler) {
        this.activitiesManager = activitiesManager;
        this.settingsData = settingsData;
        this.notificationHandler = notificationHandler;
    }
    
    /**
     * Privatna metoda pre zobrazenie notifikacii o najblizsej aktivite, ak taka existuje
     */
    private void findActivityAndShowNotification() {
        // Zistime ci pre dany cas skutocne este existuje aktivita
        if (notificationHandler.getDateTimeOfNextActivityNotification() == null) {
            return;
        }
        if ((activityAboutToBeShownInNotification = activitiesManager.findActivityByDateTime(notificationHandler.getDateTimeOfNextActivityNotification())) != null) {
            try {
                if (!ActivityNotification.showNotification(activityAboutToBeShownInNotification, settingsData)) {
                    notificationHandler.setNotificationSupported(false);
                }
            } catch (AWTException | MalformedURLException e) {
                notificationHandler.setNotificationSupported(false);
                System.out.println("Doslo ku chybe pri spusteni notifikacie. Zrejme notifikacie nie su podporovane plaformou.");
            }
        }
    }
    
    /**
     * Runnable metoda pre zobrazenie najblizsej aktivity a predchystanie na dalsiu, ak existuje 
     */
    @Override
    public void run() {
        LocalDateTime localDateTimeNow = LocalDateTime.now(Clock.systemUTC());
        findActivityAndShowNotification();
        //Nastavime dalsiu novu aktivitu zatial na null, kedze este len budeme hladat dalsiu
        notificationHandler.setDateTimeOfNextActivityNotification(null);
        
        if (notificationHandler.isNotificationSupported() && notificationHandler.isNotificationRunning()) {
            // Najdeme najblizsi mozny cas zaciatku aktivity pre zobrazenie notifikacie o danej aktivite.
            // Taky cas nemusi existovat
            LocalDateTime nextDateTime;
            if ((nextDateTime = activitiesManager.findClosestActivityStartDateTimeFromNow(localDateTimeNow)) != null) {

                notificationHandler.setDateTimeOfNextActivityNotification(nextDateTime);
                long waitForTimeInMiliseconds = Math.abs(TimeUnit.SECONDS.toMillis(((Duration)Duration.between(localDateTimeNow, nextDateTime)).getSeconds()));
                // Vytvorime novy timer, a pridame ho do fronty predpripravenych casovacov pre notifikacie
                // Aby sme mali referencie na casovace ak ich bude treba ukoncit
                
                // Ziskame casovac pre notifikacie
                Timer notificationTimer = notificationHandler.getTimer();
                
                // Vytvorime novu ulohu pre zobrazenie nofitikacie pre casovac, a pridame ju do zoznamu referencii uloh, aby sme neskor mohli previest dodatocne operacie nad ulohami, ich zrusenie
                Deque<WakeUpForNotification> notificationTimerTasks = notificationHandler.getTimerTasks();
                WakeUpForNotification newWakeUpForNotification = new WakeUpForNotification(activitiesManager, settingsData, notificationHandler);
                notificationTimerTasks.add(newWakeUpForNotification);

                // Podla predrataneho casoveho intervalu sa spusti novy task so zobrazenim notifikacie a najdenim novej.
                notificationTimer.schedule(newWakeUpForNotification, waitForTimeInMiliseconds);
                
                // DEBUG: Nastaveny kratky casovy rozostup medzi notifikaciami (1 sekunda == 1000ms), pre overenie funkcnosti.
                // Pozor casove rozdiely su v milisekundach.
                // Nenastavovat prilis kratky casovy interval. Moze to sposobit velmi rychle zaplnenie notifikaciami. Je tazke potom vsetky tasks zastavit.
                //notificationTimer.schedule(newWakeUpForNotification, 5000);
            } else {
                //Nenasla sa dalsia najblizsia aktivita, pozastavujeme notifikacie
                notificationHandler.setNotificationRunning(false);
            }
        }
        
        this.taskFinished = true;
    }
    
    /**
     * Metoda vrati, ci dany TimerTask uz prebehol
     * @return True, ak task skoncil
     */
    public boolean isTaskFinished() {
        return this.taskFinished;
    }
}
