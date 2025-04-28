package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Teacher;
import utils.DatabaseConnection;

public class TeacherDAO {
    private Connection connection;
    
    public TeacherDAO() {
        this.connection = DatabaseConnection.getConnection();
    }
    
    public boolean addTeacher(Teacher teacher) {
        String query = "INSERT INTO teachers (first_name, last_name, date_of_birth, gender, address, phone, email, hire_date, subject_specialty) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, teacher.getFirstName());
            pstmt.setString(2, teacher.getLastName());
            pstmt.setDate(3, teacher.getDateOfBirth() != null ? new java.sql.Date(teacher.getDateOfBirth().getTime()) : null);
            pstmt.setString(4, teacher.getGender());
            pstmt.setString(5, teacher.getAddress());
            pstmt.setString(6, teacher.getPhone());
            pstmt.setString(7, teacher.getEmail());
            pstmt.setDate(8, teacher.getHireDate() != null ? new java.sql.Date(teacher.getHireDate().getTime()) : null);
            pstmt.setString(9, teacher.getSubjectSpecialty());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        teacher.setTeacherId(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateTeacher(Teacher teacher) {
        String query = "UPDATE teachers SET first_name=?, last_name=?, date_of_birth=?, gender=?, " +
                      "address=?, phone=?, email=?, hire_date=?, subject_specialty=? WHERE teacher_id=?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, teacher.getFirstName());
            pstmt.setString(2, teacher.getLastName());
            pstmt.setDate(3, teacher.getDateOfBirth() != null ? new java.sql.Date(teacher.getDateOfBirth().getTime()) : null);
            pstmt.setString(4, teacher.getGender());
            pstmt.setString(5, teacher.getAddress());
            pstmt.setString(6, teacher.getPhone());
            pstmt.setString(7, teacher.getEmail());
            pstmt.setDate(8, teacher.getHireDate() != null ? new java.sql.Date(teacher.getHireDate().getTime()) : null);
            pstmt.setString(9, teacher.getSubjectSpecialty());
            pstmt.setInt(10, teacher.getTeacherId());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteTeacher(int teacherId) {
        String query = "DELETE FROM teachers WHERE teacher_id=?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, teacherId);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Teacher getTeacherById(int teacherId) {
        String query = "SELECT * FROM teachers WHERE teacher_id=?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, teacherId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Teacher teacher = new Teacher();
                    teacher.setTeacherId(rs.getInt("teacher_id"));
                    teacher.setFirstName(rs.getString("first_name"));
                    teacher.setLastName(rs.getString("last_name"));
                    teacher.setDateOfBirth(rs.getDate("date_of_birth"));
                    teacher.setGender(rs.getString("gender"));
                    teacher.setAddress(rs.getString("address"));
                    teacher.setPhone(rs.getString("phone"));
                    teacher.setEmail(rs.getString("email"));
                    teacher.setHireDate(rs.getDate("hire_date"));
                    teacher.setSubjectSpecialty(rs.getString("subject_specialty"));
                    return teacher;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Teacher> getAllTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        String query = "SELECT * FROM teachers";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Teacher teacher = new Teacher();
                teacher.setTeacherId(rs.getInt("teacher_id"));
                teacher.setFirstName(rs.getString("first_name"));
                teacher.setLastName(rs.getString("last_name"));
                teacher.setDateOfBirth(rs.getDate("date_of_birth"));
                teacher.setGender(rs.getString("gender"));
                teacher.setAddress(rs.getString("address"));
                teacher.setPhone(rs.getString("phone"));
                teacher.setEmail(rs.getString("email"));
                teacher.setHireDate(rs.getDate("hire_date"));
                teacher.setSubjectSpecialty(rs.getString("subject_specialty"));
                teachers.add(teacher);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return teachers;
    }
}

