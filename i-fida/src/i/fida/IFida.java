/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package i.fida;

import i.fida.gui.iFidaGui;
import java.io.File;
import java.util.ArrayList;
import javax.swing.SwingUtilities;

/**
 *
 * @author emanuele
 */
public class IFida {
    
    public static void scanFolder() {
        iFidaGui.setOutMsgStr(Message.START_SCAN);
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

    public static ArrayList<String> getCSVinDirectory (String directoryName){
        File dir = new File(directoryName);
        ArrayList<String> CSV_files = new ArrayList<>();
        
        for(File f :dir.listFiles())
        {
            if (f.getName().endsWith(".csv"))
            {
                CSV_files.add(f.getName());
            }
        }
        return CSV_files;
    }
    
}
