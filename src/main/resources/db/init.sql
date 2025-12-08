-- Database Creation Script
CREATE DATABASE IF NOT EXISTS authdb;

-- Connect to the database
\c authdb;

-- Users Table
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- User Profiles Table
CREATE TABLE IF NOT EXISTS user_profiles (
    user_id UUID PRIMARY KEY,
    full_name VARCHAR(255),
    height DOUBLE PRECISION,
    bmi DOUBLE PRECISION,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);