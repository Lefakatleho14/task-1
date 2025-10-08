# School Management System

A comprehensive web-based school management system built with Flask, SQLAlchemy, and Bootstrap. This system provides complete functionality for managing students, teachers, classes, attendance, grades, and reports.

## Features

### 🎓 Student Management
- Add, view, edit, and delete student records
- Student enrollment tracking
- Class assignments
- Contact information management

### 👨‍🏫 Teacher Management
- Teacher profile management
- Subject assignments
- Class teacher assignments
- Contact information tracking

### 🏫 Class Management
- Create and manage classes
- Grade level organization
- Room assignments
- Capacity management
- Teacher assignments

### 📚 Course Management
- Course creation and management
- Course codes and descriptions
- Credit hour tracking
- Teacher assignments

### 📅 Attendance Tracking
- Daily attendance marking
- Class-based attendance
- Present/absent status tracking
- Attendance history

### 📊 Grade Management
- Grade entry and tracking
- Assignment management
- Grade calculations
- Student performance tracking

### 📈 Reports & Analytics
- Dashboard with key statistics
- Attendance reports
- Student performance analytics
- Export capabilities

## Technology Stack

- **Backend**: Python Flask
- **Database**: SQLite (with SQLAlchemy ORM)
- **Frontend**: HTML5, CSS3, JavaScript, Bootstrap 5
- **Authentication**: Flask-Login
- **Forms**: WTForms with Flask-WTF

## Installation & Setup

1. **Clone or download the project**
   ```bash
   cd /workspace
   ```

2. **Install dependencies**
   ```bash
   pip install -r requirements.txt
   ```

3. **Run the application**
   ```bash
   python app.py
   ```

4. **Access the application**
   - Open your browser and go to `http://localhost:5000`
   - Default admin credentials: `admin` / `admin123`

## Usage

### Getting Started
1. Login with the default admin credentials
2. Navigate through the dashboard to see system overview
3. Start by adding teachers and classes
4. Add students and assign them to classes
5. Create courses and assign teachers
6. Mark daily attendance
7. Enter grades for assignments
8. View reports and analytics

### Key Features

#### Dashboard
- Overview of system statistics
- Quick action buttons
- Recent activity tracking

#### Student Management
- Add new students with complete information
- View student lists with filtering
- Edit student details
- Assign students to classes

#### Teacher Management
- Add teacher profiles
- Assign subjects and classes
- Track teacher information

#### Attendance System
- Select class and date
- Mark present/absent for students
- View attendance history
- Generate attendance reports

#### Grade Management
- Enter grades for assignments
- Calculate percentages automatically
- Track student performance
- View grade history

## Database Schema

The system uses the following main entities:
- **Users**: Authentication and user management
- **Students**: Student information and enrollment
- **Teachers**: Teacher profiles and assignments
- **Classes**: Class organization and management
- **Courses**: Course information and assignments
- **Attendance**: Daily attendance records
- **Grades**: Student grade tracking

## Security Features

- User authentication with password hashing
- Role-based access control
- Session management
- CSRF protection
- Input validation

## Customization

The system is designed to be easily customizable:
- Modify database models in `app.py`
- Update templates in the `templates/` directory
- Customize styling in `static/css/style.css`
- Add new features by extending the Flask routes

## Future Enhancements

- Email notifications
- Advanced reporting with charts
- Mobile app integration
- Parent portal
- Fee management
- Library management
- Transport management
- Multi-school support

## Support

For support or questions about the School Management System, please refer to the code documentation or create an issue in the project repository.

## License

This project is open source and available under the MIT License.