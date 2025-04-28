package ui;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import dao.ClassDAO;
import dao.TeacherDAO;
import models.Teacher;

public class ClassPanel extends JPanel {
    private MainFrame parent;
    private JTable classTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton addButton, editButton, deleteButton;
    private ClassDAO classDAO;
    private TeacherDAO teacherDAO;
    private List<models.Class> classes;
    public ClassPanel(MainFrame parent) {
        this.parent = parent;
        this.classDAO = new ClassDAO();
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
                filterClasses();
            }
        });
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        addButton = new JButton("Add Class");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");
        
        buttonsPanel.add(addButton);
        buttonsPanel.add(editButton);
        buttonsPanel.add(deleteButton);
        
        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(buttonsPanel, BorderLayout.EAST);
        
        // Create table
        createTable();
        JScrollPane scrollPane = new JScrollPane(classTable);
        
        // Add components to panel
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
        // Add action listeners
        addButton.addActionListener(e -> showClassDialog(null));
        editButton.addActionListener(e -> {
            int selectedRow = classTable.getSelectedRow();
            if (selectedRow >= 0) {
                int classId = (int) tableModel.getValueAt(selectedRow, 0);
                models.Class selectedClass = classDAO.getClassById(classId);
                showClassDialog(selectedClass);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a class to edit", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        deleteButton.addActionListener(e -> {
            int selectedRow = classTable.getSelectedRow();
            if (selectedRow >= 0) {
                int classId = (int) tableModel.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(this, 
                    "Are you sure you want to delete this class?", 
                    "Confirm Deletion", 
                    JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    if (classDAO.deleteClass(classId)) {
                        loadClasses();
                        JOptionPane.showMessageDialog(this, "Class deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to delete class", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a class to delete", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        // Load classes
        loadClasses();
    }
    
    private void createTable() {
        String[] columns = {"ID", "Class Name", "Grade Level", "Teacher", "Room Number"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        classTable = new JTable(tableModel);
        classTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        classTable.setRowHeight(25);
        classTable.getTableHeader().setReorderingAllowed(false);
    }
    
    private void loadClasses() {
        tableModel.setRowCount(0);
        classes = classDAO.getAllClasses();
        
        for (models.Class courseClass : classes) {
            String teacherName = "";
            if (courseClass.getTeacherId() > 0) {
                Teacher teacher = teacherDAO.getTeacherById(courseClass.getTeacherId());
                if (teacher != null) {
                    teacherName = teacher.getFirstName() + " " + teacher.getLastName();
                }
            }
            
            Object[] row = {
                courseClass.getClassId(),
                courseClass.getClassName(),
                courseClass.getGradeLevel(),
                teacherName,
                courseClass.getRoomNumber() != null ? courseClass.getRoomNumber() : ""
            };
            tableModel.addRow(row);
        }
    }
    
    private void filterClasses() {
        String searchText = searchField.getText().toLowerCase();
        tableModel.setRowCount(0);
        
        for (models.Class courseClass : classes) {
            String className = courseClass.getClassName() != null ? courseClass.getClassName().toLowerCase() : "";
            String roomNumber = courseClass.getRoomNumber() != null ? courseClass.getRoomNumber().toLowerCase() : "";
            
            if (className.contains(searchText) || roomNumber.contains(searchText)) {
                
                String teacherName = "";
                if (courseClass.getTeacherId() > 0) {
                    Teacher teacher = teacherDAO.getTeacherById(courseClass.getTeacherId());
                    if (teacher != null) {
                        teacherName = teacher.getFirstName() + " " + teacher.getLastName();
                    }
                }
                
                Object[] row = {
                    courseClass.getClassId(),
                    courseClass.getClassName(),
                    courseClass.getGradeLevel(),
                    teacherName,
                    courseClass.getRoomNumber() != null ? courseClass.getRoomNumber() : ""
                };
                tableModel.addRow(row);
            }
        }
    }
    
    private void showClassDialog(models.Class courseClass) {
        boolean isNewClass = (courseClass == null);
        
        // Create a new class instance if null
        if (isNewClass) {
            courseClass = new models.Class();
        }
        
        // Create a final reference for use in lambda expressions
        final models.Class finalCourseClass = courseClass;
        
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        JDialog dialog;
        
        if (parentWindow instanceof Frame) {
            dialog = new JDialog((Frame) parentWindow, isNewClass ? "Add New Class" : "Edit Class", true);
        } else if (parentWindow instanceof Dialog) {
            dialog = new JDialog((Dialog) parentWindow, isNewClass ? "Add New Class" : "Edit Class", true);
        } else {
            // Fallback to a frameless dialog if no parent window is found
            dialog = new JDialog((Frame) null, isNewClass ? "Add New Class" : "Edit Class", true);
        }
        
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Class Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Class Name:"), gbc);
        
        gbc.gridx = 1;
        JTextField classNameField = new JTextField(20);
        classNameField.setText(finalCourseClass.getClassName() != null ? finalCourseClass.getClassName() : "");
        formPanel.add(classNameField, gbc);
        
        // Grade Level
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Grade Level:"), gbc);
        
        gbc.gridx = 1;
        SpinnerNumberModel gradeModel = new SpinnerNumberModel(
            finalCourseClass.getGradeLevel() > 0 ? finalCourseClass.getGradeLevel() : 1, 1, 12, 1);
        JSpinner gradeLevelSpinner = new JSpinner(gradeModel);
        formPanel.add(gradeLevelSpinner, gbc);
        
        // Teacher
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Teacher:"), gbc);
        
        gbc.gridx = 1;
        List<Teacher> teachers = teacherDAO.getAllTeachers();
        
        // Create a custom model for the combo box
        DefaultComboBoxModel<TeacherItem> teacherModel = new DefaultComboBoxModel<>();
        teacherModel.addElement(new TeacherItem(0, "-- Select Teacher --"));
        
        for (Teacher teacher : teachers) {
            teacherModel.addElement(new TeacherItem(
                teacher.getTeacherId(), 
                teacher.getFirstName() + " " + teacher.getLastName()
            ));
        }
        
        JComboBox<TeacherItem> teacherCombo = new JComboBox<>(teacherModel);
        
        if (finalCourseClass.getTeacherId() > 0) {
            for (int i = 0; i < teacherCombo.getItemCount(); i++) {
                if (teacherCombo.getItemAt(i).getId() == finalCourseClass.getTeacherId()) {
                    teacherCombo.setSelectedIndex(i);
                    break;
                }
            }
        }
        formPanel.add(teacherCombo, gbc);
        
        // Room Number
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Room Number:"), gbc);
        
        gbc.gridx = 1;
        JTextField roomNumberField = new JTextField(20);
        roomNumberField.setText(finalCourseClass.getRoomNumber() != null ? finalCourseClass.getRoomNumber() : "");
        formPanel.add(roomNumberField, gbc);
        
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
            if (classNameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Class name is required", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Set class properties
            finalCourseClass.setClassName(classNameField.getText().trim());
            finalCourseClass.setGradeLevel((Integer) gradeLevelSpinner.getValue());
            
            TeacherItem selectedTeacher = (TeacherItem) teacherCombo.getSelectedItem();
            finalCourseClass.setTeacherId(selectedTeacher.getId());
            finalCourseClass.setRoomNumber(roomNumberField.getText().trim());
            
            // Save class
            boolean success;
            if (isNewClass) {
                success = classDAO.addClass(finalCourseClass);
            } else {
                success = classDAO.updateClass(finalCourseClass);
            }
            
            if (success) {
                loadClasses();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Class saved successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to save class", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        dialog.setVisible(true);
    }
    
    // Helper class for the teacher combo box
    private static class TeacherItem {
        private int id;
        private String name;
        
        public TeacherItem(int id, String name) {
            this.id = id;
            this.name = name;
        }
        
        public int getId() {
            return id;
        }
        
        @Override
        public String toString() {
            return name;
        }
    }
}
