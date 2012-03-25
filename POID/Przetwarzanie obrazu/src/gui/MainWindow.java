/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import sys.*;

/**
 *
 * @author Lukasz
 */
public class MainWindow extends javax.swing.JFrame {

    private List<IFilter> filters;
    private List<IView> views;
    DiffWindow diffWindow;

    /**
     * Creates new form MainWindow
     */
    public MainWindow() {
        initComponents();
    }

    public MainWindow(ArrayList<IFilter> filtry, ArrayList<IView> views) {
        this();
        this.filters = filtry;
        this.views = views;
        diffWindow= new DiffWindow();
        Collections.sort(filters, new FilterSortyByName());
        filterSubMenuLoad(this.filtryMenu);
        viewSubMenuLoad(this.widokMenu);
        this.closableTabbedPane.addContainerListener(new ContainerListener() {

            @Override
            public void componentAdded(ContainerEvent e) {
                doTest();
            }

            @Override
            public void componentRemoved(ContainerEvent e) {
                doTest();
            }
        });
        doTest();
    }

    private void doTest() {
        if (closableTabbedPane.getTabCount() == 0) {
            saveFileMenuItem.setEnabled(false);
            widokMenu.setEnabled(false);
            filtryMenu.setEnabled(false);
        } else {
            saveFileMenuItem.setEnabled(true);
            widokMenu.setEnabled(true);
            filtryMenu.setEnabled(true);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        openFileChooser = new javax.swing.JFileChooser();
        saveFileChooser = new javax.swing.JFileChooser();
        closableTabbedPane = new gui.ClosableTabbedPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        plikMenu = new javax.swing.JMenu();
        openFileMenuItem = new javax.swing.JMenuItem();
        saveFileMenuItem = new javax.swing.JMenuItem();
        closeMenuItem = new javax.swing.JMenuItem();
        widokMenu = new javax.swing.JMenu();
        filtryMenu = new javax.swing.JMenu();
        jMenu1 = new javax.swing.JMenu();

        openFileChooser.setAcceptAllFileFilterUsed(false);
        openFileChooser.setAccessory(new components.ImagePreview(this.openFileChooser));
        openFileChooser.setDialogTitle("Open file");
        openFileChooser.setFileFilter(new components.ImageFilter());
        openFileChooser.setFileView(new components.ImageFileView());

        saveFileChooser.setAccessory(new components.ImagePreview(this.openFileChooser));
        saveFileChooser.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);
        saveFileChooser.setDialogTitle("Save file");
        saveFileChooser.setFileFilter(new components.ImageFilter());
        saveFileChooser.setFileView(new components.ImageFileView());

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Przetwarzanie obrazu");
        setLocationByPlatform(true);
        setName("mainWindow");

        closableTabbedPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);

        plikMenu.setText("Plik");

        openFileMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/open_image.png"))); // NOI18N
        openFileMenuItem.setText("Otwórz obraz");
        openFileMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openFileMenuItemActionPerformed(evt);
            }
        });
        plikMenu.add(openFileMenuItem);

        saveFileMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save.png"))); // NOI18N
        saveFileMenuItem.setText("Zapisz obraz");
        saveFileMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveFileMenuItemActionPerformed(evt);
            }
        });
        plikMenu.add(saveFileMenuItem);

        closeMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/window_app_list_close.png"))); // NOI18N
        closeMenuItem.setText("Zamknij");
        closeMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeMenuItemActionPerformed(evt);
            }
        });
        plikMenu.add(closeMenuItem);

        jMenuBar1.add(plikMenu);

        widokMenu.setText("Widok");
        jMenuBar1.add(widokMenu);

        filtryMenu.setText("Filtry");
        filtryMenu.setActionCommand("Filtr");
        jMenuBar1.add(filtryMenu);

        jMenu1.setText("Diff");
        jMenu1.addMenuListener(new javax.swing.event.MenuListener() {
            public void menuCanceled(javax.swing.event.MenuEvent evt) {
            }
            public void menuDeselected(javax.swing.event.MenuEvent evt) {
            }
            public void menuSelected(javax.swing.event.MenuEvent evt) {
                jMenu1MenuSelected(evt);
            }
        });
        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(closableTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(closableTabbedPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 579, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void openFileMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openFileMenuItemActionPerformed

        int returnVal = this.openFileChooser.showDialog(this, "Open file");

        //Process the results.
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = this.openFileChooser.getSelectedFile();
            TabData tabdata = null;
            try {
                tabdata = new TabData(ImageIO.read(file));
            } catch (IOException ex) {
                Messages.error("Błąd odczytu pliku. " + ex.getMessage());
            }
            if (tabdata != null) {
                MainPanel mp = new MainPanel(tabdata);
                this.closableTabbedPane.addTab(file.getName(), mp);
                this.closableTabbedPane.setSelectedIndex((this.closableTabbedPane.getTabCount() - 1));
            }
        }
        this.openFileChooser.setSelectedFile(null);

    }//GEN-LAST:event_openFileMenuItemActionPerformed

    private void closeMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeMenuItemActionPerformed
        dispose();
        System.exit(0);
    }//GEN-LAST:event_closeMenuItemActionPerformed

    private void saveFileMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveFileMenuItemActionPerformed
        int returnVal = this.saveFileChooser.showDialog(this, "Save file");

        //Process the results.
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = this.saveFileChooser.getSelectedFile();
            int selectedTabIndex = closableTabbedPane.getSelectedIndex();
            if (selectedTabIndex != -1) {
                MainPanel panel = (MainPanel) closableTabbedPane.getComponentAt(selectedTabIndex);
                try {
                    ImageIO.write(panel.navigableImagePanel.image, "jpg", file);
                } catch (IOException ex) {
                    Messages.error("Błąd zapisu pliku. " + ex.getMessage());
                }
            }
        }
        this.saveFileChooser.setSelectedFile(null);        // TODO add your handling code here:
    }//GEN-LAST:event_saveFileMenuItemActionPerformed

    private void jMenu1MenuSelected(javax.swing.event.MenuEvent evt) {//GEN-FIRST:event_jMenu1MenuSelected
        // TODO add your handling code here:
            diffWindow.setVisible(true);
    }//GEN-LAST:event_jMenu1MenuSelected

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gui.ClosableTabbedPane closableTabbedPane;
    private javax.swing.JMenuItem closeMenuItem;
    private javax.swing.JMenu filtryMenu;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JFileChooser openFileChooser;
    private javax.swing.JMenuItem openFileMenuItem;
    private javax.swing.JMenu plikMenu;
    private javax.swing.JFileChooser saveFileChooser;
    private javax.swing.JMenuItem saveFileMenuItem;
    private javax.swing.JMenu widokMenu;
    // End of variables declaration//GEN-END:variables

    private void filterSubMenuLoad(JMenu filtryMenu) {
        if (filters != null && !filters.isEmpty()) {
            JMenuItem menuItem;
            for (IFilter filtr : filters) {
                menuItem = new JMenuItem(filtr.getName());
                menuItem.setToolTipText(filtr.getDescription());
                menuItem.addActionListener(new FilterMenuItemListener(this.closableTabbedPane, filtr));
                if (filtr.getIcon() == null) {
                    menuItem.setIcon(IconHelper.getDefaultIcon());
                } else {
                    menuItem.setIcon(new ImageIcon(getClass().getResource(filtr.getIcon())));
                }

                filtryMenu.add(menuItem);
            }
        } else {
            filtryMenu.add("Brak...");
        }
    }

    private void viewSubMenuLoad(JMenu filtryMenu) {
        if (views != null && !views.isEmpty()) {
            JMenuItem menuItem;
            for (IView view : views) {
                menuItem = new JMenuItem(view.getName());
                menuItem.addActionListener(new ViewMenuItemListener(this.closableTabbedPane, view));
                if (view.getIcon() == null) {
                    menuItem.setIcon(IconHelper.getDefaultIcon());
                } else {
                    menuItem.setIcon(new ImageIcon(getClass().getResource(view.getIcon())));
                }

                filtryMenu.add(menuItem);
            }
        } else {
            filtryMenu.add("Brak...");
        }
    }
}
