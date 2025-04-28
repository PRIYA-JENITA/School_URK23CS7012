package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Student;
import utils.DatabaseConnection;

public class StudentDAO {
    
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Student student = new Student();
                student.setStudentId(rs.getInt("student_id"));
                student.setFirstName(rs.getString("first_name"));
                student.setLastName(rs.getString("last_name"));
                student.setGender(rs.getString("gender"));
                student.setDateOfBirth(rs.getDate("date_of_birth"));
                student.setAddress(rs.getString("address"));
                student.setPhone(rs.getString("phone"));
                student.setEmail(rs.getString("email"));
                student.setEnrollmentDate(rs.getDate("enrollment_date"));
                
                // Handle class_id which might be NULL
                // Handle class_id which might be NULL
                int classId = rs.getInt("class_id");
                if (!rs.wasNull()) {
                    student.setClassId(classId);
                } else {
                    student.setClassId(0); // Use 0 to represent NULL/no class
                }
                
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return students;
    }
    
    public Student getStudentById(int studentId) {
        String sql = "SELECT * FROM students WHERE student_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, studentId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Student student = new Student();
                    student.setStudentId(rs.getInt("student_id"));
                    student.setFirstName(rs.getString("first_name"));
                    student.setLastName(rs.getString("last_name"));
                    student.setGender(rs.getString("gender"));
                    student.setDateOfBirth(rs.getDate("date_of_birth"));
                    student.setAddress(rs.getString("address"));
                    student.setPhone(rs.getString("phone"));
                    student.setEmail(rs.getString("email"));
                    student.setEnrollmentDate(rs.getDate("enrollment_date"));
                    
                    // Handle class_id which might be NULL
                    int classId = rs.getInt("class_id");
                    if (!rs.wasNull()) {
                        student.setClassId(classId);
                    } else {
                        student.setClassId(0); // Use 0 to represent NULL/no class
                    }
                    
                    return student;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public boolean addStudent(Student student) {
        String sql = "INSERT INTO students (first_name, last_name, gender, date_of_birth, address, phone, email, enrollment_date, class_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, student.getFirstName());
            stmt.setString(2, student.getLastName());
            stmt.setString(3, student.getGender());
            
            if (student.getDateOfBirth() != null) {
                stmt.setDate(4, new java.sql.Date(student.getDateOfBirth().getTime()));
            } else {
                stmt.setNull(4, java.sql.Types.DATE);
            }
            
            stmt.setString(5, student.getAddress());
            stmt.setString(6, student.getPhone());
            stmt.setString(7, student.getEmail());
            
            if (student.getEnrollmentDate() != null) {
                stmt.setDate(8, new java.sql.Date(student.getEnrollmentDate().getTime()));
            } else {
                stmt.setNull(8, java.sql.Types.DATE);
            }
            
            // Handle class_id - Fix for foreign key constraint
            if (student.getClassId() > 0) {
                stmt.setInt(9, student.getClassId());
            } else {
                stmt.setNull(9, java.sql.Types.INTEGER);
            }
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateStudent(Student student) {
        String sql = "UPDATE students SET first_name = ?, last_name = ?, gender = ?, date_of_birth = ?, address = ?, phone = ?, email = ?, enrollment_date = ?, class_id = ? WHERE student_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, student.getFirstName());
            stmt.setString(2, student.getLastName());
            stmt.setString(3, student.getGender());
            
            if (student.getDateOfBirth() != null) {
                stmt.setDate(4, new java.sql.Date(student.getDateOfBirth().getTime()));
            } else {
                stmt.setNull(4, java.sql.Types.DATE);
            }
            
            stmt.setString(5, student.getAddress());
            stmt.setString(6, student.getPhone());
            stmt.setString(7, student.getEmail());
            
            if (student.getEnrollmentDate() != null) {
                stmt.setDate(8, new java.sql.Date(student.getEnrollmentDate().getTime()));
            } else {
                stmt.setNull(8, java.sql.Types.DATE);
            }
            
            // Handle class_id - Fix for foreign key constraint
            if (student.getClassId() > 0) {
                stmt.setInt(9, student.getClassId());
            } else {
                stmt.setNull(9, java.sql.Types.INTEGER);
            }
            
            stmt.setInt(10, student.getStudentId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteStudent(int studentId) {
        String sql = "DELETE FROM students WHERE student_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, studentId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Student> getStudentsByClassId(int classId) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE class_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, classId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Student student = new Student();
                    student.setStudentId(rs.getInt("student_id"));
                    student.setFirstName(rs.getString("first_name"));
                    student.setLastName(rs.getString("last_name"));
                    student.setGender(rs.getString("gender"));
                    student.setDateOfBirth(rs.getDate("date_of_birth"));
                    student.setAddress(rs.getString("address"));
                    student.setPhone(rs.getString("phone"));
                    student.setEmail(rs.getString("email"));
                    student.setEnrollmentDate(rs.getDate("enrollment_date"));
                    student.setClassId(classId);
                    
                    students.add(student);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return students;
    }
}
