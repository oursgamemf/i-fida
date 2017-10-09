/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package i.fida;

import i.fida.IFida;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author virginia
 */
public class Folder {

    private String folderName;
    private String folderFullPath;
    private boolean autoRefresh;
    private java.sql.Date dateLastUpdate;
    private int fileNumber;

    public Folder(String folderPath, boolean autoRef, java.sql.Date lastUpdate) {
        String mainFolderPath = IFida.getMainFolder();
        folderName = folderPath.substring(mainFolderPath.length() + 1);
        folderFullPath = folderPath;
        autoRefresh = autoRef;
        fileNumber = IFida.getCSVinDirectory(folderPath).size();

        // To delete when get date from excel-csv is implemented
        Date a = new Date(Calendar.getInstance().getTime().getTime());
        java.sql.Date sysDate = new java.sql.Date(a.getTime());
        lastUpdate = sysDate;
        // To delete when get date from excel-csv is implemented

        dateLastUpdate = lastUpdate;

    }

    public Folder() {
        java.sql.Date lastUpdate;
        String mainFolderPath = IFida.getMainFolder();
        folderName = "";
        folderFullPath = mainFolderPath;
        autoRefresh = true;
        fileNumber = 0;

        // To delete when get date from excel-csv is implemented
        Date a = new Date(Calendar.getInstance().getTime().getTime());
        java.sql.Date sysDate = new java.sql.Date(a.getTime());
        lastUpdate = sysDate;
        // To delete when get date from excel-csv is implemented

        dateLastUpdate = lastUpdate;

    }

    public void setName(String folderName) {
        this.folderName = folderName;
    }

    public void setFullPath(String folderFullPath) {;
        this.folderFullPath = folderFullPath;
    }

    public void setFileNumber(int fileNumber) {
        this.fileNumber = fileNumber;
    }

    public void setAutoRefresh(boolean autoRefresh) {
        this.autoRefresh = autoRefresh;
    }
    
    public void setAutoRefreshAsString(String autoRefresh) {
        if (autoRefresh.toLowerCase().trim().equals("true")) {
            this.autoRefresh = true;
        } else
            this.autoRefresh = false;
            
    }
    
    public void setAutoRefreshAsInt(int autoRefresh) {
        if (autoRefresh == 1) {
            this.autoRefresh = true;
        } else
            this.autoRefresh = false;
            
    }

    public void setDateLastUpdate(java.sql.Date dateLastUpdate) {
        this.dateLastUpdate = dateLastUpdate;
    }

    public String getName() {
        return folderName;
    }

    public String getFullPath() {
        return folderFullPath;
    }

    public int getFileNumber() {
        return fileNumber;
    }

    public boolean getAutoRefresh() {
        return autoRefresh;
    }

    public int getAutoRefreshAsInt() {
        int refreshInt = 0;
        if (autoRefresh) {
            refreshInt = 1;
        }
        return refreshInt;
    }

    public java.sql.Date getDateLastUpdate() {
        return dateLastUpdate;
    }

}
