/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package i.fida.db;

import i.fida.Folder;
import i.fida.IFida;
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
    private static final String FILTER4MAIN_FOLDER = " WHERE MAIN_FOLDER LIKE ";

    private static String sDBname;
    private static String sTable;
    private static String sFieldTableCreate;
    private static String queryGet = null;
    private static String queryFill = null;
    private static String queryUpd = null;

    public static void setsQueryFill(String squeryFill) {
        Sources.queryFill = squeryFill;
    }

    public static String getsQueryFill() {
        return queryFill;
    }

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

    public static void setQueryGet(String queryGet) {
        Sources.queryGet = queryGet;
    }

    public static String getQueryGet() {
        return queryGet;
    }

    public static void setQueryUpdate(String get) {
        Sources.queryUpd = get;
    }
    
    public static String getQueryUpd() {
        return queryUpd;
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
                folderFromDB.setAutoRefresh(rs.getBoolean("UPD"));
                folderFromDB.setFileNumber(rs.getInt("FILE_NUMBER"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Sources.class.getName()).log(Level.SEVERE, null, ex);
        }
        return folderFromDB;
    }

    public static ArrayList<Folder> getAllFolderFromDB() {
        Connection conn = connectOrCreate();
        String pstmtSelect = SELECT_ALL.concat(getsTable()).concat(FILTER4MAIN_FOLDER).concat("'").concat(IFida.getMainFolder()).concat("'").concat(";");// + +  +  +;        
        ArrayList<Folder> folders = new ArrayList<>();
        try (Statement stmt = conn.createStatement();) {
            ResultSet rs = stmt.executeQuery(pstmtSelect);
            while (rs.next()) {
                Folder folderFromDB = new Folder(); //new Folder();
                folderFromDB.setFullPath(rs.getString("FULL_PATH"));
                folderFromDB.setName(rs.getString("NAME_FOLDER"));
                folderFromDB.setDateLastUpdate(rs.getDate("DATE_LAST_UPD"));
                folderFromDB.setAutoRefresh(rs.getBoolean("UPD"));
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
                folderFromDB.setAutoRefresh(rs.getBoolean("UPD"));
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
        String pstmtUpdate = INSERT + getsTable() + getsQueryFill() + ";";
        try (PreparedStatement pstmt = conn.prepareStatement(pstmtUpdate);) {
            Date a = new Date(Calendar.getInstance().getTime().getTime());
            java.sql.Date sysDate = new java.sql.Date(a.getTime());
            for (Folder fol : information) {
                pstmt.setString(1, IFida.getMainFolder());
                pstmt.setString(2, fol.getFullPath());
                pstmt.setString(3, fol.getName());
                pstmt.setDate(4, sysDate);
                pstmt.setInt(5, fol.getAutoRefreshAsInt());
                pstmt.setInt(6, fol.getFileNumber());
                pstmt.execute();
                createSuccessful = true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(Sources.class.getName()).log(Level.SEVERE, null, ex);
        }

        return createSuccessful;
    }
    
    public static boolean updateFolderInDB(Folder updFolder) {
        Connection conn = connectOrCreate();
        boolean createSuccessful = false;
        String pstmtUpdate = getQueryUpd();
        try (PreparedStatement pstmt = conn.prepareStatement(pstmtUpdate);) {
                //pstmt.setString(1, Sources.getsTable());
                pstmt.setInt(1, updFolder.getAutoRefreshAsInt());
                pstmt.setString(2, IFida.getMainFolder());
                pstmt.setString(3, updFolder.getName());
                pstmt.execute();
                createSuccessful = true;            
        } catch (SQLException ex) {
            Logger.getLogger(Sources.class.getName()).log(Level.SEVERE, null, ex);
        }
        return createSuccessful;
    }

}
