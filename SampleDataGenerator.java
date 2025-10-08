import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Utility class to generate sample data for testing the School Management System
 */
public class SampleDataGenerator {
    private DataManager dataManager;
    private StudentManager studentManager;
    private TeacherManager teacherManager;
    private CourseManager courseManager;
    private GradeManager gradeManager;
    private Random random;
    
    public SampleDataGenerator() {
        this.dataManager = new DataManager();
        this.studentManager = new StudentManager(dataManager);
        this.teacherManager = new TeacherManager(dataManager);
        this.courseManager = new CourseManager(dataManager);
        this.gradeManager = new GradeManager(dataManager);
        this.random = new Random();
    }
    
    /**
     * Generate complete sample data for the school management system
     */
    public void generateSampleData() {
        System.out.println("Generating sample data...");
        
        // Generate sample teachers first
        generateSampleTeachers();
        
        // Generate sample courses
        generateSampleCourses();
        
        // Generate sample students
        generateSampleStudents();
        
        // Enroll students in courses
        enrollStudentsInCourses();
        
        // Generate sample grades
        generateSampleGrades();
        
        // Update student GPAs
        updateStudentGPAs();
        
        System.out.println("Sample data generation completed!");
        printDataSummary();
    }
    
    private void generateSampleTeachers() {
        String[] firstNames = {"John", "Jane", "Michael", "Sarah", "David", "Lisa", "Robert", "Emily", "James", "Maria"};
        String[] lastNames = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez"};
        String[] departments = {"Mathematics", "Science", "English", "History", "Physical Education", "Art", "Music", "Computer Science"};
        String[] subjects = {"Algebra", "Biology", "Literature", "World History", "PE", "Drawing", "Band", "Programming"};
        String[] qualifications = {"Bachelor's Degree", "Master's Degree", "PhD", "Master's in Education"};
        
        for (int i = 0; i < 15; i++) {
            String teacherId = teacherManager.generateNextTeacherId();
            String firstName = firstNames[random.nextInt(firstNames.length)];
            String lastName = lastNames[random.nextInt(lastNames.length)];
            String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@school.edu";
            String phoneNumber = "555-" + String.format("%04d", random.nextInt(10000));
            String department = departments[random.nextInt(departments.length)];
            String subject = subjects[random.nextInt(subjects.length)];
            double salary = 40000 + random.nextDouble() * 40000; // $40k - $80k
            String qualification = qualifications[random.nextInt(qualifications.length)];
            
            Teacher teacher = new Teacher(teacherId, firstName, lastName, email, phoneNumber, 
                                        department, subject, salary, qualification);
            teacher.setHireDate(LocalDate.now().minusDays(random.nextInt(3650))); // Up to 10 years ago
            
            teacherManager.addTeacher(teacher);
        }
        
        System.out.println("Generated 15 sample teachers");
    }
    
    private void generateSampleCourses() {
        String[] courseNames = {
            "Algebra I", "Algebra II", "Geometry", "Calculus",
            "Biology", "Chemistry", "Physics", "Earth Science",
            "English Literature", "Creative Writing", "Grammar & Composition",
            "World History", "US History", "Government", "Economics",
            "Physical Education", "Health", "Art Fundamentals", "Music Theory",
            "Computer Programming", "Web Design", "Digital Media"
        };
        
        String[] departments = {"Mathematics", "Science", "English", "History", "Physical Education", "Art", "Music", "Computer Science"};
        String[] semesters = {"Fall 2024", "Spring 2025"};
        String[] schedules = {"MWF 8:00-9:00", "MWF 10:00-11:00", "TTH 9:00-10:30", "TTH 1:00-2:30", "Daily 2:00-3:00"};
        
        List<Teacher> teachers = teacherManager.getAllTeachers();
        
        for (int i = 0; i < courseNames.length; i++) {
            String courseId = courseManager.generateNextCourseId();
            String courseName = courseNames[i];
            String description = "Comprehensive course covering " + courseName.toLowerCase();
            int credits = 3 + random.nextInt(2); // 3-4 credits
            String department = departments[random.nextInt(departments.length)];
            int maxStudents = 20 + random.nextInt(11); // 20-30 students
            String schedule = schedules[random.nextInt(schedules.length)];
            String classroom = "Room " + (100 + random.nextInt(300));
            String semester = semesters[random.nextInt(semesters.length)];
            
            Course course = new Course(courseId, courseName, description, credits, 
                                     department, maxStudents, schedule, classroom, semester);
            
            // Assign a random teacher
            if (!teachers.isEmpty()) {
                Teacher teacher = teachers.get(random.nextInt(teachers.size()));
                course.setTeacherId(teacher.getTeacherId());
                teacher.assignCourse(courseId);
            }
            
            courseManager.addCourse(course);
        }
        
        System.out.println("Generated " + courseNames.length + " sample courses");
    }
    
    private void generateSampleStudents() {
        String[] firstNames = {"Alex", "Emma", "Noah", "Olivia", "Liam", "Ava", "William", "Sophia", "Mason", "Isabella",
                              "James", "Charlotte", "Benjamin", "Amelia", "Lucas", "Mia", "Henry", "Harper", "Alexander", "Evelyn"};
        String[] lastNames = {"Anderson", "Thompson", "Garcia", "Martinez", "Robinson", "Clark", "Rodriguez", "Lewis", 
                             "Lee", "Walker", "Hall", "Allen", "Young", "Hernandez", "King", "Wright", "Lopez", "Hill", "Scott", "Green"};
        String[] grades = {"9th", "10th", "11th", "12th"};
        String[] streets = {"Main St", "Oak Ave", "Pine Rd", "Elm Dr", "Maple Ln", "Cedar Ct", "Birch Way", "Willow St"};
        
        for (int i = 0; i < 50; i++) {
            String studentId = studentManager.generateNextStudentId();
            String firstName = firstNames[random.nextInt(firstNames.length)];
            String lastName = lastNames[random.nextInt(lastNames.length)];
            
            // Generate birth date (14-18 years old)
            LocalDate dateOfBirth = LocalDate.now().minusYears(14 + random.nextInt(5)).minusDays(random.nextInt(365));
            
            String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@student.school.edu";
            String phoneNumber = "555-" + String.format("%04d", random.nextInt(10000));
            String address = (100 + random.nextInt(9900)) + " " + streets[random.nextInt(streets.length)];
            String grade = grades[random.nextInt(grades.length)];
            
            Student student = new Student(studentId, firstName, lastName, dateOfBirth, 
                                        email, phoneNumber, address, grade);
            
            studentManager.addStudent(student);
        }
        
        System.out.println("Generated 50 sample students");
    }
    
    private void enrollStudentsInCourses() {
        List<Student> students = studentManager.getAllStudents();
        List<Course> courses = courseManager.getAllCourses();
        
        for (Student student : students) {
            // Enroll each student in 4-6 random courses
            int numCourses = 4 + random.nextInt(3);
            
            for (int i = 0; i < numCourses && i < courses.size(); i++) {
                Course course = courses.get(random.nextInt(courses.size()));
                
                // Check if student is not already enrolled and course has space
                if (!student.getEnrolledCourses().contains(course.getCourseId()) && !course.isFull()) {
                    courseManager.enrollStudentInCourse(course.getCourseId(), student.getStudentId());
                    studentManager.enrollStudentInCourse(student.getStudentId(), course.getCourseId());
                }
            }
        }
        
        System.out.println("Enrolled students in courses");
    }
    
    private void generateSampleGrades() {
        List<Student> students = studentManager.getAllStudents();
        String[] assignmentTypes = {"EXAM", "QUIZ", "HOMEWORK", "PROJECT", "PARTICIPATION"};
        String[] assignmentNames = {
            "Midterm Exam", "Final Exam", "Quiz 1", "Quiz 2", "Homework 1", "Homework 2", 
            "Research Project", "Lab Report", "Class Participation", "Presentation"
        };
        
        for (Student student : students) {
            for (String courseId : student.getEnrolledCourses()) {
                // Generate 5-8 grades per course
                int numGrades = 5 + random.nextInt(4);
                
                for (int i = 0; i < numGrades; i++) {
                    String gradeId = gradeManager.generateNextGradeId();
                    String assignmentName = assignmentNames[random.nextInt(assignmentNames.length)];
                    String gradeType = assignmentTypes[random.nextInt(assignmentTypes.length)];
                    
                    // Generate realistic scores based on grade type
                    double maxScore = 100.0;
                    double score;
                    
                    if (gradeType.equals("PARTICIPATION")) {
                        score = 80 + random.nextDouble() * 20; // 80-100%
                    } else if (gradeType.equals("HOMEWORK")) {
                        score = 70 + random.nextDouble() * 30; // 70-100%
                    } else {
                        // Normal distribution around 80% with some variation
                        score = Math.max(0, Math.min(100, 80 + (random.nextGaussian() * 15)));
                    }
                    
                    Grade grade = new Grade(gradeId, student.getStudentId(), courseId, 
                                          assignmentName, score, maxScore, gradeType);
                    
                    // Set random date within last 3 months
                    grade.setDateRecorded(LocalDate.now().minusDays(random.nextInt(90)));
                    
                    gradeManager.addGrade(grade);
                }
            }
        }
        
        System.out.println("Generated sample grades for all students");
    }
    
    private void updateStudentGPAs() {
        List<Student> students = studentManager.getAllStudents();
        List<Grade> allGrades = gradeManager.getAllGrades();
        
        for (Student student : students) {
            studentManager.updateStudentGPA(student.getStudentId(), allGrades);
        }
        
        System.out.println("Updated student GPAs");
    }
    
    private void printDataSummary() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("           DATA GENERATION SUMMARY");
        System.out.println("=".repeat(50));
        
        System.out.println("Students: " + studentManager.getAllStudents().size());
        System.out.println("Teachers: " + teacherManager.getAllTeachers().size());
        System.out.println("Courses: " + courseManager.getAllCourses().size());
        System.out.println("Grades: " + gradeManager.getAllGrades().size());
        
        // Calculate total enrollments
        int totalEnrollments = studentManager.getAllStudents().stream()
                .mapToInt(s -> s.getEnrolledCourses().size())
                .sum();
        System.out.println("Total Enrollments: " + totalEnrollments);
        
        // Calculate average GPA
        double avgGPA = studentManager.getAllStudents().stream()
                .mapToDouble(Student::getGpa)
                .average()
                .orElse(0.0);
        System.out.println("Average Student GPA: " + String.format("%.2f", avgGPA));
        
        System.out.println("=".repeat(50));
    }
    
    /**
     * Main method to run sample data generation independently
     */
    public static void main(String[] args) {
        SampleDataGenerator generator = new SampleDataGenerator();
        generator.generateSampleData();
        
        System.out.println("\nSample data has been generated and saved to files.");
        System.out.println("You can now run the SchoolManagementSystem to see the data.");
    }
}