"""
Database initialization script.
Creates sample data for testing the School Management System.
"""
from datetime import date, timedelta
from backend.database import engine, SessionLocal, Base
from backend.models import User, Student, Teacher, Course, Attendance, Grade
from backend.auth import get_password_hash

def init_database():
    # Create all tables
    Base.metadata.create_all(bind=engine)
    
    db = SessionLocal()
    
    try:
        # Check if admin already exists
        existing_admin = db.query(User).filter(User.username == "admin").first()
        if existing_admin:
            print("Database already initialized. Skipping...")
            return
        
        # Create admin user
        admin_user = User(
            username="admin",
            email="admin@school.com",
            hashed_password=get_password_hash("admin123"),
            role="admin",
            is_active=True
        )
        db.add(admin_user)
        db.flush()
        
        # Create teacher user
        teacher_user = User(
            username="teacher1",
            email="teacher1@school.com",
            hashed_password=get_password_hash("teacher123"),
            role="teacher",
            is_active=True
        )
        db.add(teacher_user)
        db.flush()
        
        # Create teachers
        teachers_data = [
            {
                "user_id": teacher_user.id,
                "first_name": "John",
                "last_name": "Smith",
                "date_of_birth": date(1985, 3, 15),
                "gender": "male",
                "address": "123 Main St, City",
                "phone": "+1234567890",
                "email": "john.smith@school.com",
                "subject_specialization": "Mathematics",
                "qualification": "M.Sc in Mathematics",
                "hire_date": date(2015, 8, 1),
                "employee_id": "T001"
            },
            {
                "first_name": "Sarah",
                "last_name": "Johnson",
                "date_of_birth": date(1988, 7, 20),
                "gender": "female",
                "address": "456 Oak Ave, City",
                "phone": "+1234567891",
                "email": "sarah.johnson@school.com",
                "subject_specialization": "Science",
                "qualification": "M.Sc in Physics",
                "hire_date": date(2016, 8, 1),
                "employee_id": "T002"
            },
            {
                "first_name": "Michael",
                "last_name": "Brown",
                "date_of_birth": date(1982, 11, 10),
                "gender": "male",
                "address": "789 Pine Rd, City",
                "phone": "+1234567892",
                "email": "michael.brown@school.com",
                "subject_specialization": "English",
                "qualification": "M.A in English Literature",
                "hire_date": date(2014, 8, 1),
                "employee_id": "T003"
            }
        ]
        
        teachers = []
        for teacher_data in teachers_data:
            teacher = Teacher(**teacher_data)
            db.add(teacher)
            teachers.append(teacher)
        
        db.flush()
        
        # Create students
        students_data = [
            {
                "first_name": "Emma",
                "last_name": "Wilson",
                "date_of_birth": date(2010, 5, 12),
                "gender": "female",
                "address": "101 Elm St, City",
                "phone": "+1234560001",
                "email": "emma.wilson@student.com",
                "parent_name": "Robert Wilson",
                "parent_phone": "+1234560010",
                "parent_email": "robert.wilson@email.com",
                "enrollment_date": date(2022, 8, 15),
                "grade_level": 8,
                "section": "A",
                "roll_number": "2022001"
            },
            {
                "first_name": "Liam",
                "last_name": "Davis",
                "date_of_birth": date(2010, 8, 25),
                "gender": "male",
                "address": "202 Maple Dr, City",
                "phone": "+1234560002",
                "email": "liam.davis@student.com",
                "parent_name": "Jennifer Davis",
                "parent_phone": "+1234560020",
                "parent_email": "jennifer.davis@email.com",
                "enrollment_date": date(2022, 8, 15),
                "grade_level": 8,
                "section": "A",
                "roll_number": "2022002"
            },
            {
                "first_name": "Olivia",
                "last_name": "Martinez",
                "date_of_birth": date(2010, 3, 8),
                "gender": "female",
                "address": "303 Cedar Ln, City",
                "phone": "+1234560003",
                "email": "olivia.martinez@student.com",
                "parent_name": "Carlos Martinez",
                "parent_phone": "+1234560030",
                "parent_email": "carlos.martinez@email.com",
                "enrollment_date": date(2022, 8, 15),
                "grade_level": 8,
                "section": "A",
                "roll_number": "2022003"
            },
            {
                "first_name": "Noah",
                "last_name": "Anderson",
                "date_of_birth": date(2011, 1, 18),
                "gender": "male",
                "address": "404 Birch St, City",
                "phone": "+1234560004",
                "email": "noah.anderson@student.com",
                "parent_name": "Lisa Anderson",
                "parent_phone": "+1234560040",
                "parent_email": "lisa.anderson@email.com",
                "enrollment_date": date(2023, 8, 15),
                "grade_level": 7,
                "section": "A",
                "roll_number": "2023001"
            },
            {
                "first_name": "Ava",
                "last_name": "Taylor",
                "date_of_birth": date(2011, 6, 22),
                "gender": "female",
                "address": "505 Spruce Ave, City",
                "phone": "+1234560005",
                "email": "ava.taylor@student.com",
                "parent_name": "Mark Taylor",
                "parent_phone": "+1234560050",
                "parent_email": "mark.taylor@email.com",
                "enrollment_date": date(2023, 8, 15),
                "grade_level": 7,
                "section": "A",
                "roll_number": "2023002"
            }
        ]
        
        students = []
        for student_data in students_data:
            student = Student(**student_data)
            db.add(student)
            students.append(student)
        
        db.flush()
        
        # Create courses
        courses_data = [
            {
                "course_name": "Mathematics Grade 8",
                "course_code": "MATH8A",
                "description": "Algebra, Geometry, and Statistics",
                "teacher_id": teachers[0].id,
                "grade_level": 8,
                "section": "A",
                "schedule": "Mon/Wed/Fri 9:00-10:00",
                "room_number": "101",
                "max_students": 30
            },
            {
                "course_name": "Science Grade 8",
                "course_code": "SCI8A",
                "description": "Physics, Chemistry, and Biology",
                "teacher_id": teachers[1].id,
                "grade_level": 8,
                "section": "A",
                "schedule": "Tue/Thu 10:00-11:00",
                "room_number": "102",
                "max_students": 30
            },
            {
                "course_name": "English Grade 8",
                "course_code": "ENG8A",
                "description": "Literature and Grammar",
                "teacher_id": teachers[2].id,
                "grade_level": 8,
                "section": "A",
                "schedule": "Mon/Wed/Fri 11:00-12:00",
                "room_number": "103",
                "max_students": 30
            },
            {
                "course_name": "Mathematics Grade 7",
                "course_code": "MATH7A",
                "description": "Basic Algebra and Geometry",
                "teacher_id": teachers[0].id,
                "grade_level": 7,
                "section": "A",
                "schedule": "Tue/Thu 9:00-10:00",
                "room_number": "101",
                "max_students": 30
            }
        ]
        
        courses = []
        for course_data in courses_data:
            course = Course(**course_data)
            db.add(course)
            courses.append(course)
        
        db.flush()
        
        # Create sample attendance records
        today = date.today()
        for i in range(5):
            attendance_date = today - timedelta(days=i)
            
            # Attendance for Grade 8 students in Math
            for student in students[:3]:
                attendance = Attendance(
                    student_id=student.id,
                    course_id=courses[0].id,
                    date=attendance_date,
                    status="present" if i % 3 != 0 else "late",
                    remarks="",
                    marked_by=teacher_user.id
                )
                db.add(attendance)
            
            # Attendance for Grade 7 students in Math
            for student in students[3:]:
                attendance = Attendance(
                    student_id=student.id,
                    course_id=courses[3].id,
                    date=attendance_date,
                    status="present",
                    remarks="",
                    marked_by=teacher_user.id
                )
                db.add(attendance)
        
        # Create sample grades
        grades_data = [
            # Emma's grades
            {
                "student_id": students[0].id,
                "course_id": courses[0].id,
                "assessment_type": "exam",
                "assessment_name": "Midterm Exam",
                "max_marks": 100,
                "obtained_marks": 92,
                "grade": "A",
                "assessment_date": today - timedelta(days=30)
            },
            {
                "student_id": students[0].id,
                "course_id": courses[1].id,
                "assessment_type": "exam",
                "assessment_name": "Midterm Exam",
                "max_marks": 100,
                "obtained_marks": 88,
                "grade": "A",
                "assessment_date": today - timedelta(days=30)
            },
            # Liam's grades
            {
                "student_id": students[1].id,
                "course_id": courses[0].id,
                "assessment_type": "exam",
                "assessment_name": "Midterm Exam",
                "max_marks": 100,
                "obtained_marks": 85,
                "grade": "B+",
                "assessment_date": today - timedelta(days=30)
            },
            {
                "student_id": students[1].id,
                "course_id": courses[1].id,
                "assessment_type": "assignment",
                "assessment_name": "Lab Report",
                "max_marks": 50,
                "obtained_marks": 45,
                "grade": "A",
                "assessment_date": today - timedelta(days=15)
            }
        ]
        
        for grade_data in grades_data:
            grade = Grade(**grade_data)
            db.add(grade)
        
        db.commit()
        print("Database initialized successfully!")
        print("\nDefault Login Credentials:")
        print("Username: admin")
        print("Password: admin123")
        print("\nTeacher Login:")
        print("Username: teacher1")
        print("Password: teacher123")
        
    except Exception as e:
        print(f"Error initializing database: {e}")
        db.rollback()
    finally:
        db.close()

if __name__ == "__main__":
    init_database()
