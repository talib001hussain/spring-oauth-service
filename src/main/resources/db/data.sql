-- Insert a test user (password is 'password' encrypted with BCrypt)
INSERT INTO users (id, email, password)
VALUES 
    ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'test@example.com', '$2a$10$rGITqEzMeYCpkqgOgPkHxOOGwP5XTQdGgYVKVQnqMCi9sgGVcJ7N6')
ON CONFLICT (email) DO NOTHING;

-- Insert a test user profile
INSERT INTO user_profiles (user_id, full_name, height, bmi)
VALUES 
    ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'Test User', 175.0, 22.5)
ON CONFLICT (user_id) DO NOTHING;