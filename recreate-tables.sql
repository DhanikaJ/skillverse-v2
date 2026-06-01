-- Drop all tables if they exist (in correct order to handle foreign keys)
DROP TABLE IF EXISTS quiz_question CASCADE;
DROP TABLE IF EXISTS quiz_attempt CASCADE;
DROP TABLE IF EXISTS quiz CASCADE;
DROP TABLE IF EXISTS lesson CASCADE;
DROP TABLE IF EXISTS review CASCADE;
DROP TABLE IF EXISTS cart CASCADE;
DROP TABLE IF EXISTS order_item CASCADE;
DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS payment CASCADE;
DROP TABLE IF EXISTS enrollment CASCADE;
DROP TABLE IF EXISTS certificate CASCADE;
DROP TABLE IF EXISTS course CASCADE;
DROP TABLE IF EXISTS category CASCADE;
DROP TABLE IF EXISTS payment_method CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS status CASCADE;
DROP TABLE IF EXISTS gender CASCADE;
DROP TABLE IF EXISTS city CASCADE;
DROP TABLE IF EXISTS role CASCADE;

-- Create city table
CREATE TABLE city (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255)
);

-- Create gender table
CREATE TABLE gender (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255)
);

-- Create status table
CREATE TABLE status (
    id SERIAL PRIMARY KEY,
    type VARCHAR(255)
);

-- Create payment_method table
CREATE TABLE payment_method (
    id SERIAL PRIMARY KEY,
    method VARCHAR(255)
);

-- Create category table
CREATE TABLE category (
    id SERIAL PRIMARY KEY,
    type VARCHAR(100) NOT NULL UNIQUE
);

-- Create users table
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    fname VARCHAR(255),
    lname VARCHAR(255),
    email VARCHAR(255),
    password_hash VARCHAR(255),
    verification VARCHAR(255),
    role VARCHAR(50),
    photo VARCHAR(255),
    created_at TIMESTAMP,
    city_id INTEGER REFERENCES city(id),
    gender_id INTEGER REFERENCES gender(id),
    status_id INTEGER REFERENCES status(id)
);

-- Create course table
CREATE TABLE course (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255),
    description TEXT,
    pricelevel VARCHAR(255),
    difficulty VARCHAR(255),
    price DOUBLE PRECISION,
    thumbnail VARCHAR(255),
    created_at TIMESTAMP,
    users_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
    category_id INTEGER REFERENCES category(id),
    status_id INTEGER REFERENCES status(id)
);

-- Create lesson table
CREATE TABLE lesson (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255),
    video_url VARCHAR(255),
    order_index INTEGER,
    resource_file VARCHAR(255),
    course_id INTEGER NOT NULL REFERENCES course(id) ON DELETE CASCADE
);

-- Create cart table
CREATE TABLE cart (
    id SERIAL PRIMARY KEY,
    session_id VARCHAR(255),
    added_at TIMESTAMP NOT NULL,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    course_id INTEGER NOT NULL REFERENCES course(id) ON DELETE CASCADE
);

-- Create quiz table
CREATE TABLE quiz (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255),
    course_id INTEGER NOT NULL REFERENCES course(id) ON DELETE CASCADE
);

-- Create quiz_attempt table
CREATE TABLE quiz_attempt (
    id SERIAL PRIMARY KEY,
    score DOUBLE PRECISION NOT NULL,
    attempted_at TIMESTAMP NOT NULL,
    quiz_id INTEGER NOT NULL REFERENCES quiz(id) ON DELETE CASCADE,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE
);

-- Create quiz_question table
CREATE TABLE quiz_question (
    id SERIAL PRIMARY KEY,
    question_text TEXT,
    option_a TEXT,
    option_b TEXT,
    option_c TEXT,
    option_d TEXT,
    correct_option VARCHAR(255),
    quiz_id INTEGER NOT NULL REFERENCES quiz(id) ON DELETE CASCADE
);

-- Create review table
CREATE TABLE review (
    id SERIAL PRIMARY KEY,
    rating INTEGER NOT NULL,
    comment VARCHAR(1000),
    reviewed_at TIMESTAMP NOT NULL,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    course_id INTEGER NOT NULL REFERENCES course(id) ON DELETE CASCADE
);

-- Create enrollment table with unique constraint
CREATE TABLE enrollment (
    id SERIAL PRIMARY KEY,
    progress DOUBLE PRECISION NOT NULL,
    enrolled_at TIMESTAMP NOT NULL,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    course_id INTEGER NOT NULL REFERENCES course(id) ON DELETE CASCADE,
    status_id INTEGER REFERENCES status(id),
    UNIQUE (user_id, course_id)
);

-- Create certificate table
CREATE TABLE certificate (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    course_id INTEGER NOT NULL REFERENCES course(id) ON DELETE CASCADE,
    issue_date TIMESTAMP NOT NULL,
    certificate_path VARCHAR(500)
);

-- Create orders table
CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    total DOUBLE PRECISION,
    status VARCHAR(255),
    created_at TIMESTAMP,
    user_id INTEGER REFERENCES users(id)
);

-- Create order_item table
CREATE TABLE order_item (
    id SERIAL PRIMARY KEY,
    price DOUBLE PRECISION,
    orders_id INTEGER REFERENCES orders(id) ON DELETE CASCADE,
    course_id INTEGER REFERENCES course(id)
);

-- Create payment table
CREATE TABLE payment (
    id SERIAL PRIMARY KEY,
    amount DOUBLE PRECISION,
    status VARCHAR(255),
    txn_reference VARCHAR(255),
    paid_at TIMESTAMP,
    created_at TIMESTAMP,
    user_id INTEGER REFERENCES users(id),
    course_id INTEGER REFERENCES course(id),
    payment_method_id INTEGER REFERENCES payment_method(id)
);

-- Insert sample data - Status
INSERT INTO status (id, type) VALUES (1, 'ACTIVE') ON CONFLICT DO NOTHING;
INSERT INTO status (id, type) VALUES (2, 'INACTIVE') ON CONFLICT DO NOTHING;
INSERT INTO status (id, type) VALUES (3, 'PENDING') ON CONFLICT DO NOTHING;

-- Insert sample data - Gender
INSERT INTO gender (id, name) VALUES (1, 'Male') ON CONFLICT DO NOTHING;
INSERT INTO gender (id, name) VALUES (2, 'Female') ON CONFLICT DO NOTHING;
INSERT INTO gender (id, name) VALUES (3, 'Other') ON CONFLICT DO NOTHING;

-- Insert sample data - City
INSERT INTO city (id, name) VALUES (1, 'Colombo') ON CONFLICT DO NOTHING;
INSERT INTO city (id, name) VALUES (2, 'Kandy') ON CONFLICT DO NOTHING;
INSERT INTO city (id, name) VALUES (3, 'Galle') ON CONFLICT DO NOTHING;

-- Insert sample data - PaymentMethod
INSERT INTO payment_method (id, method) VALUES (1, 'CREDIT_CARD') ON CONFLICT DO NOTHING;
INSERT INTO payment_method (id, method) VALUES (2, 'DEBIT_CARD') ON CONFLICT DO NOTHING;
INSERT INTO payment_method (id, method) VALUES (3, 'PAYPAL') ON CONFLICT DO NOTHING;

-- Insert sample data - User
INSERT INTO users (id, fname, lname, email, password_hash, verification, role, created_at, city_id, gender_id, status_id)
VALUES (1, 'Admin', 'User', 'admin@skillverse.com', '$2a$10$slYQmyNdGzin7olVN3p5Be.Kex7vOFrJ0nFMGxMV3YWHwsVjLRnfe', 'verified', 'ADMIN', NOW(), 1, 1, 1) ON CONFLICT DO NOTHING;

-- Insert sample courses
INSERT INTO course (id, title, description, pricelevel, difficulty, price, thumbnail, users_id, created_at, status_id)
VALUES
(1, 'Java Spring Boot Mastery', 'Learn to build enterprise applications with Spring Boot', 'INTERMEDIATE', 'INTERMEDIATE', 29.99, 'https://via.placeholder.com/300?text=Spring+Boot', 1, NOW(), 1),
(2, 'React JS Advanced', 'Master modern React with hooks, state management, and performance optimization', 'ADVANCED', 'ADVANCED', 39.99, 'https://via.placeholder.com/300?text=React', 1, NOW(), 1),
(3, 'Python Data Science', 'Complete guide to data analysis, visualization, and machine learning with Python', 'BEGINNER', 'BEGINNER', 19.99, 'https://via.placeholder.com/300?text=Python', 1, NOW(), 1)
ON CONFLICT DO NOTHING;

