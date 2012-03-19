package sys;

import javax.swing.JOptionPane;

public class Messages {

    public static void error(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Błąd", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void fatalError(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Błąd", JOptionPane.INFORMATION_MESSAGE);
        System.out.println("Program sie zakonczyl.");
        System.exit(-1);
    }

    public static void info(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Info", JOptionPane.INFORMATION_MESSAGE);
    }
}
