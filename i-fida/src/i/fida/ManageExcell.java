/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package i.fida;

import i.fida.gui.iFidaGui;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JTextField;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
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

        String mySheetName = "Giornaliero";
        addSheet2Excel(myWb, myCreateHelper, mySheetName, myTicker);
        
        // TO BE IMPLEMENTED
        ArrayList<RowTicker> myMonthTicker = ManageCSV.getMonthlyTicker(myTicker);
        String myMonthSheetName = "Mensile";        
        addSheet2Excel(myWb, myCreateHelper, myMonthSheetName, myMonthTicker);

        ArrayList<RowTicker> myQuarterTicker = ManageCSV.getQuarterlyTicker(myMonthTicker);
        String myQuarterSheetName = "Trimestrale";
        addSheet2Excel(myWb, myCreateHelper, myQuarterSheetName, myQuarterTicker);

        ArrayList<RowTicker> myAnnualTicker = ManageCSV.getAnnualTicker(myQuarterTicker);
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
            addRow2Excel(myWb, myCrHelper, myRow, myRowTk, isLast);
            lastRow = lastRow + 1;
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
    
    public static void modifyExcel(ArrayList<RowTicker> myTicker, Folder fold, String fileName) {
        
        String inputFilePath = fold.getFullPath() + File.separator + fileName.substring(0, fileName.length()-4) + ".xlsx";
       
        FileInputStream file = null;
        XSSFWorkbook workbook = new XSSFWorkbook();
        try {
            file = new FileInputStream(inputFilePath);
            workbook = new XSSFWorkbook(file);
        } catch (FileNotFoundException ex) {
            System.out.println("File not found in ManageExcel - modifyExcel ");
            iFidaGui.setOutMsgStr(Message.FILE_NOTREADBLE + "\'" + fileName + "\'");
        } catch (IOException ex) {
            System.out.println("Workbook not found in ManageExcel - modifyExcel ");
            iFidaGui.setOutMsgStr(Message.FILE_NOTREADBLE + "\'" + fileName + "\'");
        }

        CreationHelper myCrHelper = workbook.getCreationHelper();
        
        XSSFSheet mySheet = workbook.getSheet("Giornaliero");
        modifySheet2Excel(workbook, myCrHelper, mySheet, myTicker);
        
        XSSFSheet mySheet1 = workbook.getSheet("Mensile");
        ArrayList<RowTicker> myMonthlyTicker = ManageCSV.getMonthlyTicker(myTicker);
        modifySheet2Excel(workbook, myCrHelper, mySheet1, myMonthlyTicker);

        XSSFSheet mySheet2 = workbook.getSheet("Trimestrale");
        ArrayList<RowTicker> myQuarterTicker = ManageCSV.getQuarterlyTicker(myMonthlyTicker);
        modifySheet2Excel(workbook, myCrHelper, mySheet2, myQuarterTicker);

        XSSFSheet mySheet3 = workbook.getSheet("Annuale");
        ArrayList<RowTicker> myAnnualTicker = ManageCSV.getAnnualTicker(myQuarterTicker);
        modifySheet2Excel(workbook, myCrHelper, mySheet3, myAnnualTicker);

        XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
        try {
            file.close();           
        } catch (IOException ex) {
            System.out.println("File not found in ManageExcel - modifyExcel - during Close ");
        }
        //
        File myFile = new File(inputFilePath);       
        //
        try (FileOutputStream outFile = new FileOutputStream(myFile)) {
            workbook.write(outFile);
            iFidaGui.setOutMsgStr("\'" + fileName + "\'" + Message.FILE_MODIFIED);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write the file");
            iFidaGui.setOutMsgStr(Message.FILE_NOTMODIFIED + "\'" + fileName + "\'");
        } catch (IOException e) {
            System.out.println("Unable to write the file I/O Ex");
            iFidaGui.setOutMsgStr(Message.FILE_NOTMODIFIED + "\'" + fileName + "\'");
        }
    
    }
    
    
    public static void modifySheet2Excel(Workbook myWb, CreationHelper myCrHelper, XSSFSheet mySheet, ArrayList<RowTicker> myTicker) {

        int lastRow = 1;
        int rowsNum = myTicker.size();
        boolean isLast;
        Calendar cal_today;
        Calendar cal_yesterday = null;
        for (RowTicker myRowTk : myTicker) {
            isLast = lastRow == rowsNum;
            
            Row myRow = mySheet.getRow(lastRow);
            
            cal_today = Calendar.getInstance();
            cal_today.setTime(myRowTk.getDateTk());
            if(lastRow != 1){
                cal_yesterday = Calendar.getInstance();
                cal_yesterday.setTime(lastRowTk.getDateTk());
                if (myRow == null || myRow.getCell(0) == null || myRow.getCell(1) == null || myRow.getCell(2) == null
                        || myRow.getCell(3) == null || myRow.getCell(4) == null) {
                    if(mySheet.getSheetName().equals("Giornaliero")){
                        while (!ManageCSV.checkConsecutiveDate(cal_today, cal_yesterday)){
                            cal_yesterday.add(Calendar.DATE, 1);
                            java.sql.Date sqlDate = new java.sql.Date(cal_yesterday.getTime().getTime());
                            lastRowTk.setDateTk(sqlDate);
                            myRow = mySheet.createRow(lastRow);
                            addRow2Excel(myWb, myCrHelper, myRow, lastRowTk, isLast);
                            lastRow += 1;
                        }
                    }
                    myRow = mySheet.createRow(lastRow);
                    addRow2Excel(myWb, myCrHelper, myRow, myRowTk, isLast);
                    lastRowTk = myRowTk;
                } else {
                    if(mySheet.getSheetName().equals("Giornaliero")){
                        while (!ManageCSV.checkConsecutiveDate(cal_today, cal_yesterday)){
                            cal_yesterday.add(Calendar.DATE, 1);
                            java.sql.Date sqlDate = new java.sql.Date(cal_yesterday.getTime().getTime());
                            lastRowTk.setDateTk(sqlDate);
                            modifyRow2Excel(myWb, myCrHelper, lastRowTk, myRow, isLast);
                            lastRow += 1;
                            try {
                                myRow = mySheet.getRow(lastRow);
                              }
                              catch (NullPointerException  exc) {
                                  break;
                              }
                        }
                    }
                    modifyRow2Excel(myWb, myCrHelper, myRowTk, myRow, isLast);
                    lastRowTk = myRowTk;
                }
            }else{
                if (myRow == null || myRow.getCell(0) == null || myRow.getCell(1) == null || myRow.getCell(2) == null || myRow.getCell(3) == null) {
                        myRow = mySheet.createRow(lastRow);
                        addRow2Excel(myWb, myCrHelper, myRow, myRowTk, isLast);
                    } else {
                        modifyRow2Excel(myWb, myCrHelper, myRowTk, myRow, isLast);
                    }
                    lastRowTk = myRowTk;
                }
            lastRow += 1;
        }
        
        mySheet.autoSizeColumn(0, false);
        
    }
    
    
     public static void modifyRow2Excel(Workbook myWb, CreationHelper myCrHelper, RowTicker myRowTk, Row myRow, boolean isLastRow) {
        Cell myCell;

        Calendar cal = Calendar.getInstance();
        cal.setTime(myRowTk.getDateTk());
        if (isLastRow & cal.get(Calendar.DAY_OF_MONTH) != cal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
        } else {
            myCell = myRow.createCell(0);
            String dayOfWeek = getDayOfWeek(cal.get(Calendar.DAY_OF_WEEK));
            int day = cal.get(Calendar.DAY_OF_MONTH);
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

            myCell = myRow.createCell(1);
            myCell.setCellValue(myRowTk.getOpenTk());

            myCell = myRow.createCell(2);
            myCell.setCellValue(myRowTk.getHighTk());

            myCell = myRow.createCell(3);
            myCell.setCellValue(myRowTk.getLowTk());

            myCell = myRow.createCell(4);
            myCell.setCellValue(myRowTk.getCloseTk());
        }
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
    
    
    public static boolean checkIfExists(String fileName, String filePath) {
    
        boolean exists = false;

        String outputFilePath = filePath + File.separator + fileName + ".xlsx";

            System.out.println(outputFilePath);
        // Create an abstract definition of configuration file to be read.
        File file = new File(outputFilePath);

        // If configuration file fileName does exist in the current returns true
        if (file.exists()) {
            exists = true;
        }

        return exists;
    }
    
    
}
