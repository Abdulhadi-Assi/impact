-- ============================================================
-- 01-init-schema.sql — Online School Volunteer System
-- PostgreSQL — run this BEFORE 02-init-data.sql
-- ============================================================

-- ============================================================
-- 1. COLLEGE
-- ============================================================
CREATE TABLE IF NOT EXISTS college (
    college_id   SERIAL       PRIMARY KEY,
    name         VARCHAR(150) NOT NULL,
    description  TEXT,
    created_at   TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMP
);

-- ============================================================
-- 2. USER
-- ============================================================
CREATE TABLE IF NOT EXISTS "user" (
    user_id        SERIAL       PRIMARY KEY,
    college_id     INT          REFERENCES college(college_id) ON DELETE SET NULL,
    student_number VARCHAR(20)  UNIQUE,
    keycloak_id    VARCHAR(100) UNIQUE NOT NULL,
    first_name     VARCHAR(100) NOT NULL,
    last_name      VARCHAR(100) NOT NULL,
    email          VARCHAR(150) UNIQUE NOT NULL,
    phone          VARCHAR(30),
    academic_year  SMALLINT,
    is_banned      BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at     TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at     TIMESTAMP
);

-- ============================================================
-- 3. CATEGORY
-- ============================================================
CREATE TABLE IF NOT EXISTS category (
    category_id SERIAL       PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE,
    created_at  TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP
);

-- ============================================================
-- 4. CAMPAIGN
-- ============================================================
CREATE TYPE campaign_status AS ENUM (
    'PENDING',
    'APPROVED',
    'ONGOING',
    'COMPLETED',
    'REJECTED',
    'CANCELLED'
);

CREATE TABLE IF NOT EXISTS campaign (
    campaign_id     SERIAL          PRIMARY KEY,
    proposed_by_id  INT             NOT NULL REFERENCES "user"(user_id) ON DELETE RESTRICT,
    approved_by_id  INT             REFERENCES "user"(user_id) ON DELETE SET NULL,
    managed_by_id   INT             REFERENCES "user"(user_id) ON DELETE SET NULL,
    category_id     INT             REFERENCES category(category_id) ON DELETE SET NULL,
    title           VARCHAR(200)    NOT NULL,
    description     TEXT,
    location        VARCHAR(200),
    start_date      DATE,
    end_date        DATE,
    max_volunteers  INT,
    status          campaign_status NOT NULL DEFAULT 'PENDING',
    published_at    TIMESTAMP,
    created_at      TIMESTAMP       NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP
);

-- ============================================================
-- 5. PROGRESS
-- ============================================================
CREATE TABLE IF NOT EXISTS progress (
    progress_id   SERIAL    PRIMARY KEY,
    campaign_id   INT       NOT NULL REFERENCES campaign(campaign_id) ON DELETE CASCADE,
    updated_by_id INT       REFERENCES "user"(user_id) ON DELETE SET NULL,
    percentage    SMALLINT  NOT NULL CHECK (percentage BETWEEN 0 AND 100),
    notes         TEXT,
    created_at    TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMP
);

-- ============================================================
-- 6. CAMPAIGN_PHOTO
-- ============================================================
CREATE TABLE IF NOT EXISTS campaign_photo (
    photo_id    SERIAL       PRIMARY KEY,
    campaign_id INT          NOT NULL REFERENCES campaign(campaign_id) ON DELETE CASCADE,
    progress_id INT          REFERENCES progress(progress_id) ON DELETE SET NULL,
    photo_url   VARCHAR(500) NOT NULL,
    uploaded_at TIMESTAMP    NOT NULL DEFAULT NOW(),
    created_at  TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP
);

-- ============================================================
-- 7. APPLICATION
-- ============================================================
CREATE TYPE application_status AS ENUM (
    'PENDING',
    'APPROVED',
    'REJECTED',
    'WITHDRAWN'
);

CREATE TABLE IF NOT EXISTS application (
    id                SERIAL             PRIMARY KEY,
    student_id        INT                NOT NULL REFERENCES "user"(user_id) ON DELETE CASCADE,
    campaign_id       INT                NOT NULL REFERENCES campaign(campaign_id) ON DELETE CASCADE,
    reviewed_by_id    INT                REFERENCES "user"(user_id) ON DELETE SET NULL,
    motivation_letter TEXT,
    status            application_status NOT NULL DEFAULT 'PENDING',
    admin_notes       TEXT,
    rejection_reason  TEXT,
    applied_at        TIMESTAMP          NOT NULL DEFAULT NOW(),
    reviewed_at       TIMESTAMP,
    removed_by_id     INT                REFERENCES "user"(user_id) ON DELETE SET NULL,
    removal_reason    TEXT,
    removed_at        TIMESTAMP,
    created_at        TIMESTAMP          NOT NULL DEFAULT NOW(),
    updated_at        TIMESTAMP,
    UNIQUE (student_id, campaign_id)
);

-- ============================================================
-- 8. ATTENDANCE
-- ============================================================
CREATE TYPE attendance_status AS ENUM (
    'PRESENT',
    'ABSENT',
    'EXCUSED'
);

CREATE TABLE IF NOT EXISTS attendance (
    attendance_id   SERIAL            PRIMARY KEY,
    student_id      INT               NOT NULL REFERENCES "user"(user_id) ON DELETE CASCADE,
    campaign_id     INT               NOT NULL REFERENCES campaign(campaign_id) ON DELETE CASCADE,
    recorded_by_id  INT               REFERENCES "user"(user_id) ON DELETE SET NULL,
    attendance_date DATE              NOT NULL,
    status          attendance_status NOT NULL,
    hours_that_day  SMALLINT          NOT NULL DEFAULT 0,
    notes           TEXT,
    recorded_at     TIMESTAMP         NOT NULL DEFAULT NOW(),
    created_at      TIMESTAMP         NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP,
    UNIQUE (student_id, campaign_id, attendance_date)
);
