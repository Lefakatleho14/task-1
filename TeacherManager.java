import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * TeacherManager class for handling teacher-related operations
 */
public class TeacherManager {
    private List<Teacher> teachers;
    private DataManager dataManager;
    
    public TeacherManager(DataManager dataManager) {
        this.dataManager = dataManager;
        this.teachers = dataManager.loadTeachers();
    }
    
    // Add a new teacher
    public boolean addTeacher(Teacher teacher) {
        if (findTeacherById(teacher.getTeacherId()) != null) {
            return false; // Teacher with this ID already exists
        }
        teachers.add(teacher);
        saveData();
        return true;
    }
    
    // Remove a teacher
    public boolean removeTeacher(String teacherId) {
        Teacher teacher = findTeacherById(teacherId);
        if (teacher != null) {
            teachers.remove(teacher);
            saveData();
            return true;
        }
        return false;
    }
    
    // Update teacher information
    public boolean updateTeacher(Teacher updatedTeacher) {
        Teacher existingTeacher = findTeacherById(updatedTeacher.getTeacherId());
        if (existingTeacher != null) {
            int index = teachers.indexOf(existingTeacher);
            teachers.set(index, updatedTeacher);
            saveData();
            return true;
        }
        return false;
    }
    
    // Find teacher by ID
    public Teacher findTeacherById(String teacherId) {
        return teachers.stream()
                .filter(t -> t.getTeacherId().equals(teacherId))
                .findFirst()
                .orElse(null);
    }
    
    // Search teachers by name
    public List<Teacher> searchTeachersByName(String name) {
        String searchTerm = name.toLowerCase();
        return teachers.stream()
                .filter(t -> t.getFullName().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
    }
    
    // Get teachers by department
    public List<Teacher> getTeachersByDepartment(String department) {
        return teachers.stream()
                .filter(t -> t.getDepartment().equalsIgnoreCase(department))
                .collect(Collectors.toList());
    }
    
    // Get teachers by subject
    public List<Teacher> getTeachersBySubject(String subject) {
        return teachers.stream()
                .filter(t -> t.getSubject().equalsIgnoreCase(subject))
                .collect(Collectors.toList());
    }
    
    // Assign course to teacher
    public boolean assignCourseToTeacher(String teacherId, String courseId) {
        Teacher teacher = findTeacherById(teacherId);
        if (teacher != null) {
            teacher.assignCourse(courseId);
            saveData();
            return true;
        }
        return false;
    }
    
    // Remove course from teacher
    public boolean removeCourseFromTeacher(String teacherId, String courseId) {
        Teacher teacher = findTeacherById(teacherId);
        if (teacher != null) {
            teacher.removeCourse(courseId);
            saveData();
            return true;
        }
        return false;
    }
    
    // Get available teachers (teachers with less than maximum course load)
    public List<Teacher> getAvailableTeachers(int maxCourseLoad) {
        return teachers.stream()
                .filter(t -> t.getAssignedCourses().size() < maxCourseLoad)
                .collect(Collectors.toList());
    }
    
    // Get teachers with heavy workload
    public List<Teacher> getOverloadedTeachers(int maxCourseLoad) {
        return teachers.stream()
                .filter(t -> t.getAssignedCourses().size() > maxCourseLoad)
                .sorted((t1, t2) -> Integer.compare(t2.getAssignedCourses().size(), t1.getAssignedCourses().size()))
                .collect(Collectors.toList());
    }
    
    // Get all teachers
    public List<Teacher> getAllTeachers() {
        return new ArrayList<>(teachers);
    }
    
    // Get teachers by years of service
    public List<Teacher> getTeachersByExperience(int minYears) {
        return teachers.stream()
                .filter(t -> t.getYearsOfService() >= minYears)
                .sorted((t1, t2) -> Integer.compare(t2.getYearsOfService(), t1.getYearsOfService()))
                .collect(Collectors.toList());
    }
    
    // Calculate average salary by department
    public Map<String, Double> getAverageSalaryByDepartment() {
        return teachers.stream()
                .collect(Collectors.groupingBy(
                    Teacher::getDepartment,
                    Collectors.averagingDouble(Teacher::getSalary)
                ));
    }
    
    // Generate teacher statistics
    public Map<String, Object> getTeacherStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalTeachers", teachers.size());
        stats.put("averageSalary", teachers.stream()
                .mapToDouble(Teacher::getSalary)
                .average()
                .orElse(0.0));
        stats.put("averageExperience", teachers.stream()
                .mapToInt(Teacher::getYearsOfService)
                .average()
                .orElse(0.0));
        
        // Department distribution
        Map<String, Long> departmentDistribution = teachers.stream()
                .collect(Collectors.groupingBy(Teacher::getDepartment, Collectors.counting()));
        stats.put("departmentDistribution", departmentDistribution);
        
        // Subject distribution
        Map<String, Long> subjectDistribution = teachers.stream()
                .collect(Collectors.groupingBy(Teacher::getSubject, Collectors.counting()));
        stats.put("subjectDistribution", subjectDistribution);
        
        // Course load distribution
        Map<String, Long> courseLoadDistribution = new HashMap<>();
        long light = teachers.stream().mapToLong(t -> t.getAssignedCourses().size() <= 2 ? 1 : 0).sum();
        long medium = teachers.stream().mapToLong(t -> t.getAssignedCourses().size() >= 3 && t.getAssignedCourses().size() <= 4 ? 1 : 0).sum();
        long heavy = teachers.stream().mapToLong(t -> t.getAssignedCourses().size() >= 5 ? 1 : 0).sum();
        
        courseLoadDistribution.put("Light (1-2 courses)", light);
        courseLoadDistribution.put("Medium (3-4 courses)", medium);
        courseLoadDistribution.put("Heavy (5+ courses)", heavy);
        stats.put("courseLoadDistribution", courseLoadDistribution);
        
        return stats;
    }
    
    // Generate next teacher ID
    public String generateNextTeacherId() {
        int maxId = teachers.stream()
                .mapToInt(t -> {
                    try {
                        return Integer.parseInt(t.getTeacherId().replaceAll("[^0-9]", ""));
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                })
                .max()
                .orElse(0);
        return "TCH" + String.format("%04d", maxId + 1);
    }
    
    private void saveData() {
        dataManager.saveTeachers(teachers);
    }
}