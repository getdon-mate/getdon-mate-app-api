-- ============================================================
-- V3 : users 테이블 NOT NULL 제약 추가
-- ============================================================

ALTER TABLE users
    ALTER COLUMN user_name SET NOT NULL,
    ALTER COLUMN email     SET NOT NULL,
    ALTER COLUMN password  SET NOT NULL;