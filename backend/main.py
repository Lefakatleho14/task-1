from fastapi import FastAPI, Depends, HTTPException, status
from fastapi.security import OAuth2PasswordRequestForm
from fastapi.middleware.cors import CORSMiddleware
from sqlalchemy.orm import Session
from typing import List
from datetime import timedelta

from backend.database import engine, get_db, Base
from backend.models import User, Student, Teacher, Course, Attendance, Grade
from backend.schemas import (
    UserCreate, User as UserSchema, Token, UserLogin,
    StudentCreate, StudentUpdate, Student as StudentSchema,
    TeacherCreate, TeacherUpdate, Teacher as TeacherSchema,
    CourseCreate, CourseUpdate, Course as CourseSchema,
    AttendanceCreate, Attendance as AttendanceSchema,
    GradeCreate, Grade as GradeSchema
)
from backend.auth import (
    get_password_hash, authenticate_user, create_access_token,
    get_current_user, require_role, ACCESS_TOKEN_EXPIRE_MINUTES
)

# Create database tables
Base.metadata.create_all(bind=engine)

app = FastAPI(title="School Management System")

# Configure CORS
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Authentication Endpoints
@app.post("/api/auth/register", response_model=UserSchema)
def register(user: UserCreate, db: Session = Depends(get_db)):
    db_user = db.query(User).filter(User.username == user.username).first()
    if db_user:
        raise HTTPException(status_code=400, detail="Username already registered")
    
    db_user = db.query(User).filter(User.email == user.email).first()
    if db_user:
        raise HTTPException(status_code=400, detail="Email already registered")
    
    hashed_password = get_password_hash(user.password)
    db_user = User(
        username=user.username,
        email=user.email,
        hashed_password=hashed_password,
        role=user.role
    )
    db.add(db_user)
    db.commit()
    db.refresh(db_user)
    return db_user

@app.post("/api/auth/login", response_model=Token)
def login(form_data: OAuth2PasswordRequestForm = Depends(), db: Session = Depends(get_db)):
    user = authenticate_user(db, form_data.username, form_data.password)
    if not user:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Incorrect username or password",
            headers={"WWW-Authenticate": "Bearer"},
        )
    access_token_expires = timedelta(minutes=ACCESS_TOKEN_EXPIRE_MINUTES)
    access_token = create_access_token(
        data={"sub": user.username}, expires_delta=access_token_expires
    )
    return {"access_token": access_token, "token_type": "bearer"}

@app.get("/api/auth/me", response_model=UserSchema)
async def read_users_me(current_user: User = Depends(get_current_user)):
    return current_user

# Student Endpoints
@app.post("/api/students", response_model=StudentSchema)
async def create_student(
    student: StudentCreate,
    db: Session = Depends(get_db),
    current_user: User = Depends(require_role(["admin", "teacher"]))
):
    db_student = db.query(Student).filter(Student.roll_number == student.roll_number).first()
    if db_student:
        raise HTTPException(status_code=400, detail="Roll number already exists")
    
    db_student = Student(**student.dict())
    db.add(db_student)
    db.commit()
    db.refresh(db_student)
    return db_student

@app.get("/api/students", response_model=List[StudentSchema])
async def get_students(
    skip: int = 0,
    limit: int = 100,
    grade_level: int = None,
    section: str = None,
    db: Session = Depends(get_db),
    current_user: User = Depends(get_current_user)
):
    query = db.query(Student).filter(Student.is_active == True)
    if grade_level:
        query = query.filter(Student.grade_level == grade_level)
    if section:
        query = query.filter(Student.section == section)
    students = query.offset(skip).limit(limit).all()
    return students

@app.get("/api/students/{student_id}", response_model=StudentSchema)
async def get_student(
    student_id: int,
    db: Session = Depends(get_db),
    current_user: User = Depends(get_current_user)
):
    student = db.query(Student).filter(Student.id == student_id).first()
    if not student:
        raise HTTPException(status_code=404, detail="Student not found")
    return student

@app.put("/api/students/{student_id}", response_model=StudentSchema)
async def update_student(
    student_id: int,
    student_update: StudentUpdate,
    db: Session = Depends(get_db),
    current_user: User = Depends(require_role(["admin", "teacher"]))
):
    db_student = db.query(Student).filter(Student.id == student_id).first()
    if not db_student:
        raise HTTPException(status_code=404, detail="Student not found")
    
    for key, value in student_update.dict(exclude_unset=True).items():
        setattr(db_student, key, value)
    
    db.commit()
    db.refresh(db_student)
    return db_student

@app.delete("/api/students/{student_id}")
async def delete_student(
    student_id: int,
    db: Session = Depends(get_db),
    current_user: User = Depends(require_role(["admin"]))
):
    db_student = db.query(Student).filter(Student.id == student_id).first()
    if not db_student:
        raise HTTPException(status_code=404, detail="Student not found")
    
    db_student.is_active = False
    db.commit()
    return {"message": "Student deactivated successfully"}

# Teacher Endpoints
@app.post("/api/teachers", response_model=TeacherSchema)
async def create_teacher(
    teacher: TeacherCreate,
    db: Session = Depends(get_db),
    current_user: User = Depends(require_role(["admin"]))
):
    db_teacher = db.query(Teacher).filter(Teacher.employee_id == teacher.employee_id).first()
    if db_teacher:
        raise HTTPException(status_code=400, detail="Employee ID already exists")
    
    db_teacher = Teacher(**teacher.dict())
    db.add(db_teacher)
    db.commit()
    db.refresh(db_teacher)
    return db_teacher

@app.get("/api/teachers", response_model=List[TeacherSchema])
async def get_teachers(
    skip: int = 0,
    limit: int = 100,
    db: Session = Depends(get_db),
    current_user: User = Depends(get_current_user)
):
    teachers = db.query(Teacher).filter(Teacher.is_active == True).offset(skip).limit(limit).all()
    return teachers

@app.get("/api/teachers/{teacher_id}", response_model=TeacherSchema)
async def get_teacher(
    teacher_id: int,
    db: Session = Depends(get_db),
    current_user: User = Depends(get_current_user)
):
    teacher = db.query(Teacher).filter(Teacher.id == teacher_id).first()
    if not teacher:
        raise HTTPException(status_code=404, detail="Teacher not found")
    return teacher

@app.put("/api/teachers/{teacher_id}", response_model=TeacherSchema)
async def update_teacher(
    teacher_id: int,
    teacher_update: TeacherUpdate,
    db: Session = Depends(get_db),
    current_user: User = Depends(require_role(["admin"]))
):
    db_teacher = db.query(Teacher).filter(Teacher.id == teacher_id).first()
    if not db_teacher:
        raise HTTPException(status_code=404, detail="Teacher not found")
    
    for key, value in teacher_update.dict(exclude_unset=True).items():
        setattr(db_teacher, key, value)
    
    db.commit()
    db.refresh(db_teacher)
    return db_teacher

@app.delete("/api/teachers/{teacher_id}")
async def delete_teacher(
    teacher_id: int,
    db: Session = Depends(get_db),
    current_user: User = Depends(require_role(["admin"]))
):
    db_teacher = db.query(Teacher).filter(Teacher.id == teacher_id).first()
    if not db_teacher:
        raise HTTPException(status_code=404, detail="Teacher not found")
    
    db_teacher.is_active = False
    db.commit()
    return {"message": "Teacher deactivated successfully"}

# Course Endpoints
@app.post("/api/courses", response_model=CourseSchema)
async def create_course(
    course: CourseCreate,
    db: Session = Depends(get_db),
    current_user: User = Depends(require_role(["admin"]))
):
    db_course = db.query(Course).filter(Course.course_code == course.course_code).first()
    if db_course:
        raise HTTPException(status_code=400, detail="Course code already exists")
    
    db_course = Course(**course.dict())
    db.add(db_course)
    db.commit()
    db.refresh(db_course)
    return db_course

@app.get("/api/courses", response_model=List[CourseSchema])
async def get_courses(
    skip: int = 0,
    limit: int = 100,
    grade_level: int = None,
    teacher_id: int = None,
    db: Session = Depends(get_db),
    current_user: User = Depends(get_current_user)
):
    query = db.query(Course).filter(Course.is_active == True)
    if grade_level:
        query = query.filter(Course.grade_level == grade_level)
    if teacher_id:
        query = query.filter(Course.teacher_id == teacher_id)
    courses = query.offset(skip).limit(limit).all()
    return courses

@app.get("/api/courses/{course_id}", response_model=CourseSchema)
async def get_course(
    course_id: int,
    db: Session = Depends(get_db),
    current_user: User = Depends(get_current_user)
):
    course = db.query(Course).filter(Course.id == course_id).first()
    if not course:
        raise HTTPException(status_code=404, detail="Course not found")
    return course

@app.put("/api/courses/{course_id}", response_model=CourseSchema)
async def update_course(
    course_id: int,
    course_update: CourseUpdate,
    db: Session = Depends(get_db),
    current_user: User = Depends(require_role(["admin"]))
):
    db_course = db.query(Course).filter(Course.id == course_id).first()
    if not db_course:
        raise HTTPException(status_code=404, detail="Course not found")
    
    for key, value in course_update.dict(exclude_unset=True).items():
        setattr(db_course, key, value)
    
    db.commit()
    db.refresh(db_course)
    return db_course

# Attendance Endpoints
@app.post("/api/attendance", response_model=AttendanceSchema)
async def mark_attendance(
    attendance: AttendanceCreate,
    db: Session = Depends(get_db),
    current_user: User = Depends(require_role(["admin", "teacher"]))
):
    db_attendance = Attendance(**attendance.dict(), marked_by=current_user.id)
    db.add(db_attendance)
    db.commit()
    db.refresh(db_attendance)
    return db_attendance

@app.get("/api/attendance", response_model=List[AttendanceSchema])
async def get_attendance(
    student_id: int = None,
    course_id: int = None,
    skip: int = 0,
    limit: int = 100,
    db: Session = Depends(get_db),
    current_user: User = Depends(get_current_user)
):
    query = db.query(Attendance)
    if student_id:
        query = query.filter(Attendance.student_id == student_id)
    if course_id:
        query = query.filter(Attendance.course_id == course_id)
    attendance = query.offset(skip).limit(limit).all()
    return attendance

@app.get("/api/attendance/student/{student_id}/summary")
async def get_attendance_summary(
    student_id: int,
    db: Session = Depends(get_db),
    current_user: User = Depends(get_current_user)
):
    attendances = db.query(Attendance).filter(Attendance.student_id == student_id).all()
    total = len(attendances)
    present = len([a for a in attendances if a.status == "present"])
    absent = len([a for a in attendances if a.status == "absent"])
    late = len([a for a in attendances if a.status == "late"])
    
    return {
        "total": total,
        "present": present,
        "absent": absent,
        "late": late,
        "percentage": round((present / total * 100) if total > 0 else 0, 2)
    }

# Grade Endpoints
@app.post("/api/grades", response_model=GradeSchema)
async def create_grade(
    grade: GradeCreate,
    db: Session = Depends(get_db),
    current_user: User = Depends(require_role(["admin", "teacher"]))
):
    db_grade = Grade(**grade.dict())
    db.add(db_grade)
    db.commit()
    db.refresh(db_grade)
    return db_grade

@app.get("/api/grades", response_model=List[GradeSchema])
async def get_grades(
    student_id: int = None,
    course_id: int = None,
    skip: int = 0,
    limit: int = 100,
    db: Session = Depends(get_db),
    current_user: User = Depends(get_current_user)
):
    query = db.query(Grade)
    if student_id:
        query = query.filter(Grade.student_id == student_id)
    if course_id:
        query = query.filter(Grade.course_id == course_id)
    grades = query.offset(skip).limit(limit).all()
    return grades

@app.get("/api/grades/student/{student_id}/report")
async def get_student_report(
    student_id: int,
    db: Session = Depends(get_db),
    current_user: User = Depends(get_current_user)
):
    grades = db.query(Grade).filter(Grade.student_id == student_id).all()
    
    if not grades:
        return {"message": "No grades found for this student"}
    
    total_marks = sum([g.obtained_marks for g in grades])
    max_marks = sum([g.max_marks for g in grades])
    percentage = round((total_marks / max_marks * 100) if max_marks > 0 else 0, 2)
    
    course_wise = {}
    for grade in grades:
        if grade.course_id not in course_wise:
            course_wise[grade.course_id] = {
                "total_marks": 0,
                "max_marks": 0,
                "grades": []
            }
        course_wise[grade.course_id]["total_marks"] += grade.obtained_marks
        course_wise[grade.course_id]["max_marks"] += grade.max_marks
        course_wise[grade.course_id]["grades"].append({
            "assessment_name": grade.assessment_name,
            "obtained_marks": grade.obtained_marks,
            "max_marks": grade.max_marks,
            "grade": grade.grade
        })
    
    return {
        "student_id": student_id,
        "overall_percentage": percentage,
        "total_marks": total_marks,
        "max_marks": max_marks,
        "course_wise": course_wise
    }

# Dashboard Endpoint
@app.get("/api/dashboard/stats")
async def get_dashboard_stats(
    db: Session = Depends(get_db),
    current_user: User = Depends(require_role(["admin"]))
):
    total_students = db.query(Student).filter(Student.is_active == True).count()
    total_teachers = db.query(Teacher).filter(Teacher.is_active == True).count()
    total_courses = db.query(Course).filter(Course.is_active == True).count()
    
    return {
        "total_students": total_students,
        "total_teachers": total_teachers,
        "total_courses": total_courses
    }

@app.get("/")
def read_root():
    return {"message": "School Management System API", "version": "1.0"}
