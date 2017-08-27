/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package i.fida;

import com.opencsv.CSVReader;
import i.fida.db.Sources;
import i.fida.gui.iFidaGui;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
    private static final String PATH_TO_TEMP_CONFIG = "i_fida_temp.cfg";
    private static final Path CUR_PATH = Paths.get(System.getProperty("user.dir"));
    private static final String INSIDE_FULL_PATH = CUR_PATH.toString() + File.separator;//curPath.getParent().toString() + File.separator
    private static final String CONFIG_FULL_PATH = INSIDE_FULL_PATH + PATH_TO_CONFIG;
    private static final String CONFIG_TEMP_FULL_PATH = INSIDE_FULL_PATH + PATH_TO_TEMP_CONFIG;

    private static String PATH_TO_MAIN_FOLDER = null;

    public static void initConfig() {
        // Load Config file
        ArrayList<ArrayList<String>> configData = getAllDataFromCfgFile(CONFIG_FULL_PATH, ';');
        Sources sessionDB = new Sources();
        Sources.setsDBname(configData.get(0).get(1));
        Sources.setsTable(configData.get(1).get(1));
        Sources.setsFieldTableCreate(configData.get(2).get(1));
        Sources.setQuery(configData.get(3).get(1));
        Sources.connectOrCreate();
        Sources.createTable();
        Sources.setsFieldTableCreate(configData.get(4).get(1));
        setMainFolder(configData.get(5).get(1));        
    }

    public static void scanFolder() {
        iFidaGui.setOutMsgStr(Message.START_SCAN);

        iFidaGui.setOutMsgStr(Message.END_SCAN);
    }

    public static ArrayList<String> getSubDirectories(String parentName) {
        File file = new File(parentName);
        String[] filesAndDirectories = file.list();
        ArrayList<String> directories = new ArrayList<>();

        for (String f_d : filesAndDirectories) {
            if (new File(parentName + File.separator + f_d).isDirectory()) {
                directories.add(f_d);
            }
        }
        return directories;
    }

    public static ArrayList<String> getCSVinDirectory(String directoryName) {
        File dir = new File(directoryName);
        ArrayList<String> CSV_files = new ArrayList<>();

        for (File f : dir.listFiles()) {
            if (f.getName().endsWith(".csv")) {
                CSV_files.add(f.getName());
            }
        }
        return CSV_files;
    }

    public static ArrayList<ArrayList<String>> getAllDataFromCfgFile(String csvInputPath, char sep) {
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

    public static String getDataFromCfgFile(String csvInputPath, char sep, String key) {
        // Get datas from csv file to ArrayList of ArrayList
        ArrayList<ArrayList<String>> allData = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(csvInputPath)), sep, '"', '|');) {
            String[] nextLine;
            int numRow = 0;
            while ((nextLine = reader.readNext()) != null && numRow < 8000) {
                if (nextLine[0].toString().equals(key.concat("="))) {
                    return nextLine[1];
                }
                numRow += 1;
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(IFida.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(IFida.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static void setMainFolder(String path) {
        PATH_TO_MAIN_FOLDER = path;
        write2configFile("savedFidaPath", path);
    }

    public static String getMainFolder() {
        return PATH_TO_MAIN_FOLDER;
    }

    public static void write2configFile(String paramName, String selectedValue) {
        File inputFile = new File(getConfigFullPath());
        File tempFile = new File(getConfigTempFullPath());
        paramName += "=";
        boolean altradyExist = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile)); BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempFile), "UTF-8")) //BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
                ) {
            StringBuilder inputBuffer = new StringBuilder();

            for (String line; (line = reader.readLine()) != null;) {
                String[] rowElement = line.split(";");
                if (rowElement[0].equals(paramName)) {
                    altradyExist = true;
                    rowElement[1] = selectedValue;
                    inputBuffer.append(rowElement[0]).append(";").append(selectedValue).append(";").append(System.getProperty("line.separator"));
                } else {
                    inputBuffer.append(rowElement[0]).append(";").append(rowElement[1]).append(";").append(System.getProperty("line.separator"));
                }               
            }
            if (!altradyExist){
                    inputBuffer.append(paramName).append(";").append(selectedValue).append(";").append(System.getProperty("line.separator"));
                }
            String inputStr = inputBuffer.toString();
            reader.close();

            //System.out.println(inputStr); // check that it's inputted right
            try (FileOutputStream fileOut = new FileOutputStream(getConfigFullPath())) {
                fileOut.write(inputStr.getBytes());
                //inputFile.delete();
            }
            tempFile.delete();

        } catch (IOException ex) {
            Logger.getLogger(IFida.class.getName()).log(Level.SEVERE, null, ex);
            iFidaGui.setOutMsgStr(Message.ERROR_CONFIG_FILE);
        }
    }

    public static String getConfigFullPath() {
        return CONFIG_FULL_PATH;
    }

    private static String getConfigTempFullPath() {
        return CONFIG_TEMP_FULL_PATH;
    }

}


/*
// System.out.println(getDataFromCfgFile(CONFIG_FULL_PATH,';',"dbname"));
 */
