/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package i.fida;

import com.opencsv.CSVReader;
import i.fida.gui.iFidaGui;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author virginia
 */
public class ManageCSV {
    
    private static char sep = ';';
    
    public static char getSep() {
        return sep;
    }
    
    public static void setSep(char sep) {
        ManageCSV.sep = sep;
    }
    
    
    public static ArrayList<ArrayList<String>> getAllDataFromCSV(String folderName, String csvName) {
        String csvInputPath = IFida.getMainFolder()+ File.separator + folderName + File.separator + csvName;
        // Get datas from csv file to ArrayList of ArrayList
        ArrayList<ArrayList<String>> allData = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(csvInputPath), sep);) {
            String[] nextLine;
            int numRow = 0;
            while ((nextLine = reader.readNext()) != null && numRow < 8000) {
                ArrayList<String> allRow = new ArrayList<>();
                allRow.addAll(Arrays.asList(nextLine));
                allData.add(allRow);
                numRow += 1;
            }
        } catch (FileNotFoundException ex) {
            System.out.println("CSV file not found");

        } catch (IOException ex) {
            System.out.println("Can not read CSV file");
        }
        return allData;
    }
    
    
    public static ArrayList<RowTicker> getRowTickerList(ArrayList<ArrayList<String>> datas) {
        // Return an Array whose elements are instances of the class RowTicker.

        ArrayList<RowTicker> myTicker = new ArrayList<>();
        Integer colNumber = ManageExcell.getHeaderList().length;
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        NumberFormat formatDouble = NumberFormat.getInstance(Locale.FRANCE);
        for (int row = 0; datas.get(row) != null; row++) {
            RowTicker myRowTk = new RowTicker();
            
            java.util.Date dateTemp = null;
            try {
                dateTemp = formatDate.parse(datas.get(row).get(0));
            } catch (ParseException ex) {
                iFidaGui.setOutMsgStr(Message.CANT_PARSE_CSV);
            }
            java.sql.Date dateVal = new java.sql.Date(dateTemp.getTime());
            myRowTk.setDateTk(dateVal);
            
            Number openTemp = null;
            Number highTemp = null;
            Number lowTemp = null;
            Number closeTemp = null;
            try {
                openTemp = formatDouble.parse(datas.get(row).get(1));
                highTemp = formatDouble.parse(datas.get(row).get(2));
                lowTemp = formatDouble.parse(datas.get(row).get(3));
                closeTemp = formatDouble.parse(datas.get(row).get(4));
            } catch (ParseException ex) {
                Logger.getLogger(ManageExcell.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            Double openVal = openTemp.doubleValue();
            myRowTk.setOpenTk(openVal);
            
            Double highVal = highTemp.doubleValue();
            myRowTk.setHighTk(highVal);
            
            Double lowVal = lowTemp.doubleValue();
            myRowTk.setLowTk(lowVal);
            
            Double closeVal = closeTemp.doubleValue();
            myRowTk.setCloseTk(closeVal);

            System.out.println("date: " + dateVal.toString() + ", open: " + openVal.toString()
                    + ", high: " + highVal.toString()+ ", low: " + lowVal.toString()
                    + ", close: " + closeVal.toString());
            
            myTicker.add(myRowTk);
            try {
                datas.get(row + 1);
            } catch (NullPointerException | IndexOutOfBoundsException e) {
                break;
            }
        }
        return myTicker;
    }
    
    
    public static ArrayList<RowTicker> getMonthlyTicker(ArrayList<RowTicker> myTicker) {
        // TO B EIMPLEMENTED
        ArrayList<RowTicker> myQuartTicker = new ArrayList<>();

        myTicker.stream().forEach((myRowTk) -> {
            Calendar calDate = Calendar.getInstance();
            calDate.setTime(myRowTk.getDateTk());
            int month = (calDate.get(Calendar.MONTH));
            if (month == 2 || month == 5 || month == 8 || month == 11) {
                myQuartTicker.add(myRowTk);
            }
        });
        return myQuartTicker;
    }
    
     public static ArrayList<RowTicker> getQuarterlyTicker(ArrayList<RowTicker> myTicker) {

        ArrayList<RowTicker> myQuartTicker = new ArrayList<>();

        myTicker.stream().forEach((myRowTk) -> {
            Calendar calDate = Calendar.getInstance();
            calDate.setTime(myRowTk.getDateTk());
            int month = (calDate.get(Calendar.MONTH));
            if (month == 2 || month == 5 || month == 8 || month == 11) {
                myQuartTicker.add(myRowTk);
            }
        });
        return myQuartTicker;
    }
     
     public static ArrayList<RowTicker> getAnnualTicker(ArrayList<RowTicker> myTicker) {

        ArrayList<RowTicker> myAnnualTicker = new ArrayList<>();

        myTicker.stream().forEach((myRowTk) -> {
            Calendar calDate = Calendar.getInstance();
            calDate.setTime(myRowTk.getDateTk());
            int month = (calDate.get(Calendar.MONTH));
            if (month == 11) {
                myAnnualTicker.add(myRowTk);
            }
        });
        return myAnnualTicker;
    }
     
    
    
}
