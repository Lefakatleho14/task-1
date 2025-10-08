# Quick Start Guide

## Prerequisites

- Python 3.8 or higher
- Node.js 16 or higher
- npm

## Quick Setup (Automated)

Run the setup script to automatically install all dependencies and initialize the database:

```bash
./setup.sh
```

## Manual Setup

### Backend Setup

1. Create and activate a virtual environment:
```bash
python3 -m venv venv
source venv/bin/activate  # On Windows: venv\Scripts\activate
```

2. Install Python dependencies:
```bash
pip install -r requirements.txt
```

3. Initialize the database:
```bash
python3 init_db.py
```

### Frontend Setup

1. Navigate to frontend directory and install dependencies:
```bash
cd frontend
npm install
```

## Running the Application

### Option 1: Using Start Scripts

**Terminal 1 - Backend:**
```bash
./start_backend.sh
```

**Terminal 2 - Frontend:**
```bash
./start_frontend.sh
```

### Option 2: Manual Start

**Terminal 1 - Backend:**
```bash
source venv/bin/activate  # If using virtual environment
python3 -m uvicorn backend.main:app --host 0.0.0.0 --port 8000 --reload
```

**Terminal 2 - Frontend:**
```bash
cd frontend
npm run dev
```

## Accessing the Application

- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8000
- **API Documentation**: http://localhost:8000/docs

## Default Credentials

### Admin Account
- **Username**: `admin`
- **Password**: `admin123`
- **Role**: Admin (Full access)

### Teacher Account
- **Username**: `teacher1`
- **Password**: `teacher123`
- **Role**: Teacher (Limited access)

## Sample Data

The database initialization script creates:
- 5 sample students (3 in Grade 8, 2 in Grade 7)
- 3 sample teachers (Math, Science, English)
- 4 sample courses
- Attendance records for the past 5 days
- Sample grades for some students

## Features Overview

### Dashboard
- View statistics: Total students, teachers, and courses
- Recent activities
- Quick action buttons

### Students
- Add/Edit/View student records
- Track enrollment details
- Manage parent information
- Filter by grade and section

### Teachers
- Manage teacher profiles
- Track qualifications and specializations
- Employee ID management

### Courses
- Create and manage courses
- Assign teachers to courses
- Set grade levels and sections
- Manage schedules and room assignments

### Attendance
- Mark attendance by student and course
- Filter by student or course
- Multiple status options (Present, Absent, Late, Excused)
- View attendance history

### Grades
- Record assessments (Quiz, Exam, Assignment, Project)
- Track marks and grades
- Filter by student or course
- View grade history

## Troubleshooting

### Backend Issues

**Port already in use:**
```bash
# Find and kill the process using port 8000
lsof -ti:8000 | xargs kill -9
```

**Database errors:**
```bash
# Remove existing database and reinitialize
rm school_management.db
python3 init_db.py
```

### Frontend Issues

**Port already in use:**
```bash
# Frontend will automatically suggest another port
# Or kill the process using port 3000
lsof -ti:3000 | xargs kill -9
```

**Module not found errors:**
```bash
cd frontend
rm -rf node_modules package-lock.json
npm install
```

## Development

### Backend Development

- **Auto-reload**: The backend runs with `--reload` flag, so changes are automatically detected
- **API Documentation**: Available at http://localhost:8000/docs (Swagger UI)
- **Database**: SQLite database file `school_management.db` in root directory

### Frontend Development

- **Hot Reload**: Vite provides hot module replacement (HMR)
- **Components**: Located in `frontend/src/components/`
- **API Service**: Located in `frontend/src/services/api.js`

## Next Steps

1. **Explore the UI**: Login and navigate through different sections
2. **Add Data**: Create new students, teachers, and courses
3. **Test Features**: Mark attendance, add grades, and view reports
4. **Customize**: Modify the code to fit your specific requirements

## Support

For issues or questions:
1. Check the main README.md for detailed information
2. Review the API documentation at http://localhost:8000/docs
3. Examine the database schema in `backend/models.py`

## Security Notes

⚠️ **Important for Production:**
- Change the `SECRET_KEY` in `backend/auth.py`
- Update default passwords
- Use HTTPS in production
- Switch from SQLite to PostgreSQL for better performance
- Implement proper authentication and authorization
- Add rate limiting
- Enable CORS only for trusted domains
