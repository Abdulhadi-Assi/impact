-- ============================================================
-- 02-init-data.sql — Sample data for Online School Volunteer System
-- 50+ rows for main tables, 10+ for lookup tables
-- Order matters: parent tables must be inserted before children
-- ============================================================

-- ============================================================
-- 1. COLLEGE (12 rows)
-- ============================================================
INSERT INTO college (college_id, name, created_at) VALUES
(1,  'كلية الهندسة',          '2023-01-10 08:00:00'),
(2,  'كلية إدارة الأعمال',     '2023-01-11 08:00:00'),
(3,  'كلية الآداب والعلوم',    '2023-01-12 08:00:00'),
(4,  'كلية التربية',           '2023-01-13 08:00:00'),
(5,  'كلية العلوم الصحية',     '2023-01-14 08:00:00'),
(6,  'كلية الطب',              '2023-01-15 08:00:00'),
(7,  'كلية الصيدلة',           '2023-01-16 08:00:00'),
(8,  'كلية الحقوق',            '2023-01-17 08:00:00'),
(9,  'كلية الإعلام',           '2023-01-18 08:00:00'),
(10, 'كلية تقنية المعلومات',   '2023-01-19 08:00:00'),
(11, 'كلية الزراعة',           '2023-01-20 08:00:00'),
(12, 'كلية العمارة',           '2023-01-21 08:00:00')
ON CONFLICT DO NOTHING;

-- ============================================================
-- 2. CATEGORY (12 rows)
-- ============================================================
INSERT INTO category (category_id, name, created_at) VALUES
(1,  'البيئة',                 '2023-03-01 10:00:00'),
(2,  'التعليم',                '2023-03-02 10:00:00'),
(3,  'الرعاية الصحية',         '2023-03-03 10:00:00'),
(4,  'خدمة المجتمع',           '2023-03-04 10:00:00'),
(5,  'الإغاثة من الكوارث',     '2023-03-05 10:00:00'),
(6,  'كبار السن',              '2023-03-06 10:00:00'),
(7,  'الأطفال والأيتام',       '2023-03-07 10:00:00'),
(8,  'التراث والثقافة',        '2023-03-08 10:00:00'),
(9,  'التوعية',                '2023-03-09 10:00:00'),
(10, 'الرياضة والترفيه',       '2023-03-10 10:00:00'),
(11, 'تكنولوجيا للجميع',       '2023-03-11 10:00:00'),
(12, 'الأمن الغذائي',          '2023-03-12 10:00:00')
ON CONFLICT DO NOTHING;

-- ============================================================
-- 3. USER (55 rows: 45 students, 5 admins, 5 managers)
-- ============================================================
INSERT INTO "user"
    (user_id, college_id, student_number, keycloak_id, first_name, last_name, email,
     phone, academic_year, photo, description, birthdate, location, is_banned, created_at)
SELECT
    n,
    ((n - 1) % 12) + 1,
    'STU-' || LPAD(n::text, 4, '0'),
    'kc-user-' || LPAD(n::text, 4, '0'),
    (ARRAY['ليلى','محمد','سارة','خالد','هند','عمر','فاطمة','أحمد','نور','يوسف','مريم','عبدالله','رنا','سلمان','زينب'])[((n - 1) % 15) + 1],
    (ARRAY['العمري','الزهراني','القحطاني','الغامدي','المطيري','الحربي','الشهري','العتيبي','الدوسري','العنزي'])[((n - 1) % 10) + 1],
    'student' || n || '@school.edu',
    '050' || LPAD((1000000 + n)::text, 7, '0'),
    ((n - 1) % 5) + 1,
    'https://loremflickr.com/g/320/240/aleppo',
    'طالب متطوع شغوف بالعمل المجتمعي ومهتم بصنع فرق إيجابي في مجتمعه',
    DATE '2003-01-01' + ((n * 7) % 700),
    (ARRAY['الرياض','جدة','الدمام','مكة','المدينة المنورة','أبها','الطائف','تبوك'])[((n - 1) % 8) + 1],
    FALSE,
    TIMESTAMP '2023-02-01 09:00:00' + (n || ' hours')::interval
FROM generate_series(1, 45) AS n
ON CONFLICT DO NOTHING;

-- Admins (user_id 46-50)
INSERT INTO "user"
    (user_id, college_id, student_number, keycloak_id, first_name, last_name, email,
     phone, academic_year, photo, description, birthdate, location, is_banned, created_at)
SELECT
    n,
    ((n - 1) % 12) + 1,
    NULL,
    'kc-admin-' || LPAD((n - 45)::text, 3, '0'),
    (ARRAY['عمر','سعد','أحمد','خالد','سلطان'])[n - 45],
    'المشرف',
    'admin' || (n - 45) || '@school.edu',
    '0509' || LPAD((1000 + n)::text, 6, '0'),
    NULL,
    'https://loremflickr.com/g/320/240/aleppo',
    'مشرف على الحملات التطوعية',
    DATE '1988-01-01' + (n * 13) % 1000,
    'الرياض',
    FALSE,
    TIMESTAMP '2023-01-10 09:00:00' + (n || ' hours')::interval
FROM generate_series(46, 50) AS n
ON CONFLICT DO NOTHING;

-- Managers (user_id 51-55)
INSERT INTO "user"
    (user_id, college_id, student_number, keycloak_id, first_name, last_name, email,
     phone, academic_year, photo, description, birthdate, location, is_banned, created_at)
SELECT
    n,
    ((n - 1) % 12) + 1,
    NULL,
    'kc-manager-' || LPAD((n - 50)::text, 3, '0'),
    (ARRAY['فاطمة','نورة','مها','هدى','أمل'])[n - 50],
    'المديرة',
    'manager' || (n - 50) || '@school.edu',
    '0508' || LPAD((1000 + n)::text, 6, '0'),
    NULL,
    'https://loremflickr.com/g/320/240/aleppo',
    'مديرة برامج التطوع',
    DATE '1985-01-01' + (n * 11) % 1000,
    'جدة',
    FALSE,
    TIMESTAMP '2023-01-12 09:00:00' + (n || ' hours')::interval
FROM generate_series(51, 55) AS n
ON CONFLICT DO NOTHING;

-- ============================================================
-- 4. CAMPAIGN (50 rows)
-- ============================================================
INSERT INTO campaign
    (campaign_id, proposed_by_id, approved_by_id, managed_by_id, category_id,
     title, description, location, start_date, end_date, max_volunteers,
     photo, status, published_at, created_at, updated_at)
SELECT
    n,
    ((n - 1) % 45) + 1,                                  -- proposed by a student (1-45)
    46 + ((n - 1) % 5),                                  -- approved by admin (46-50)
    51 + ((n - 1) % 5),                                  -- managed by manager (51-55)
    ((n - 1) % 12) + 1,                                  -- category 1-12
    (ARRAY['زراعة الأشجار','التدريس للأطفال','التبرع بالدم','زيارة كبار السن',
           'تنظيف الشواطئ','تعبئة مستلزمات الإغاثة','حملة توعية صحية','جمع التبرعات',
           'ورشة برمجة','مبادرة الأمن الغذائي'])[((n - 1) % 10) + 1] || ' #' || n,
    'حملة تطوعية تهدف إلى خدمة المجتمع وتنمية مهارات المتطوعين رقم ' || n,
    (ARRAY['الحرم الجامعي','مركز المجتمع','المركز الصحي','دار الرعاية','شاطئ المدينة'])[((n - 1) % 5) + 1],
    DATE '2024-01-01' + (n * 5),
    DATE '2024-01-01' + (n * 5) + 7,
    20 + (n % 80),
    'https://loremflickr.com/g/320/240/aleppo?lock=' || n,
    (ARRAY['PENDING','APPROVED','ONGOING','COMPLETED','REJECTED'])[((n - 1) % 5) + 1]::campaign_status,
    CASE WHEN ((n - 1) % 6) + 1 IN (2,3,4) THEN TIMESTAMP '2024-01-01 08:00:00' + (n || ' days')::interval ELSE NULL END,
    TIMESTAMP '2023-12-01 08:00:00' + (n || ' hours')::interval,
    TIMESTAMP '2024-01-01 08:00:00' + (n || ' hours')::interval
FROM generate_series(1, 50) AS n
ON CONFLICT DO NOTHING;

-- ============================================================
-- 5. PROGRESS (60 rows: ~1-2 per campaign)
-- ============================================================
INSERT INTO progress
    (progress_id, campaign_id, updated_by_id, percentage, notes, created_at)
SELECT
    n,
    ((n - 1) % 50) + 1,                                  -- campaign 1-50 cycle
    51 + ((n - 1) % 5),                                  -- manager
    LEAST(100, ((n * 7) % 101)),
    'تحديث رقم ' || n || ': الحملة تسير بشكل جيد وفقاً للجدول الزمني',
    TIMESTAMP '2024-02-01 12:00:00' + (n || ' hours')::interval
FROM generate_series(1, 60) AS n
ON CONFLICT DO NOTHING;

-- ============================================================
-- 6. CAMPAIGN_PHOTO (100 rows: 2 per campaign — profile + progress)
-- ============================================================
-- Profile photos (no progress)
INSERT INTO campaign_photo
    (photo_id, campaign_id, progress_id, photo_url, uploaded_at)
SELECT
    n,
    ((n - 1) % 50) + 1,
    NULL,
    'https://loremflickr.com/g/320/240/aleppo?lock=' || (100 + n),
    TIMESTAMP '2024-02-01 09:00:00' + (n || ' hours')::interval
FROM generate_series(1, 50) AS n
ON CONFLICT DO NOTHING;

-- More profile photos (second photo per campaign so each has >1)
INSERT INTO campaign_photo
    (photo_id, campaign_id, progress_id, photo_url, uploaded_at)
SELECT
    n,
    ((n - 51) % 50) + 1,
    NULL,
    'https://loremflickr.com/g/320/240/aleppo?lock=' || (200 + n),
    TIMESTAMP '2024-02-15 09:00:00' + (n || ' hours')::interval
FROM generate_series(51, 100) AS n
ON CONFLICT DO NOTHING;

-- Progress photos
INSERT INTO campaign_photo
    (photo_id, campaign_id, progress_id, photo_url, uploaded_at)
SELECT
    n,
    ((n - 101) % 50) + 1,
    ((n - 101) % 60) + 1,
    'https://loremflickr.com/g/320/240/aleppo?lock=' || (300 + n),
    TIMESTAMP '2024-03-01 09:00:00' + (n || ' hours')::interval
FROM generate_series(101, 160) AS n
ON CONFLICT DO NOTHING;

-- ============================================================
-- 7. APPLICATION (50 rows)
-- Unique on (student_id, campaign_id); distribute 50 unique pairs
-- 10 students × 5 campaigns = 50 unique pairs
-- ============================================================
INSERT INTO application
    (id, student_id, campaign_id, reviewed_by_id, motivation_letter, status,
     admin_notes, rejection_reason, applied_at, reviewed_at)
SELECT
    n,
    ((n - 1) % 10) + 1,                                  -- students 1-10
    ((n - 1) / 10) + 1,                                  -- campaigns 1-5
    46 + ((n - 1) % 5),                                  -- reviewed by admin
    'لدي شغف كبير للمشاركة في هذه الحملة والمساهمة بفاعلية في تحقيق أهدافها رقم ' || n,
    (ARRAY['PENDING','APPROVED','REJECTED','WITHDRAWN'])[((n - 1) % 4) + 1]::application_status,
    CASE WHEN ((n - 1) % 4) + 1 IN (2) THEN 'مرشح ممتاز' ELSE NULL END,
    CASE WHEN ((n - 1) % 4) + 1 = 3 THEN 'لا يتوفر الوقت الكافي' ELSE NULL END,
    TIMESTAMP '2024-02-10 10:00:00' + (n || ' hours')::interval,
    CASE WHEN ((n - 1) % 4) + 1 IN (2, 3) THEN TIMESTAMP '2024-02-15 10:00:00' + (n || ' hours')::interval ELSE NULL END
FROM generate_series(1, 50) AS n
ON CONFLICT DO NOTHING;

-- ============================================================
-- 8. ATTENDANCE (60 rows)
-- Unique on (student_id, campaign_id, attendance_date)
-- 10 students × 6 distinct dates per student/campaign rotation
-- ============================================================
INSERT INTO attendance
    (attendance_id, student_id, campaign_id, recorded_by_id,
     attendance_date, status, hours_that_day, notes, recorded_at)
SELECT
    n,
    ((n - 1) % 10) + 1,                                                            -- students 1-10
    ((n - 1) / 10) + 1,                                                            -- campaigns 1-6
    51 + ((n - 1) % 5),                                                            -- manager
    DATE '2024-04-01' + ((n - 1) % 10),                                           -- distinct dates per student
    (ARRAY['PRESENT','ABSENT','EXCUSED','PRESENT'])[((n - 1) % 4) + 1]::attendance_status,
    CASE WHEN ((n - 1) % 4) + 1 = 2 THEN 0 ELSE 4 + ((n - 1) % 4) END,
    'سجل حضور رقم ' || n,
    TIMESTAMP '2024-04-01 18:00:00' + (n || ' hours')::interval
FROM generate_series(1, 60) AS n
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
