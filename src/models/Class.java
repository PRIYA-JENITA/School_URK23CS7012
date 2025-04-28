package models;



public class Class {
    private int classId;
    private String className;
    private int gradeLevel;
    private int teacherId;
    private String roomNumber;
    
    // Constructors
    public Class() {}
    
    public Class(int classId, String className, int gradeLevel, int teacherId, String roomNumber) {
        this.classId = classId;
        this.className = className;
        this.gradeLevel = gradeLevel;
        this.teacherId = teacherId;
        this.roomNumber = roomNumber;
    }
    
    // Getters and Setters
    public int getClassId() {
        return classId;
    }
    
    public void setClassId(int classId) {
        this.classId = classId;
    }
    
    public String getClassName() {
        return className;
    }
    
    public void setClassName(String className) {
        this.className = className;
    }
    
    public int getGradeLevel() {
        return gradeLevel;
    }
    
    public void setGradeLevel(int gradeLevel) {
        this.gradeLevel = gradeLevel;
    }
    
    public int getTeacherId() {
        return teacherId;
    }
    
    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }
    
    public String getRoomNumber() {
        return roomNumber;
    }
    
    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
    
    @Override
    public String toString() {
        return className + " (Grade " + gradeLevel + ")";
    }
}

