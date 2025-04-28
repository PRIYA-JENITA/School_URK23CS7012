package ui;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import models.User;

public class DashboardPanel extends JPanel {
    private MainFrame parent;
    private User loggedInUser;
    private JPanel contentPanel;
    
    public DashboardPanel(MainFrame parent, User loggedInUser) {
        this.parent = parent;
        this.loggedInUser = loggedInUser;
        
        setLayout(new BorderLayout());
        
        // Create sidebar
        JPanel sidebarPanel = createSidebar();
        add(sidebarPanel, BorderLayout.WEST);
        
        // Create header
        JPanel headerPanel = createHeader();
        add(headerPanel, BorderLayout.NORTH);
        
        // Create content panel
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(contentPanel, BorderLayout.CENTER);
        
        // Default content
        showWelcomePanel();
    }
    
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(52, 73, 94));
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Add menu items - without icons for now
        addMenuItem(sidebar, "Dashboard", null, e -> showWelcomePanel());
        addMenuItem(sidebar, "Students", null, e -> showStudentsPanel());
        addMenuItem(sidebar, "Teachers", null, e -> showTeachersPanel());
        addMenuItem(sidebar, "Classes", null, e -> showClassesPanel());
        
        // Add logout at the bottom
        JPanel spacer = new JPanel();
        spacer.setOpaque(false);
        sidebar.add(spacer);
        
        addMenuItem(sidebar, "Logout", null, e -> parent.logout());
        
        return sidebar;
    }
    
    private void addMenuItem(JPanel sidebar, String text, String iconPath, ActionListener action) {
        JButton menuButton = new JButton(text);
        
        // Try to load icon if path is provided
        if (iconPath != null) {
            try {
                URL iconUrl = getClass().getResource(iconPath);
                if (iconUrl != null) {
                    menuButton.setIcon(new ImageIcon(iconUrl));
                }
            } catch (Exception e) {
                // Silently ignore icon loading errors
                System.out.println("Could not load icon: " + iconPath);
            }
        }
        
        menuButton.setHorizontalAlignment(SwingConstants.LEFT);
        menuButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        menuButton.setBackground(new Color(52, 73, 94));
        menuButton.setForeground(Color.WHITE);
        menuButton.setBorderPainted(false);
        menuButton.setFocusPainted(false);
        menuButton.addActionListener(action);
        
        menuButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                menuButton.setBackground(new Color(44, 62, 80));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                menuButton.setBackground(new Color(52, 73, 94));
            }
        });
        
        sidebar.add(menuButton);
        sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
    }
    
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(41, 128, 185));
        header.setPreferredSize(new Dimension(0, 60));
        header.setBorder(new EmptyBorder(10, 20, 10, 20));
        
        JLabel titleLabel = new JLabel("School Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel userLabel = new JLabel("Logged in as: " + loggedInUser.getUsername() + " (" + loggedInUser.getRole() + ")");
        userLabel.setForeground(Color.WHITE);
        
        header.add(titleLabel, BorderLayout.WEST);
        header.add(userLabel, BorderLayout.EAST);
        
        return header;
    }
    
    public void showWelcomePanel() {
        contentPanel.removeAll();
        
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BorderLayout());
        
        JLabel welcomeLabel = new JLabel("Welcome to School Management System", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(41, 128, 185));
        
        // Create a simple placeholder instead of trying to load an image
        JPanel placeholderPanel = new JPanel();
        placeholderPanel.setPreferredSize(new Dimension(0, 200));
        placeholderPanel.setBackground(new Color(240, 240, 240));
        
        JLabel placeholderLabel = new JLabel("School Management System", JLabel.CENTER);
        placeholderLabel.setFont(new Font("Arial", Font.BOLD, 28));
        placeholderLabel.setForeground(new Color(100, 100, 100));
        placeholderPanel.add(placeholderLabel);
        
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        statsPanel.add(createStatCard("Students", "24", null));
        statsPanel.add(createStatCard("Teachers", "12", null));
        statsPanel.add(createStatCard("Classes", "12", null));
        
        welcomePanel.add(welcomeLabel, BorderLayout.NORTH);
        welcomePanel.add(placeholderPanel, BorderLayout.CENTER);
        welcomePanel.add(statsPanel, BorderLayout.SOUTH);
        
        contentPanel.add(welcomePanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private JPanel createStatCard(String title, String value, String iconPath) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        // Create a colored circle as an icon placeholder
        JPanel iconPlaceholder = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(41, 128, 185));
                g.fillOval(0, 0, 30, 30);
            }
        };
        iconPlaceholder.setPreferredSize(new Dimension(30, 30));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setForeground(new Color(41, 128, 185));
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.add(titleLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textPanel.add(valueLabel);
        
        card.add(iconPlaceholder, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    public void showStudentsPanel() {
        contentPanel.removeAll();
        contentPanel.add(new StudentPanel(parent), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    public void showTeachersPanel() {
        contentPanel.removeAll();
        contentPanel.add(new TeacherPanel(parent), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    public void showClassesPanel() {
        contentPanel.removeAll();
        contentPanel.add(new ClassPanel(parent), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}
