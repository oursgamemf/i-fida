/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package i.fida.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author emanuele
 */
public class Sources {
    
    private static final String sJdbc = "org.sqlite.JDBC";
    private static final String sDbUrl = "jdbc:sqlite:";
    private static final String DB_EXTENSION = ".db";
    
    private static final String sMakeTable = "CREATE TABLE ";
        
    private static String sDBname;
    private static String sTable;
    private static String sFieldTableCreate;
    private static String query = null;
    
    public void setsDBname(String sDBname) {
        Sources.sDBname = sDBname;
    }
    
    public String getsDBname() {
        return sDBname;
    }
    
    public void setsTable(String sTable) {
        Sources.sTable = sTable;
    }
    
    public String getsTable() {
        return sTable;
    }
    
    public void setsFieldTableCreate(String sFieldTableCreate) {
        Sources.sFieldTableCreate = sFieldTableCreate;
    }
    
    public String getsFieldTableCreate() {
        return sFieldTableCreate;
    }
    
    public void setQuery(String query) {
        Sources.query = query;
    }
    
    public boolean driverConn() {
        // register the driver 
        try {
            Class.forName(sJdbc);
            return true;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Sources.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public Connection connectOrCreate() {
        if (sDBname == null) {
            return null;
        }
        if (!driverConn()) {
            System.out.println("No DB driver found");
        }
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(sDbUrl + getsDBname() + DB_EXTENSION);

            return conn;
        } catch (SQLException ex) {
            System.out.println("No DB driver found");
            Logger.getLogger(Sources.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }
    
    public boolean createTable() {
        if ((sTable == null) || (sFieldTableCreate == null)) {
            System.out.println("nullVAlue");
            return false;
        }
        Connection conn = connectOrCreate();
        String createTableDML = sMakeTable + getsTable() + " (" + getsFieldTableCreate() + ");";
        try (PreparedStatement pstmt = conn.prepareStatement(createTableDML);) {
            pstmt.executeUpdate();

            return true;
        } catch (SQLException ex) {
            //Logger.getLogger(DBtkEvo.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Tabella importata");
        }
        return false;
    }
}
