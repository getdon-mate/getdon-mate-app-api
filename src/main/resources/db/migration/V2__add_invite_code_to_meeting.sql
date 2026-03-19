-- ============================================================
-- V2 : meeting 테이블에 invite_code 컬럼 추가
-- ============================================================

-- 1. nullable로 먼저 추가 (기존 데이터 때문에 NOT NULL 바로 불가)
ALTER TABLE meeting
    ADD COLUMN invite_code VARCHAR(8);

-- 2. 기존 데이터에 고유한 invite_code 부여
UPDATE meeting
SET invite_code = UPPER(SUBSTRING(MD5(RANDOM()::TEXT), 1, 8));

-- 3. NOT NULL 제약 + UNIQUE 추가
ALTER TABLE meeting
    ALTER COLUMN invite_code SET NOT NULL,
    ADD CONSTRAINT uq_meeting_invite_code UNIQUE (invite_code);