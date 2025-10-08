# School Management System

A comprehensive Java-based school management system that provides complete functionality for managing students, teachers, courses, and grades in an educational institution.

## Features

### Core Functionality
- **Student Management**: Add, update, remove, and search students
- **Teacher Management**: Manage teacher information and course assignments
- **Course Management**: Create and manage courses with enrollment tracking
- **Grade Management**: Record and track student grades with automatic GPA calculation
- **Enrollment Management**: Enroll students in courses with capacity management

### Advanced Features
- **Grade Calculations**: Automatic GPA calculation and grade statistics
- **Search & Filter**: Advanced search capabilities across all entities
- **Reports & Analytics**: Comprehensive reporting and statistical analysis
- **Data Persistence**: File-based data storage with backup functionality
- **User-Friendly Interface**: Console-based menu system with intuitive navigation

## System Architecture

### Core Entities
- `Student.java` - Student information and enrollment tracking
- `Teacher.java` - Teacher details and course assignments
- `Course.java` - Course information and student enrollment
- `Grade.java` - Individual grade records with calculations

### Manager Classes
- `StudentManager.java` - Student operations and GPA calculations
- `TeacherManager.java` - Teacher management and workload tracking
- `CourseManager.java` - Course operations and enrollment management
- `GradeManager.java` - Grade operations and statistical analysis
- `DataManager.java` - File-based data persistence layer

### User Interface
- `SchoolManagementUI.java` - Console-based user interface
- `SchoolManagementSystem.java` - Main application entry point

### Utilities
- `SampleDataGenerator.java` - Generate sample data for testing

## Getting Started

### Prerequisites
- Java 8 or higher
- Command line interface

### Installation & Setup

1. **Compile the system:**
   ```bash
   javac *.java
   ```

2. **Generate sample data (optional):**
   ```bash
   java SampleDataGenerator
   ```

3. **Run the main application:**
   ```bash
   java SchoolManagementSystem
   ```

## Usage Guide

### Main Menu Options

1. **Student Management**
   - Add new students with personal information
   - View all students with GPA and enrollment status
   - Search students by name or ID
   - Update student information
   - View honor roll and at-risk students

2. **Teacher Management**
   - Add new teachers with qualifications
   - Manage teacher assignments and workload
   - View teachers by department or subject
   - Track teaching experience and salary information

3. **Course Management**
   - Create new courses with schedules and capacity
   - Assign teachers to courses
   - Monitor enrollment status and availability
   - Manage course prerequisites and descriptions

4. **Grade Management**
   - Record grades for assignments, exams, and projects
   - Automatic letter grade and GPA calculation
   - View grade statistics and trends
   - Generate grade reports for students and courses

5. **Reports & Analytics**
   - Student performance reports
   - Course enrollment statistics
   - Teacher workload analysis
   - Grade distribution reports

### Sample Data

The system includes a sample data generator that creates:
- 50 sample students across different grade levels
- 15 teachers from various departments
- 22 courses with realistic schedules
- Hundreds of grade records with realistic distributions
- Proper enrollment relationships between students and courses

## Data Storage

The system uses CSV files for data persistence:
- `data/students.csv` - Student records
- `data/teachers.csv` - Teacher information
- `data/courses.csv` - Course details
- `data/grades.csv` - Grade records

### Backup System
- Automatic backup creation with timestamps
- Data integrity verification
- Easy restore functionality

## Key Features Explained

### GPA Calculation
- Automatic calculation based on course grades
- Weighted by course credits
- Real-time updates when grades are modified
- Support for different grading scales

### Enrollment Management
- Course capacity tracking
- Prerequisite checking
- Schedule conflict detection
- Waitlist functionality

### Search & Filtering
- Multi-criteria search across all entities
- Performance-based filtering (honor roll, at-risk students)
- Department and subject-based organization
- Advanced reporting queries

### Grade Analytics
- Course performance statistics
- Student progress tracking
- Grade distribution analysis
- Trend identification

## System Statistics

After generating sample data, you'll have:
- **Students**: 50 students across grades 9-12
- **Teachers**: 15 teachers across 8 departments
- **Courses**: 22 courses with realistic enrollment
- **Grades**: 1000+ grade records
- **Enrollments**: 200+ student-course enrollments
- **Average GPA**: Realistic distribution around 2.8-3.2

## Technical Details

### Design Patterns Used
- **Manager Pattern**: Separate business logic from data entities
- **Data Access Object**: Centralized data persistence
- **Model-View-Controller**: Separation of concerns
- **Factory Pattern**: ID generation for entities

### Data Validation
- Input validation for all user entries
- Date format validation
- Numeric range checking
- Duplicate ID prevention

### Error Handling
- Comprehensive exception handling
- User-friendly error messages
- Data integrity checks
- Graceful failure recovery

## Future Enhancements

Potential areas for expansion:
- Web-based user interface
- Database integration (MySQL, PostgreSQL)
- Email notification system
- Advanced scheduling algorithms
- Mobile application support
- Integration with external systems

## Contributing

This system is designed to be easily extensible. Key areas for contribution:
- Additional report types
- Enhanced search capabilities
- New entity types (e.g., Departments, Semesters)
- Improved user interface
- Performance optimizations

## License

This project is developed for educational purposes and is available for use and modification.

---

**School Management System v1.0**  
*A comprehensive solution for educational institution management*
