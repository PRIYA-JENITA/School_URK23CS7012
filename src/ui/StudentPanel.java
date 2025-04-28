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
import dao.ClassDAO;
import dao.StudentDAO;
import models.Class;
import models.Student;

public class StudentPanel extends JPanel {
    private MainFrame parent;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton addButton, editButton, deleteButton;
    private StudentDAO studentDAO;
    private ClassDAO classDAO;
    private List<Student> students;
    
    public StudentPanel(MainFrame parent) {
        this.parent = parent;
        this.studentDAO = new StudentDAO();
        this.classDAO = new ClassDAO();
        
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
                filterStudents();
            }
        });
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        addButton = new JButton("Add Student");
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
        JScrollPane scrollPane = new JScrollPane(studentTable);
        
        // Add components to panel
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
        // Add action listeners
        addButton.addActionListener(e -> showStudentDialog(null));
        editButton.addActionListener(e -> {
            int selectedRow = studentTable.getSelectedRow();
            if (selectedRow >= 0) {
                int studentId = (int) tableModel.getValueAt(selectedRow, 0);
                Student student = studentDAO.getStudentById(studentId);
                showStudentDialog(student);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a student to edit", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        deleteButton.addActionListener(e -> {
            int selectedRow = studentTable.getSelectedRow();
            if (selectedRow >= 0) {
                int studentId = (int) tableModel.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this student?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    if (studentDAO.deleteStudent(studentId)) {
                        loadStudents();
                        JOptionPane.showMessageDialog(this, "Student deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to delete student", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a student to delete", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        // Load students
        loadStudents();
    }
    
    private void createTable() {
        String[] columns = {"ID", "First Name", "Last Name", "Gender", "Date of Birth", "Phone", "Email", "Class"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        studentTable = new JTable(tableModel);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentTable.setRowHeight(25);
        studentTable.getTableHeader().setReorderingAllowed(false);
    }
    
    private void loadStudents() {
        tableModel.setRowCount(0);
        students = studentDAO.getAllStudents();
        
        for (Student student : students) {
            String className = "";
            if (student.getClassId() > 0) {
                Class classObj = classDAO.getClassById(student.getClassId());
                if (classObj != null) {
                    className = classObj.getClassName();
                }
            }
            
            Object[] row = {
                student.getStudentId(),
                student.getFirstName(),
                student.getLastName(),
                student.getGender(),
                student.getDateOfBirth(),
                student.getPhone(),
                student.getEmail(),
                className
            };
            tableModel.addRow(row);
        }
    }
    
    private void filterStudents() {
        String searchText = searchField.getText().toLowerCase();
        tableModel.setRowCount(0);
        
        for (Student student : students) {
            String firstName = student.getFirstName() != null ? student.getFirstName().toLowerCase() : "";
            String lastName = student.getLastName() != null ? student.getLastName().toLowerCase() : "";
            String email = student.getEmail() != null ? student.getEmail().toLowerCase() : "";
            
            if (firstName.contains(searchText) || lastName.contains(searchText) || email.contains(searchText)) {
                
                String className = "";
                if (student.getClassId() > 0) {
                    Class classObj = classDAO.getClassById(student.getClassId());
                    if (classObj != null) {
                        className = classObj.getClassName();
                    }
                }
                
                Object[] row = {
                    student.getStudentId(),
                    student.getFirstName(),
                    student.getLastName(),
                    student.getGender(),
                    student.getDateOfBirth(),
                    student.getPhone(),
                    student.getEmail(),
                    className
                };
                tableModel.addRow(row);
            }
        }
    }
    
    private void showStudentDialog(Student student) {
        boolean isNewStudent = (student == null);
        final Student studentToEdit = isNewStudent ? new Student() : student;
        
        JDialog dialog = new JDialog(parent, isNewStudent ? "Add New Student" : "Edit Student", true);
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
        firstNameField.setText(studentToEdit.getFirstName() != null ? studentToEdit.getFirstName() : "");
        formPanel.add(firstNameField, gbc);
        
        // Last Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Last Name:"), gbc);
        
        gbc.gridx = 1;
        JTextField lastNameField = new JTextField(20);
        lastNameField.setText(studentToEdit.getLastName() != null ? studentToEdit.getLastName() : "");
        formPanel.add(lastNameField, gbc);
        
        // Gender
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Gender:"), gbc);
        
        gbc.gridx = 1;
        String[] genders = {"Male", "Female", "Other"};
        JComboBox<String> genderCombo = new JComboBox<>(genders);
        if (studentToEdit.getGender() != null) {
            genderCombo.setSelectedItem(studentToEdit.getGender());
        }
        formPanel.add(genderCombo, gbc);
        
        // Date of Birth
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Date of Birth (yyyy-MM-dd):"), gbc);
        
        gbc.gridx = 1;
        JTextField dobField = new JTextField(20);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (studentToEdit.getDateOfBirth() != null) {
            dobField.setText(dateFormat.format(studentToEdit.getDateOfBirth()));
        }
        formPanel.add(dobField, gbc);
        
        // Address
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Address:"), gbc);
        
        gbc.gridx = 1;
        JTextField addressField = new JTextField(20);
        addressField.setText(studentToEdit.getAddress() != null ? studentToEdit.getAddress() : "");
        formPanel.add(addressField, gbc);
        
        // Phone
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Phone:"), gbc);
        
        gbc.gridx = 1;
        JTextField phoneField = new JTextField(20);
        phoneField.setText(studentToEdit.getPhone() != null ? studentToEdit.getPhone() : "");
        formPanel.add(phoneField, gbc);
        
        // Email
        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(new JLabel("Email:"), gbc);
        
        gbc.gridx = 1;
        JTextField emailField = new JTextField(20);
        emailField.setText(studentToEdit.getEmail() != null ? studentToEdit.getEmail() : "");
        formPanel.add(emailField, gbc);
        
        // Enrollment Date
        gbc.gridx = 0;
        gbc.gridy = 7;
        formPanel.add(new JLabel("Enrollment Date (yyyy-MM-dd):"), gbc);
        
        gbc.gridx = 1;
        JTextField enrollmentField = new JTextField(20);
        if (studentToEdit.getEnrollmentDate() != null) {
            enrollmentField.setText(dateFormat.format(studentToEdit.getEnrollmentDate()));
        } else {
            enrollmentField.setText(dateFormat.format(new Date()));
        }
        formPanel.add(enrollmentField, gbc);
        
        // Class
        gbc.gridx = 0;
        gbc.gridy = 8;
        formPanel.add(new JLabel("Class:"), gbc);
        
        gbc.gridx = 1;
        List<Class> classes = classDAO.getAllClasses();
        JComboBox<Class> classCombo = new JComboBox<>();
        classCombo.addItem(new Class(0, "-- Select Class --", 0, 0, ""));
        for (Class classObj : classes) {
            classCombo.addItem(classObj);
        }
        
        if (studentToEdit.getClassId() > 0) {
            for (int i = 0; i < classCombo.getItemCount(); i++) {
                if (classCombo.getItemAt(i).getClassId() == studentToEdit.getClassId()) {
                    classCombo.setSelectedIndex(i);
                    break;
                }
            }
        }
        formPanel.add(classCombo, gbc);
        
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
            
            // Set student properties
            studentToEdit.setFirstName(firstNameField.getText().trim());
            studentToEdit.setLastName(lastNameField.getText().trim());
            studentToEdit.setGender((String) genderCombo.getSelectedItem());
            studentToEdit.setAddress(addressField.getText().trim());
            studentToEdit.setPhone(phoneField.getText().trim());
            studentToEdit.setEmail(emailField.getText().trim());
            
            // Parse dates
            try {
                if (!dobField.getText().trim().isEmpty()) {
                    studentToEdit.setDateOfBirth(dateFormat.parse(dobField.getText().trim()));
                }
                if (!enrollmentField.getText().trim().isEmpty()) {
                    studentToEdit.setEnrollmentDate(dateFormat.parse(enrollmentField.getText().trim()));
                }
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid date format. Please use yyyy-MM-dd", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Set class - Fix for foreign key constraint
            Class selectedClass = (Class) classCombo.getSelectedItem();
            if (selectedClass != null && selectedClass.getClassId() > 0) {
                studentToEdit.setClassId(selectedClass.getClassId());
            } else {
                // If "-- Select Class --" is selected or no class is selected, set class_id to null
                studentToEdit.setClassId(0); // This will be converted to NULL in the DAO
            }
            
            // Save student
            boolean success;
            if (isNewStudent) {
                success = studentDAO.addStudent(studentToEdit);
            } else {
                success = studentDAO.updateStudent(studentToEdit);
            }
            
            if (success) {
                loadStudents();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Student saved successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to save student. Make sure you've selected a valid class.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        dialog.setVisible(true);
    }
}
