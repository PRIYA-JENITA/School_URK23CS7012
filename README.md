# School_URK23CS7012
# 📚 School Management System

A Java-based **School Management System** with a graphical user interface (GUI) built using **Java Swing**. It features **database connectivity** for managing students, teachers, classes, and users efficiently.

## 🚀 Features

- **Login System**: Secure login panel for users.
- **Dashboard**: Centralized dashboard for navigation.
- **Class Management**: Add, update, delete classes.
- **Student Management**: CRUD operations for students.
- **Teacher Management**: CRUD operations for teachers.
- **Database Integration**: MySQL database connectivity for storing and retrieving data.
- **User-Friendly UI**: Built with Java Swing, styled with icons.

## 🗂️ Project Structure

```
src/
├── dao/
│   ├── ClassDAO.java
│   ├── StudentDAO.java
│   ├── TeacherDAO.java
│   └── UserDAO.java
├── models/
│   ├── Class.java
│   ├── Student.java
│   ├── Teacher.java
│   └── User.java
├── school_management_system/
│   └── SchoolManagementSystem.java
├── ui/
│   ├── ClassPanel.java
│   ├── DashboardPanel.java
│   ├── LoginPanel.java
│   ├── MainFrame.java
│   ├── StudentPanel.java
│   └── TeacherPanel.java
├── utils/
│   └── DatabaseConnection.java
├── module-info.java
resources/
├── add-icon.png
├── app-icon.png
├── class-icon.png
├── dashboard-icon.png
├── delete.png
├── edit-icon.png
├── login-icon.png
├── logout-icon.png
├── school-banner.png
├── school-logo.png
├── student-icon.png
└── teachers-icon.png
```

## 🛠️ Technologies Used

- **Java SE 21**
- **Java Swing** (for GUI)
- **MySQL** (for database)
- **JDBC** (Java Database Connectivity)
- **Maven** (optional for dependency management)

## ⚙️ Setup Instructions

1. **Clone the Repository**

   ```bash
   git clone https://github.com/your-username/school_management_system.git
   ```

2. **Database Setup**

   - Create a MySQL database named (e.g., `school_db`).
   - Import the provided SQL file (if available) or manually create tables for:
     - `users`
     - `students`
     - `teachers`
     - `classes`
   - Update your database credentials inside `DatabaseConnection.java`:

     ```java
     private static final String URL = "jdbc:mysql://localhost:3306/school_db";
     private static final String USER = "your-username";
     private static final String PASSWORD = "your-password";
     ```

3. **Run the Project**

   - Open the project in **Eclipse** or **IntelliJ IDEA**.
   - Compile and run `SchoolManagementSystem.java`.

## 📷 Screenshots

*(Add some screenshots here to showcase login, dashboard, and management panels.)*

## 📋 Future Enhancements

- Role-based authentication (Admin/Teacher).
- Report generation (PDF/Excel exports).
- Enhanced UI/UX with JavaFX.
- Online deployment with server integration.

## 🙏 Acknowledgements

- Java Swing official documentation
- MySQL documentation
- StackOverflow community for troubleshooting


