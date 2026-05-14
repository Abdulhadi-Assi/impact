-- ============================================================
-- 02-init-data.sql — Sample data for Online School Volunteer System
-- Order matters: parent tables must be inserted before children
-- ============================================================

-- ============================================================
-- 1. COLLEGE
-- ============================================================
INSERT INTO college (college_id, name, created_at) VALUES
(1, 'كلية الهندسة',         '2023-01-10 08:00:00'),
(2, 'كلية إدارة الأعمال',   '2023-01-11 08:00:00'),
(3, 'كلية الآداب والعلوم',  '2023-01-12 08:00:00'),
(4, 'كلية التربية',         '2023-01-13 08:00:00'),
(5, 'كلية العلوم الصحية',   '2023-01-14 08:00:00')
ON CONFLICT DO NOTHING;

-- ============================================================
-- 2. USER
-- ============================================================
INSERT INTO "user" (user_id, college_id, student_number, keycloak_id, first_name, last_name, email, phone, academic_year, photo, birthdate, location, is_banned, created_at, updated_at) VALUES
-- طلاب
(1,  1, 'STU-001', 'kc-user-001', 'ليلى',   'العمري',    'layla.omari@school.edu',      '0501234567', 2, 'https://i.pravatar.cc/400?u=layla.omari@school.edu',      '2003-05-12', 'الرياض', FALSE, '2023-02-01 09:00:00', NULL),
(2,  2, 'STU-002', 'kc-user-002', 'محمد',   'الزهراني',  'mohammed.zahrani@school.edu', '0502345678', 3, 'https://i.pravatar.cc/400?u=mohammed.zahrani@school.edu', '2002-11-21', 'جدة', FALSE, '2023-02-02 09:00:00', NULL),
(3,  3, 'STU-003', 'kc-user-003', 'سارة',   'القحطاني',  'sara.qahtani@school.edu',     '0503456789', 1, 'https://i.pravatar.cc/400?u=sara.qahtani@school.edu',     '2004-02-08', 'الدمام', FALSE, '2023-02-03 09:00:00', NULL),
(4,  4, 'STU-004', 'kc-user-004', 'خالد',   'الغامدي',   'khalid.ghamdi@school.edu',    '0504567890', 4, 'https://i.pravatar.cc/400?u=khalid.ghamdi@school.edu',    '2001-09-30', 'مكة', FALSE, '2023-02-04 09:00:00', NULL),
(5,  5, 'STU-005', 'kc-user-005', 'هند',    'المطيري',   'hind.mutairi@school.edu',     '0505678901', 2, 'https://i.pravatar.cc/400?u=hind.mutairi@school.edu',     '2003-12-14', 'المدينة المنورة', TRUE,  '2023-02-05 09:00:00', NULL),
-- مشرفون ومديرون
(6,  1, NULL, 'kc-admin-001',   'عمر',    'المشرف',    'omar.admin@school.edu',       '0506789012', NULL, 'https://i.pravatar.cc/400?u=omar.admin@school.edu',       '1990-07-05', 'الرياض', FALSE, '2023-01-15 09:00:00', NULL),
(7,  2, NULL, 'kc-manager-001', 'فاطمة',  'المديرة',   'fatima.manager@school.edu',   '0507890123', NULL, 'https://i.pravatar.cc/400?u=fatima.manager@school.edu',   '1988-03-18', 'جدة', FALSE, '2023-01-16 09:00:00', NULL),
(8,  3, NULL, 'kc-admin-002',   'أحمد',   'المشرف',    'ahmed.admin@school.edu',      '0508901234', NULL, 'https://i.pravatar.cc/400?u=ahmed.admin@school.edu',      '1991-10-22', 'الدمام', FALSE, '2023-01-17 09:00:00', NULL)
ON CONFLICT DO NOTHING;

-- ============================================================
-- 3. CATEGORY
-- ============================================================
INSERT INTO category (category_id, name, created_at) VALUES
(1, 'البيئة',           '2023-03-01 10:00:00'),
(2, 'التعليم',          '2023-03-02 10:00:00'),
(3, 'الرعاية الصحية',  '2023-03-03 10:00:00'),
(4, 'خدمة المجتمع',    '2023-03-04 10:00:00'),
(5, 'الإغاثة من الكوارث', '2023-03-05 10:00:00')
ON CONFLICT DO NOTHING;

-- ============================================================
-- 4. CAMPAIGN
-- ============================================================
INSERT INTO campaign (campaign_id, proposed_by_id, approved_by_id, managed_by_id, category_id, title, description, location, start_date, end_date, max_volunteers, photo, status, published_at, created_at, updated_at) VALUES
(1, 1, 6, 7, 1, 'زراعة الأشجار في الحرم الجامعي', 'زراعة 500 شجرة في أرجاء الحرم الجامعي',                        'الحرم الجامعي الرئيسي', '2024-03-01', '2024-03-03', 50,  'https://storage.school.edu/campaigns/tree-campaign.jpg',       'COMPLETED', '2024-02-01 08:00:00', '2024-01-20 08:00:00', '2024-03-04 08:00:00'),
(2, 2, 6, 7, 2, 'التدريس للأطفال',                 'جلسات تدريس مجانية للأطفال المحتاجين',                          'مركز المجتمع',          '2024-04-10', '2024-04-20', 30,  'https://storage.school.edu/campaigns/tutoring-campaign.jpg',    'ONGOING',   '2024-03-15 08:00:00', '2024-03-01 08:00:00', '2024-04-10 08:00:00'),
(3, 3, 8, 7, 3, 'حملة التبرع بالدم',               'تنظيم فعالية تبرع بالدم داخل الحرم الجامعي',                    'المركز الصحي',          '2024-05-05', '2024-05-05', 100, 'https://storage.school.edu/campaigns/blood-drive.jpg',         'APPROVED',  '2024-04-01 08:00:00', '2024-03-20 08:00:00', '2024-04-01 08:00:00'),
(4, 4, 6, 7, 4, 'زيارة دار رعاية المسنين',         'قضاء وقت مع كبار السن في دار الرعاية المحلية',                  'دار الرعاية الذهبية',   '2024-06-01', '2024-06-02', 20,  'https://storage.school.edu/campaigns/elderly-home.jpg',        'PENDING',   NULL,                  '2024-04-15 08:00:00', '2024-04-15 08:00:00'),
(5, 1, 8, 7, 5, 'تعبئة مستلزمات الإغاثة',          'تعبئة مستلزمات الإغاثة للأسر المتضررة من الفيضانات',            'قاعة الرياضة',          '2024-07-01', '2024-07-03', 80,  'https://storage.school.edu/campaigns/relief-pack.jpg',         'PENDING',   NULL,                  '2024-05-01 08:00:00', '2024-05-01 08:00:00'),
(6, 2, 6, 7, 1, 'تنظيف الشاطئ',                    'تنظيف النفايات على طول شاطئ المدينة',                           'شاطئ المدينة',          '2024-08-10', '2024-08-10', 60,  'https://storage.school.edu/campaigns/beach-cleanup.jpg',       'APPROVED',  '2024-07-01 08:00:00', '2024-06-15 08:00:00', '2024-07-01 08:00:00')
ON CONFLICT DO NOTHING;

-- ============================================================
-- 5. PROGRESS
-- ============================================================
INSERT INTO progress (progress_id, campaign_id, updated_by_id, percentage, notes, created_at, updated_at) VALUES
(1, 1, 7, 100, 'تمت زراعة 500 شجرة بنجاح',                     '2024-03-03 17:00:00', NULL),
(2, 2, 7, 45,  'جلسات التدريس مستمرة، تم إنجاز النصف',         '2024-04-15 17:00:00', NULL),
(3, 3, 7, 10,  'تم حجز المكان، جارٍ تسجيل المتطوعين',          '2024-04-10 17:00:00', NULL),
(4, 4, 7, 0,   'الحملة في انتظار الموافقة',                     '2024-04-15 17:00:00', NULL),
(5, 6, 7, 20,  'تم تأكيد المتطوعين، جارٍ توفير المستلزمات',    '2024-07-05 17:00:00', NULL)
ON CONFLICT DO NOTHING;

-- ============================================================
-- 6. CAMPAIGN_PHOTO
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
-- ============================================================
INSERT INTO application (id, student_id, campaign_id, reviewed_by_id, motivation_letter, status, admin_notes, rejection_reason, applied_at, reviewed_at, removed_by_id, removal_reason, removed_at) VALUES
(1, 1, 1, 6,    'أنا شغوف بالبيئة وأرغب في المساهمة في هذه الحملة.',          'APPROVED', 'مرشح ممتاز',            NULL,                              '2024-02-10 10:00:00', '2024-02-15 10:00:00', NULL, NULL, NULL),
(2, 2, 1, 6,    'لديّ خبرة في البستنة والأنشطة الخارجية.',                    'APPROVED', 'خبرة جيدة',             NULL,                              '2024-02-11 10:00:00', '2024-02-15 10:00:00', NULL, NULL, NULL),
(3, 3, 2, 8,    'أحب التدريس ومساعدة الطلاب على النجاح.',                     'APPROVED', 'مقبول للتدريس',         NULL,                              '2024-03-20 10:00:00', '2024-03-25 10:00:00', NULL, NULL, NULL),
(4, 4, 2, 8,    'درّست الطلاب في الرياضيات والعلوم من قبل.',                  'REJECTED', NULL,                    'عدم توافر الوقت الكافي',          '2024-03-21 10:00:00', '2024-03-25 10:00:00', NULL, NULL, NULL),
(5, 5, 3, 6,    'أرغب في المساعدة في تنظيم فعالية التبرع بالدم.',             'PENDING',  NULL,                     NULL,                              '2024-04-05 10:00:00', NULL,                  NULL, NULL, NULL),
(6, 1, 6, NULL, 'تنظيف الشواطئ أمر يقع في قلبي.',                            'PENDING',  NULL,                     NULL,                              '2024-06-20 10:00:00', NULL,                  NULL, NULL, NULL),
(7, 2, 6, 6,    'أريد المساهمة في الحفاظ على نظافة شواطئنا للأجيال القادمة.', 'APPROVED', 'متطوع مؤكد',            NULL,                              '2024-06-21 10:00:00', '2024-06-25 10:00:00', NULL, NULL, NULL),
(8, 3, 1, 6,    'التطوع البيئي يتوافق مع تخصصي الدراسي.',                    'WITHDRAWN', 'انسحب في اللحظة الأخيرة', NULL,                             '2024-02-12 10:00:00', '2024-02-15 10:00:00', 6,    'انسحب المتطوع دون إشعار مسبق', '2024-02-28 10:00:00')
ON CONFLICT DO NOTHING;

-- ============================================================
-- 8. ATTENDANCE
-- ============================================================
INSERT INTO attendance (attendance_id, student_id, campaign_id, recorded_by_id, attendance_date, status, hours_that_day, notes, recorded_at) VALUES
(1, 1, 1, 7, '2024-03-01', 'PRESENT', 6, 'حضر في الوقت المحدد وعمل طوال اليوم',         '2024-03-01 18:00:00'),
(2, 1, 1, 7, '2024-03-02', 'PRESENT', 5, 'واصل العمل في زراعة الأشجار',                  '2024-03-02 18:00:00'),
(3, 2, 1, 7, '2024-03-01', 'PRESENT', 6, 'كان مفيداً جداً أثناء الزراعة',               '2024-03-01 18:00:00'),
(4, 2, 1, 7, '2024-03-02', 'ABSENT',  0, 'لم يحضر ولم يُبلّغ مسبقاً',                   '2024-03-02 18:00:00'),
(5, 3, 2, 7, '2024-04-10', 'PRESENT', 4, 'قاد مجموعة تدريسية بفاعلية',                  '2024-04-10 18:00:00'),
(6, 3, 2, 7, '2024-04-11', 'PRESENT', 4, 'واصل جلسات التدريس',                           '2024-04-11 18:00:00'),
(7, 1, 6, 7, '2024-08-10', 'PRESENT', 5, 'جمع 10 أكياس من النفايات',                    '2024-08-10 18:00:00'),
(8, 2, 6, 7, '2024-08-10', 'EXCUSED', 0, 'كان لديه موعد طبي وأبلغ مسبقاً',              '2024-08-10 18:00:00')
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
