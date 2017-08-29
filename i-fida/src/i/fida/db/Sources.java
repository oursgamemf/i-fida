/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package i.fida.db;

import i.fida.Folder;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
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

    private static final String MK_TABLE = "CREATE TABLE ";
    private static final String INSERT = "INSERT INTO ";
    private static final String SELECT_ALL = "SELECT * FROM ";

    private static String sDBname;
    private static String sTable;
    private static String sFieldTableCreate;
    private static String query = null;

    public static void setsDBname(String sDBname) {
        Sources.sDBname = sDBname;
    }

    public static String getsDBname() {
        return sDBname;
    }

    public static void setsTable(String sTable) {
        Sources.sTable = sTable;
    }

    public static String getsTable() {
        return sTable;
    }

    public static void setsFieldTableCreate(String sFieldTableCreate) {
        Sources.sFieldTableCreate = sFieldTableCreate;
    }

    public static String getsFieldTableCreate() {
        return sFieldTableCreate;
    }

    public static void setQuery(String query) {
        Sources.query = query;
    }

    public static String getQuery() {
        return query;
    }

    public static boolean driverConn() {
        // register the driver 
        try {
            Class.forName(sJdbc);
            return true;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Sources.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public static Connection connectOrCreate() {
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

    public static boolean createTable() {
        if ((sTable == null) || (sFieldTableCreate == null)) {
            System.out.println("nullVAlue");
            return false;
        }
        Connection conn = connectOrCreate();
        String createTableDML = MK_TABLE + getsTable() + " (" + getsFieldTableCreate() + ");";
        try (PreparedStatement pstmt = conn.prepareStatement(createTableDML);) {
            pstmt.executeUpdate();

            return true;
        } catch (SQLException ex) {
            //Logger.getLogger(DBtkEvo.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Tabella importata");
        }
        return false;
    }

    public static Folder getFolderFromName(String folderName) {
        Connection conn = connectOrCreate();
        String pstmtSelect = SELECT_ALL + getsTable() + " WHERE NAME_FOLDER LIKE '" + folderName + "';";
        Folder folderFromDB = null; //new Folder();
        try (Statement stmt = conn.createStatement();) {
            ResultSet rs = stmt.executeQuery(pstmtSelect);
            while (rs.next()) {
                folderFromDB.setFullPath(rs.getString("FULL_PATH"));
                folderFromDB.setName(rs.getString("NAME_FOLDER"));
                folderFromDB.setDateLastUpdate(rs.getDate("DATE_LAST_UPD"));
                folderFromDB.setAutoRefresh(rs.getBoolean("UPDATE"));
                folderFromDB.setFileNumber(rs.getInt("FILE_NUMBER"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Sources.class.getName()).log(Level.SEVERE, null, ex);
        }
        return folderFromDB;
    }
    
    public static ArrayList<Folder> getAllFolderFromName() {
        Connection conn = connectOrCreate();
        String pstmtSelect = SELECT_ALL + getsTable()+ ";";
        Folder folderFromDB = null; //new Folder();
        ArrayList<Folder> folders = null;
        try (Statement stmt = conn.createStatement();) {
            ResultSet rs = stmt.executeQuery(pstmtSelect);
            while (rs.next()) {
                folderFromDB.setFullPath(rs.getString("FULL_PATH"));
                folderFromDB.setName(rs.getString("NAME_FOLDER"));
                folderFromDB.setDateLastUpdate(rs.getDate("DATE_LAST_UPD"));
                folderFromDB.setAutoRefresh(rs.getBoolean("UPDATE"));
                folderFromDB.setFileNumber(rs.getInt("FILE_NUMBER"));
                folders.add(folderFromDB);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Sources.class.getName()).log(Level.SEVERE, null, ex);
        }
        return folders;
    }
    
    public static Folder getFolderFromFullPath(String folderFullPath) {
        Connection conn = connectOrCreate();
        String pstmtSelect = SELECT_ALL + getsTable() + " WHERE FULL_PATH LIKE '" + folderFullPath + "';";
        Folder folderFromDB = null; //new Folder(folderFullPath, true, );
        try (Statement stmt = conn.createStatement();) {
            ResultSet rs = stmt.executeQuery(pstmtSelect);
            while (rs.next()) {
                folderFromDB.setFullPath(rs.getString("FULL_PATH"));
                folderFromDB.setName(rs.getString("NAME_FOLDER"));
                folderFromDB.setDateLastUpdate(rs.getDate("DATE_LAST_UPD"));
                folderFromDB.setAutoRefresh(rs.getBoolean("UPDATE"));
                folderFromDB.setFileNumber(rs.getInt("FILE_NUMBER"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Sources.class.getName()).log(Level.SEVERE, null, ex);
        }
        return folderFromDB;
    }

    public static boolean getFolderUpdate(String folderName) {
        Folder selFolder = getFolderFromName(folderName);
        return selFolder.getAutoRefresh();
    }

    public static boolean insertFolderInDB(ArrayList<Folder> information) {
        Connection conn = connectOrCreate();
        boolean createSuccessful = false;
        String pstmtUpdate = INSERT + getsTable() + getQuery() + ";";
        try (PreparedStatement pstmt = conn.prepareStatement(pstmtUpdate);) {
            Date a = new Date(Calendar.getInstance().getTime().getTime());
            java.sql.Date sysDate = new java.sql.Date(a.getTime());
            for (Folder fol : information) {
                pstmt.setString(1, fol.getFullPath());
                pstmt.setString(2, fol.getName());
                pstmt.setDate(3, sysDate);
                pstmt.setInt(4, fol.getAutoRefreshAsInt());
                pstmt.setInt(5, fol.getFileNumber());
                pstmt.execute();
                createSuccessful= true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(Sources.class.getName()).log(Level.SEVERE, null, ex);
        }

        return createSuccessful;
    }
}
