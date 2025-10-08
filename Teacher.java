import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Teacher entity class representing a teacher in the school management system
 */
public class Teacher {
    private String teacherId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String department;
    private String subject;
    private LocalDate hireDate;
    private double salary;
    private List<String> assignedCourses;
    private String qualification;
    
    // Constructor
    public Teacher(String teacherId, String firstName, String lastName, 
                   String email, String phoneNumber, String department, 
                   String subject, double salary, String qualification) {
        this.teacherId = teacherId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.department = department;
        this.subject = subject;
        this.salary = salary;
        this.qualification = qualification;
        this.hireDate = LocalDate.now();
        this.assignedCourses = new ArrayList<>();
    }
    
    // Getters and Setters
    public String getTeacherId() { return teacherId; }
    public void setTeacherId(String teacherId) { this.teacherId = teacherId; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getFullName() { return firstName + " " + lastName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    
    public LocalDate getHireDate() { return hireDate; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }
    
    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }
    
    public List<String> getAssignedCourses() { return assignedCourses; }
    public void setAssignedCourses(List<String> assignedCourses) { this.assignedCourses = assignedCourses; }
    
    public String getQualification() { return qualification; }
    public void setQualification(String qualification) { this.qualification = qualification; }
    
    // Utility methods
    public void assignCourse(String courseId) {
        if (!assignedCourses.contains(courseId)) {
            assignedCourses.add(courseId);
        }
    }
    
    public void removeCourse(String courseId) {
        assignedCourses.remove(courseId);
    }
    
    public int getYearsOfService() {
        return LocalDate.now().getYear() - hireDate.getYear();
    }
    
    @Override
    public String toString() {
        return String.format("Teacher{ID='%s', Name='%s', Subject='%s', Department='%s', Courses=%d}", 
                           teacherId, getFullName(), subject, department, assignedCourses.size());
    }
    
    // CSV format for file storage
    public String toCSV() {
        return String.join(",", 
            teacherId, firstName, lastName, email, phoneNumber, 
            department, subject, hireDate.toString(), String.valueOf(salary), 
            qualification, String.join(";", assignedCourses));
    }
    
    // Create Teacher from CSV
    public static Teacher fromCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length >= 10) {
            Teacher teacher = new Teacher(
                parts[0], parts[1], parts[2], parts[3], parts[4],
                parts[5], parts[6], Double.parseDouble(parts[8]), parts[9]
            );
            teacher.setHireDate(LocalDate.parse(parts[7]));
            if (parts.length > 10 && !parts[10].isEmpty()) {
                String[] courses = parts[10].split(";");
                for (String course : courses) {
                    teacher.assignCourse(course);
                }
            }
            return teacher;
        }
        return null;
    }
}