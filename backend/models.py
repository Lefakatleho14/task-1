from sqlalchemy import Column, Integer, String, Float, Date, ForeignKey, Boolean, DateTime, Text
from sqlalchemy.orm import relationship
from datetime import datetime
from backend.database import Base

class User(Base):
    __tablename__ = "users"
    
    id = Column(Integer, primary_key=True, index=True)
    username = Column(String, unique=True, index=True)
    email = Column(String, unique=True, index=True)
    hashed_password = Column(String)
    role = Column(String)  # admin, teacher, student, parent
    is_active = Column(Boolean, default=True)
    created_at = Column(DateTime, default=datetime.utcnow)

class Student(Base):
    __tablename__ = "students"
    
    id = Column(Integer, primary_key=True, index=True)
    user_id = Column(Integer, ForeignKey("users.id"), nullable=True)
    first_name = Column(String, index=True)
    last_name = Column(String, index=True)
    date_of_birth = Column(Date)
    gender = Column(String)
    address = Column(Text)
    phone = Column(String)
    email = Column(String)
    parent_name = Column(String)
    parent_phone = Column(String)
    parent_email = Column(String)
    enrollment_date = Column(Date)
    grade_level = Column(Integer)
    section = Column(String)
    roll_number = Column(String, unique=True)
    is_active = Column(Boolean, default=True)
    
    user = relationship("User", backref="student_profile")
    attendances = relationship("Attendance", back_populates="student")
    grades = relationship("Grade", back_populates="student")

class Teacher(Base):
    __tablename__ = "teachers"
    
    id = Column(Integer, primary_key=True, index=True)
    user_id = Column(Integer, ForeignKey("users.id"), nullable=True)
    first_name = Column(String, index=True)
    last_name = Column(String, index=True)
    date_of_birth = Column(Date)
    gender = Column(String)
    address = Column(Text)
    phone = Column(String)
    email = Column(String)
    subject_specialization = Column(String)
    qualification = Column(String)
    hire_date = Column(Date)
    employee_id = Column(String, unique=True)
    is_active = Column(Boolean, default=True)
    
    user = relationship("User", backref="teacher_profile")
    courses = relationship("Course", back_populates="teacher")

class Course(Base):
    __tablename__ = "courses"
    
    id = Column(Integer, primary_key=True, index=True)
    course_name = Column(String, index=True)
    course_code = Column(String, unique=True, index=True)
    description = Column(Text)
    teacher_id = Column(Integer, ForeignKey("teachers.id"))
    grade_level = Column(Integer)
    section = Column(String)
    schedule = Column(String)  # JSON string for schedule
    room_number = Column(String)
    max_students = Column(Integer)
    is_active = Column(Boolean, default=True)
    
    teacher = relationship("Teacher", back_populates="courses")
    attendances = relationship("Attendance", back_populates="course")
    grades = relationship("Grade", back_populates="course")

class Attendance(Base):
    __tablename__ = "attendances"
    
    id = Column(Integer, primary_key=True, index=True)
    student_id = Column(Integer, ForeignKey("students.id"))
    course_id = Column(Integer, ForeignKey("courses.id"))
    date = Column(Date)
    status = Column(String)  # present, absent, late, excused
    remarks = Column(Text)
    marked_by = Column(Integer, ForeignKey("users.id"))
    created_at = Column(DateTime, default=datetime.utcnow)
    
    student = relationship("Student", back_populates="attendances")
    course = relationship("Course", back_populates="attendances")

class Grade(Base):
    __tablename__ = "grades"
    
    id = Column(Integer, primary_key=True, index=True)
    student_id = Column(Integer, ForeignKey("students.id"))
    course_id = Column(Integer, ForeignKey("courses.id"))
    assessment_type = Column(String)  # quiz, exam, assignment, project
    assessment_name = Column(String)
    max_marks = Column(Float)
    obtained_marks = Column(Float)
    grade = Column(String)
    assessment_date = Column(Date)
    remarks = Column(Text)
    created_at = Column(DateTime, default=datetime.utcnow)
    
    student = relationship("Student", back_populates="grades")
    course = relationship("Course", back_populates="grades")
