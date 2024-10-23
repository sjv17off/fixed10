-- This file will be executed after schema.sql to insert initial data

-- Sample enterprises
INSERT INTO enterprises (name) VALUES 
('TechCorp'),
('FinanceHub'),
('HealthCare Inc.');

-- Sample users (password is 'password' for all users)
INSERT INTO users (username, password, enabled, enterprise_id, role)
VALUES 
('user', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', true, 1, 'USER'),
('admin', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', true, 1, 'ADMIN'),
('enterprise', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', true, 2, 'ENTERPRISE');

-- Sample tasks
INSERT INTO tasks (title, description, due_date, completed, enterprise_id)
VALUES 
('Complete Project Proposal', 'Finish the project proposal for the new client', '2023-06-30 17:00:00', false, 1),
('Review Financial Reports', 'Go through Q2 financial reports and prepare summary', '2023-07-15 12:00:00', false, 2),
('Update Patient Records', 'Ensure all patient records are up to date in the new system', '2023-07-10 16:00:00', false, 3);

-- Sample task assignments
INSERT INTO task_assignments (task_id, user_id)
VALUES 
(1, 1),
(2, 2),
(3, 3);

-- Sample attendance records
INSERT INTO attendance (user_id, date, status)
VALUES 
(1, '2023-06-01', 'PRESENT'),
(1, '2023-06-02', 'PRESENT'),
(2, '2023-06-01', 'PRESENT'),
(2, '2023-06-02', 'ABSENT'),
(3, '2023-06-01', 'PRESENT'),
(3, '2023-06-02', 'PRESENT');