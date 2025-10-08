import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * GradeManager class for handling grade-related operations
 */
public class GradeManager {
    private List<Grade> grades;
    private DataManager dataManager;
    
    public GradeManager(DataManager dataManager) {
        this.dataManager = dataManager;
        this.grades = dataManager.loadGrades();
    }
    
    // Add a new grade
    public boolean addGrade(Grade grade) {
        if (findGradeById(grade.getGradeId()) != null) {
            return false; // Grade with this ID already exists
        }
        grades.add(grade);
        saveData();
        return true;
    }
    
    // Remove a grade
    public boolean removeGrade(String gradeId) {
        Grade grade = findGradeById(gradeId);
        if (grade != null) {
            grades.remove(grade);
            saveData();
            return true;
        }
        return false;
    }
    
    // Update grade information
    public boolean updateGrade(Grade updatedGrade) {
        Grade existingGrade = findGradeById(updatedGrade.getGradeId());
        if (existingGrade != null) {
            int index = grades.indexOf(existingGrade);
            grades.set(index, updatedGrade);
            saveData();
            return true;
        }
        return false;
    }
    
    // Find grade by ID
    public Grade findGradeById(String gradeId) {
        return grades.stream()
                .filter(g -> g.getGradeId().equals(gradeId))
                .findFirst()
                .orElse(null);
    }
    
    // Get grades by student
    public List<Grade> getGradesByStudent(String studentId) {
        return grades.stream()
                .filter(g -> g.getStudentId().equals(studentId))
                .sorted((g1, g2) -> g2.getDateRecorded().compareTo(g1.getDateRecorded()))
                .collect(Collectors.toList());
    }
    
    // Get grades by course
    public List<Grade> getGradesByCourse(String courseId) {
        return grades.stream()
                .filter(g -> g.getCourseId().equals(courseId))
                .sorted((g1, g2) -> g2.getDateRecorded().compareTo(g1.getDateRecorded()))
                .collect(Collectors.toList());
    }
    
    // Get grades by student and course
    public List<Grade> getGradesByStudentAndCourse(String studentId, String courseId) {
        return grades.stream()
                .filter(g -> g.getStudentId().equals(studentId) && g.getCourseId().equals(courseId))
                .sorted((g1, g2) -> g2.getDateRecorded().compareTo(g1.getDateRecorded()))
                .collect(Collectors.toList());
    }
    
    // Get grades by type (EXAM, QUIZ, etc.)
    public List<Grade> getGradesByType(String gradeType) {
        return grades.stream()
                .filter(g -> g.getGradeType().equalsIgnoreCase(gradeType))
                .collect(Collectors.toList());
    }
    
    // Calculate student's average in a course
    public double calculateStudentCourseAverage(String studentId, String courseId) {
        List<Grade> studentCourseGrades = getGradesByStudentAndCourse(studentId, courseId);
        return studentCourseGrades.stream()
                .mapToDouble(Grade::getPercentage)
                .average()
                .orElse(0.0);
    }
    
    // Calculate course average
    public double calculateCourseAverage(String courseId) {
        List<Grade> courseGrades = getGradesByCourse(courseId);
        return courseGrades.stream()
                .mapToDouble(Grade::getPercentage)
                .average()
                .orElse(0.0);
    }
    
    // Get student's overall GPA
    public double calculateStudentGPA(String studentId) {
        List<Grade> studentGrades = getGradesByStudent(studentId);
        
        // Group by course and calculate course averages
        Map<String, List<Grade>> gradesByCourse = studentGrades.stream()
                .collect(Collectors.groupingBy(Grade::getCourseId));
        
        double totalGradePoints = 0.0;
        int totalCourses = 0;
        
        for (Map.Entry<String, List<Grade>> entry : gradesByCourse.entrySet()) {
            double courseAverage = entry.getValue().stream()
                    .mapToDouble(Grade::getPercentage)
                    .average()
                    .orElse(0.0);
            
            totalGradePoints += percentageToGradePoints(courseAverage);
            totalCourses++;
        }
        
        return totalCourses > 0 ? totalGradePoints / totalCourses : 0.0;
    }
    
    private double percentageToGradePoints(double percentage) {
        if (percentage >= 90) return 4.0;
        else if (percentage >= 80) return 3.0;
        else if (percentage >= 70) return 2.0;
        else if (percentage >= 60) return 1.0;
        else return 0.0;
    }
    
    // Get failing grades (below 60%)
    public List<Grade> getFailingGrades() {
        return grades.stream()
                .filter(g -> g.getPercentage() < 60)
                .collect(Collectors.toList());
    }
    
    // Get excellent grades (90% and above)
    public List<Grade> getExcellentGrades() {
        return grades.stream()
                .filter(g -> g.getPercentage() >= 90)
                .collect(Collectors.toList());
    }
    
    // Get recent grades (within last 30 days)
    public List<Grade> getRecentGrades() {
        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
        return grades.stream()
                .filter(g -> g.getDateRecorded().isAfter(thirtyDaysAgo))
                .sorted((g1, g2) -> g2.getDateRecorded().compareTo(g1.getDateRecorded()))
                .collect(Collectors.toList());
    }
    
    // Get all grades
    public List<Grade> getAllGrades() {
        return new ArrayList<>(grades);
    }
    
    // Generate grade report for a student
    public Map<String, Object> generateStudentGradeReport(String studentId) {
        Map<String, Object> report = new HashMap<>();
        List<Grade> studentGrades = getGradesByStudent(studentId);
        
        report.put("studentId", studentId);
        report.put("totalGrades", studentGrades.size());
        report.put("overallGPA", calculateStudentGPA(studentId));
        
        // Grades by course
        Map<String, List<Grade>> gradesByCourse = studentGrades.stream()
                .collect(Collectors.groupingBy(Grade::getCourseId));
        
        Map<String, Double> courseAverages = new HashMap<>();
        for (Map.Entry<String, List<Grade>> entry : gradesByCourse.entrySet()) {
            double average = entry.getValue().stream()
                    .mapToDouble(Grade::getPercentage)
                    .average()
                    .orElse(0.0);
            courseAverages.put(entry.getKey(), average);
        }
        report.put("courseAverages", courseAverages);
        
        // Grade distribution
        Map<String, Long> gradeDistribution = studentGrades.stream()
                .collect(Collectors.groupingBy(Grade::getLetterGrade, Collectors.counting()));
        report.put("gradeDistribution", gradeDistribution);
        
        return report;
    }
    
    // Generate course grade report
    public Map<String, Object> generateCourseGradeReport(String courseId) {
        Map<String, Object> report = new HashMap<>();
        List<Grade> courseGrades = getGradesByCourse(courseId);
        
        report.put("courseId", courseId);
        report.put("totalGrades", courseGrades.size());
        report.put("courseAverage", calculateCourseAverage(courseId));
        
        // Grade statistics
        DoubleSummaryStatistics stats = courseGrades.stream()
                .mapToDouble(Grade::getPercentage)
                .summaryStatistics();
        
        report.put("highestGrade", stats.getMax());
        report.put("lowestGrade", stats.getMin());
        report.put("averageGrade", stats.getAverage());
        
        // Grade distribution
        Map<String, Long> gradeDistribution = courseGrades.stream()
                .collect(Collectors.groupingBy(Grade::getLetterGrade, Collectors.counting()));
        report.put("gradeDistribution", gradeDistribution);
        
        // Students by performance
        long excellentCount = courseGrades.stream().mapToLong(g -> g.getPercentage() >= 90 ? 1 : 0).sum();
        long goodCount = courseGrades.stream().mapToLong(g -> g.getPercentage() >= 80 && g.getPercentage() < 90 ? 1 : 0).sum();
        long averageCount = courseGrades.stream().mapToLong(g -> g.getPercentage() >= 70 && g.getPercentage() < 80 ? 1 : 0).sum();
        long belowAverageCount = courseGrades.stream().mapToLong(g -> g.getPercentage() >= 60 && g.getPercentage() < 70 ? 1 : 0).sum();
        long failingCount = courseGrades.stream().mapToLong(g -> g.getPercentage() < 60 ? 1 : 0).sum();
        
        Map<String, Long> performanceDistribution = new HashMap<>();
        performanceDistribution.put("Excellent (90-100%)", excellentCount);
        performanceDistribution.put("Good (80-89%)", goodCount);
        performanceDistribution.put("Average (70-79%)", averageCount);
        performanceDistribution.put("Below Average (60-69%)", belowAverageCount);
        performanceDistribution.put("Failing (<60%)", failingCount);
        report.put("performanceDistribution", performanceDistribution);
        
        return report;
    }
    
    // Generate next grade ID
    public String generateNextGradeId() {
        int maxId = grades.stream()
                .mapToInt(g -> {
                    try {
                        return Integer.parseInt(g.getGradeId().replaceAll("[^0-9]", ""));
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                })
                .max()
                .orElse(0);
        return "GRD" + String.format("%06d", maxId + 1);
    }
    
    private void saveData() {
        dataManager.saveGrades(grades);
    }
}