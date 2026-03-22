-- ============================================
-- UserVault — Database Setup Script
-- Run in pgAdmin Query Tool
-- ============================================

-- 1. Create Database (if not exists)
-- CREATE DATABASE usermgmt;

-- 2. Create users table
CREATE TABLE IF NOT EXISTS users (
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(100) NOT NULL,
    email      VARCHAR(150) NOT NULL UNIQUE,
    phone      VARCHAR(15)  NOT NULL,
    department VARCHAR(100),
    role       VARCHAR(100)
);

-- 3. Indexes for fast search
CREATE INDEX IF NOT EXISTS idx_users_name  ON users (LOWER(name));
CREATE INDEX IF NOT EXISTS idx_users_email ON users (LOWER(email));

-- 4. Sample Data (optional)
INSERT INTO users (name, email, phone, department, role) VALUES
('Arjun Sharma',  'arjun@example.com',  '9876543210', 'Engineering', 'Developer'),
('Priya Mehta',   'priya@example.com',  '9123456780', 'Design',      'UI/UX Designer'),
('Rohan Das',     'rohan@example.com',  '9988776655', 'HR',          'Manager'),
('Sneha Iyer',    'sneha@example.com',  '9011223344', 'Marketing',   'Analyst'),
('Vikram Nair',   'vikram@example.com', '9345678901', 'Engineering', 'Backend Dev')
ON CONFLICT (email) DO NOTHING;
