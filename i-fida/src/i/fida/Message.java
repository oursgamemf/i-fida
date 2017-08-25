/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package i.fida;

import java.awt.Color;
import javax.swing.JTextField;

/**
 *
 * @author emanuele
 */
public class Message {

    public static String START_SCAN;
    public static String MAIN_FOLDER_SELECTED;
    public static String WRONG_BOOLEAN_STRING;
    public static String ERROR_CONFIG_FILE;
    public static String END_SCAN;

    public static void setLanguage(String leng) {
        switch (leng.toUpperCase().trim()) {
            case "IT":
                START_SCAN = "Inizio scansione cartelle";
                MAIN_FOLDER_SELECTED = "Cartella principale selezionata";
                WRONG_BOOLEAN_STRING = "Inserire \'true\' per aggiornare automaticamente i file, \'false\' altrimenti";
                ERROR_CONFIG_FILE = "Errore nei dati all'interno del file di configurazione";
                END_SCAN = "Scansione terminata";
                break;
            case "EN":
                START_SCAN = "Start folder scan";
                MAIN_FOLDER_SELECTED = "Main folder setted";
                WRONG_BOOLEAN_STRING = "Set \'true\' to automatically update files, \'false\' otherwise";
                END_SCAN = "Scan ended";
                break;
        }

    }


}
