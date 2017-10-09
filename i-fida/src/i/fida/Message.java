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
    public static String TABLE_HEADER_NAME;
    public static String TABLE_HEADER_UPDATEATSTART;
    public static String TABLE_HEADER_NUMBER;
    public static String TABLE_HEADER_LASTUPDATE;
    public static String DB_OK;
    public static String INVALID_MAINFOLDER_PATH;
    public static String CANT_PARSE_CSV;
    public static String FILE_SAVED;
    public static String FILE_NOTSAVED;
    public static String BU_ELAB_PRESSED;
    public static String BU_ELAB_END;
    public static String END_ELAB;
    public static String FILE_NOTREADBLE;
    public static String FILE_MODIFIED;
    public static String FILE_NOTMODIFIED;
    public static String DEC_SEP_ERR;
            
    public static void setLanguage(String leng) {
        switch (leng.toUpperCase().trim()) {
            case "IT":
                START_SCAN = "Inizio scansione cartelle";
                MAIN_FOLDER_SELECTED = "Cartella principale selezionata";
                WRONG_BOOLEAN_STRING = "Inserire \'true\' per aggiornare automaticamente i file, \'false\' altrimenti";
                ERROR_CONFIG_FILE = "Errore nei dati all'interno del file di configurazione";
                END_SCAN = "Analisi struttura delle cartelle terminata";
                TABLE_HEADER_NAME = "Nome";
                TABLE_HEADER_UPDATEATSTART = "Elabora contenuto";
                TABLE_HEADER_NUMBER = "Numero di file CSV";
                TABLE_HEADER_LASTUPDATE = "Ultimo aggiornamento";
                DB_OK = "Data Base Correttamente inizializzato";
                INVALID_MAINFOLDER_PATH = "Cartella principale invalida";
                CANT_PARSE_CSV = "Impossibile parsare il file CSV";
                FILE_SAVED = " salvato correttamente";
                FILE_NOTSAVED = "Impossibile salvare il file ";
                BU_ELAB_PRESSED = "In elaborazione attendere...";
                BU_ELAB_END = "Elabora CSV nelle cartelle";
                END_ELAB = "Generazione/Aggiornamento .xlsx terminato";
                FILE_NOTREADBLE = "Impossibile leggere il file ";
                FILE_MODIFIED = " modificato correttamente";
                FILE_NOTMODIFIED = "Impossibile modificare il file "; 
                DEC_SEP_ERR = "Separatore decimale non previsto. Usare '.' o ','";
                break;
                
            case "EN":
                START_SCAN = "Start folder scan";
                MAIN_FOLDER_SELECTED = "Main folder setted";
                WRONG_BOOLEAN_STRING = "Set \'true\' to automatically update files, \'false\' otherwise";
                END_SCAN = "Folder Scan ended";
                TABLE_HEADER_NAME = "Folder name";
                TABLE_HEADER_UPDATEATSTART = "File processing";
                TABLE_HEADER_NUMBER = "CSV files number";
                TABLE_HEADER_LASTUPDATE = "Last update";
                DB_OK = "DB ready";
                INVALID_MAINFOLDER_PATH = "Invalid main folder path";
                CANT_PARSE_CSV = "Can not parse CSV file";
                FILE_SAVED = " correctly saved";
                FILE_NOTSAVED = "Unable to save ";
                BU_ELAB_PRESSED = "File processing wait please...";
                BU_ELAB_END = "Process CSVs into the folders";
                END_ELAB = "Generation/Update .xlsx completed";
                FILE_NOTREADBLE = "Unable to read ";
                FILE_MODIFIED = " correctly modified";
                FILE_NOTMODIFIED = "Unable to modify ";
                DEC_SEP_ERR = "Wrong decimal separator, please use '.' o ','";
                break;
        }

    }


}
