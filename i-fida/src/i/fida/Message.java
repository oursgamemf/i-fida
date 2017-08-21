/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package i.fida;

import java.awt.Color;
import javax.swing.JTextField;

/**
 *
 * @author emanuele
 */
public class Message {

    public static String START_SCAN;

    public static void setLanguage(String leng) {
        switch (leng.toUpperCase().trim()) {
            case "IT":
                START_SCAN = "Inizio scansione cartelle";
                break;
            case "EN":
                START_SCAN = "Start folder scan";
                break;
        }

    }


}
