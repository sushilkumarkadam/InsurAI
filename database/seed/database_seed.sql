-- ============================================================
-- InsurAI Database Seed Script (PostgreSQL)
-- ============================================================
-- IMPORTANT: The password hash below is BCrypt for 'password123'
-- After running this seed, log in with username/password: admin / password123, staff1 / password123, employee1 / password123
-- ============================================================

-- 1. Seed Admin, Staff and Employee users
INSERT INTO users (username, password, email, role, phone, department, created_at)
VALUES 
  ('admin',     '$2a$10$KpwqA/8G7180qHq5N24G1uCg03V3gZzE6w7zYI.cQ4Z1y./G8G7yq', 'admin@insurai.com',     'ADMIN',    '+91-9000000001', 'Management',   NOW()),
  ('staff1',    '$2a$10$KpwqA/8G7180qHq5N24G1uCg03V3gZzE6w7zYI.cQ4Z1y./G8G7yq', 'staff1@insurai.com',    'STAFF',    '+91-9000000002', 'Claims',       NOW()),
  ('staff2',    '$2a$10$KpwqA/8G7180qHq5N24G1uCg03V3gZzE6w7zYI.cQ4Z1y./G8G7yq', 'staff2@insurai.com',    'STAFF',    '+91-9000000005', 'Claims',       NOW()),
  ('employee1', '$2a$10$KpwqA/8G7180qHq5N24G1uCg03V3gZzE6w7zYI.cQ4Z1y./G8G7yq', 'employee1@insurai.com', 'EMPLOYEE', '+91-9000000003', 'Engineering',  NOW()),
  ('employee2', '$2a$10$KpwqA/8G7180qHq5N24G1uCg03V3gZzE6w7zYI.cQ4Z1y./G8G7yq', 'employee2@insurai.com', 'EMPLOYEE', '+91-9000000004', 'Finance',      NOW()),
  ('employee3', '$2a$10$KpwqA/8G7180qHq5N24G1uCg03V3gZzE6w7zYI.cQ4Z1y./G8G7yq', 'employee3@insurai.com', 'EMPLOYEE', '+91-9000000006', 'Design',       NOW()),
  ('employee4', '$2a$10$KpwqA/8G7180qHq5N24G1uCg03V3gZzE6w7zYI.cQ4Z1y./G8G7yq', 'employee4@insurai.com', 'EMPLOYEE', '+91-9000000007', 'HR',           NOW())
ON CONFLICT (username) DO NOTHING;

-- 2. Seed Corporate Policies
INSERT INTO policies (policy_number, title, type, description, coverage_amount, premium, status, expiry_date, created_at, assigned_user_id)
SELECT 'POL-1001', 'Standard Corporate Health', 'Health Insurance', 'Comprehensive health coverage including hospitalization.', 500000.00, 2000.00, 'ACTIVE', NOW() + INTERVAL '1 year', NOW(), id FROM users WHERE username='employee1'
WHERE NOT EXISTS (SELECT 1 FROM policies WHERE policy_number='POL-1001');

INSERT INTO policies (policy_number, title, type, description, coverage_amount, premium, status, expiry_date, created_at, assigned_user_id)
SELECT 'POL-1002', 'Premium Life Cover', 'Life Insurance', 'Term life insurance with accidental death benefits.', 2500000.00, 1500.00, 'ACTIVE', NOW() + INTERVAL '2 years', NOW(), id FROM users WHERE username='employee1'
WHERE NOT EXISTS (SELECT 1 FROM policies WHERE policy_number='POL-1002');

INSERT INTO policies (policy_number, title, type, description, coverage_amount, premium, status, expiry_date, created_at, assigned_user_id)
SELECT 'POL-1003', 'Corporate Vehicle and Transport', 'Auto Insurance', 'Full coverage for vehicles including roadside assistance.', 1000000.00, 4000.00, 'ACTIVE', NOW() + INTERVAL '1 year', NOW(), id FROM users WHERE username='employee2'
WHERE NOT EXISTS (SELECT 1 FROM policies WHERE policy_number='POL-1003');

INSERT INTO policies (policy_number, title, type, description, coverage_amount, premium, status, expiry_date, created_at, assigned_user_id)
SELECT 'POL-1004', 'Employee Travel Insurance', 'Travel Insurance', 'Business travel coverage including medical emergencies.', 750000.00, 1200.00, 'ACTIVE', NOW() + INTERVAL '1 year', NOW(), id FROM users WHERE username='employee3'
WHERE NOT EXISTS (SELECT 1 FROM policies WHERE policy_number='POL-1004');

INSERT INTO policies (policy_number, title, type, description, coverage_amount, premium, status, expiry_date, created_at, assigned_user_id)
SELECT 'POL-1005', 'Office Property and Equipment', 'Property Insurance', 'Coverage for company premises and assets.', 3000000.00, 5000.00, 'ACTIVE', NOW() + INTERVAL '1 year', NOW(), id FROM users WHERE username='employee4'
WHERE NOT EXISTS (SELECT 1 FROM policies WHERE policy_number='POL-1005');

-- 3. Seed Claims
INSERT INTO claims (user_id, policy_id, amount, reason, claim_type, status, date_submitted, risk_score)
SELECT (SELECT id FROM users WHERE username = 'employee1'), (SELECT id FROM policies WHERE policy_number = 'POL-1001'), 45000.00, 'Hospitalization – Appendicitis surgery at Apollo Hospital', 'Health', 'PENDING', NOW() - INTERVAL '2 days', 12
WHERE EXISTS (SELECT 1 FROM users WHERE username = 'employee1') AND EXISTS (SELECT 1 FROM policies WHERE policy_number = 'POL-1001');

INSERT INTO claims (user_id, policy_id, amount, reason, claim_type, notes, status, date_submitted, date_approved, assigned_staff_id, risk_score)
SELECT (SELECT id FROM users WHERE username = 'employee1'), (SELECT id FROM policies WHERE policy_number = 'POL-1001'), 8500.00, 'Outpatient treatment – Fever and diagnostics', 'Health', 'All bills verified via Apollo Clinic.', 'APPROVED', NOW() - INTERVAL '10 days', NOW() - INTERVAL '8 days', (SELECT id FROM users WHERE username='staff1'), 5
WHERE EXISTS (SELECT 1 FROM users WHERE username = 'employee1') AND EXISTS (SELECT 1 FROM policies WHERE policy_number = 'POL-1001');

INSERT INTO claims (user_id, policy_id, amount, reason, claim_type, status, date_submitted, risk_score)
SELECT (SELECT id FROM users WHERE username = 'employee2'), (SELECT id FROM policies WHERE policy_number = 'POL-1003'), 125000.00, 'Vehicle damage – Collision on expressway requiring full repair', 'Auto', 'PENDING', NOW() - INTERVAL '1 day', 75
WHERE EXISTS (SELECT 1 FROM users WHERE username = 'employee2') AND EXISTS (SELECT 1 FROM policies WHERE policy_number = 'POL-1003');

INSERT INTO claims (user_id, policy_id, amount, reason, claim_type, notes, status, date_submitted, date_approved, assigned_staff_id, risk_score)
SELECT (SELECT id FROM users WHERE username = 'employee3'), (SELECT id FROM policies WHERE policy_number = 'POL-1004'), 25000.00, 'Missed flight due to medical emergency during corporate trip', 'Travel', 'Medical cert verified.', 'REJECTED', NOW() - INTERVAL '5 days', NOW() - INTERVAL '4 days', (SELECT id FROM users WHERE username='staff2'), 45
WHERE EXISTS (SELECT 1 FROM users WHERE username = 'employee3') AND EXISTS (SELECT 1 FROM policies WHERE policy_number = 'POL-1004');

INSERT INTO claims (user_id, policy_id, amount, reason, claim_type, status, date_submitted, assigned_staff_id, risk_score)
SELECT (SELECT id FROM users WHERE username = 'employee4'), (SELECT id FROM policies WHERE policy_number = 'POL-1005'), 50000.00, 'Water damage to corporate laptop', 'Property', 'PENDING', NOW() - INTERVAL '12 hours', (SELECT id FROM users WHERE username='staff1'), 20
WHERE EXISTS (SELECT 1 FROM users WHERE username = 'employee4') AND EXISTS (SELECT 1 FROM policies WHERE policy_number = 'POL-1005');

-- 4. Seed Notifications
INSERT INTO notifications (user_id, message, is_read, date_sent)
SELECT (SELECT id FROM users WHERE username = 'employee1'), 'Welcome to InsurAI! Your account is set up.', false, NOW() - INTERVAL '1 day'
WHERE EXISTS (SELECT 1 FROM users WHERE username = 'employee1');

INSERT INTO notifications (user_id, message, is_read, date_sent)
SELECT (SELECT id FROM users WHERE username = 'employee1'), 'Your claim for ₹8,500 has been APPROVED.', true, NOW() - INTERVAL '5 days'
WHERE EXISTS (SELECT 1 FROM users WHERE username = 'employee1');

INSERT INTO notifications (user_id, message, is_read, date_sent)
SELECT (SELECT id FROM users WHERE username = 'employee2'), 'Your claim for ₹1,25,000 is under review.', false, NOW() - INTERVAL '12 hours'
WHERE EXISTS (SELECT 1 FROM users WHERE username = 'employee2');
