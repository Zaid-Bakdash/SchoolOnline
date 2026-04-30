-- Lab 4: Database & SQL
-- Database schema for Online School application

-- Students table
CREATE TABLE IF NOT EXISTS students (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    gpa DOUBLE NOT NULL DEFAULT 0.0,
    enrollment_date VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    INDEX idx_student_id (student_id),
    INDEX idx_email (email),
    INDEX idx_gpa (gpa)
);

-- Courses table
CREATE TABLE IF NOT EXISTS courses (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    course_id VARCHAR(20) NOT NULL UNIQUE,
    course_name VARCHAR(100) NOT NULL,
    credits INT NOT NULL,
    instructor VARCHAR(100) NOT NULL,
    max_students INT NOT NULL,
    enrolled_students INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    INDEX idx_course_id (course_id),
    INDEX idx_instructor (instructor),
    INDEX idx_enrolled (enrolled_students)
);

-- Registrations table (for future use)
CREATE TABLE IF NOT EXISTS registrations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    registration_date TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    FOREIGN KEY (student_id) REFERENCES students(id),
    FOREIGN KEY (course_id) REFERENCES courses(id),
    UNIQUE KEY unique_registration (student_id, course_id),
    INDEX idx_status (status)
);

-- Departments table
CREATE TABLE IF NOT EXISTS departments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    department_id VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL UNIQUE,
    head VARCHAR(100),
    budget DOUBLE NOT NULL,
    location VARCHAR(100),
    established_date DATE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    INDEX idx_department_id (department_id),
    INDEX idx_name (name),
    INDEX idx_location (location)
);
