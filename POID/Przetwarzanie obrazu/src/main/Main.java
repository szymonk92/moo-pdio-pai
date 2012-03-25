package main;

import gui.MainWindow;
import java.util.ArrayList;
import sys.IFilter;
import sys.IView;
import sys.ModulLoader;

public class Main {

    private static ModulLoader modulLoader;
    /**
     * @param args
     */
    public static void  main(String[] args) {
        modulLoader = new ModulLoader<IFilter>();
        final ArrayList<IFilter> filters  = modulLoader.LoadByInterface("filters", "sys.IFilter");
        modulLoader = new ModulLoader<IView>();
        final ArrayList<IView> views = modulLoader.LoadByInterface("views", "sys.IView");
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new MainWindow(filters,views).setVisible(true);
            }
        });

    }
}
