/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package i.fida;

import com.opencsv.CSVReader;
import i.fida.db.Sources;
import i.fida.gui.iFidaGui;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/**
 *
 * @author emanuele
 */
public class IFida {
    
    private static final String PATH_TO_CONFIG = "i_fida.cfg";
    private static final Path CUR_PATH = Paths.get(System.getProperty("user.dir"));
    private static final String INSIDE_FULL_PATH = CUR_PATH.toString() + File.separator;//curPath.getParent().toString() + File.separator
    private static final String CONFIG_FULL_PATH = INSIDE_FULL_PATH + PATH_TO_CONFIG;
    
    private static String PATH_TO_MAIN_FOLDER = null;
    
    public static void initConfig(){
        // Load Config file
        ArrayList<ArrayList<String>> configData = getAllDataFromFile(CONFIG_FULL_PATH, ';');
        Sources sessionDB = new Sources();
        sessionDB.setsDBname(configData.get(0).get(1));
        sessionDB.setsTable(configData.get(1).get(1));
        sessionDB.setsFieldTableCreate(configData.get(2).get(1));
        sessionDB.setQuery(configData.get(3).get(1));
        sessionDB.connectOrCreate();
        sessionDB.createTable();
        sessionDB.setsFieldTableCreate(configData.get(4).get(1));
        
        System.out.println(configData.get(0).get(1));
        System.out.println(configData.get(1).get(1));
        System.out.println(configData.get(2).get(1));
        System.out.println(configData.get(3).get(1));
        System.out.println(configData.get(4).get(1));
    }
    
    public static void scanFolder() {
        iFidaGui.setOutMsgStr(Message.START_SCAN);
    }
     
    public static ArrayList<String> getSubDirectories(String parentName){
        File file = new File(parentName);
        String[] filesAndDirectories = file.list();
        ArrayList<String> directories = new ArrayList<>();
        
        for(String f_d : filesAndDirectories)
        {
            if (new File(parentName+ "\\" + f_d).isDirectory())
            {
                directories.add(f_d);
            }
        }
        return directories;
    }

    public static ArrayList<String> getCSVinDirectory (String directoryName){
        File dir = new File(directoryName);
        ArrayList<String> CSV_files = new ArrayList<>();
        
        for(File f :dir.listFiles())
        {
            if (f.getName().endsWith(".csv"))
            {
                CSV_files.add(f.getName());
            }
        }
        return CSV_files;
    }
    
    public static ArrayList<ArrayList<String>> getAllDataFromFile(String csvInputPath, char sep) {
        // Get datas from csv file to ArrayList of ArrayList
        ArrayList<ArrayList<String>> allData = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(csvInputPath)), sep, '"', '|');) {
            String[] nextLine;
            int numRow = 0;
            while ((nextLine = reader.readNext()) != null && numRow < 8000) {
                ArrayList<String> allRow = new ArrayList<>();
                allRow.addAll(Arrays.asList(nextLine));
                allData.add(allRow);
                numRow += 1;
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(IFida.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(IFida.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return allData;
    }
    
    public static void setMainFolder(String path){
        PATH_TO_MAIN_FOLDER = path;
    }
    
    public static String getMainFolder(){
        return PATH_TO_MAIN_FOLDER;
    }
    
    public static void test(){
        initConfig();
    }
}
