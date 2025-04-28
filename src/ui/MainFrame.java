package ui;

import java.awt.*;
import javax.swing.*;
import models.User;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private LoginPanel loginPanel;
    private DashboardPanel dashboardPanel;
    private User loggedInUser;
    private JButton studentsButton, teachersButton, classesButton, gradesButton;
    
    public MainFrame() {
        setTitle("School Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        // Set application icon
        studentsButton = new JButton("Students");
        teachersButton = new JButton("Teachers");
        classesButton = new JButton("Classes");
        gradesButton = new JButton("Grades");
        
        // Create main panel with card layout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // Create login panel
        loginPanel = new LoginPanel(this);
        mainPanel.add(loginPanel, "login");
        
        // Add main panel to frame
        add(mainPanel);
        
        // Show login panel by default
        cardLayout.show(mainPanel, "login");
    }
    
    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
    }
    
    public User getLoggedInUser() {
        return loggedInUser;
    }
    
    public void showDashboard() {
        // Create dashboard panel
        dashboardPanel = new DashboardPanel(this, loggedInUser);
        mainPanel.add(dashboardPanel, "dashboard");
        
        // Show dashboard
        cardLayout.show(mainPanel, "dashboard");
    }
    
    public void logout() {
        // Remove dashboard panel
        if (dashboardPanel != null) {
            mainPanel.remove(dashboardPanel);
            dashboardPanel = null;
        }
        
        // Reset logged in user
        loggedInUser = null;
        
        // Show login panel
        cardLayout.show(mainPanel, "login");
    }
    
    // No need to close database connection when application closes
    // since we're using try-with-resources in all our DAO classes
    @Override
    public void dispose() {
        // Just call the parent's dispose method
        super.dispose();
    }
}
