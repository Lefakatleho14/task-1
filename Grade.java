import java.time.LocalDate;

/**
 * Grade entity class representing a student's grade in a specific course
 */
public class Grade {
    private String gradeId;
    private String studentId;
    private String courseId;
    private String assignmentName;
    private double score;
    private double maxScore;
    private String letterGrade;
    private LocalDate dateRecorded;
    private String comments;
    private String gradeType; // "EXAM", "QUIZ", "HOMEWORK", "PROJECT", "PARTICIPATION"
    
    // Constructor
    public Grade(String gradeId, String studentId, String courseId, 
                 String assignmentName, double score, double maxScore, String gradeType) {
        this.gradeId = gradeId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.assignmentName = assignmentName;
        this.score = score;
        this.maxScore = maxScore;
        this.gradeType = gradeType;
        this.dateRecorded = LocalDate.now();
        this.letterGrade = calculateLetterGrade();
    }
    
    // Getters and Setters
    public String getGradeId() { return gradeId; }
    public void setGradeId(String gradeId) { this.gradeId = gradeId; }
    
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    
    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }
    
    public String getAssignmentName() { return assignmentName; }
    public void setAssignmentName(String assignmentName) { this.assignmentName = assignmentName; }
    
    public double getScore() { return score; }
    public void setScore(double score) { 
        this.score = score; 
        this.letterGrade = calculateLetterGrade();
    }
    
    public double getMaxScore() { return maxScore; }
    public void setMaxScore(double maxScore) { 
        this.maxScore = maxScore; 
        this.letterGrade = calculateLetterGrade();
    }
    
    public String getLetterGrade() { return letterGrade; }
    
    public LocalDate getDateRecorded() { return dateRecorded; }
    public void setDateRecorded(LocalDate dateRecorded) { this.dateRecorded = dateRecorded; }
    
    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
    
    public String getGradeType() { return gradeType; }
    public void setGradeType(String gradeType) { this.gradeType = gradeType; }
    
    // Utility methods
    public double getPercentage() {
        return maxScore > 0 ? (score / maxScore) * 100 : 0;
    }
    
    private String calculateLetterGrade() {
        double percentage = getPercentage();
        if (percentage >= 90) return "A";
        else if (percentage >= 80) return "B";
        else if (percentage >= 70) return "C";
        else if (percentage >= 60) return "D";
        else return "F";
    }
    
    public double getGradePoints() {
        switch (letterGrade) {
            case "A": return 4.0;
            case "B": return 3.0;
            case "C": return 2.0;
            case "D": return 1.0;
            case "F": return 0.0;
            default: return 0.0;
        }
    }
    
    @Override
    public String toString() {
        return String.format("Grade{Student='%s', Course='%s', Assignment='%s', Score=%.1f/%.1f (%.1f%%), Grade='%s'}", 
                           studentId, courseId, assignmentName, score, maxScore, getPercentage(), letterGrade);
    }
    
    // CSV format for file storage
    public String toCSV() {
        return String.join(",", 
            gradeId, studentId, courseId, assignmentName, 
            String.valueOf(score), String.valueOf(maxScore), letterGrade,
            dateRecorded.toString(), comments != null ? comments : "", gradeType);
    }
    
    // Create Grade from CSV
    public static Grade fromCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length >= 8) {
            Grade grade = new Grade(
                parts[0], parts[1], parts[2], parts[3],
                Double.parseDouble(parts[4]), Double.parseDouble(parts[5]), 
                parts.length > 9 ? parts[9] : "ASSIGNMENT"
            );
            grade.setDateRecorded(LocalDate.parse(parts[7]));
            if (parts.length > 8 && !parts[8].isEmpty()) {
                grade.setComments(parts[8]);
            }
            return grade;
        }
        return null;
    }
}