import java.io.*;
import java.util.*;

/**
 * DataManager class for handling file-based data persistence
 */
public class DataManager {
    private static final String DATA_DIR = "data/";
    private static final String STUDENTS_FILE = DATA_DIR + "students.csv";
    private static final String TEACHERS_FILE = DATA_DIR + "teachers.csv";
    private static final String COURSES_FILE = DATA_DIR + "courses.csv";
    private static final String GRADES_FILE = DATA_DIR + "grades.csv";
    
    public DataManager() {
        createDataDirectory();
    }
    
    private void createDataDirectory() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
    
    // Generic method to save data to CSV file
    private void saveToFile(String filename, List<String> lines) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (String line : lines) {
                writer.println(line);
            }
        } catch (IOException e) {
            System.err.println("Error saving to file " + filename + ": " + e.getMessage());
        }
    }
    
    // Generic method to load data from CSV file
    private List<String> loadFromFile(String filename) {
        List<String> lines = new ArrayList<>();
        File file = new File(filename);
        if (!file.exists()) {
            return lines; // Return empty list if file doesn't exist
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error loading from file " + filename + ": " + e.getMessage());
        }
        return lines;
    }
    
    // Student data methods
    public void saveStudents(List<Student> students) {
        List<String> lines = new ArrayList<>();
        for (Student student : students) {
            lines.add(student.toCSV());
        }
        saveToFile(STUDENTS_FILE, lines);
    }
    
    public List<Student> loadStudents() {
        List<Student> students = new ArrayList<>();
        List<String> lines = loadFromFile(STUDENTS_FILE);
        for (String line : lines) {
            Student student = Student.fromCSV(line);
            if (student != null) {
                students.add(student);
            }
        }
        return students;
    }
    
    // Teacher data methods
    public void saveTeachers(List<Teacher> teachers) {
        List<String> lines = new ArrayList<>();
        for (Teacher teacher : teachers) {
            lines.add(teacher.toCSV());
        }
        saveToFile(TEACHERS_FILE, lines);
    }
    
    public List<Teacher> loadTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        List<String> lines = loadFromFile(TEACHERS_FILE);
        for (String line : lines) {
            Teacher teacher = Teacher.fromCSV(line);
            if (teacher != null) {
                teachers.add(teacher);
            }
        }
        return teachers;
    }
    
    // Course data methods
    public void saveCourses(List<Course> courses) {
        List<String> lines = new ArrayList<>();
        for (Course course : courses) {
            lines.add(course.toCSV());
        }
        saveToFile(COURSES_FILE, lines);
    }
    
    public List<Course> loadCourses() {
        List<Course> courses = new ArrayList<>();
        List<String> lines = loadFromFile(COURSES_FILE);
        for (String line : lines) {
            Course course = Course.fromCSV(line);
            if (course != null) {
                courses.add(course);
            }
        }
        return courses;
    }
    
    // Grade data methods
    public void saveGrades(List<Grade> grades) {
        List<String> lines = new ArrayList<>();
        for (Grade grade : grades) {
            lines.add(grade.toCSV());
        }
        saveToFile(GRADES_FILE, lines);
    }
    
    public List<Grade> loadGrades() {
        List<Grade> grades = new ArrayList<>();
        List<String> lines = loadFromFile(GRADES_FILE);
        for (String line : lines) {
            Grade grade = Grade.fromCSV(line);
            if (grade != null) {
                grades.add(grade);
            }
        }
        return grades;
    }
    
    // Backup methods
    public void createBackup() {
        String backupDir = DATA_DIR + "backup_" + System.currentTimeMillis() + "/";
        File dir = new File(backupDir);
        dir.mkdirs();
        
        try {
            copyFile(STUDENTS_FILE, backupDir + "students.csv");
            copyFile(TEACHERS_FILE, backupDir + "teachers.csv");
            copyFile(COURSES_FILE, backupDir + "courses.csv");
            copyFile(GRADES_FILE, backupDir + "grades.csv");
            System.out.println("Backup created successfully in: " + backupDir);
        } catch (IOException e) {
            System.err.println("Error creating backup: " + e.getMessage());
        }
    }
    
    private void copyFile(String source, String destination) throws IOException {
        File sourceFile = new File(source);
        if (!sourceFile.exists()) return;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(source));
             PrintWriter writer = new PrintWriter(new FileWriter(destination))) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.println(line);
            }
        }
    }
}