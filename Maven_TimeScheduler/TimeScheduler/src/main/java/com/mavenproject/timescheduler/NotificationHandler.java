package com.mavenproject.timescheduler;

import com.mavenproject.timescheduler.data.SettingsData;
import com.mavenproject.timescheduler.services.WakeUpForNotification;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Timer;

/**
 * Trieda reprezentuje spravcu notifikacii, ktore budu zobrazovane pocas behu programu v urcity preddefinovany cas.
 */
public class NotificationHandler {
    private Timer timer = null;
    private Deque<WakeUpForNotification> timerTasks = null;
    private ActivitiesManager activitiesManager = null;
    private SettingsData settingsData = null;
    private LocalDateTime dateTimeOfNextActivityNotification = null;
    private boolean notificationSupported = true;
    private boolean notificationRunning = false;

    /**
     * Konstruktor nastavi sluzbu WakeUpForNotification, ktora sa bude starat o hladanie najblizsich casov aktivit a ich zobrazenie, ktore maju nastat.
     * @param activitiesManager Spravca dat s aktivitami
     * @param settingsData Konfiguracne data programu
     */
    public NotificationHandler(ActivitiesManager activitiesManager, SettingsData settingsData) {
        this.activitiesManager = activitiesManager;
        this.settingsData = settingsData;
        this.timer = null;
        this.timerTasks = new ArrayDeque<>();
    }
    
    /**
     * Metoda ukonci vsetky naplanovane casove, ak nejake existuju
     */
    private void cancelOldTimerTasksIfPresent() {
        if (!timerTasks.isEmpty()) {
            
            // Zrusime vsetky casovace v Queue timeru, manualne, kedze mozu este stale prebiehat
            timerTasks.forEach(t -> t.cancel());
           
            // Odoberieme vsetky zrusene notifikacie z Queue casoavaca
            if (timer != null) {
                timer.cancel();
                timer.purge();
            }
            
            
            // Zmazeme referencie na nacasovane notifikacie
            timerTasks.clear();
        }
    }
    
    /**
     * Metoda spusti zobrazovanie notifikacii.
     */
    public void startNotifications() {
        notificationRunning = true;
        // Zrusime vsetky stare naplanovane notifikacie
        cancelOldTimerTasksIfPresent();
        if (notificationSupported) {
            WakeUpForNotification newWakeUpForNotification = new WakeUpForNotification(activitiesManager, settingsData, this);
            this.timerTasks.add(newWakeUpForNotification);
            this.timer = new Timer();
            this.timer.schedule(newWakeUpForNotification, 0);
        }
    }
    
    /**
     * Metoda pozastavi zobrazovanie notifikacii.
     */
    public void stopNotifications() {
        // Zrusime vsetky stare naplanovane notifikacie
        notificationRunning = false;
        cancelOldTimerTasksIfPresent();
        
    }
    
    /**
     * Metoda vrati casovy udaj zaciatku pre dalsiu nadchadzajucu aktivitu v buducnosti.
     * @return Casovy udaj dalsej aktivity od aktualneho casu
     */
    public LocalDateTime getDateTimeOfNextActivityNotification() {
        return dateTimeOfNextActivityNotification;
    }
    
    /**
     * Metoda nastavi najblizsi casovy udaj zaciatku pre dalsiu nadchadzajucu aktivitu v buducnosti.
     * @param dateTimeOfNextActivityNotification Casovy udaj dalsej aktivity od aktualneho casu.
     */
    public void setDateTimeOfNextActivityNotification(LocalDateTime dateTimeOfNextActivityNotification) {
        this.dateTimeOfNextActivityNotification = dateTimeOfNextActivityNotification;
    }
    
    /**
     * Metoda vrati referenciu na casovac pre notfikacie/
     * @return Casovac notifikacii.
     */
    public Timer getTimer() {
        return this.timer;
    }
    
    /**
     * Metoda vrati vsetky naplanovane casovace.
     * @return Naplanovane casovace.
     */
    public Deque<WakeUpForNotification> getTimerTasks() {
        return timerTasks;
    }

    /**
     * Metoda vrati, ci su notifikacie podporovane systemom.
     * @return true ak su notifikacie podporovane.
     */
    public boolean isNotificationSupported() {
        return notificationSupported;
    }

    /**
     * Metoda vrati, ci je dovolene aby bola notifikacia zobrazovana.
     * @return True ak je dovolene zobrazit notifikaciu.
     */
    public boolean isNotificationRunning() {
        return notificationRunning;
    }

    /**
     * Metoda nastavi, ci je dovolene zobrazovat notifikace.
     * @param notificationRunning True ak je dovolene zobrazit notifikaciu.
     */
    public void setNotificationRunning(boolean notificationRunning) {
        this.notificationRunning = notificationRunning;
    }
    
    /**
     * Metoda nastavi ci su notifikacie na platforme podporovane, alebo notifikacie zastavi.
     * @param notificationSupported flag ze notifikacie su podporvane/nepodporovane.
     */    
    public void setNotificationSupported(boolean notificationSupported) {
        this.notificationSupported = notificationSupported;
    }
    
    
    
}
