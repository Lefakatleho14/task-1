from flask import Flask, render_template, request, redirect, url_for, flash, jsonify
from flask_sqlalchemy import SQLAlchemy
from flask_login import LoginManager, UserMixin, login_user, login_required, logout_user, current_user
from werkzeug.security import generate_password_hash, check_password_hash
from datetime import datetime, date
import os

app = Flask(__name__)
app.config['SECRET_KEY'] = 'your-secret-key-here'
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///school_management.db'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False

db = SQLAlchemy(app)
login_manager = LoginManager()
login_manager.init_app(app)
login_manager.login_view = 'login'

# Database Models
class User(UserMixin, db.Model):
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(80), unique=True, nullable=False)
    email = db.Column(db.String(120), unique=True, nullable=False)
    password_hash = db.Column(db.String(120), nullable=False)
    role = db.Column(db.String(20), nullable=False)  # admin, teacher, student
    created_at = db.Column(db.DateTime, default=datetime.utcnow)

class Student(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    student_id = db.Column(db.String(20), unique=True, nullable=False)
    first_name = db.Column(db.String(50), nullable=False)
    last_name = db.Column(db.String(50), nullable=False)
    email = db.Column(db.String(120), unique=True, nullable=False)
    phone = db.Column(db.String(20))
    date_of_birth = db.Column(db.Date)
    address = db.Column(db.Text)
    class_id = db.Column(db.Integer, db.ForeignKey('class.id'))
    enrollment_date = db.Column(db.Date, default=date.today)
    is_active = db.Column(db.Boolean, default=True)
    user_id = db.Column(db.Integer, db.ForeignKey('user.id'))

class Teacher(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    teacher_id = db.Column(db.String(20), unique=True, nullable=False)
    first_name = db.Column(db.String(50), nullable=False)
    last_name = db.Column(db.String(50), nullable=False)
    email = db.Column(db.String(120), unique=True, nullable=False)
    phone = db.Column(db.String(20))
    subject = db.Column(db.String(100))
    hire_date = db.Column(db.Date, default=date.today)
    is_active = db.Column(db.Boolean, default=True)
    user_id = db.Column(db.Integer, db.ForeignKey('user.id'))

class Class(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    class_name = db.Column(db.String(50), nullable=False)
    grade_level = db.Column(db.Integer, nullable=False)
    room_number = db.Column(db.String(20))
    capacity = db.Column(db.Integer, default=30)
    teacher_id = db.Column(db.Integer, db.ForeignKey('teacher.id'))
    students = db.relationship('Student', backref='class_ref', lazy=True)

class Course(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    course_code = db.Column(db.String(20), unique=True, nullable=False)
    course_name = db.Column(db.String(100), nullable=False)
    description = db.Column(db.Text)
    credits = db.Column(db.Integer, default=3)
    teacher_id = db.Column(db.Integer, db.ForeignKey('teacher.id'))
    class_id = db.Column(db.Integer, db.ForeignKey('class.id'))

class Attendance(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    student_id = db.Column(db.Integer, db.ForeignKey('student.id'), nullable=False)
    class_id = db.Column(db.Integer, db.ForeignKey('class.id'), nullable=False)
    date = db.Column(db.Date, nullable=False)
    status = db.Column(db.String(20), nullable=False)  # present, absent, late
    notes = db.Column(db.Text)

class Grade(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    student_id = db.Column(db.Integer, db.ForeignKey('student.id'), nullable=False)
    course_id = db.Column(db.Integer, db.ForeignKey('course.id'), nullable=False)
    assignment_name = db.Column(db.String(100), nullable=False)
    grade = db.Column(db.Float, nullable=False)
    max_grade = db.Column(db.Float, default=100)
    date = db.Column(db.Date, default=date.today)
    notes = db.Column(db.Text)

@login_manager.user_loader
def load_user(user_id):
    return User.query.get(int(user_id))

# Routes
@app.route('/')
def index():
    if current_user.is_authenticated:
        return redirect(url_for('dashboard'))
    return render_template('index.html')

@app.route('/login', methods=['GET', 'POST'])
def login():
    if request.method == 'POST':
        username = request.form['username']
        password = request.form['password']
        user = User.query.filter_by(username=username).first()
        
        if user and check_password_hash(user.password_hash, password):
            login_user(user)
            return redirect(url_for('dashboard'))
        else:
            flash('Invalid username or password')
    
    return render_template('login.html')

@app.route('/logout')
@login_required
def logout():
    logout_user()
    return redirect(url_for('index'))

@app.route('/dashboard')
@login_required
def dashboard():
    stats = {
        'total_students': Student.query.filter_by(is_active=True).count(),
        'total_teachers': Teacher.query.filter_by(is_active=True).count(),
        'total_classes': Class.query.count(),
        'total_courses': Course.query.count()
    }
    return render_template('dashboard.html', stats=stats)

@app.route('/students')
@login_required
def students():
    students = Student.query.filter_by(is_active=True).all()
    return render_template('students.html', students=students)

@app.route('/students/add', methods=['GET', 'POST'])
@login_required
def add_student():
    if request.method == 'POST':
        student = Student(
            student_id=request.form['student_id'],
            first_name=request.form['first_name'],
            last_name=request.form['last_name'],
            email=request.form['email'],
            phone=request.form.get('phone'),
            date_of_birth=datetime.strptime(request.form['date_of_birth'], '%Y-%m-%d').date() if request.form['date_of_birth'] else None,
            address=request.form.get('address'),
            class_id=request.form.get('class_id') or None
        )
        db.session.add(student)
        db.session.commit()
        flash('Student added successfully!')
        return redirect(url_for('students'))
    
    classes = Class.query.all()
    return render_template('add_student.html', classes=classes)

@app.route('/teachers')
@login_required
def teachers():
    teachers = Teacher.query.filter_by(is_active=True).all()
    return render_template('teachers.html', teachers=teachers)

@app.route('/teachers/add', methods=['GET', 'POST'])
@login_required
def add_teacher():
    if request.method == 'POST':
        teacher = Teacher(
            teacher_id=request.form['teacher_id'],
            first_name=request.form['first_name'],
            last_name=request.form['last_name'],
            email=request.form['email'],
            phone=request.form.get('phone'),
            subject=request.form.get('subject')
        )
        db.session.add(teacher)
        db.session.commit()
        flash('Teacher added successfully!')
        return redirect(url_for('teachers'))
    
    return render_template('add_teacher.html')

@app.route('/classes')
@login_required
def classes():
    classes = Class.query.all()
    return render_template('classes.html', classes=classes)

@app.route('/classes/add', methods=['GET', 'POST'])
@login_required
def add_class():
    if request.method == 'POST':
        class_obj = Class(
            class_name=request.form['class_name'],
            grade_level=int(request.form['grade_level']),
            room_number=request.form.get('room_number'),
            capacity=int(request.form.get('capacity', 30)),
            teacher_id=request.form.get('teacher_id') or None
        )
        db.session.add(class_obj)
        db.session.commit()
        flash('Class added successfully!')
        return redirect(url_for('classes'))
    
    teachers = Teacher.query.filter_by(is_active=True).all()
    return render_template('add_class.html', teachers=teachers)

@app.route('/attendance')
@login_required
def attendance():
    classes = Class.query.all()
    selected_class = request.args.get('class_id')
    selected_date = request.args.get('date', date.today().strftime('%Y-%m-%d'))
    
    students = []
    if selected_class:
        students = Student.query.filter_by(class_id=selected_class, is_active=True).all()
    
    return render_template('attendance.html', classes=classes, students=students, 
                         selected_class=selected_class, selected_date=selected_date)

@app.route('/attendance/mark', methods=['POST'])
@login_required
def mark_attendance():
    class_id = request.form['class_id']
    date_str = request.form['date']
    attendance_data = request.form.getlist('attendance')
    
    for student_id in attendance_data:
        existing = Attendance.query.filter_by(
            student_id=student_id, 
            class_id=class_id, 
            date=datetime.strptime(date_str, '%Y-%m-%d').date()
        ).first()
        
        if not existing:
            attendance = Attendance(
                student_id=student_id,
                class_id=class_id,
                date=datetime.strptime(date_str, '%Y-%m-%d').date(),
                status='present'
            )
            db.session.add(attendance)
    
    db.session.commit()
    flash('Attendance marked successfully!')
    return redirect(url_for('attendance'))

@app.route('/grades')
@login_required
def grades():
    courses = Course.query.all()
    selected_course = request.args.get('course_id')
    students = []
    if selected_course:
        course = Course.query.get(selected_course)
        if course and course.class_id:
            students = Student.query.filter_by(class_id=course.class_id, is_active=True).all()
    
    return render_template('grades.html', courses=courses, students=students, selected_course=selected_course)

@app.route('/grades/add', methods=['GET', 'POST'])
@login_required
def add_grade():
    if request.method == 'POST':
        grade = Grade(
            student_id=request.form['student_id'],
            course_id=request.form['course_id'],
            assignment_name=request.form['assignment_name'],
            grade=float(request.form['grade']),
            max_grade=float(request.form['max_grade']),
            notes=request.form.get('notes')
        )
        db.session.add(grade)
        db.session.commit()
        flash('Grade added successfully!')
        return redirect(url_for('grades'))
    
    courses = Course.query.all()
    students = Student.query.filter_by(is_active=True).all()
    return render_template('add_grade.html', courses=courses, students=students)

@app.route('/reports')
@login_required
def reports():
    # Basic statistics for reports
    stats = {
        'total_students': Student.query.filter_by(is_active=True).count(),
        'total_teachers': Teacher.query.filter_by(is_active=True).count(),
        'total_classes': Class.query.count(),
        'attendance_rate': 0  # This would be calculated based on actual attendance data
    }
    
    # Calculate attendance rate if there's attendance data
    total_attendance_records = Attendance.query.count()
    present_records = Attendance.query.filter_by(status='present').count()
    if total_attendance_records > 0:
        stats['attendance_rate'] = round((present_records / total_attendance_records) * 100, 2)
    
    return render_template('reports.html', stats=stats)

if __name__ == '__main__':
    with app.app_context():
        db.create_all()
        
        # Create admin user if it doesn't exist
        admin = User.query.filter_by(username='admin').first()
        if not admin:
            admin = User(
                username='admin',
                email='admin@school.com',
                password_hash=generate_password_hash('admin123'),
                role='admin'
            )
            db.session.add(admin)
            db.session.commit()
    
    app.run(debug=True, host='0.0.0.0', port=5000)