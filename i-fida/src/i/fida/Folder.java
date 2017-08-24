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
    
    
    public Folder(String folderPath, boolean autoRef, java.sql.Date lastUpdate){
        String mainFolderPath = IFida.getMainFolder();
        folderName = folderPath.substring(mainFolderPath.length());
        folderFullPath = folderPath;
        autoRefresh = autoRef;
        
        // To delete when get date from excel-csv is implemented
        Date a = new Date(Calendar.getInstance().getTime().getTime());
        java.sql.Date sysDate = new java.sql.Date(a.getTime());
        lastUpdate = sysDate;
        // To delete when get date from excel-csv is implemented
        
        dateLastUpdate = lastUpdate;
        
    }
    
    
    public String getFolderName() {
        return folderName;
    }

    public String getFolderFullPath() {
        return folderFullPath;
    }

    public boolean isAutoRefresh() {
        return autoRefresh;
    }

    public java.sql.Date getDateLastUpdate() {
        return dateLastUpdate;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public void setFolderFullPath(String folderFullPath) {
        this.folderFullPath = folderFullPath;
    }

    public void setAutoRefresh(boolean autoRefresh) {
        this.autoRefresh = autoRefresh;
    }

    public void setDateLastUpdate(java.sql.Date dateLastUpdate) {
        this.dateLastUpdate = dateLastUpdate;
    }
    
    
}
