import java.util.*;
import java.util.stream.Collectors;

/**
 * CourseManager class for handling course-related operations
 */
public class CourseManager {
    private List<Course> courses;
    private DataManager dataManager;
    
    public CourseManager(DataManager dataManager) {
        this.dataManager = dataManager;
        this.courses = dataManager.loadCourses();
    }
    
    // Add a new course
    public boolean addCourse(Course course) {
        if (findCourseById(course.getCourseId()) != null) {
            return false; // Course with this ID already exists
        }
        courses.add(course);
        saveData();
        return true;
    }
    
    // Remove a course
    public boolean removeCourse(String courseId) {
        Course course = findCourseById(courseId);
        if (course != null) {
            courses.remove(course);
            saveData();
            return true;
        }
        return false;
    }
    
    // Update course information
    public boolean updateCourse(Course updatedCourse) {
        Course existingCourse = findCourseById(updatedCourse.getCourseId());
        if (existingCourse != null) {
            int index = courses.indexOf(existingCourse);
            courses.set(index, updatedCourse);
            saveData();
            return true;
        }
        return false;
    }
    
    // Find course by ID
    public Course findCourseById(String courseId) {
        return courses.stream()
                .filter(c -> c.getCourseId().equals(courseId))
                .findFirst()
                .orElse(null);
    }
    
    // Search courses by name
    public List<Course> searchCoursesByName(String name) {
        String searchTerm = name.toLowerCase();
        return courses.stream()
                .filter(c -> c.getCourseName().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
    }
    
    // Get courses by department
    public List<Course> getCoursesByDepartment(String department) {
        return courses.stream()
                .filter(c -> c.getDepartment().equalsIgnoreCase(department))
                .collect(Collectors.toList());
    }
    
    // Get courses by teacher
    public List<Course> getCoursesByTeacher(String teacherId) {
        return courses.stream()
                .filter(c -> teacherId.equals(c.getTeacherId()))
                .collect(Collectors.toList());
    }
    
    // Get courses by semester
    public List<Course> getCoursesBySemester(String semester) {
        return courses.stream()
                .filter(c -> c.getSemester().equalsIgnoreCase(semester))
                .collect(Collectors.toList());
    }
    
    // Enroll student in course
    public boolean enrollStudentInCourse(String courseId, String studentId) {
        Course course = findCourseById(courseId);
        if (course != null) {
            boolean success = course.enrollStudent(studentId);
            if (success) {
                saveData();
            }
            return success;
        }
        return false;
    }
    
    // Remove student from course
    public boolean removeStudentFromCourse(String courseId, String studentId) {
        Course course = findCourseById(courseId);
        if (course != null) {
            course.removeStudent(studentId);
            saveData();
            return true;
        }
        return false;
    }
    
    // Assign teacher to course
    public boolean assignTeacherToCourse(String courseId, String teacherId) {
        Course course = findCourseById(courseId);
        if (course != null) {
            course.setTeacherId(teacherId);
            saveData();
            return true;
        }
        return false;
    }
    
    // Get available courses (courses with available spots)
    public List<Course> getAvailableCourses() {
        return courses.stream()
                .filter(c -> !c.isFull())
                .collect(Collectors.toList());
    }
    
    // Get full courses
    public List<Course> getFullCourses() {
        return courses.stream()
                .filter(Course::isFull)
                .collect(Collectors.toList());
    }
    
    // Get courses without assigned teacher
    public List<Course> getCoursesWithoutTeacher() {
        return courses.stream()
                .filter(c -> c.getTeacherId() == null || c.getTeacherId().isEmpty())
                .collect(Collectors.toList());
    }
    
    // Get courses with low enrollment (less than 50% capacity)
    public List<Course> getCoursesWithLowEnrollment() {
        return courses.stream()
                .filter(c -> c.getCurrentEnrollment() < c.getMaxStudents() * 0.5)
                .collect(Collectors.toList());
    }
    
    // Get all courses
    public List<Course> getAllCourses() {
        return new ArrayList<>(courses);
    }
    
    // Get courses by credit hours
    public List<Course> getCoursesByCredits(int credits) {
        return courses.stream()
                .filter(c -> c.getCredits() == credits)
                .collect(Collectors.toList());
    }
    
    // Generate course statistics
    public Map<String, Object> getCourseStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalCourses", courses.size());
        stats.put("totalEnrollments", courses.stream()
                .mapToInt(Course::getCurrentEnrollment)
                .sum());
        stats.put("averageEnrollment", courses.stream()
                .mapToInt(Course::getCurrentEnrollment)
                .average()
                .orElse(0.0));
        
        // Department distribution
        Map<String, Long> departmentDistribution = courses.stream()
                .collect(Collectors.groupingBy(Course::getDepartment, Collectors.counting()));
        stats.put("departmentDistribution", departmentDistribution);
        
        // Credit distribution
        Map<Integer, Long> creditDistribution = courses.stream()
                .collect(Collectors.groupingBy(Course::getCredits, Collectors.counting()));
        stats.put("creditDistribution", creditDistribution);
        
        // Enrollment status
        long fullCourses = courses.stream().mapToLong(c -> c.isFull() ? 1 : 0).sum();
        long availableCourses = courses.stream().mapToLong(c -> !c.isFull() ? 1 : 0).sum();
        long lowEnrollment = courses.stream().mapToLong(c -> c.getCurrentEnrollment() < c.getMaxStudents() * 0.5 ? 1 : 0).sum();
        
        Map<String, Long> enrollmentStatus = new HashMap<>();
        enrollmentStatus.put("Full Courses", fullCourses);
        enrollmentStatus.put("Available Courses", availableCourses);
        enrollmentStatus.put("Low Enrollment", lowEnrollment);
        stats.put("enrollmentStatus", enrollmentStatus);
        
        // Semester distribution
        Map<String, Long> semesterDistribution = courses.stream()
                .collect(Collectors.groupingBy(Course::getSemester, Collectors.counting()));
        stats.put("semesterDistribution", semesterDistribution);
        
        return stats;
    }
    
    // Generate next course ID
    public String generateNextCourseId() {
        int maxId = courses.stream()
                .mapToInt(c -> {
                    try {
                        return Integer.parseInt(c.getCourseId().replaceAll("[^0-9]", ""));
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                })
                .max()
                .orElse(0);
        return "CRS" + String.format("%04d", maxId + 1);
    }
    
    private void saveData() {
        dataManager.saveCourses(courses);
    }
}