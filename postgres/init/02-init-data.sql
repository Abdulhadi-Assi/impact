-- ============================================================
-- 02-init-data.sql — Sample data for Online School Volunteer System
-- Order matters: parent tables must be inserted before children
-- ============================================================

-- ============================================================
-- 1. COLLEGE
-- ============================================================
INSERT INTO college (college_id, name, description, created_at) VALUES
(1, 'College of Engineering',       'Focuses on technical and engineering disciplines',         '2023-01-10 08:00:00'),
(2, 'College of Business',          'Covers management, finance, and entrepreneurship',         '2023-01-11 08:00:00'),
(3, 'College of Arts and Sciences', 'Broad liberal arts and natural sciences programs',         '2023-01-12 08:00:00'),
(4, 'College of Education',         'Teacher training and educational leadership programs',     '2023-01-13 08:00:00'),
(5, 'College of Health Sciences',   'Nursing, public health, and medical support programs',     '2023-01-14 08:00:00')
ON CONFLICT DO NOTHING;

-- ============================================================
-- 2. USER
-- Roles: student, admin, manager (stored via application logic)
-- proposed_by, approved_by, managed_by, reviewed_by etc. all
-- reference user_id, so we need a mix of roles here.
-- ============================================================
INSERT INTO "user" (user_id, college_id, student_number, keycloak_id, first_name, last_name, email, phone, academic_year, is_banned, created_at, updated_at) VALUES
-- Students
(1,  1, 'STU-001', 'kc-user-001', 'Alice',   'Johnson',  'alice.johnson@school.edu',   '555-1001', 2, FALSE, '2023-02-01 09:00:00', '2024-01-01 09:00:00'),
(2,  2, 'STU-002', 'kc-user-002', 'Bob',     'Martinez', 'bob.martinez@school.edu',    '555-1002', 3, FALSE, '2023-02-02 09:00:00', '2024-01-02 09:00:00'),
(3,  3, 'STU-003', 'kc-user-003', 'Carol',   'White',    'carol.white@school.edu',     '555-1003', 1, FALSE, '2023-02-03 09:00:00', '2024-01-03 09:00:00'),
(4,  4, 'STU-004', 'kc-user-004', 'David',   'Lee',      'david.lee@school.edu',       '555-1004', 4, FALSE, '2023-02-04 09:00:00', '2024-01-04 09:00:00'),
(5,  5, 'STU-005', 'kc-user-005', 'Eva',     'Brown',    'eva.brown@school.edu',       '555-1005', 2, TRUE,  '2023-02-05 09:00:00', '2024-01-05 09:00:00'),
-- Admins / Managers (no student_number needed, set NULL)
(6,  1, NULL,      'kc-admin-001', 'Frank',   'Admin',    'frank.admin@school.edu',     '555-2001', NULL, FALSE, '2023-01-15 09:00:00', '2024-01-06 09:00:00'),
(7,  2, NULL,      'kc-manager-001', 'Grace',   'Manager',  'grace.manager@school.edu',   '555-2002', NULL, FALSE, '2023-01-16 09:00:00', '2024-01-07 09:00:00'),
(8,  3, NULL,      'kc-admin-002', 'Henry',   'Admin',    'henry.admin@school.edu',     '555-2003', NULL, FALSE, '2023-01-17 09:00:00', '2024-01-08 09:00:00')
ON CONFLICT DO NOTHING;

-- ============================================================
-- 3. CATEGORY
-- ============================================================
INSERT INTO category (category_id, name, created_at) VALUES
(1, 'Environmental',    '2023-03-01 10:00:00'),
(2, 'Education',        '2023-03-02 10:00:00'),
(3, 'Healthcare',       '2023-03-03 10:00:00'),
(4, 'Community Service','2023-03-04 10:00:00'),
(5, 'Disaster Relief',  '2023-03-05 10:00:00')
ON CONFLICT DO NOTHING;

-- ============================================================
-- 4. CAMPAIGN
-- proposed_by → user (student), approved_by → user (admin),
-- managed_by  → user (manager), category_id → category
-- ============================================================
INSERT INTO campaign (campaign_id, proposed_by_id, approved_by_id, managed_by_id, category_id, title, description, location, start_date, end_date, max_volunteers, status, published_at, created_at, updated_at) VALUES
(1, 1, 6, 7, 1, 'Campus Tree Planting',      'Plant 500 trees around campus grounds',              'Main Campus',        '2024-03-01', '2024-03-03', 50,  'COMPLETED', '2024-02-01 08:00:00', '2024-01-20 08:00:00', '2024-03-04 08:00:00'),
(2, 2, 6, 7, 2, 'Tutoring for Kids',         'Free tutoring sessions for underprivileged children','Community Center',   '2024-04-10', '2024-04-20', 30,  'ONGOING',   '2024-03-15 08:00:00', '2024-03-01 08:00:00', '2024-04-10 08:00:00'),
(3, 3, 8, 7, 3, 'Blood Donation Drive',      'Organize a blood donation event on campus',          'Health Center',      '2024-05-05', '2024-05-05', 100, 'APPROVED',  '2024-04-01 08:00:00', '2024-03-20 08:00:00', '2024-04-01 08:00:00'),
(4, 4, 6, 7, 4, 'Elderly Home Visit',        'Spend time with elderly residents at local home',    'Sunrise Care Home',  '2024-06-01', '2024-06-02', 20,  'PENDING',   NULL,                  '2024-04-15 08:00:00', '2024-04-15 08:00:00'),
(5, 1, 8, 7, 5, 'Flood Relief Packing',      'Pack relief supplies for flood-affected families',   'Sports Hall',        '2024-07-01', '2024-07-03', 80,  'PENDING',   NULL,                  '2024-05-01 08:00:00', '2024-05-01 08:00:00'),
(6, 2, 6, 7, 1, 'Beach Cleanup',             'Clean up litter along the city beach',               'City Beach',         '2024-08-10', '2024-08-10', 60,  'APPROVED',  '2024-07-01 08:00:00', '2024-06-15 08:00:00', '2024-07-01 08:00:00')
ON CONFLICT DO NOTHING;

-- ============================================================
-- 5. PROGRESS
-- Must be inserted before campaign_photo (photo references progress)
-- updated_by → user (admin or manager)
-- ============================================================
INSERT INTO progress (progress_id, campaign_id, updated_by_id, percentage, notes, updated_at) VALUES
(1, 1, 7, 100, 'All 500 trees successfully planted',          '2024-03-03 17:00:00'),
(2, 2, 7, 45,  'Tutoring sessions ongoing, halfway through',  '2024-04-15 17:00:00'),
(3, 3, 7, 10,  'Venue booked, volunteers being registered',   '2024-04-10 17:00:00'),
(4, 4, 7, 0,   'Campaign pending approval',                   '2024-04-15 17:00:00'),
(5, 6, 7, 20,  'Volunteers confirmed, supplies being sourced','2024-07-05 17:00:00')
ON CONFLICT DO NOTHING;

-- ============================================================
-- 6. CAMPAIGN_PHOTO
-- campaign_id → campaign, progress_id → progress (optional)
-- ============================================================
INSERT INTO campaign_photo (photo_id, campaign_id, progress_id, photo_url, uploaded_at) VALUES
(1, 1, 1,    'https://storage.school.edu/photos/tree-planting-day1.jpg',   '2024-03-01 12:00:00'),
(2, 1, 1,    'https://storage.school.edu/photos/tree-planting-done.jpg',   '2024-03-03 16:00:00'),
(3, 2, 2,    'https://storage.school.edu/photos/tutoring-session1.jpg',    '2024-04-11 14:00:00'),
(4, 3, NULL, 'https://storage.school.edu/photos/blood-drive-setup.jpg',    '2024-04-30 10:00:00'),
(5, 6, 5,    'https://storage.school.edu/photos/beach-cleanup-before.jpg', '2024-07-05 09:00:00'),
(6, 6, 5,    'https://storage.school.edu/photos/beach-cleanup-after.jpg',  '2024-07-10 15:00:00')
ON CONFLICT DO NOTHING;

-- ============================================================
-- 7. APPLICATION
-- student_id → user, campaign_id → campaign,
-- reviewed_by → user (admin), removed_by → user (optional)
-- ============================================================
INSERT INTO application (id, student_id, campaign_id, reviewed_by_id, motivation_letter, status, admin_notes, rejection_reason, applied_at, reviewed_at, removed_by_id, removal_reason, removed_at) VALUES
(1, 1, 1, 6,    'I am passionate about the environment and want to contribute.', 'APPROVED', 'Great candidate',      NULL,                         '2024-02-10 10:00:00', '2024-02-15 10:00:00', NULL, NULL, NULL),
(2, 2, 1, 6,    'I have experience in gardening and outdoor activities.',        'APPROVED', 'Good experience',      NULL,                         '2024-02-11 10:00:00', '2024-02-15 10:00:00', NULL, NULL, NULL),
(3, 3, 2, 8,    'I love teaching and helping younger students succeed.',         'APPROVED', 'Approved for tutoring', NULL,                         '2024-03-20 10:00:00', '2024-03-25 10:00:00', NULL, NULL, NULL),
(4, 4, 2, 8,    'I have tutored students in math and science before.',           'REJECTED', NULL,                   'Insufficient availability',  '2024-03-21 10:00:00', '2024-03-25 10:00:00', NULL, NULL, NULL),
(5, 5, 3, 6,    'I want to help organise the blood donation event.',             'PENDING',  NULL,                    NULL,                         '2024-04-05 10:00:00', NULL,                  NULL, NULL, NULL),
(6, 1, 6, NULL, 'Beach cleanup is close to my heart.',                           'PENDING',  NULL,                    NULL,                         '2024-06-20 10:00:00', NULL,                  NULL, NULL, NULL),
(7, 2, 6, 6,    'I want to help keep our beaches clean for future generations.', 'APPROVED', 'Confirmed volunteer',   NULL,                         '2024-06-21 10:00:00', '2024-06-25 10:00:00', NULL, NULL, NULL),
(8, 3, 1, 6,    'Environmental volunteering aligns with my studies.',            'WITHDRAWN',  'Withdrew last minute',  NULL,                         '2024-02-12 10:00:00', '2024-02-15 10:00:00', 6,    'Volunteer withdrew without notice', '2024-02-28 10:00:00')
ON CONFLICT DO NOTHING;

-- ============================================================
-- 8. ATTENDANCE
-- student_id → user, campaign_id → campaign,
-- recorded_by → user (admin or manager)
-- ============================================================
INSERT INTO attendance (attendance_id, student_id, campaign_id, recorded_by_id, attendance_date, status, hours_that_day, notes, recorded_at) VALUES
(1, 1, 1, 7, '2024-03-01', 'PRESENT', 6, 'Arrived on time, worked full day',        '2024-03-01 18:00:00'),
(2, 1, 1, 7, '2024-03-02', 'PRESENT', 5, 'Continued tree planting',                 '2024-03-02 18:00:00'),
(3, 2, 1, 7, '2024-03-01', 'PRESENT', 6, 'Very helpful during planting',            '2024-03-01 18:00:00'),
(4, 2, 1, 7, '2024-03-02', 'ABSENT',  0, 'Did not show up, no notice given',        '2024-03-02 18:00:00'),
(5, 3, 2, 7, '2024-04-10', 'PRESENT', 4, 'Led a tutoring group effectively',        '2024-04-10 18:00:00'),
(6, 3, 2, 7, '2024-04-11', 'PRESENT', 4, 'Continued tutoring sessions',             '2024-04-11 18:00:00'),
(7, 1, 6, 7, '2024-08-10', 'PRESENT', 5, 'Collected 10 bags of litter',             '2024-08-10 18:00:00'),
(8, 2, 6, 7, '2024-08-10', 'EXCUSED', 0, 'Had a medical appointment, notified early','2024-08-10 18:00:00')
ON CONFLICT DO NOTHING;

-- ============================================================
-- Reset sequences so auto-increment continues after seeded IDs
-- ============================================================
SELECT setval('college_college_id_seq',       (SELECT MAX(college_id)    FROM college));
SELECT setval('user_user_id_seq',             (SELECT MAX(user_id)       FROM "user"));
SELECT setval('category_category_id_seq',     (SELECT MAX(category_id)   FROM category));
SELECT setval('campaign_campaign_id_seq',     (SELECT MAX(campaign_id)   FROM campaign));
SELECT setval('progress_progress_id_seq',     (SELECT MAX(progress_id)   FROM progress));
SELECT setval('campaign_photo_photo_id_seq',  (SELECT MAX(photo_id)      FROM campaign_photo));
SELECT setval('application_id_seq',           (SELECT MAX(id)            FROM application));
SELECT setval('attendance_attendance_id_seq', (SELECT MAX(attendance_id) FROM attendance));
