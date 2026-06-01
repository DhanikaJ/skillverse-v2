-- Insert dummy users with different roles
INSERT INTO users (email, password_hash, role, created_at)
VALUES
('instructor@example.com', '$2a$10$Y3xZ8q7pL9mK2jX4wQ8nP.wR6tY5uI9oP2sL3mK4nJ5oI8pQ7rW2', 'INSTRUCTOR', CURRENT_TIMESTAMP),
('student@example.com', '$2a$10$Y3xZ8q7pL9mK2jX4wQ8nP.wR6tY5uI9oP2sL3mK4nJ5oI8pQ7rW2', 'STUDENT', CURRENT_TIMESTAMP),
('admin@example.com', '$2a$10$Y3xZ8q7pL9mK2jX4wQ8nP.wR6tY5uI9oP2sL3mK4nJ5oI8pQ7rW2', 'ADMIN', CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;

-- Insert dummy courses
INSERT INTO course (title, description, pricelevel, difficulty, price, thumbnail, users_id, created_at, status_id)
VALUES
('Java Spring Boot Mastery', 'Learn to build enterprise applications with Spring Boot', 'INTERMEDIATE', 'INTERMEDIATE', 29.99, 'https://via.placeholder.com/300?text=Spring+Boot', 1, CURRENT_TIMESTAMP, 1),
('React JS Advanced', 'Master modern React with hooks, state management, and performance optimization', 'ADVANCED', 'ADVANCED', 39.99, 'https://via.placeholder.com/300?text=React', 1, CURRENT_TIMESTAMP, 1),
('Python Data Science', 'Complete guide to data analysis, visualization, and machine learning with Python', 'BEGINNER', 'BEGINNER', 19.99, 'https://via.placeholder.com/300?text=Python', 1, CURRENT_TIMESTAMP, 1)
ON CONFLICT DO NOTHING;
