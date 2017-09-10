/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package i.fida;

import i.fida.gui.iFidaGui;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author virginia
 */
public class ManageExcell {
    private static String[] headerList =  new String[] {"Data", "Open", "High", "Low", "Close"};
    public static RowTicker lastRowTk;

    
    public static String[] getHeaderList() {
        return headerList;
    }
 
    public static void setHeaderList(String[] headerList) {
        ManageExcell.headerList = headerList;
    }
       
    
    
    public static void createExcel(ArrayList<RowTicker> myTicker, Folder fold, String fileName) {
        Workbook myWb = new XSSFWorkbook();
        CreationHelper myCreateHelper = myWb.getCreationHelper();

        String mySheetName = "Girnaliero";
        addSheet2Excel(myWb, myCreateHelper, mySheetName, myTicker);
        
        // TO BE IMPLEMENTED
        ArrayList<RowTicker> myMonthTicker = ManageCSV.getMonthlyTicker(myTicker);
        String myMonthSheetName = "Mensile";        
        addSheet2Excel(myWb, myCreateHelper, myMonthSheetName, myMonthTicker);

        ArrayList<RowTicker> myQuarterTicker = ManageCSV.getQuarterlyTicker(myTicker);
        String myQuarterSheetName = "Trimestrale";
        addSheet2Excel(myWb, myCreateHelper, myQuarterSheetName, myQuarterTicker);

        ArrayList<RowTicker> myAnnualTicker = ManageCSV.getAnnualTicker(myTicker);
        String myAnnualSheetName = "Annuale";
        addSheet2Excel(myWb, myCreateHelper, myAnnualSheetName, myAnnualTicker);

        // Write the output to a file
        String outputFilePath = fold.getFullPath() + File.separator + fileName.substring(0, fileName.length()-4) + ".xlsx";
        // System.out.println(outputFilePath);
        FileOutputStream fileOut;
        try {
            fileOut = new FileOutputStream(outputFilePath);
            myWb.write(fileOut);
            fileOut.close();
            iFidaGui.setOutMsgStr("\'" + fileName + "\'" + Message.FILE_SAVED);
        } catch (FileNotFoundException ex) {
            //Logger.getLogger(ManageExcel.class.getName()).log(Level.SEVERE, null, ex);
            //System.out.println("Unable to write the file");
            iFidaGui.setOutMsgStr(Message.FILE_NOTSAVED + fileName + "\'");
        } catch (IOException ex) {
            //Logger.getLogger(ManageExcel.class.getName()).log(Level.SEVERE, null, ex);
            //System.out.println("Unable to write the file I/O Ex");
            iFidaGui.setOutMsgStr(Message.FILE_NOTSAVED + fileName + "\'");
        }

    }
    
    public static void addSheet2Excel(Workbook myWb, CreationHelper myCrHelper, String sheetName, ArrayList<RowTicker> myTicker) {
        Sheet mySheet = myWb.createSheet(sheetName);
        Row myHeaderRow = mySheet.createRow(0);
        setExcelHeader(myHeaderRow);
        int lastRow = 1;
        int rowsNum = myTicker.size();
        boolean isLast;
        for (RowTicker myRowTk : myTicker) {
            isLast = lastRow == rowsNum;
            Row myRow = mySheet.createRow(lastRow);
            if (lastRow != 1){
               Calendar cal_today = Calendar.getInstance();
               cal_today.setTime(myRowTk.getDateTk());
               Calendar cal_yesterday = Calendar.getInstance();
               cal_yesterday.setTime(lastRowTk.getDateTk());
               while(!checkConsecutiveDate(cal_today, cal_yesterday)){
                   cal_yesterday.add(Calendar.DATE, 1);
                   java.sql.Date sqlDate = new java.sql.Date(cal_yesterday.getTime().getTime());
                   lastRowTk.setDateTk(sqlDate);
                   addRow2Excel(myWb, myCrHelper, myRow, lastRowTk, isLast);
                   lastRow = lastRow + 1;
                   myRow = mySheet.createRow(lastRow);
               }   
            }
            addRow2Excel(myWb, myCrHelper, myRow, myRowTk, isLast);
            lastRow = lastRow + 1;
            lastRowTk = myRowTk;
        }
        mySheet.autoSizeColumn(0, false);
    }
    
     public static void setExcelHeader(Row myHeaderRow) {
        myHeaderRow.createCell(0).setCellValue(headerList[0]);
        myHeaderRow.createCell(1).setCellValue(headerList[1]);
        myHeaderRow.createCell(2).setCellValue(headerList[2]);
        myHeaderRow.createCell(3).setCellValue(headerList[3]);
        myHeaderRow.createCell(4).setCellValue(headerList[4]);

    }
     
     public static boolean checkConsecutiveDate(Calendar today, Calendar yesterday){
        boolean consecutiveDay = today.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) &&
                  today.get(Calendar.DAY_OF_YEAR) == (yesterday.get(Calendar.DAY_OF_YEAR)+1);
        return consecutiveDay;
    }
 
    public static void addRow2Excel(Workbook myWb, CreationHelper myCreateHelper,
            Row myRow, RowTicker myRowTk, boolean isLastRow) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(myRowTk.getDateTk());
        Cell myCell = myRow.createCell(0);
        
        if (isLastRow & cal.get(Calendar.DAY_OF_MONTH) != cal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
        } else {
            int day = cal.get(Calendar.DAY_OF_MONTH);
            String dayOfWeek = getDayOfWeek(cal.get(Calendar.DAY_OF_WEEK));
            int dayDigits = String.valueOf(day).length();
            String dayOfMonth;
            if(dayDigits<2){
                dayOfMonth = "0"+String.valueOf(day);
            }else{
                dayOfMonth = String.valueOf(day);
            }
            String monthOfYear = getMonthOfYear(cal.get(Calendar.MONTH));
            String year = String.valueOf(cal.get(Calendar.YEAR));
            String formattedDate = dayOfWeek + ", " + dayOfMonth + " " + monthOfYear + " " + year;
            myCell.setCellValue(formattedDate);
            myRow.createCell(1).setCellValue(myRowTk.getOpenTk());
            myRow.createCell(2).setCellValue(myRowTk.getHighTk());
            myRow.createCell(3).setCellValue(myRowTk.getLowTk());
            myRow.createCell(4).setCellValue(myRowTk.getCloseTk());
        }
    }
 
    public static String getDayOfWeek(int dayNumber){
    switch(dayNumber){
        case (1): return "Domenica";
        case (2): return "Lunedì";
        case (3): return "Martedì";
        case (4): return "Mercoledì";
        case (5): return "Giovedì";
        case (6): return "Venerdì";
        case (7): return "Sabato";
        default: return "Invalid day";
        }
    }
    
    public static String getMonthOfYear(int dayNumber){
        switch(dayNumber){
            case (0): return "Gennaio";
            case (1): return "Febbraio";
            case (2): return "Marzo";
            case (3): return "Aprile";
            case (4): return "Maggio";
            case (5): return "Giugno";
            case (6): return "Luglio";
            case (7): return "Agosto";
            case (8): return "Settembre";
            case (9): return "Ottobre";
            case (10): return "Novembre";
            case (11): return "Dicembre";
            default: return "Invalid month";
            }
    }
    
    
}
