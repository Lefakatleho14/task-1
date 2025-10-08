import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * StudentManager class for handling student-related operations
 */
public class StudentManager {
    private List<Student> students;
    private DataManager dataManager;
    
    public StudentManager(DataManager dataManager) {
        this.dataManager = dataManager;
        this.students = dataManager.loadStudents();
    }
    
    // Add a new student
    public boolean addStudent(Student student) {
        if (findStudentById(student.getStudentId()) != null) {
            return false; // Student with this ID already exists
        }
        students.add(student);
        saveData();
        return true;
    }
    
    // Remove a student
    public boolean removeStudent(String studentId) {
        Student student = findStudentById(studentId);
        if (student != null) {
            students.remove(student);
            saveData();
            return true;
        }
        return false;
    }
    
    // Update student information
    public boolean updateStudent(Student updatedStudent) {
        Student existingStudent = findStudentById(updatedStudent.getStudentId());
        if (existingStudent != null) {
            int index = students.indexOf(existingStudent);
            students.set(index, updatedStudent);
            saveData();
            return true;
        }
        return false;
    }
    
    // Find student by ID
    public Student findStudentById(String studentId) {
        return students.stream()
                .filter(s -> s.getStudentId().equals(studentId))
                .findFirst()
                .orElse(null);
    }
    
    // Search students by name
    public List<Student> searchStudentsByName(String name) {
        String searchTerm = name.toLowerCase();
        return students.stream()
                .filter(s -> s.getFullName().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
    }
    
    // Get students by grade level
    public List<Student> getStudentsByGrade(String grade) {
        return students.stream()
                .filter(s -> s.getGrade().equals(grade))
                .collect(Collectors.toList());
    }
    
    // Get students enrolled in a specific course
    public List<Student> getStudentsInCourse(String courseId) {
        return students.stream()
                .filter(s -> s.getEnrolledCourses().contains(courseId))
                .collect(Collectors.toList());
    }
    
    // Enroll student in a course
    public boolean enrollStudentInCourse(String studentId, String courseId) {
        Student student = findStudentById(studentId);
        if (student != null) {
            student.addCourse(courseId);
            saveData();
            return true;
        }
        return false;
    }
    
    // Remove student from a course
    public boolean removeStudentFromCourse(String studentId, String courseId) {
        Student student = findStudentById(studentId);
        if (student != null) {
            student.removeCourse(courseId);
            saveData();
            return true;
        }
        return false;
    }
    
    // Calculate and update student GPA
    public void updateStudentGPA(String studentId, List<Grade> studentGrades) {
        Student student = findStudentById(studentId);
        if (student != null) {
            double totalPoints = 0.0;
            int totalCredits = 0;
            
            // Group grades by course and calculate course GPAs
            Map<String, List<Grade>> gradesByCourse = studentGrades.stream()
                    .filter(g -> g.getStudentId().equals(studentId))
                    .collect(Collectors.groupingBy(Grade::getCourseId));
            
            for (Map.Entry<String, List<Grade>> entry : gradesByCourse.entrySet()) {
                List<Grade> courseGrades = entry.getValue();
                double courseAverage = courseGrades.stream()
                        .mapToDouble(Grade::getPercentage)
                        .average()
                        .orElse(0.0);
                
                // Convert percentage to grade points
                double gradePoints = percentageToGradePoints(courseAverage);
                totalPoints += gradePoints * 3; // Assuming 3 credits per course
                totalCredits += 3;
            }
            
            double gpa = totalCredits > 0 ? totalPoints / totalCredits : 0.0;
            student.setGpa(Math.round(gpa * 100.0) / 100.0); // Round to 2 decimal places
            saveData();
        }
    }
    
    private double percentageToGradePoints(double percentage) {
        if (percentage >= 90) return 4.0;
        else if (percentage >= 80) return 3.0;
        else if (percentage >= 70) return 2.0;
        else if (percentage >= 60) return 1.0;
        else return 0.0;
    }
    
    // Get all students
    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }
    
    // Get students with low GPA (below 2.0)
    public List<Student> getStudentsWithLowGPA() {
        return students.stream()
                .filter(s -> s.getGpa() < 2.0)
                .collect(Collectors.toList());
    }
    
    // Get honor roll students (GPA >= 3.5)
    public List<Student> getHonorRollStudents() {
        return students.stream()
                .filter(s -> s.getGpa() >= 3.5)
                .sorted((s1, s2) -> Double.compare(s2.getGpa(), s1.getGpa()))
                .collect(Collectors.toList());
    }
    
    // Generate student statistics
    public Map<String, Object> getStudentStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalStudents", students.size());
        stats.put("averageGPA", students.stream()
                .mapToDouble(Student::getGpa)
                .average()
                .orElse(0.0));
        
        // Count students by grade level
        Map<String, Long> gradeDistribution = students.stream()
                .collect(Collectors.groupingBy(Student::getGrade, Collectors.counting()));
        stats.put("gradeDistribution", gradeDistribution);
        
        // GPA distribution
        long highGPA = students.stream().mapToLong(s -> s.getGpa() >= 3.5 ? 1 : 0).sum();
        long mediumGPA = students.stream().mapToLong(s -> s.getGpa() >= 2.0 && s.getGpa() < 3.5 ? 1 : 0).sum();
        long lowGPA = students.stream().mapToLong(s -> s.getGpa() < 2.0 ? 1 : 0).sum();
        
        Map<String, Long> gpaDistribution = new HashMap<>();
        gpaDistribution.put("High (3.5+)", highGPA);
        gpaDistribution.put("Medium (2.0-3.4)", mediumGPA);
        gpaDistribution.put("Low (<2.0)", lowGPA);
        stats.put("gpaDistribution", gpaDistribution);
        
        return stats;
    }
    
    // Generate next student ID
    public String generateNextStudentId() {
        int maxId = students.stream()
                .mapToInt(s -> {
                    try {
                        return Integer.parseInt(s.getStudentId().replaceAll("[^0-9]", ""));
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                })
                .max()
                .orElse(0);
        return "STU" + String.format("%04d", maxId + 1);
    }
    
    private void saveData() {
        dataManager.saveStudents(students);
    }
}