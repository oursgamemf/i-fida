/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package i.fida.gui;

import i.fida.Folder;
import i.fida.IFida;
import i.fida.ManageCSV;
import i.fida.Message;
import i.fida.db.Sources;
import java.awt.Color;
import java.awt.Cursor;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author emanuele
 */
public class iFidaGui extends javax.swing.JFrame {

    public static JTable myTable;

    /**
     * Creates new form iFidaGui
     */
    public iFidaGui() {
        initComponents();        
        initLanguage();
        IFida.initConfig();
        initGUI();

        // set Table Model
        myTable = setTableAtStart();
        myTable.getDefaultEditor(String.class).addCellEditorListener(ChangeNotification);

        // fill Table Model
        fillTableFromMainFolder(myTable);
    }

    private JTable setTableAtStart() {
        String[] columnNames = {Message.TABLE_HEADER_NAME, Message.TABLE_HEADER_UPDATEATSTART,
            Message.TABLE_HEADER_NUMBER, Message.TABLE_HEADER_LASTUPDATE};
        Object[][] data = {};
//            {null, null, null, null}
//        };
        JTable table = new javax.swing.JTable();
        table.setModel(new javax.swing.table.DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return !(column == 0 || column == 2);
            }
        });
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setVisible(true);
        table.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.getViewport().add(table);
        table.setFillsViewportHeight(true);

        table.putClientProperty("terminateEditOnFocusLost", true);
        return table;
    }

    private void fillTableFromMainFolder(JTable tableUI) {
        ArrayList<Folder> dirs = IFida.getFolderListFromMainFolder();
        IFida.setFolderListIntoDB(dirs);
        ArrayList<Folder> dirsFromDB = IFida.getFoldersListFromDB();
        if (!dirsFromDB.isEmpty()) {
            DefaultTableModel modelDef = (DefaultTableModel) tableUI.getModel();
            int rowCount = modelDef.getRowCount();
            //Remove rows one by one from the end of the table
            for (int i = rowCount - 1; i >= 0; i--) {
                modelDef.removeRow(i);
            }

            int ii = 0;
            for (Folder d : dirsFromDB) {
                Object[][] data = {
                    {null, null, null, null}
                };
                modelDef.addRow(data);
                //!boolean update = Sources.getFolderUpdate(d);

                tableUI.getModel().setValueAt(d.getName(), ii, 0);
                //!tableUI.getModel().setValueAt(update, ii, 1);
                tableUI.getModel().setValueAt(d.getAutoRefresh(), ii, 1);
                tableUI.getModel().setValueAt(d.getFileNumber(), ii, 2);
                tableUI.getModel().setValueAt(d.getDateLastUpdate(), ii, 3);
                ii += 1;

            }
        }

        //column = tableUI.getColumnModel().getColumn(i);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jSplitPane4 = new javax.swing.JSplitPane();
        jSplitPane6 = new javax.swing.JSplitPane();
        jSplitPane5 = new javax.swing.JSplitPane();
        jSplitPane2 = new javax.swing.JSplitPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        outmsg = new javax.swing.JTextArea();
        jScrollPane1 = new javax.swing.JScrollPane();
        jButtonScanFolder = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jSplitPane7 = new javax.swing.JSplitPane();
        jSplitPane8 = new javax.swing.JSplitPane();
        jComboBox_language = new javax.swing.JComboBox<>();
        jSplitPane3 = new javax.swing.JSplitPane();
        jSplitPane9 = new javax.swing.JSplitPane();
        jLabel2 = new javax.swing.JLabel();
        jComboBox_sep = new javax.swing.JComboBox<>();
        jButton4 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jSplitPane1.setLeftComponent(jPanel2);

        jSplitPane4.setBorder(new javax.swing.border.MatteBorder(null));
        jSplitPane4.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jSplitPane6.setDividerLocation(0);

        jSplitPane5.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jSplitPane2.setDividerLocation(200);
        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        outmsg.setColumns(20);
        outmsg.setRows(2);
        outmsg.setTabSize(4);
        outmsg.setMaximumSize(new java.awt.Dimension(80, 180));
        outmsg.setMinimumSize(new java.awt.Dimension(24, 38));
        jScrollPane2.setViewportView(outmsg);

        jSplitPane2.setBottomComponent(jScrollPane2);

        jScrollPane1.setPreferredSize(new java.awt.Dimension(22, 22));
        jSplitPane2.setLeftComponent(jScrollPane1);

        jSplitPane5.setBottomComponent(jSplitPane2);

        jButtonScanFolder.setText("File processing");
        jButtonScanFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScanFolderActionPerformed(evt);
            }
        });
        jSplitPane5.setLeftComponent(jButtonScanFolder);

        jSplitPane6.setRightComponent(jSplitPane5);
        jSplitPane6.setLeftComponent(jButton3);

        jSplitPane4.setRightComponent(jSplitPane6);

        jSplitPane7.setDividerLocation(250);

        jSplitPane8.setDividerLocation(1000);
        jSplitPane8.setMaximumSize(new java.awt.Dimension(340, 34));
        jSplitPane8.setMinimumSize(new java.awt.Dimension(340, 22));

        jComboBox_language.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "IT", "EN" }));
        jComboBox_language.setMaximumSize(new java.awt.Dimension(25, 28));
        jComboBox_language.setMinimumSize(new java.awt.Dimension(25, 20));
        jComboBox_language.setPreferredSize(new java.awt.Dimension(25, 27));
        jComboBox_language.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox_languageItemStateChanged(evt);
            }
        });
        jComboBox_language.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_languageActionPerformed(evt);
            }
        });
        jSplitPane8.setRightComponent(jComboBox_language);

        jSplitPane3.setDividerSize(5);
        jSplitPane3.setMinimumSize(new java.awt.Dimension(100, 50));
        jSplitPane3.setPreferredSize(new java.awt.Dimension(700, 50));

        jSplitPane9.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane9.setMinimumSize(new java.awt.Dimension(416, 50));

        jLabel2.setText("_____________SEP_____________");
        jSplitPane9.setTopComponent(jLabel2);

        jComboBox_sep.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { ";", ",", "|" }));
        jComboBox_sep.setAutoscrolls(true);
        jComboBox_sep.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox_sepItemStateChanged(evt);
            }
        });
        jSplitPane9.setRightComponent(jComboBox_sep);

        jSplitPane3.setLeftComponent(jSplitPane9);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/i/fida/gui/language-icon.png"))); // NOI18N
        jButton4.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/i/fida/gui/language-icon.png"))); // NOI18N
        jButton4.setEnabled(false);
        jSplitPane3.setRightComponent(jButton4);

        jSplitPane8.setLeftComponent(jSplitPane3);

        jSplitPane7.setRightComponent(jSplitPane8);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/i/fida/gui/16932184.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jSplitPane7.setLeftComponent(jButton2);

        jSplitPane4.setLeftComponent(jSplitPane7);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 678, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
        );

        jSplitPane1.setRightComponent(jPanel3);

        jButton1.setText("jButton1");
        jSplitPane1.setLeftComponent(jButton1);

        jTextField1.setEditable(false);
        jTextField1.setText("folder path");
        jSplitPane1.setTopComponent(jTextField1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox_languageItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox_languageItemStateChanged
        // TODO add your handling code here:
        Message.setLanguage(jComboBox_language.getSelectedItem().toString());
    }//GEN-LAST:event_jComboBox_languageItemStateChanged

    private void jButtonScanFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScanFolderActionPerformed
        
        IFida.elaborateCSVinMainPath();
        jButtonScanFolder.setText(Message.BU_ELAB_END);
    }//GEN-LAST:event_jButtonScanFolderActionPerformed

    private void jComboBox_languageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_languageActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox_languageActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        iFidaGui.setOutMsgStr(Message.MAIN_FOLDER_SELECTED);
        // TODO add your handling code here:
        //Create a file chooser
        final JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //In response to a button click:
        int result = fc.showOpenDialog(jButton1);

        String selectedPath = "";
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedPath = fc.getSelectedFile().getAbsolutePath();
            iFidaGui.setOutMsgStr(Message.MAIN_FOLDER_SELECTED);
            iFidaGui.setOutMsgStr(selectedPath);
            /* try {
                write2configFile(selectedPath);
                outputExcelFile = selectedPath;

            } catch (IOException ex) {
                Logger.getLogger(ViewTicker.class.getName()).log(Level.SEVERE, null, ex);
            }
             */
            jButton2.setEnabled(true);
        } else if (result == JFileChooser.CANCEL_OPTION) {
            selectedPath = fc.getCurrentDirectory().getAbsolutePath();
            System.out.println("Cancel was selected: " + "none");
        }
        IFida.setMainFolder(selectedPath);
        jTextField1.setText(IFida.getMainFolder());
        fillTableFromMainFolder(myTable);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jComboBox_sepItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox_sepItemStateChanged
        ManageCSV.setSep(jComboBox_sep.getSelectedItem().toString().charAt(0));
    }//GEN-LAST:event_jComboBox_sepItemStateChanged

    private void initLanguage() {
        Message.setLanguage(jComboBox_language.getSelectedItem().toString());
        startOutputMsgStream();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(iFidaGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(iFidaGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(iFidaGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(iFidaGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new iFidaGui().setVisible(true);
                
            }
        });
    }

    public static void setOutMsgStr(String msgtoOut) {
        outMsgStr = msgtoOut;
    }

    private void initGUI() {
        jTextField1.setText(IFida.getMainFolder());
    }

    private void startOutputMsgStream() {

        Thread thread = new Thread() {
            public void run() {
                while (true) {

                    outmsg.setForeground(Color.BLACK);
                    outmsg.setText(outMsgStr);

                }
            }
        };

        thread.start();
        iFidaGui.setOutMsgStr(Message.END_SCAN);
    }

    CellEditorListener ChangeNotification = new CellEditorListener() {
        public void editingCanceled(ChangeEvent e) {
            int editedRow = myTable.getSelectedRow();
            int editedCol = myTable.getSelectedColumn();
            String updateValue = myTable.getModel().getValueAt(editedRow, editedCol).toString();
            System.out.println(updateValue);
            System.out.println("!!!");
            if (!updateValue.equals("false") & !updateValue.equals("true")) {
                iFidaGui.setOutMsgStr(Message.WRONG_BOOLEAN_STRING);
                myTable.getModel().setValueAt("false", editedRow, editedCol);
            }
            // !!! aggiorna DB

        }

        public void editingStopped(ChangeEvent e) {
            int editedRow = myTable.getSelectedRow();
            int editedCol = myTable.getSelectedColumn();
            String updateValue = myTable.getModel().getValueAt(editedRow, editedCol).toString();
            System.out.println(updateValue);
            System.out.println("---");
            if (!updateValue.equals("false") & !updateValue.equals("true")) {
                iFidaGui.setOutMsgStr(Message.WRONG_BOOLEAN_STRING);
                myTable.getModel().setValueAt("false", editedRow, editedCol);
            }
            // !!! aggiorna DB
            Folder updatedFolder = new Folder();
            updatedFolder.setName(myTable.getModel().getValueAt(editedRow, 0).toString());
            updatedFolder.setAutoRefresh(Boolean.getBoolean(myTable.getModel().getValueAt(editedRow, editedCol).toString()));
            IFida.setFolderIfUpdate(updatedFolder);
        }
    };

    
    private static String outMsgStr = "";
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButtonScanFolder;
    private javax.swing.JComboBox<String> jComboBox_language;
    private javax.swing.JComboBox<String> jComboBox_sep;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JSplitPane jSplitPane4;
    private javax.swing.JSplitPane jSplitPane5;
    private javax.swing.JSplitPane jSplitPane6;
    private javax.swing.JSplitPane jSplitPane7;
    private javax.swing.JSplitPane jSplitPane8;
    private javax.swing.JSplitPane jSplitPane9;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextArea outmsg;
    // End of variables declaration//GEN-END:variables

}
