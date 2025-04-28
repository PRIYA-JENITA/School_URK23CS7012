package ui;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import dao.TeacherDAO;
import models.Teacher;

public class TeacherPanel extends JPanel {
    private MainFrame parent;
    private JTable teacherTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton addButton, editButton, deleteButton;
    private TeacherDAO teacherDAO;
    private List<Teacher> teachers;
    
    public TeacherPanel(MainFrame parent) {
        this.parent = parent;
        this.teacherDAO = new TeacherDAO();
        
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Create top panel with search and buttons
        JPanel topPanel = new JPanel(new BorderLayout(10, 0));
        
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel searchLabel = new JLabel("Search: ");
        searchField = new JTextField(20);
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterTeachers();
            }
        });
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        addButton = new JButton("Add Teacher");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");
        
        // Try to load icons, but don't fail if they're not found
        try {
            URL addIconUrl = getClass().getResource("/resources/add-icon.png");
            if (addIconUrl != null) {
                addButton.setIcon(new ImageIcon(addIconUrl));
            }
        } catch (Exception e) {
            System.out.println("Could not load add icon");
        }
        
        try {
            URL editIconUrl = getClass().getResource("/resources/edit-icon.png");
            if (editIconUrl != null) {
                editButton.setIcon(new ImageIcon(editIconUrl));
            }
        } catch (Exception e) {
            System.out.println("Could not load edit icon");
        }
        
        try {
            URL deleteIconUrl = getClass().getResource("/resources/delete-icon.png");
            if (deleteIconUrl != null) {
                deleteButton.setIcon(new ImageIcon(deleteIconUrl));
            }
        } catch (Exception e) {
            System.out.println("Could not load delete icon");
        }
        
        buttonsPanel.add(addButton);
        buttonsPanel.add(editButton);
        buttonsPanel.add(deleteButton);
        
        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(buttonsPanel, BorderLayout.EAST);
        
        // Create table
        createTable();
        JScrollPane scrollPane = new JScrollPane(teacherTable);
        
        // Add components to panel
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
        // Add action listeners
        addButton.addActionListener(e -> showTeacherDialog(null));
        editButton.addActionListener(e -> {
            int selectedRow = teacherTable.getSelectedRow();
            if (selectedRow >= 0) {
                int teacherId = (int) tableModel.getValueAt(selectedRow, 0);
                Teacher teacher = teacherDAO.getTeacherById(teacherId);
                showTeacherDialog(teacher);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a teacher to edit", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        deleteButton.addActionListener(e -> {
            int selectedRow = teacherTable.getSelectedRow();
            if (selectedRow >= 0) {
                int teacherId = (int) tableModel.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(this, 
                    "Are you sure you want to delete this teacher?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    if (teacherDAO.deleteTeacher(teacherId)) {
                        loadTeachers();
                        JOptionPane.showMessageDialog(this, "Teacher deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to delete teacher", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a teacher to delete", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        // Load teachers
        loadTeachers();
    }
    
    private void createTable() {
        String[] columns = {"ID", "First Name", "Last Name", "Gender", "Date of Birth", "Phone", "Email", "Subject Specialty"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        teacherTable = new JTable(tableModel);
        teacherTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        teacherTable.setRowHeight(25);
        teacherTable.getTableHeader().setReorderingAllowed(false);
    }
    
    private void loadTeachers() {
        tableModel.setRowCount(0);
        teachers = teacherDAO.getAllTeachers();
        
        for (Teacher teacher : teachers) {
            Object[] row = {
                teacher.getTeacherId(),
                teacher.getFirstName(),
                teacher.getLastName(),
                teacher.getGender(),
                teacher.getDateOfBirth(),
                teacher.getPhone(),
                teacher.getEmail(),
                teacher.getSubjectSpecialty()
            };
            tableModel.addRow(row);
        }
    }
    
    private void filterTeachers() {
        String searchText = searchField.getText().toLowerCase();
        tableModel.setRowCount(0);
        
        for (Teacher teacher : teachers) {
            String firstName = teacher.getFirstName() != null ? teacher.getFirstName().toLowerCase() : "";
            String lastName = teacher.getLastName() != null ? teacher.getLastName().toLowerCase() : "";
            String email = teacher.getEmail() != null ? teacher.getEmail().toLowerCase() : "";
            String specialty = teacher.getSubjectSpecialty() != null ? teacher.getSubjectSpecialty().toLowerCase() : "";
            
            if (firstName.contains(searchText) || 
                lastName.contains(searchText) || 
                email.contains(searchText) || 
                specialty.contains(searchText)) {
                
                Object[] row = {
                    teacher.getTeacherId(),
                    teacher.getFirstName(),
                    teacher.getLastName(),
                    teacher.getGender(),
                    teacher.getDateOfBirth(),
                    teacher.getPhone(),
                    teacher.getEmail(),
                    teacher.getSubjectSpecialty()
                };
                tableModel.addRow(row);
            }
        }
    }
    
    private void showTeacherDialog(Teacher teacher) {
        boolean isNewTeacher = (teacher == null);
        final Teacher teacherToEdit = isNewTeacher ? new Teacher() : teacher;
        
        JDialog dialog = new JDialog(parent, isNewTeacher ? "Add New Teacher" : "Edit Teacher", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 500);
        dialog.setLocationRelativeTo(this);
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // First Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("First Name:"), gbc);
        
        gbc.gridx = 1;
        JTextField firstNameField = new JTextField(20);
        firstNameField.setText(teacherToEdit.getFirstName() != null ? teacherToEdit.getFirstName() : "");
        formPanel.add(firstNameField, gbc);
        
        // Last Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Last Name:"), gbc);
        
        gbc.gridx = 1;
        JTextField lastNameField = new JTextField(20);
        lastNameField.setText(teacherToEdit.getLastName() != null ? teacherToEdit.getLastName() : "");
        formPanel.add(lastNameField, gbc);
        
        // Gender
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Gender:"), gbc);
        
        gbc.gridx = 1;
        String[] genders = {"Male", "Female", "Other"};
        JComboBox<String> genderCombo = new JComboBox<>(genders);
        if (teacherToEdit.getGender() != null) {
            genderCombo.setSelectedItem(teacherToEdit.getGender());
        }
        formPanel.add(genderCombo, gbc);
        
        // Date of Birth
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Date of Birth (yyyy-MM-dd):"), gbc);
        
        gbc.gridx = 1;
        JTextField dobField = new JTextField(20);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (teacherToEdit.getDateOfBirth() != null) {
            dobField.setText(dateFormat.format(teacherToEdit.getDateOfBirth()));
        }
        formPanel.add(dobField, gbc);
        
        // Address
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Address:"), gbc);
        
        gbc.gridx = 1;
        JTextField addressField = new JTextField(20);
        addressField.setText(teacherToEdit.getAddress() != null ? teacherToEdit.getAddress() : "");
        formPanel.add(addressField, gbc);
        
        // Phone
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Phone:"), gbc);
        
        gbc.gridx = 1;
        JTextField phoneField = new JTextField(20);
        phoneField.setText(teacherToEdit.getPhone() != null ? teacherToEdit.getPhone() : "");
        formPanel.add(phoneField, gbc);
        
        // Email
        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(new JLabel("Email:"), gbc);
        
        gbc.gridx = 1;
        JTextField emailField = new JTextField(20);
        emailField.setText(teacherToEdit.getEmail() != null ? teacherToEdit.getEmail() : "");
        formPanel.add(emailField, gbc);
        
        // Hire Date
        gbc.gridx = 0;
        gbc.gridy = 7;
        formPanel.add(new JLabel("Hire Date (yyyy-MM-dd):"), gbc);
        
        gbc.gridx = 1;
        JTextField hireDateField = new JTextField(20);
        if (teacherToEdit.getHireDate() != null) {
            hireDateField.setText(dateFormat.format(teacherToEdit.getHireDate()));
        } else {
            hireDateField.setText(dateFormat.format(new Date()));
        }
        formPanel.add(hireDateField, gbc);
        
        // Subject Specialty
        gbc.gridx = 0;
        gbc.gridy = 8;
        formPanel.add(new JLabel("Subject Specialty:"), gbc);
        
        gbc.gridx = 1;
        JTextField specialtyField = new JTextField(20);
        specialtyField.setText(teacherToEdit.getSubjectSpecialty() != null ? teacherToEdit.getSubjectSpecialty() : "");
        formPanel.add(specialtyField, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add action listeners
        saveButton.addActionListener(e -> {
            // Validate input
            if (firstNameField.getText().trim().isEmpty() || lastNameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "First name and last name are required", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Set teacher properties
            teacherToEdit.setFirstName(firstNameField.getText().trim());
            teacherToEdit.setLastName(lastNameField.getText().trim());
            teacherToEdit.setGender((String) genderCombo.getSelectedItem());
            teacherToEdit.setAddress(addressField.getText().trim());
            teacherToEdit.setPhone(phoneField.getText().trim());
            teacherToEdit.setEmail(emailField.getText().trim());
            teacherToEdit.setSubjectSpecialty(specialtyField.getText().trim());
            
            // Parse dates
            try {
                if (!dobField.getText().trim().isEmpty()) {
                    teacherToEdit.setDateOfBirth(dateFormat.parse(dobField.getText().trim()));
                }
                if (!hireDateField.getText().trim().isEmpty()) {
                    teacherToEdit.setHireDate(dateFormat.parse(hireDateField.getText().trim()));
                }
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid date format. Please use yyyy-MM-dd", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Save teacher
            boolean success;
            if (isNewTeacher) {
                success = teacherDAO.addTeacher(teacherToEdit);
            } else {
                success = teacherDAO.updateTeacher(teacherToEdit);
            }
            
            if (success) {
                loadTeachers();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Teacher saved successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to save teacher", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        dialog.setVisible(true);
    }
}
