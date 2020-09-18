package com.mavenproject.timescheduler;

import com.mavenproject.timescheduler.data.SettingsData;
import com.mavenproject.timescheduler.models.Activity;
import com.mavenproject.timescheduler.models.ActivityStringRepresentation;
import java.awt.TrayIcon.MessageType;
import java.awt.AWTException;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.net.MalformedURLException;

/**
 *  Trieda zobrazi notifikaciu o najblizsej aktivite, ktora studenta caka.
 * 
 * @author Michal
 */
public class ActivityNotification {
    
    private static void generateNotification(String messageTitle, String messageDescription) throws AWTException, MalformedURLException {
        // Ziskame objekt pre zobrazenie notifikacie
        SystemTray notification = SystemTray.getSystemTray();
        
        // Zistime, ci nasa aplikacia uz nema TrayIcon zobrazeny
        // Tato metoda vrati iba TrayIcons vytvorene tymto programom
        TrayIcon[] messages = notification.getTrayIcons();
        int messagesLength = messages.length;
        
        TrayIcon message;
        
        if (messagesLength > 1) {
            message = messages[messagesLength - 1];
            
            for (int i = 0; i < (messagesLength - 1); i++) {
                notification.remove(messages[i]);
            }
            
        } else if (messages.length == 1) {
            message = messages[0];
            
        } else {
            // Pre zobrazenie spravy v notifikacii potrebujeme vytvorit TrayIcon object.
            // Je potrebne pridat obrazok ikonky v notifikacii
            message = new TrayIcon(
                    Toolkit.getDefaultToolkit().createImage("calendar-icon.png"),
                    "Notifikacia aktivity"
            );
            
            // Dalsie prednastavenia vzhladu spravy
            message.setImageAutoSize(true);
            message.setToolTip("Time Scheduler");
            // Pridame spravu do notifikacie
            notification.add(message);
        }
        
        // Zobrazime spravu
        message.displayMessage(messageTitle, messageDescription, MessageType.INFO);

    }
    
    public static boolean showNotification(Activity activity, SettingsData settingsData) throws AWTException, MalformedURLException {
        if (SystemTray.isSupported()) {
            
            ActivityStringRepresentation asr = activity.generateStringRepresentation(settingsData.getTimeZone());
            
            String descript = "";
            descript = asr.description.stream().map((str) -> str + "\n").reduce(descript, String::concat);
            generateNotification(asr.title, descript);
            
            return true;
        } else {
            System.err.println("Notifikacie nie su systemom podporovane.");
            return false;
        }
    }
}
