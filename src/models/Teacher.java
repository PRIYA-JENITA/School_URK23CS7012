package models;



import java.util.Date;

public class Teacher {
    private int teacherId;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String gender;
    private String address;
    private String phone;
    private String email;
    private Date hireDate;
    private String subjectSpecialty;
    
    // Constructors
    public Teacher() {}
    
    public Teacher(int teacherId, String firstName, String lastName, Date dateOfBirth, 
                  String gender, String address, String phone, String email, 
                  Date hireDate, String subjectSpecialty) {
        this.teacherId = teacherId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.hireDate = hireDate;
        this.subjectSpecialty = subjectSpecialty;
    }
    
    // Getters and Setters
    public int getTeacherId() {
        return teacherId;
    }
    
    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public Date getDateOfBirth() {
        return dateOfBirth;
    }
    
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public Date getHireDate() {
        return hireDate;
    }
    
    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }
    
    public String getSubjectSpecialty() {
        return subjectSpecialty;
    }
    
    public void setSubjectSpecialty(String subjectSpecialty) {
        this.subjectSpecialty = subjectSpecialty;
    }
    
    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}

