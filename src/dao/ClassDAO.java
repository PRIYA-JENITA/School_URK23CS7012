package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Class;
import utils.DatabaseConnection;

public class ClassDAO {
    
    public ClassDAO() {
        // Empty constructor - no stored connection
    }
    
    public boolean addClass(Class classObj) {
        String query = "INSERT INTO classes (class_name, grade_level, teacher_id, room_number) VALUES (?, ?, ?, ?)";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, classObj.getClassName());
            pstmt.setInt(2, classObj.getGradeLevel());
            pstmt.setInt(3, classObj.getTeacherId());
            pstmt.setString(4, classObj.getRoomNumber());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        classObj.setClassId(rs.getInt(1));
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
    
    public boolean updateClass(Class classObj) {
        String query = "UPDATE classes SET class_name=?, grade_level=?, teacher_id=?, room_number=? WHERE class_id=?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            
            pstmt.setString(1, classObj.getClassName());
            pstmt.setInt(2, classObj.getGradeLevel());
            pstmt.setInt(3, classObj.getTeacherId());
            pstmt.setString(4, classObj.getRoomNumber());
            pstmt.setInt(5, classObj.getClassId());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteClass(int classId) {
        String query = "DELETE FROM classes WHERE class_id=?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            
            pstmt.setInt(1, classId);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Class getClassById(int classId) {
        String query = "SELECT * FROM classes WHERE class_id=?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            
            pstmt.setInt(1, classId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Class classObj = new Class();
                    classObj.setClassId(rs.getInt("class_id"));
                    classObj.setClassName(rs.getString("class_name"));
                    classObj.setGradeLevel(rs.getInt("grade_level"));
                    classObj.setTeacherId(rs.getInt("teacher_id"));
                    classObj.setRoomNumber(rs.getString("room_number"));
                    return classObj;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Class> getAllClasses() {
        List<Class> classes = new ArrayList<>();
        String query = "SELECT * FROM classes";
        
        try (Connection connection = DatabaseConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Class classObj = new Class();
                classObj.setClassId(rs.getInt("class_id"));
                classObj.setClassName(rs.getString("class_name"));
                classObj.setGradeLevel(rs.getInt("grade_level"));
                classObj.setTeacherId(rs.getInt("teacher_id"));
                classObj.setRoomNumber(rs.getString("room_number"));
                classes.add(classObj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classes;
    }
    
    public List<Class> getClassesByTeacher(int teacherId) {
        List<Class> classes = new ArrayList<>();
        String query = "SELECT * FROM classes WHERE teacher_id=?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            
            pstmt.setInt(1, teacherId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Class classObj = new Class();
                    classObj.setClassId(rs.getInt("class_id"));
                    classObj.setClassName(rs.getString("class_name"));
                    classObj.setGradeLevel(rs.getInt("grade_level"));
                    classObj.setTeacherId(rs.getInt("teacher_id"));
                    classObj.setRoomNumber(rs.getString("room_number"));
                    classes.add(classObj);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classes;
    }
}
