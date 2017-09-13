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
import java.util.Date;
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
        String csvInputPath = IFida.getMainFolder() + File.separator + folderName + File.separator + csvName;
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
        
        RowTicker lastRowTk = new RowTicker();
        
        int lastRow = 1;
        for (int row = 0; datas.get(row) != null; row++) {
            
            if (lastRow != 1){
                Calendar cal_today = string2Cal(datas.get(row).get(0));
                Calendar cal_yesterday = Calendar.getInstance();
                cal_yesterday.setTime(lastRowTk.getDateTk());
                while(!checkConsecutiveDate(cal_today, cal_yesterday)){
                    cal_yesterday.add(Calendar.DATE, 1);
                    java.sql.Date sqlDate = new java.sql.Date(cal_yesterday.getTime().getTime());
                    RowTicker holidayTk = new RowTicker();
                    holidayTk.setDateTk(sqlDate);
                    holidayTk.setOpenTk(lastRowTk.getOpenTk());
                    holidayTk.setHighTk(lastRowTk.getHighTk());
                    holidayTk.setLowTk(lastRowTk.getLowTk());
                    holidayTk.setCloseTk(lastRowTk.getCloseTk());
                    myTicker.add(holidayTk);
                    lastRow = lastRow + 1;
               }
            }
            
            RowTicker myRowTk = new RowTicker(datas.get(row).get(0), datas.get(row).get(1), 
                datas.get(row).get(2), datas.get(row).get(3), datas.get(row).get(4));
            
            myTicker.add(myRowTk);
            lastRowTk = myRowTk;
            lastRow = lastRow +1;
            
            try {
                datas.get(row + 1);
            } catch (NullPointerException | IndexOutOfBoundsException e) {
                break;
            }
            
        }
        return myTicker;
    }

    public static ArrayList<RowTicker> getMonthlyTicker(ArrayList<RowTicker> myTicker) {

        ArrayList<RowTicker> myQuartTicker = new ArrayList<>();
        
        Integer tkNum = 1;
        Calendar lastCal = Calendar.getInstance();
        Integer lastMonth = 0;
        for (RowTicker tk: myTicker){
            if (tkNum.equals(1)){
                myQuartTicker.add(tk);
            }
            else{
                Calendar cal = Calendar.getInstance();
                cal.setTime(tk.getDateTk());
                Integer month = cal.get(Calendar.MONTH);
                if(!(month.equals(lastMonth))){
                    myQuartTicker.add(tk);
                }
            }
            lastCal.setTime(tk.getDateTk());
            lastMonth = lastCal.get(Calendar.MONTH);
            tkNum = tkNum + 1;
        }
        
        return myQuartTicker;
    }

    public static ArrayList<RowTicker> getQuarterlyTicker(ArrayList<RowTicker> myTicker) {

        ArrayList<RowTicker> myQuartTicker = new ArrayList<>();

        myTicker.stream().forEach((myRowTk) -> {
            Calendar calDate = Calendar.getInstance();
            calDate.setTime(myRowTk.getDateTk());
            int month = (calDate.get(Calendar.MONTH));
            if (month == 0 || month == 3 || month == 6 || month == 9) {
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
            Integer month = (calDate.get(Calendar.MONTH));
            if (month.equals(0)) {
                myAnnualTicker.add(myRowTk);
            }
        });
        return myAnnualTicker;
    }

    private static Calendar string2Cal(String dateStr) {
        java.sql.Date dateVal = RowTicker.string2SqlDate(dateStr);
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateVal);
        return cal;
    }
         
     public static boolean checkConsecutiveDate(Calendar today, Calendar yesterday){
        boolean consecutiveDaySameYear = today.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) &&
                  today.get(Calendar.DAY_OF_YEAR) == (yesterday.get(Calendar.DAY_OF_YEAR)+1);
        boolean consecutiveDayDifferentYear = today.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR)+1 &&
                  today.get(Calendar.DAY_OF_YEAR)== 1 &&
                (yesterday.get(Calendar.DAY_OF_YEAR)==365 || yesterday.get(Calendar.DAY_OF_YEAR)==366);
        boolean consecutiveDay = consecutiveDaySameYear || consecutiveDayDifferentYear;
        return consecutiveDay;
    }
    
}
