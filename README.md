# School Management System

A comprehensive web-based school management system built with Flask, SQLAlchemy, and Bootstrap. This system provides complete functionality for managing students, teachers, classes, attendance, and academic records.

## Features

### 🎓 Student Management
- Add, edit, and view student records
- Student enrollment tracking
- Class assignments
- Contact information management

### 👨‍🏫 Teacher Management
- Teacher profile management
- Subject assignments
- Class assignments
- Contact information tracking

### 🏫 Class Management
- Create and manage classes
- Grade level organization
- Room assignments
- Capacity management
- Teacher assignments

### 📅 Attendance Tracking
- Daily attendance marking
- Class-based attendance
- Present/Absent status tracking
- Date-based filtering

### 📊 Dashboard & Analytics
- Real-time statistics
- Quick action buttons
- System overview
- Activity tracking

### 🔐 Authentication & Security
- User login system
- Role-based access control
- Secure password hashing
- Session management

## Technology Stack

- **Backend**: Python Flask
- **Database**: SQLite (with SQLAlchemy ORM)
- **Frontend**: HTML5, CSS3, JavaScript, Bootstrap 5
- **Authentication**: Flask-Login
- **Forms**: Flask-WTF with WTForms

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
   - Default admin credentials:
     - Username: `admin`
     - Password: `admin123`

## Usage

### Getting Started
1. Login with the default admin credentials
2. Navigate through the dashboard to see system statistics
3. Start adding students, teachers, and classes
4. Mark attendance for classes
5. View and manage records

### Adding Students
1. Go to Students → Add Student
2. Fill in the required information
3. Assign to a class (optional)
4. Save the student record

### Adding Teachers
1. Go to Teachers → Add Teacher
2. Enter teacher details and subject
3. Save the teacher record

### Managing Classes
1. Go to Classes → Add Class
2. Set class name, grade level, and room
3. Assign a teacher (optional)
4. Set capacity limits

### Marking Attendance
1. Go to Attendance
2. Select a class and date
3. Check off present students
4. Save attendance records

## Database Schema

The system uses the following main entities:
- **Users**: Authentication and user management
- **Students**: Student information and records
- **Teachers**: Teacher profiles and assignments
- **Classes**: Class organization and management
- **Courses**: Course definitions and assignments
- **Attendance**: Daily attendance records
- **Grades**: Academic performance tracking

## File Structure

```
school-management-system/
├── app.py                 # Main Flask application
├── requirements.txt       # Python dependencies
├── README.md             # Project documentation
├── templates/            # HTML templates
│   ├── base.html
│   ├── index.html
│   ├── login.html
│   ├── dashboard.html
│   ├── students.html
│   ├── add_student.html
│   ├── teachers.html
│   ├── add_teacher.html
│   ├── classes.html
│   ├── add_class.html
│   └── attendance.html
└── static/               # Static assets
    ├── css/
    │   └── style.css
    └── js/
        └── main.js
```

## Customization

### Adding New Features
1. Create new database models in `app.py`
2. Add corresponding routes and views
3. Create HTML templates in `templates/`
4. Update navigation in `base.html`

### Styling
- Modify `static/css/style.css` for custom styling
- Update Bootstrap classes in templates
- Add custom JavaScript in `static/js/main.js`

## Security Considerations

- Change default admin password
- Use environment variables for sensitive configuration
- Implement proper input validation
- Add CSRF protection for forms
- Use HTTPS in production

## Future Enhancements

- Grade management system
- Report generation
- Email notifications
- Parent portal
- Mobile app integration
- Advanced analytics
- Multi-school support
- API endpoints

## Support

For issues or questions:
1. Check the documentation
2. Review the code comments
3. Test with sample data
4. Verify database connections

## License

This project is open source and available under the MIT License.