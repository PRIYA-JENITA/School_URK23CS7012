package school_management_system;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import ui.MainFrame;

public class SchoolManagementSystem {

public static void main(String[] args) {
    // Set look and feel to system default
    try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
        e.printStackTrace();
    }
    
    // Launch application
    SwingUtilities.invokeLater(() -> {
        MainFrame mainFrame = new MainFrame();
        mainFrame.setVisible(true);
    });
}}

