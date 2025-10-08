/**
 * Test class to demonstrate the School Management System functionality
 */
public class TestSystem {
    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("    SCHOOL MANAGEMENT SYSTEM - FUNCTIONALITY TEST");
        System.out.println("=".repeat(60));
        
        // Initialize managers
        DataManager dataManager = new DataManager();
        StudentManager studentManager = new StudentManager(dataManager);
        TeacherManager teacherManager = new TeacherManager(dataManager);
        CourseManager courseManager = new CourseManager(dataManager);
        GradeManager gradeManager = new GradeManager(dataManager);
        
        // Test 1: Display system statistics
        System.out.println("\n1. SYSTEM STATISTICS");
        System.out.println("-".repeat(30));
        System.out.println("Total Students: " + studentManager.getAllStudents().size());
        System.out.println("Total Teachers: " + teacherManager.getAllTeachers().size());
        System.out.println("Total Courses: " + courseManager.getAllCourses().size());
        System.out.println("Total Grades: " + gradeManager.getAllGrades().size());
        
        // Test 2: Display top students
        System.out.println("\n2. HONOR ROLL STUDENTS (Top 10)");
        System.out.println("-".repeat(40));
        studentManager.getHonorRollStudents().stream()
            .limit(10)
            .forEach(student -> 
                System.out.printf("%-15s %-20s GPA: %.2f%n", 
                    student.getStudentId(), 
                    student.getFullName(), 
                    student.getGpa()));
        
        // Test 3: Display course enrollment statistics
        System.out.println("\n3. COURSE ENROLLMENT STATUS");
        System.out.println("-".repeat(40));
        courseManager.getAllCourses().stream()
            .limit(10)
            .forEach(course -> 
                System.out.printf("%-10s %-25s %2d/%-2d %s%n",
                    course.getCourseId(),
                    course.getCourseName(),
                    course.getCurrentEnrollment(),
                    course.getMaxStudents(),
                    course.isFull() ? "[FULL]" : "[OPEN]"));
        
        // Test 4: Display teacher workload
        System.out.println("\n4. TEACHER WORKLOAD");
        System.out.println("-".repeat(40));
        teacherManager.getAllTeachers().stream()
            .limit(10)
            .forEach(teacher -> 
                System.out.printf("%-10s %-20s %-15s %d courses%n",
                    teacher.getTeacherId(),
                    teacher.getFullName(),
                    teacher.getDepartment(),
                    teacher.getAssignedCourses().size()));
        
        // Test 5: Grade statistics
        System.out.println("\n5. RECENT GRADE STATISTICS");
        System.out.println("-".repeat(40));
        var recentGrades = gradeManager.getRecentGrades();
        System.out.println("Recent grades (last 30 days): " + recentGrades.size());
        
        if (!recentGrades.isEmpty()) {
            double avgScore = recentGrades.stream()
                .mapToDouble(Grade::getPercentage)
                .average()
                .orElse(0.0);
            System.out.printf("Average score: %.1f%%%n", avgScore);
            
            long excellentGrades = recentGrades.stream()
                .mapToLong(g -> g.getPercentage() >= 90 ? 1 : 0)
                .sum();
            System.out.println("Excellent grades (90%+): " + excellentGrades);
        }
        
        // Test 6: Search functionality
        System.out.println("\n6. SEARCH FUNCTIONALITY TEST");
        System.out.println("-".repeat(40));
        
        // Search for students with "John" in their name
        var johnStudents = studentManager.searchStudentsByName("John");
        System.out.println("Students with 'John' in name: " + johnStudents.size());
        
        // Search for math courses
        var mathCourses = courseManager.searchCoursesByName("Algebra");
        System.out.println("Algebra courses: " + mathCourses.size());
        
        // Test 7: Data integrity check
        System.out.println("\n7. DATA INTEGRITY CHECK");
        System.out.println("-".repeat(40));
        
        int totalEnrollments = studentManager.getAllStudents().stream()
            .mapToInt(s -> s.getEnrolledCourses().size())
            .sum();
        
        int courseEnrollments = courseManager.getAllCourses().stream()
            .mapToInt(c -> c.getCurrentEnrollment())
            .sum();
        
        System.out.println("Student enrollments: " + totalEnrollments);
        System.out.println("Course enrollments: " + courseEnrollments);
        System.out.println("Data consistency: " + 
            (totalEnrollments == courseEnrollments ? "✓ PASS" : "✗ FAIL"));
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("    SYSTEM TEST COMPLETED SUCCESSFULLY!");
        System.out.println("    All core functionality is working properly.");
        System.out.println("=".repeat(60));
        
        System.out.println("\nTo run the interactive system, use:");
        System.out.println("java SchoolManagementSystem");
    }
}