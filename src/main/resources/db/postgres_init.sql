-- PostgreSQL-specific initialization script

-- Database Creation
CREATE DATABASE authdb;

-- Connect to the database
\c authdb;

-- Users Table
CREATE TABLE users (
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- User Profiles Table
CREATE TABLE user_profiles (
    user_id UUID PRIMARY KEY,
    full_name VARCHAR(255),
    height DOUBLE PRECISION,
    bmi DOUBLE PRECISION,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE INDEX idx_users_email ON users(email);

-- Insert a test user (password is 'password' encrypted with BCrypt)
INSERT INTO users (id, email, password)
VALUES 
    ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'test@example.com', '$2a$10$rGITqEzMeYCpkqgOgPkHxOOGwP5XTQdGgYVKVQnqMCi9sgGVcJ7N6');

-- Insert a test user profile
INSERT INTO user_profiles (user_id, full_name, height, bmi)
VALUES 
    ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'Test User', 175.0, 22.5);