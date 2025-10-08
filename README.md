# School Management System

A comprehensive school management system built with FastAPI (backend) and React (frontend).

## Features

### Core Modules
- **Dashboard**: Overview of school statistics and recent activities
- **Student Management**: Add, edit, view, and manage student records
- **Teacher Management**: Manage teacher profiles and information
- **Course Management**: Create and manage courses with teacher assignments
- **Attendance Tracking**: Mark and track student attendance by course
- **Grade Management**: Record and view student grades and assessments

### User Roles
- **Admin**: Full access to all features
- **Teacher**: Can manage students, mark attendance, and enter grades
- **Student**: View their own records (can be extended)
- **Parent**: View their child's information (can be extended)

## Technology Stack

### Backend
- **FastAPI**: Modern, fast web framework for building APIs
- **SQLAlchemy**: SQL toolkit and ORM
- **SQLite**: Database (can be easily switched to PostgreSQL)
- **JWT**: Authentication with JSON Web Tokens
- **Pydantic**: Data validation using Python type annotations

### Frontend
- **React 18**: JavaScript library for building user interfaces
- **React Router**: Client-side routing
- **React Query**: Data fetching and state management
- **Tailwind CSS**: Utility-first CSS framework
- **Axios**: HTTP client for API requests
- **React Icons**: Icon library

## Installation

### Prerequisites
- Python 3.8 or higher
- Node.js 16 or higher
- npm or yarn

### Backend Setup

1. Create a virtual environment:
```bash
python -m venv venv
source venv/bin/activate  # On Windows: venv\Scripts\activate
```

2. Install dependencies:
```bash
pip install -r requirements.txt
```

3. Run the backend server:
```bash
python run.py
```

The API will be available at `http://localhost:8000`

### Frontend Setup

1. Navigate to the frontend directory:
```bash
cd frontend
```

2. Install dependencies:
```bash
npm install
```

3. Start the development server:
```bash
npm run dev
```

The frontend will be available at `http://localhost:3000`

## Default Credentials

After running the initialization script, you can login with:

- **Username**: `admin`
- **Password**: `admin123`
- **Role**: Admin

## API Documentation

Once the backend is running, you can access:
- **Swagger UI**: http://localhost:8000/docs
- **ReDoc**: http://localhost:8000/redoc

## Project Structure

```
school-management-system/
├── backend/
│   ├── __init__.py
│   ├── main.py          # FastAPI application
│   ├── database.py      # Database configuration
│   ├── models.py        # SQLAlchemy models
│   ├── schemas.py       # Pydantic schemas
│   └── auth.py          # Authentication utilities
├── frontend/
│   ├── public/
│   ├── src/
│   │   ├── components/  # React components
│   │   ├── context/     # React context
│   │   ├── services/    # API services
│   │   ├── App.jsx      # Main application
│   │   └── main.jsx     # Entry point
│   ├── index.html
│   ├── package.json
│   └── vite.config.js
├── requirements.txt     # Python dependencies
├── run.py              # Backend server runner
├── init_db.py          # Database initialization script
└── README.md
```

## Key Features Explained

### Authentication
- JWT-based authentication
- Role-based access control
- Secure password hashing with bcrypt

### Student Management
- Complete student profile management
- Parent information tracking
- Roll number and grade/section assignment
- Enrollment tracking

### Teacher Management
- Teacher profile with qualifications
- Subject specialization
- Employee ID management
- Course assignment tracking

### Course Management
- Course creation with teacher assignment
- Grade level and section association
- Room number and schedule management
- Maximum student capacity

### Attendance System
- Mark attendance by student and course
- Multiple status options (Present, Absent, Late, Excused)
- Date-wise tracking
- Attendance summary reports

### Grade Management
- Multiple assessment types (Quiz, Exam, Assignment, Project)
- Flexible grading system
- Course-wise grade tracking
- Student performance reports

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login
- `GET /api/auth/me` - Get current user

### Students
- `GET /api/students` - List all students
- `POST /api/students` - Create student
- `GET /api/students/{id}` - Get student details
- `PUT /api/students/{id}` - Update student
- `DELETE /api/students/{id}` - Delete student

### Teachers
- `GET /api/teachers` - List all teachers
- `POST /api/teachers` - Create teacher
- `GET /api/teachers/{id}` - Get teacher details
- `PUT /api/teachers/{id}` - Update teacher
- `DELETE /api/teachers/{id}` - Delete teacher

### Courses
- `GET /api/courses` - List all courses
- `POST /api/courses` - Create course
- `GET /api/courses/{id}` - Get course details
- `PUT /api/courses/{id}` - Update course

### Attendance
- `GET /api/attendance` - List attendance records
- `POST /api/attendance` - Mark attendance
- `GET /api/attendance/student/{id}/summary` - Get attendance summary

### Grades
- `GET /api/grades` - List grade records
- `POST /api/grades` - Add grade
- `GET /api/grades/student/{id}/report` - Get student report

### Dashboard
- `GET /api/dashboard/stats` - Get dashboard statistics

## Development

### Running Tests
```bash
# Backend tests (when implemented)
pytest

# Frontend tests (when implemented)
cd frontend
npm test
```

### Building for Production

#### Backend
```bash
# The backend can be deployed using uvicorn or gunicorn
uvicorn backend.main:app --host 0.0.0.0 --port 8000
```

#### Frontend
```bash
cd frontend
npm run build
```

## Future Enhancements

- [ ] Email notifications
- [ ] SMS alerts for attendance
- [ ] Advanced reporting with PDF export
- [ ] Timetable management
- [ ] Fee management
- [ ] Library management
- [ ] Exam scheduling
- [ ] Parent portal
- [ ] Mobile application
- [ ] Real-time notifications
- [ ] Document management
- [ ] Transport management

## License

This project is licensed under the MIT License.

## Support

For issues and questions, please create an issue in the repository.
