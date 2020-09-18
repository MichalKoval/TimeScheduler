/*
 * 
 */
package com.mavenproject.timescheduler.dialogs;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Trieda obsahuje staticke metody pre spracovanie zadeho vstupu od uzivatela. Typy vstupov: y/s; textovy vstup, ciselny vstup, vyber polozky, casovy vstup
 */
public class ConsoleUserInput {
    private static final String newCommandArrow = "\n> ";
    
    /**
     * OLD, NOT USED: Metoda nacita vstup od uzivatela.
     * @return Retaz znakov zo vstupu.
     */
    private static String readInput2() {
        Console console = null;
        String inputStr = null;
        
        try {
            //Ziskame Console objekt
            console = System.console();
            
            //Ak sa nam podarilo ziskat console object, budeme citat vstup od uzivatela
            if (console != null) {
                inputStr = console.readLine();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return inputStr;
    }
    
    /**
     * Metoda vrati retazec znakov nacitanych zo vstupu od uzivatela
     * @return Retaz znakov zo vstupu.
     */
    private static String readInput() {
        String inputStr = null;
        
        try {
            BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
            inputStr = bufferRead.readLine();
        }
        catch(IOException ex)
        {
           ex.printStackTrace();
        }
        
        return inputStr;
    }
    
    private static void handleErrorRequireUserNavigation(ConsoleUserInput_NavigationSelect navigation) {
        
        if (navigation.isRequireNavigationSelection()) { 
            String errorMsgNavigationSelect = "ERROR: Zadajte prosim jeden z navigacnych prikazov: ";

            // Ziskame flags pre pozadovane navigacne prikazy
            ConsoleUserInput_Navigation requiredNavigations = navigation.getRequiredNavigations();

            if (requiredNavigations.isExit()) {
                errorMsgNavigationSelect += "e,";
            }
            if (requiredNavigations.isBack()) {
                errorMsgNavigationSelect += "b,";
            }
            if (requiredNavigations.isCancel()) {
                errorMsgNavigationSelect += "c,";
            }
            if (requiredNavigations.isSkip()) {
                errorMsgNavigationSelect += "s,";
            }
            if (requiredNavigations.isFinish()) {
                errorMsgNavigationSelect += "f,";
            }

            int lastCharIndex_ErrorMsgNavigationSelect = errorMsgNavigationSelect.length() - 1;

            if (errorMsgNavigationSelect.charAt(lastCharIndex_ErrorMsgNavigationSelect) == ',') {
                errorMsgNavigationSelect = errorMsgNavigationSelect.substring(0, lastCharIndex_ErrorMsgNavigationSelect);
            }

            // Input sipka pre novy vstup
            errorMsgNavigationSelect += "." + newCommandArrow;

            System.out.print(errorMsgNavigationSelect);

            navigation.setNavigation(ConsoleUserInput_Navigation.Navigations.REPEAT);
        }
    }
    
    /**
     * Metoda skontroluje, ci uzivatel zadal jeden z ocakavanych navigacnych prikazov: Exit, Naspat, Zrusit, Preskocit, Dokoncit
     * a vypiseme chybu ak nie.
     * @return False, ak nebol zvoleny jeden z moznych navigacnych prikazov.
     */
    private static boolean checkRequiredUserNavigation(ConsoleUserInput_NavigationSelect navigation) {
        // Zistime, ci bolo pozadovane kontrolovat spravne zadane navigacne prikazy pre konkretne menu
        
        // Ziskame flags pre pozadovane navigacne prikazy
        ConsoleUserInput_Navigation requiredNavigations = navigation.getRequiredNavigations();

        switch(navigation.getNavigation()) {
            case EXIT: 
                if (requiredNavigations.isExit()) {
                    return true;
                }

                handleErrorRequireUserNavigation(navigation);

                return false;

            case BACK: 
                if (requiredNavigations.isBack()) {
                    return true;
                }

                handleErrorRequireUserNavigation(navigation);

                return false;

            case CANCEL: 
                if (requiredNavigations.isCancel()) {
                    return true;
                }

                handleErrorRequireUserNavigation(navigation);

                return false;

            case SKIP: 
                if (requiredNavigations.isSkip()) {
                    return true;
                }

                handleErrorRequireUserNavigation(navigation);

                return false;

            case FINISH: 
                if (requiredNavigations.isFinish()) {
                    return true;
                }

                handleErrorRequireUserNavigation(navigation);

                return false;
            case NONE: 
                
                handleErrorRequireUserNavigation(navigation);

                return false;
        }
        
        
        // Vratime false, pretoze nebol zadany navigacny prikaz, a jednoznakovy text moze byt ako vstup od uzivatela pre ulozenie
        return false;
        
        
  
    }
    
    /**
     * Ak bol na vstupe zvoleny jeden z navigacnych prikazov: Exit, Naspat, Zrusit, Preskocit, Dokoncit
     * @param str Retazec zo vstupu.
     * @param navigation Ocakavane navigacne prikazy.
     * @return True, ak bol zvoleny jeden zo spravnych navigacnych prikazov. False
     */
    private static boolean isUserNavigation(String str, ConsoleUserInput_NavigationSelect navigation) {
        if (str.length() == 1) {
            
            switch (str) {
                case "e": navigation.setNavigation(ConsoleUserInput_Navigation.Navigations.EXIT); break;
                case "b": navigation.setNavigation(ConsoleUserInput_Navigation.Navigations.BACK); break;
                case "c": navigation.setNavigation(ConsoleUserInput_Navigation.Navigations.CANCEL); break;
                case "s": navigation.setNavigation(ConsoleUserInput_Navigation.Navigations.SKIP); break;
                case "f": navigation.setNavigation(ConsoleUserInput_Navigation.Navigations.FINISH); break;
                // Ak by to bol jednoznakovy vstup a nebolo by to pre prikazy navigacie, mohla by to byt YES/NO otazka, alebo vstup o velkosti 1, co vpripade textoveho vstupu zakazeme
                default: navigation.setNavigation(ConsoleUserInput_Navigation.Navigations.NONE); break;
            }

            // Skontrolujeme, ci uzivatel zadal jeden z ocakavanych navigacnych prikazov: Exit, Naspat, Zrusit, Preskocit, Dokoncit
            // a vypiseme chybu ak nie.
            return checkRequiredUserNavigation(navigation);
            
        } else {
            navigation.setNavigation(ConsoleUserInput_Navigation.Navigations.NONE);
            return false;
        }
    }
    
    /**
     * Enum reprezentujuci rozne typu vstupov, ake mozeme od uzivatela ocakavat
     */
    public enum QuestionType {
        YES_NO,
        CHOOSE_OPTION,
        NUMBER,
        TEXT,
        TIME,
        DATETIME
    }
    
    /**
     * Staticka metoda nacita zo vstupu odpoved na YES/NO otazku a overi korektnost. Ak je vstup nekorektny, bude uzivatel poziadany o zadanie YES/NO, Y/N znova.
     * @param navigation Typ navigacneho prikazu, ktory bol zvoleny pri vstupe. Ak bol zvoleny, potom ma navigacny prikaz prioritu pred inym vstupom
     * @param preFillWith NOT IMPLEMENTED: Ocakavany vstup od uzivatela, ktory by bol vyplneny do vstupu.
     * @return Ak bola zvolena odpoved YES vrati sa True, ak NO tak false. 
     */
    public static Boolean YesNoQuestion(ConsoleUserInput_NavigationSelect navigation, String preFillWith) {
        String userAnswer;
        
        while ((userAnswer = readInput()) != null) {            
            userAnswer = userAnswer.toLowerCase();
            
            //Overime, ze ci bolo zvolene exit, back, cancel, skip alebo finish
            if (isUserNavigation(userAnswer, navigation)) {
                return null; 
            } else if (navigation.getNavigation() == ConsoleUserInput_Navigation.Navigations.REPEAT) {
                continue;
            }
            
            switch (userAnswer) {
                case "y":
                case "yes":
                    return true;
                case "n":
                case "no":
                    return false;
                default:
                    System.out.print("ERROR: Zadajte prosim (y/n alebo yes/no)." + newCommandArrow);
                    break;
            }
        }
        
        return null;
    }
    /**
     * Staticka metoda nacita zo vstupu cislo zvolenej polozky a overi korektnost. Ak je vstup nekorektny, bude uzivatel poziadany o zadanie polozky z intervalu {-k, ..., 0, ..., n} znova.
     * @param navigation Typ navigacneho prikazu, ktory bol zvoleny pri vstupe. Ak bol zvoleny, potom ma navigacny prikaz prioritu pred inym vstupom.
     * @param preFillWith NOT IMPLEMENTED: Ocakavany vstup od uzivatela, ktory by bol vyplneny do vstupu.
     * @param rangeStart Zaciatok intervalu s polozkami, moze byt aj zaporne cislo, ake je potrebne zvolit casovu zonu z intervalu {-11, ... -1, 0, 1, ... 14 }.
     * @param rangeEnd Koniec pozadovaneho intervalu, do ktore maju spadnut ciselne hodnoty zo vstupu.
     * @return LocalTime reprezentujuci cislo zvolenej polozky.
     */
    public static Integer ChooseOption(ConsoleUserInput_NavigationSelect navigation, String preFillWith, int rangeStart, int rangeEnd) {
        String errorMsgNumberRange = "ERROR: Zadajte prosim ciselnu hodnotu z intervalu {" + rangeStart + ",...," + rangeEnd + "}." + newCommandArrow;
        String userOptionStr;
        int userOptionInt;
        
        if (rangeStart > rangeEnd) {
            System.err.println("INTERNAL ERROR 'ChooseOption': rangeStart < rangeEnd !.");
            return -1;
        }
        
        while ((userOptionStr = readInput()) != null) {            
            
            //Overime, ze ci bolo zvolene exit, back, cancel, skip alebo finish
            if (isUserNavigation(userOptionStr, navigation)) {
                return null; 
            } else if (navigation.getNavigation() == ConsoleUserInput_Navigation.Navigations.REPEAT) {
                continue;
            }
            
            try {
                userOptionInt = Integer.parseInt(userOptionStr);
                
                if (userOptionInt < rangeStart || userOptionInt > rangeEnd) {
                    System.out.print(errorMsgNumberRange);
                    continue;
                }
                
                return userOptionInt;
                
            } catch (NumberFormatException nfe) {
                System.out.print(errorMsgNumberRange);
            }
        }
        
        return null;
    }
    /**
     * Staticka metoda nacita zo vstupu ciselny udaj(typicky dlzka trvania aktivity v minutach) a overi korektnost. Ak je vstup nekorektny, bude uzivatel poziadany o zadanie ciselneho udaju znova.
     * @param navigation Typ navigacneho prikazu, ktory bol zvoleny pri vstupe. Ak bol zvoleny, potom ma navigacny prikaz prioritu pred inym vstupom.
     * @param preFillWith NOT IMPLEMENTED: Ocakavany vstup od uzivatela, ktory by bol vyplneny do vstupu.
     * @return LocalTime reprezentujuci zadane Int cislo.
     */
    public static Integer Number(ConsoleUserInput_NavigationSelect navigation, String preFillWith) {
        String errorMsgNumberRange = "Zadajte prosim ciselnu hodnotu. Hodnota musi byt nezaporne cislo." + newCommandArrow;
        String userIntStr;
        int userInt;
                
        while ((userIntStr = readInput()) != null) {            
            
            //Overime, ze ci bolo zvolene exit, back, cancel, skip alebo finish
            if (isUserNavigation(userIntStr, navigation)) {
                return null; 
            } else if (navigation.getNavigation() == ConsoleUserInput_Navigation.Navigations.REPEAT) {
                continue;
            }
            
            try {
                userInt = Integer.parseInt(userIntStr);
                
                if (userInt < 0 || userInt > Long.MAX_VALUE) {
                    System.out.print(errorMsgNumberRange);
                    continue;
                }
                
                return userInt;
                
            } catch (NumberFormatException nfe) {
                System.out.print(errorMsgNumberRange);
            }
        }
        
        return null;
    }
    /**
     * Staticka metoda nacita zo vstupu text a overi korektnost. Ak je vstup nekorektny, bude uzivatel poziadany o zadanie textu znova.
     * @param navigation Typ navigacneho prikazu, ktory bol zvoleny pri vstupe. Ak bol zvoleny, potom ma navigacny prikaz prioritu pred inym vstupom.
     * @param errorMsg Sprava zobrazena v pripade, ze text nebol korektne zadany, napr,: ze bol prazdny.
     * @param preFillWith NOT IMPLEMENTED: Ocakavany vstup od uzivatela, ktory by bol vyplneny do vstupu.
     * @return String reprezentujuci text. (Min. dlzka == dva znaky)
     */
    public static String Text(ConsoleUserInput_NavigationSelect navigation, String preFillWith, String errorMsg) {
        String userText;
        
        while((userText = readInput()) != null) {
            
            if (userText.isEmpty()) {
                System.out.print("ERROR: Zadajte prosim " + errorMsg + "." + newCommandArrow);
            } else {
                
                //Overime, ze ci bolo zvolene exit, back, cancel, skip alebo finish
                if (isUserNavigation(userText, navigation)) {
                    return null; 
                } else if (navigation.getNavigation() == ConsoleUserInput_Navigation.Navigations.REPEAT) {
                    continue;
                }
                
//                // Ak bol text dlzky == 1, ale nebol to navigacny prikaz, potom uzivatela upozornime, ze text musim byt dlhy minimalne dva znaky.
//                if (userText.length() == 1) {
//                    System.out.print("ERROR: Zadajte prosim text, ktory ma aspon dva znaky." + newCommandArrow);
//                }
                
                return userText;
            }
        }
        
        return null;
    }
    /**
     * Staticka metoda nacita zo vstupu casovy udaj a overi korektnost. Ak je vstup nekorektny, bude uzivatel poziadany o zadanie casu znova.
     * @param navigation Typ navigacneho prikazu, ktory bol zvoleny pri vstupe. Ak bol zvoleny, potom ma navigacny prikaz prioritu pred inym vstupom.
     * @param preFillWith NOT IMPLEMENTED: Ocakavany vstup od uzivatela, ktory by bol vyplneny do vstupu.
     * @return LocalTime reprezentujuci cas.
     */
    public static LocalTime Time(ConsoleUserInput_NavigationSelect navigation, String preFillWith) {
        String userAnswer;
        
        while ((userAnswer = readInput()) != null) {            
            userAnswer = userAnswer.toLowerCase();
            
            //Overime ze nebolo zvolene exit, back, cancel, skip alebo finish
            if (isUserNavigation(userAnswer, navigation)) {
                return null; 
            } else if (navigation.getNavigation() == ConsoleUserInput_Navigation.Navigations.REPEAT) {
                continue;
            }
            
            try {
                return LocalTime.parse(userAnswer);
                
            } catch (DateTimeParseException dtpe) {
                System.out.print("ERROR: Zadajte prosim cas vo formate: HH:mm. (Napr. 16:45)" + newCommandArrow);
            }            
        }
        
        return null;
    }
    /**
     * Staticka metoda nacita zo vstupu datum a cas a overi ich korektnost. Ak je vstup nekorektny, bude uzivatel poziadany o zadanie datumu a casu znova.
     * @param navigation Typ navigacneho prikazu, ktory bol zvoleny pri vstupe. Ak bol zvoleny, potom ma navigacny prikaz prioritu pred inym vstupom.
     * @param preFillWith NOT IMPLEMENTED: Ocakavany vstup od uzivatela, ktory by bol vyplneny do vstupu.
     * @return LocalDateTime reprezentujuci datum a cas.
     */
    public static LocalDateTime DateTime(ConsoleUserInput_NavigationSelect navigation, String preFillWith) {
        String userAnswer;
        
        while ((userAnswer = readInput()) != null) {            
            userAnswer = userAnswer.toLowerCase();
            
            //Overime ze nebolo zvolene exit, back, cancel, skip alebo finish
            if (isUserNavigation(userAnswer, navigation)) {
                return null; 
            } else if (navigation.getNavigation() == ConsoleUserInput_Navigation.Navigations.REPEAT) {
                continue;
            }
            
            try {
                return LocalDateTime.parse(userAnswer, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                
            } catch (DateTimeParseException dtpe) {
                System.out.print("ERROR: Zadajte prosim cas vo formate: yyyy-MM-dd HH:mm. (Napr. 2018-05-21 16:45)" + newCommandArrow);
            }   
        }
        
        return null;
    }
}
