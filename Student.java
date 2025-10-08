import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Student entity class representing a student in the school management system
 */
public class Student {
    private String studentId;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String email;
    private String phoneNumber;
    private String address;
    private LocalDate enrollmentDate;
    private String grade; // Grade level (e.g., "9th", "10th", etc.)
    private List<String> enrolledCourses;
    private double gpa;
    
    // Constructor
    public Student(String studentId, String firstName, String lastName, 
                   LocalDate dateOfBirth, String email, String phoneNumber, 
                   String address, String grade) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.grade = grade;
        this.enrollmentDate = LocalDate.now();
        this.enrolledCourses = new ArrayList<>();
        this.gpa = 0.0;
    }
    
    // Getters and Setters
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getFullName() { return firstName + " " + lastName; }
    
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public LocalDate getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(LocalDate enrollmentDate) { this.enrollmentDate = enrollmentDate; }
    
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
    
    public List<String> getEnrolledCourses() { return enrolledCourses; }
    public void setEnrolledCourses(List<String> enrolledCourses) { this.enrolledCourses = enrolledCourses; }
    
    public double getGpa() { return gpa; }
    public void setGpa(double gpa) { this.gpa = gpa; }
    
    // Utility methods
    public void addCourse(String courseId) {
        if (!enrolledCourses.contains(courseId)) {
            enrolledCourses.add(courseId);
        }
    }
    
    public void removeCourse(String courseId) {
        enrolledCourses.remove(courseId);
    }
    
    public int getAge() {
        return LocalDate.now().getYear() - dateOfBirth.getYear();
    }
    
    @Override
    public String toString() {
        return String.format("Student{ID='%s', Name='%s', Grade='%s', GPA=%.2f, Courses=%d}", 
                           studentId, getFullName(), grade, gpa, enrolledCourses.size());
    }
    
    // CSV format for file storage
    public String toCSV() {
        return String.join(",", 
            studentId, firstName, lastName, dateOfBirth.toString(), 
            email, phoneNumber, address, grade, enrollmentDate.toString(), 
            String.valueOf(gpa), String.join(";", enrolledCourses));
    }
    
    // Create Student from CSV
    public static Student fromCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length >= 10) {
            Student student = new Student(
                parts[0], parts[1], parts[2], LocalDate.parse(parts[3]),
                parts[4], parts[5], parts[6], parts[7]
            );
            student.setEnrollmentDate(LocalDate.parse(parts[8]));
            student.setGpa(Double.parseDouble(parts[9]));
            if (parts.length > 10 && !parts[10].isEmpty()) {
                String[] courses = parts[10].split(";");
                for (String course : courses) {
                    student.addCourse(course);
                }
            }
            return student;
        }
        return null;
    }
}