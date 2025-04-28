CREATE DATABASE IF NOT EXISTS school_management;
USE school_management;

-- Students table
CREATE TABLE IF NOT EXISTS students (
    student_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    date_of_birth DATE,
    gender ENUM('Male', 'Female', 'Other'),
    address VARCHAR(255),
    phone VARCHAR(20),
    email VARCHAR(100),
    enrollment_date DATE,
    class_id INT
);

-- Teachers table
CREATE TABLE IF NOT EXISTS teachers (
    teacher_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    date_of_birth DATE,
    gender ENUM('Male', 'Female', 'Other'),
    address VARCHAR(255),
    phone VARCHAR(20),
    email VARCHAR(100),
    hire_date DATE,
    subject_specialty VARCHAR(100)
);

-- Classes table
CREATE TABLE IF NOT EXISTS classes (
    class_id INT AUTO_INCREMENT PRIMARY KEY,
    class_name VARCHAR(50) NOT NULL,
    grade_level INT,
    teacher_id INT,
    room_number VARCHAR(20),
    FOREIGN KEY (teacher_id) REFERENCES teachers(teacher_id)
);

-- Alter students table to add foreign key
ALTER TABLE students
ADD FOREIGN KEY (class_id) REFERENCES classes(class_id);

-

-- Users table for login
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('Admin', 'Teacher', 'Student') NOT NULL,
    related_id INT COMMENT 'ID in respective table (teacher_id or student_id)'
);

-- Insert admin user
INSERT INTO users (username, password, role) VALUES ('admins', 'admin12', 'Admin');
select * from students;
select * from teachers; 
select * from classes;

