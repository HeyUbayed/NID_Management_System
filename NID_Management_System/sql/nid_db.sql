DROP DATABASE IF EXISTS nid_db;
CREATE DATABASE nid_db;
USE nid_db;

CREATE TABLE citizen (
    citizen_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    father_name VARCHAR(100) NOT NULL,
    mother_name VARCHAR(100) NOT NULL,
    date_of_birth DATE NOT NULL,
    gender ENUM('Male', 'Female') NOT NULL,
    blood_group ENUM('A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-') DEFAULT NULL,
    religion VARCHAR(50),
    phone_number VARCHAR(11) UNIQUE,
    email VARCHAR(100) UNIQUE,
    division VARCHAR(50),
    district VARCHAR(50),
    upazila VARCHAR(50),
    postal_code VARCHAR(10),
    nid_number VARCHAR(20) UNIQUE,
    password VARCHAR(255) NOT NULL,
    status ENUM('Pending', 'Approved', 'Rejected') DEFAULT 'Pending',
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE admin (
    admin_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

INSERT INTO admin (username, password) VALUES
('admin', 'admin123');

INSERT INTO citizen (full_name, father_name, mother_name, date_of_birth, gender, blood_group, religion, phone_number, email, division, district, upazila, postal_code, password, status, nid_number) VALUES
('Ubaidur Rahman', 'Tahir Ali', 'Hazira Begum', '2004-01-01', 'Male', 'A+', 'Islam', '01712345678', 'ubaid@email.com', 'Dhaka', 'Dhaka', 'Mohammadpur', '1207', 'admin123', 'Approved', '1234567890123'),
('Ahmad Hossain Tamim', 'Karim Uddin', 'Rohima Begum', '2002-01-04', 'Male', 'B+', 'Islam', '01787654321', 'tamim@email.com', 'Sylhet', 'Sylhet', 'Kanaighat', '4203', 'admin123', 'Pending', NULL);