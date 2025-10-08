import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Console-based User Interface for the School Management System
 */
public class SchoolManagementUI {
    private Scanner scanner;
    private StudentManager studentManager;
    private TeacherManager teacherManager;
    private CourseManager courseManager;
    private GradeManager gradeManager;
    private DataManager dataManager;
    
    public SchoolManagementUI() {
        this.scanner = new Scanner(System.in);
        this.dataManager = new DataManager();
        this.studentManager = new StudentManager(dataManager);
        this.teacherManager = new TeacherManager(dataManager);
        this.courseManager = new CourseManager(dataManager);
        this.gradeManager = new GradeManager(dataManager);
    }
    
    public void start() {
        System.out.println("=".repeat(60));
        System.out.println("    WELCOME TO SCHOOL MANAGEMENT SYSTEM");
        System.out.println("=".repeat(60));
        
        while (true) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    studentMenu();
                    break;
                case 2:
                    teacherMenu();
                    break;
                case 3:
                    courseMenu();
                    break;
                case 4:
                    gradeMenu();
                    break;
                case 5:
                    reportsMenu();
                    break;
                case 6:
                    enrollmentMenu();
                    break;
                case 7:
                    searchMenu();
                    break;
                case 8:
                    dataManagementMenu();
                    break;
                case 0:
                    System.out.println("Thank you for using School Management System!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private void displayMainMenu() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("           MAIN MENU");
        System.out.println("=".repeat(40));
        System.out.println("1. Student Management");
        System.out.println("2. Teacher Management");
        System.out.println("3. Course Management");
        System.out.println("4. Grade Management");
        System.out.println("5. Reports");
        System.out.println("6. Enrollment Management");
        System.out.println("7. Search");
        System.out.println("8. Data Management");
        System.out.println("0. Exit");
        System.out.println("=".repeat(40));
    }
    
    // Student Management Menu
    private void studentMenu() {
        while (true) {
            System.out.println("\n" + "-".repeat(30));
            System.out.println("    STUDENT MANAGEMENT");
            System.out.println("-".repeat(30));
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Search Student");
            System.out.println("4. Update Student");
            System.out.println("5. Remove Student");
            System.out.println("6. View Student Details");
            System.out.println("7. Students by Grade Level");
            System.out.println("8. Honor Roll Students");
            System.out.println("9. Students with Low GPA");
            System.out.println("0. Back to Main Menu");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    viewAllStudents();
                    break;
                case 3:
                    searchStudent();
                    break;
                case 4:
                    updateStudent();
                    break;
                case 5:
                    removeStudent();
                    break;
                case 6:
                    viewStudentDetails();
                    break;
                case 7:
                    viewStudentsByGrade();
                    break;
                case 8:
                    viewHonorRollStudents();
                    break;
                case 9:
                    viewLowGPAStudents();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private void addStudent() {
        System.out.println("\n--- Add New Student ---");
        
        String studentId = studentManager.generateNextStudentId();
        System.out.println("Student ID: " + studentId);
        
        String firstName = getStringInput("First Name: ");
        String lastName = getStringInput("Last Name: ");
        LocalDate dateOfBirth = getDateInput("Date of Birth (YYYY-MM-DD): ");
        String email = getStringInput("Email: ");
        String phoneNumber = getStringInput("Phone Number: ");
        String address = getStringInput("Address: ");
        String grade = getStringInput("Grade Level (e.g., 9th, 10th, 11th, 12th): ");
        
        Student student = new Student(studentId, firstName, lastName, dateOfBirth, 
                                    email, phoneNumber, address, grade);
        
        if (studentManager.addStudent(student)) {
            System.out.println("Student added successfully!");
        } else {
            System.out.println("Failed to add student. Student ID may already exist.");
        }
    }
    
    private void viewAllStudents() {
        System.out.println("\n--- All Students ---");
        List<Student> students = studentManager.getAllStudents();
        
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        
        System.out.printf("%-10s %-20s %-10s %-6s %-8s%n", 
                         "ID", "Name", "Grade", "GPA", "Courses");
        System.out.println("-".repeat(60));
        
        for (Student student : students) {
            System.out.printf("%-10s %-20s %-10s %-6.2f %-8d%n",
                             student.getStudentId(),
                             student.getFullName(),
                             student.getGrade(),
                             student.getGpa(),
                             student.getEnrolledCourses().size());
        }
    }
    
    // Teacher Management Menu
    private void teacherMenu() {
        while (true) {
            System.out.println("\n" + "-".repeat(30));
            System.out.println("    TEACHER MANAGEMENT");
            System.out.println("-".repeat(30));
            System.out.println("1. Add Teacher");
            System.out.println("2. View All Teachers");
            System.out.println("3. Search Teacher");
            System.out.println("4. Update Teacher");
            System.out.println("5. Remove Teacher");
            System.out.println("6. View Teacher Details");
            System.out.println("7. Teachers by Department");
            System.out.println("8. Available Teachers");
            System.out.println("9. Overloaded Teachers");
            System.out.println("0. Back to Main Menu");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    addTeacher();
                    break;
                case 2:
                    viewAllTeachers();
                    break;
                case 3:
                    searchTeacher();
                    break;
                case 4:
                    updateTeacher();
                    break;
                case 5:
                    removeTeacher();
                    break;
                case 6:
                    viewTeacherDetails();
                    break;
                case 7:
                    viewTeachersByDepartment();
                    break;
                case 8:
                    viewAvailableTeachers();
                    break;
                case 9:
                    viewOverloadedTeachers();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private void addTeacher() {
        System.out.println("\n--- Add New Teacher ---");
        
        String teacherId = teacherManager.generateNextTeacherId();
        System.out.println("Teacher ID: " + teacherId);
        
        String firstName = getStringInput("First Name: ");
        String lastName = getStringInput("Last Name: ");
        String email = getStringInput("Email: ");
        String phoneNumber = getStringInput("Phone Number: ");
        String department = getStringInput("Department: ");
        String subject = getStringInput("Subject: ");
        double salary = getDoubleInput("Salary: ");
        String qualification = getStringInput("Qualification: ");
        
        Teacher teacher = new Teacher(teacherId, firstName, lastName, email, 
                                    phoneNumber, department, subject, salary, qualification);
        
        if (teacherManager.addTeacher(teacher)) {
            System.out.println("Teacher added successfully!");
        } else {
            System.out.println("Failed to add teacher. Teacher ID may already exist.");
        }
    }
    
    private void viewAllTeachers() {
        System.out.println("\n--- All Teachers ---");
        List<Teacher> teachers = teacherManager.getAllTeachers();
        
        if (teachers.isEmpty()) {
            System.out.println("No teachers found.");
            return;
        }
        
        System.out.printf("%-10s %-20s %-15s %-15s %-8s%n", 
                         "ID", "Name", "Department", "Subject", "Courses");
        System.out.println("-".repeat(75));
        
        for (Teacher teacher : teachers) {
            System.out.printf("%-10s %-20s %-15s %-15s %-8d%n",
                             teacher.getTeacherId(),
                             teacher.getFullName(),
                             teacher.getDepartment(),
                             teacher.getSubject(),
                             teacher.getAssignedCourses().size());
        }
    }
    
    // Course Management Menu
    private void courseMenu() {
        while (true) {
            System.out.println("\n" + "-".repeat(30));
            System.out.println("    COURSE MANAGEMENT");
            System.out.println("-".repeat(30));
            System.out.println("1. Add Course");
            System.out.println("2. View All Courses");
            System.out.println("3. Search Course");
            System.out.println("4. Update Course");
            System.out.println("5. Remove Course");
            System.out.println("6. View Course Details");
            System.out.println("7. Courses by Department");
            System.out.println("8. Available Courses");
            System.out.println("9. Full Courses");
            System.out.println("10. Assign Teacher to Course");
            System.out.println("0. Back to Main Menu");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    addCourse();
                    break;
                case 2:
                    viewAllCourses();
                    break;
                case 3:
                    searchCourse();
                    break;
                case 4:
                    updateCourse();
                    break;
                case 5:
                    removeCourse();
                    break;
                case 6:
                    viewCourseDetails();
                    break;
                case 7:
                    viewCoursesByDepartment();
                    break;
                case 8:
                    viewAvailableCourses();
                    break;
                case 9:
                    viewFullCourses();
                    break;
                case 10:
                    assignTeacherToCourse();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private void addCourse() {
        System.out.println("\n--- Add New Course ---");
        
        String courseId = courseManager.generateNextCourseId();
        System.out.println("Course ID: " + courseId);
        
        String courseName = getStringInput("Course Name: ");
        String description = getStringInput("Description: ");
        int credits = getIntInput("Credits: ");
        String department = getStringInput("Department: ");
        int maxStudents = getIntInput("Maximum Students: ");
        String schedule = getStringInput("Schedule (e.g., MWF 10:00-11:00): ");
        String classroom = getStringInput("Classroom: ");
        String semester = getStringInput("Semester: ");
        
        Course course = new Course(courseId, courseName, description, credits, 
                                 department, maxStudents, schedule, classroom, semester);
        
        if (courseManager.addCourse(course)) {
            System.out.println("Course added successfully!");
        } else {
            System.out.println("Failed to add course. Course ID may already exist.");
        }
    }
    
    private void viewAllCourses() {
        System.out.println("\n--- All Courses ---");
        List<Course> courses = courseManager.getAllCourses();
        
        if (courses.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }
        
        System.out.printf("%-10s %-25s %-8s %-12s %-10s%n", 
                         "ID", "Name", "Credits", "Enrollment", "Teacher");
        System.out.println("-".repeat(70));
        
        for (Course course : courses) {
            String teacherInfo = course.getTeacherId() != null ? course.getTeacherId() : "Unassigned";
            System.out.printf("%-10s %-25s %-8d %-12s %-10s%n",
                             course.getCourseId(),
                             course.getCourseName(),
                             course.getCredits(),
                             course.getCurrentEnrollment() + "/" + course.getMaxStudents(),
                             teacherInfo);
        }
    }
    
    // Grade Management Menu
    private void gradeMenu() {
        while (true) {
            System.out.println("\n" + "-".repeat(30));
            System.out.println("    GRADE MANAGEMENT");
            System.out.println("-".repeat(30));
            System.out.println("1. Add Grade");
            System.out.println("2. View All Grades");
            System.out.println("3. View Student Grades");
            System.out.println("4. View Course Grades");
            System.out.println("5. Update Grade");
            System.out.println("6. Remove Grade");
            System.out.println("7. Calculate Student GPA");
            System.out.println("8. Course Grade Statistics");
            System.out.println("9. Recent Grades");
            System.out.println("0. Back to Main Menu");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    addGrade();
                    break;
                case 2:
                    viewAllGrades();
                    break;
                case 3:
                    viewStudentGrades();
                    break;
                case 4:
                    viewCourseGrades();
                    break;
                case 5:
                    updateGrade();
                    break;
                case 6:
                    removeGrade();
                    break;
                case 7:
                    calculateStudentGPA();
                    break;
                case 8:
                    viewCourseGradeStatistics();
                    break;
                case 9:
                    viewRecentGrades();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private void addGrade() {
        System.out.println("\n--- Add New Grade ---");
        
        String gradeId = gradeManager.generateNextGradeId();
        System.out.println("Grade ID: " + gradeId);
        
        String studentId = getStringInput("Student ID: ");
        String courseId = getStringInput("Course ID: ");
        String assignmentName = getStringInput("Assignment Name: ");
        double score = getDoubleInput("Score: ");
        double maxScore = getDoubleInput("Maximum Score: ");
        
        System.out.println("Grade Types: EXAM, QUIZ, HOMEWORK, PROJECT, PARTICIPATION");
        String gradeType = getStringInput("Grade Type: ").toUpperCase();
        
        Grade grade = new Grade(gradeId, studentId, courseId, assignmentName, 
                              score, maxScore, gradeType);
        
        String comments = getStringInput("Comments (optional): ");
        if (!comments.trim().isEmpty()) {
            grade.setComments(comments);
        }
        
        if (gradeManager.addGrade(grade)) {
            System.out.println("Grade added successfully!");
            System.out.printf("Percentage: %.1f%%, Letter Grade: %s%n", 
                             grade.getPercentage(), grade.getLetterGrade());
        } else {
            System.out.println("Failed to add grade. Grade ID may already exist.");
        }
    }
    
    // Utility methods for input handling
    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
    
    private double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
    
    private LocalDate getDateInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String dateStr = scanner.nextLine().trim();
                return LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (DateTimeParseException e) {
                System.out.println("Please enter date in YYYY-MM-DD format.");
            }
        }
    }
    
    // Additional menu methods (simplified for brevity)
    private void searchStudent() {
        String name = getStringInput("Enter student name to search: ");
        List<Student> results = studentManager.searchStudentsByName(name);
        
        if (results.isEmpty()) {
            System.out.println("No students found with that name.");
        } else {
            System.out.println("\nSearch Results:");
            for (Student student : results) {
                System.out.println(student);
            }
        }
    }
    
    private void viewStudentDetails() {
        String studentId = getStringInput("Enter Student ID: ");
        Student student = studentManager.findStudentById(studentId);
        
        if (student != null) {
            System.out.println("\n--- Student Details ---");
            System.out.println("ID: " + student.getStudentId());
            System.out.println("Name: " + student.getFullName());
            System.out.println("Grade: " + student.getGrade());
            System.out.println("Email: " + student.getEmail());
            System.out.println("Phone: " + student.getPhoneNumber());
            System.out.println("Address: " + student.getAddress());
            System.out.println("GPA: " + student.getGpa());
            System.out.println("Enrolled Courses: " + student.getEnrolledCourses().size());
        } else {
            System.out.println("Student not found.");
        }
    }
    
    // Placeholder methods for other menu options
    private void updateStudent() { System.out.println("Update Student - Feature coming soon!"); }
    private void removeStudent() { System.out.println("Remove Student - Feature coming soon!"); }
    private void viewStudentsByGrade() { System.out.println("Students by Grade - Feature coming soon!"); }
    private void viewHonorRollStudents() { 
        List<Student> honorRoll = studentManager.getHonorRollStudents();
        System.out.println("\n--- Honor Roll Students (GPA >= 3.5) ---");
        for (Student student : honorRoll) {
            System.out.println(student);
        }
    }
    private void viewLowGPAStudents() {
        List<Student> lowGPA = studentManager.getStudentsWithLowGPA();
        System.out.println("\n--- Students with Low GPA (<2.0) ---");
        for (Student student : lowGPA) {
            System.out.println(student);
        }
    }
    
    private void searchTeacher() { System.out.println("Search Teacher - Feature coming soon!"); }
    private void updateTeacher() { System.out.println("Update Teacher - Feature coming soon!"); }
    private void removeTeacher() { System.out.println("Remove Teacher - Feature coming soon!"); }
    private void viewTeacherDetails() { System.out.println("Teacher Details - Feature coming soon!"); }
    private void viewTeachersByDepartment() { System.out.println("Teachers by Department - Feature coming soon!"); }
    private void viewAvailableTeachers() { System.out.println("Available Teachers - Feature coming soon!"); }
    private void viewOverloadedTeachers() { System.out.println("Overloaded Teachers - Feature coming soon!"); }
    
    private void searchCourse() { System.out.println("Search Course - Feature coming soon!"); }
    private void updateCourse() { System.out.println("Update Course - Feature coming soon!"); }
    private void removeCourse() { System.out.println("Remove Course - Feature coming soon!"); }
    private void viewCourseDetails() { System.out.println("Course Details - Feature coming soon!"); }
    private void viewCoursesByDepartment() { System.out.println("Courses by Department - Feature coming soon!"); }
    private void viewAvailableCourses() { System.out.println("Available Courses - Feature coming soon!"); }
    private void viewFullCourses() { System.out.println("Full Courses - Feature coming soon!"); }
    private void assignTeacherToCourse() { System.out.println("Assign Teacher - Feature coming soon!"); }
    
    private void viewAllGrades() { System.out.println("All Grades - Feature coming soon!"); }
    private void viewStudentGrades() { System.out.println("Student Grades - Feature coming soon!"); }
    private void viewCourseGrades() { System.out.println("Course Grades - Feature coming soon!"); }
    private void updateGrade() { System.out.println("Update Grade - Feature coming soon!"); }
    private void removeGrade() { System.out.println("Remove Grade - Feature coming soon!"); }
    private void calculateStudentGPA() { System.out.println("Calculate GPA - Feature coming soon!"); }
    private void viewCourseGradeStatistics() { System.out.println("Course Statistics - Feature coming soon!"); }
    private void viewRecentGrades() { System.out.println("Recent Grades - Feature coming soon!"); }
    
    private void reportsMenu() { System.out.println("Reports Menu - Feature coming soon!"); }
    private void enrollmentMenu() { System.out.println("Enrollment Menu - Feature coming soon!"); }
    private void searchMenu() { System.out.println("Search Menu - Feature coming soon!"); }
    private void dataManagementMenu() { System.out.println("Data Management Menu - Feature coming soon!"); }
}