from pydantic import BaseModel, EmailStr
from typing import Optional
from datetime import date, datetime

# User Schemas
class UserBase(BaseModel):
    username: str
    email: EmailStr
    role: str

class UserCreate(UserBase):
    password: str

class UserLogin(BaseModel):
    username: str
    password: str

class User(UserBase):
    id: int
    is_active: bool
    created_at: datetime
    
    class Config:
        from_attributes = True

class Token(BaseModel):
    access_token: str
    token_type: str

# Student Schemas
class StudentBase(BaseModel):
    first_name: str
    last_name: str
    date_of_birth: date
    gender: str
    address: Optional[str] = None
    phone: Optional[str] = None
    email: Optional[str] = None
    parent_name: Optional[str] = None
    parent_phone: Optional[str] = None
    parent_email: Optional[str] = None
    enrollment_date: date
    grade_level: int
    section: Optional[str] = None
    roll_number: str

class StudentCreate(StudentBase):
    pass

class StudentUpdate(BaseModel):
    first_name: Optional[str] = None
    last_name: Optional[str] = None
    date_of_birth: Optional[date] = None
    gender: Optional[str] = None
    address: Optional[str] = None
    phone: Optional[str] = None
    email: Optional[str] = None
    parent_name: Optional[str] = None
    parent_phone: Optional[str] = None
    parent_email: Optional[str] = None
    grade_level: Optional[int] = None
    section: Optional[str] = None
    is_active: Optional[bool] = None

class Student(StudentBase):
    id: int
    is_active: bool
    
    class Config:
        from_attributes = True

# Teacher Schemas
class TeacherBase(BaseModel):
    first_name: str
    last_name: str
    date_of_birth: date
    gender: str
    address: Optional[str] = None
    phone: Optional[str] = None
    email: Optional[str] = None
    subject_specialization: str
    qualification: str
    hire_date: date
    employee_id: str

class TeacherCreate(TeacherBase):
    pass

class TeacherUpdate(BaseModel):
    first_name: Optional[str] = None
    last_name: Optional[str] = None
    date_of_birth: Optional[date] = None
    gender: Optional[str] = None
    address: Optional[str] = None
    phone: Optional[str] = None
    email: Optional[str] = None
    subject_specialization: Optional[str] = None
    qualification: Optional[str] = None
    is_active: Optional[bool] = None

class Teacher(TeacherBase):
    id: int
    is_active: bool
    
    class Config:
        from_attributes = True

# Course Schemas
class CourseBase(BaseModel):
    course_name: str
    course_code: str
    description: Optional[str] = None
    teacher_id: int
    grade_level: int
    section: Optional[str] = None
    schedule: Optional[str] = None
    room_number: Optional[str] = None
    max_students: Optional[int] = 40

class CourseCreate(CourseBase):
    pass

class CourseUpdate(BaseModel):
    course_name: Optional[str] = None
    description: Optional[str] = None
    teacher_id: Optional[int] = None
    schedule: Optional[str] = None
    room_number: Optional[str] = None
    max_students: Optional[int] = None
    is_active: Optional[bool] = None

class Course(CourseBase):
    id: int
    is_active: bool
    
    class Config:
        from_attributes = True

# Attendance Schemas
class AttendanceBase(BaseModel):
    student_id: int
    course_id: int
    date: date
    status: str
    remarks: Optional[str] = None

class AttendanceCreate(AttendanceBase):
    pass

class Attendance(AttendanceBase):
    id: int
    marked_by: int
    created_at: datetime
    
    class Config:
        from_attributes = True

# Grade Schemas
class GradeBase(BaseModel):
    student_id: int
    course_id: int
    assessment_type: str
    assessment_name: str
    max_marks: float
    obtained_marks: float
    grade: str
    assessment_date: date
    remarks: Optional[str] = None

class GradeCreate(GradeBase):
    pass

class Grade(GradeBase):
    id: int
    created_at: datetime
    
    class Config:
        from_attributes = True
