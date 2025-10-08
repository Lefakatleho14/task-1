import java.util.ArrayList;
import java.util.List;

/**
 * Course entity class representing a course in the school management system
 */
public class Course {
    private String courseId;
    private String courseName;
    private String description;
    private int credits;
    private String teacherId;
    private String department;
    private int maxStudents;
    private List<String> enrolledStudents;
    private String schedule; // e.g., "MWF 10:00-11:00"
    private String classroom;
    private String semester;
    
    // Constructor
    public Course(String courseId, String courseName, String description, 
                  int credits, String department, int maxStudents, 
                  String schedule, String classroom, String semester) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.description = description;
        this.credits = credits;
        this.department = department;
        this.maxStudents = maxStudents;
        this.schedule = schedule;
        this.classroom = classroom;
        this.semester = semester;
        this.enrolledStudents = new ArrayList<>();
    }
    
    // Getters and Setters
    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }
    
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }
    
    public String getTeacherId() { return teacherId; }
    public void setTeacherId(String teacherId) { this.teacherId = teacherId; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public int getMaxStudents() { return maxStudents; }
    public void setMaxStudents(int maxStudents) { this.maxStudents = maxStudents; }
    
    public List<String> getEnrolledStudents() { return enrolledStudents; }
    public void setEnrolledStudents(List<String> enrolledStudents) { this.enrolledStudents = enrolledStudents; }
    
    public String getSchedule() { return schedule; }
    public void setSchedule(String schedule) { this.schedule = schedule; }
    
    public String getClassroom() { return classroom; }
    public void setClassroom(String classroom) { this.classroom = classroom; }
    
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }
    
    // Utility methods
    public boolean enrollStudent(String studentId) {
        if (enrolledStudents.size() < maxStudents && !enrolledStudents.contains(studentId)) {
            enrolledStudents.add(studentId);
            return true;
        }
        return false;
    }
    
    public void removeStudent(String studentId) {
        enrolledStudents.remove(studentId);
    }
    
    public int getCurrentEnrollment() {
        return enrolledStudents.size();
    }
    
    public int getAvailableSpots() {
        return maxStudents - enrolledStudents.size();
    }
    
    public boolean isFull() {
        return enrolledStudents.size() >= maxStudents;
    }
    
    @Override
    public String toString() {
        return String.format("Course{ID='%s', Name='%s', Credits=%d, Enrolled=%d/%d, Teacher='%s'}", 
                           courseId, courseName, credits, getCurrentEnrollment(), maxStudents, teacherId);
    }
    
    // CSV format for file storage
    public String toCSV() {
        return String.join(",", 
            courseId, courseName, description, String.valueOf(credits), 
            teacherId != null ? teacherId : "", department, String.valueOf(maxStudents),
            schedule, classroom, semester, String.join(";", enrolledStudents));
    }
    
    // Create Course from CSV
    public static Course fromCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length >= 10) {
            Course course = new Course(
                parts[0], parts[1], parts[2], Integer.parseInt(parts[3]),
                parts[5], Integer.parseInt(parts[6]), parts[7], parts[8], parts[9]
            );
            if (!parts[4].isEmpty()) {
                course.setTeacherId(parts[4]);
            }
            if (parts.length > 10 && !parts[10].isEmpty()) {
                String[] students = parts[10].split(";");
                for (String student : students) {
                    course.enrollStudent(student);
                }
            }
            return course;
        }
        return null;
    }
}