/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package i.fida;

import i.fida.gui.iFidaGui;
import java.sql.Date;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author virginia
 */
public class RowTicker {

    private java.sql.Date dateTk;
    private Double openTk;
    private Double highTk;
    private Double lowTk;
    private Double closeTk;
    private static SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
    private static NumberFormat formatDouble = NumberFormat.getInstance(Locale.FRANCE);

    public RowTicker(String date, String open, String high, String low, String close) {

        java.sql.Date dateVal = string2SqlDate(date);
        this.dateTk = dateVal;

        Number openTemp = null;
        Number highTemp = null;
        Number lowTemp = null;
        Number closeTemp = null;
        try {
            openTemp = formatDouble.parse(open);
            highTemp = formatDouble.parse(high);
            lowTemp = formatDouble.parse(low);
            closeTemp = formatDouble.parse(close);
        } catch (ParseException ex) {
            Logger.getLogger(ManageExcell.class.getName()).log(Level.SEVERE, null, ex);
        }

        Double openVal = openTemp.doubleValue();
        this.openTk = openVal;

        Double highVal = highTemp.doubleValue();
        this.highTk = highVal;

        Double lowVal = lowTemp.doubleValue();
        this.lowTk = lowVal;

        Double closeVal = closeTemp.doubleValue();
        this.closeTk = closeVal;
    }

    public static java.sql.Date string2SqlDate(String dateStr) {
        java.util.Date dateTemp = null;
        try {
            dateTemp = formatDate.parse(dateStr);

        } catch (ParseException ex) {
            iFidaGui.setOutMsgStr(Message.CANT_PARSE_CSV);
        }
        java.sql.Date dateVal = new java.sql.Date(0);
        dateVal = new java.sql.Date(dateTemp.getTime());
        return dateVal;
    }
    
    
    RowTicker() {
    
    }
    
    public Date getDateTk() {
        return dateTk;
    }

    public void setDateTk(java.sql.Date dateTk) {
        this.dateTk = dateTk;
    }

    public Double getOpenTk() {
        return openTk;
    }

    public void setOpenTk(Double openTk) {
        this.openTk = openTk;
    }

    public Double getHighTk() {
        return highTk;
    }

    public void setHighTk(Double highTk) {
        this.highTk = highTk;
    }

    public Double getLowTk() {
        return lowTk;
    }

    public void setLowTk(Double lowTk) {
        this.lowTk = lowTk;
    }

    public Double getCloseTk() {
        return closeTk;
    }

    public void setCloseTk(Double closeTk) {
        this.closeTk = closeTk;
    }

}
