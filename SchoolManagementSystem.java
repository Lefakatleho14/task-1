/**
 * Main class for the School Management System
 * 
 * This comprehensive school management system provides functionality for:
 * - Student management (add, update, remove, search students)
 * - Teacher management (manage teacher information and assignments)
 * - Course management (create and manage courses)
 * - Grade management (record and track student grades)
 * - Enrollment management (enroll students in courses)
 * - Reporting and analytics
 * - Data persistence using file-based storage
 * 
 * Features:
 * - Complete CRUD operations for all entities
 * - Grade calculations and GPA tracking
 * - Course enrollment management
 * - Search and filtering capabilities
 * - Statistical reports and analytics
 * - Data backup and restore
 * - Console-based user interface
 * 
 * @author School Management System
 * @version 1.0
 */
public class SchoolManagementSystem {
    
    /**
     * Main method - Entry point of the application
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            // Initialize and start the user interface
            SchoolManagementUI ui = new SchoolManagementUI();
            ui.start();
        } catch (Exception e) {
            System.err.println("An error occurred while running the School Management System:");
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}