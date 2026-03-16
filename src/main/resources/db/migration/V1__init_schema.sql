-- ============================================================
-- V1 : 초기 스키마 생성
-- ============================================================

CREATE TABLE IF NOT EXISTS users (
    user_id        BIGSERIAL PRIMARY KEY,
    user_name      VARCHAR(50),
    profile_url    VARCHAR(255),
    email          VARCHAR(255),
    password       VARCHAR(255),
    provider       VARCHAR(6)  NOT NULL,
    provider_id    VARCHAR(255),
    use_yn         VARCHAR(1)  NOT NULL DEFAULT 'Y',
    create_dt      TIMESTAMP   NOT NULL,
    update_dt      TIMESTAMP   NOT NULL
);

CREATE TABLE IF NOT EXISTS meeting (
    meeting_id   BIGSERIAL PRIMARY KEY,
    user_id      BIGINT      NOT NULL REFERENCES users(user_id),
    title        VARCHAR(50) NOT NULL,
    bank_name    VARCHAR(20) NOT NULL,
    bank_account INTEGER     NOT NULL,
    amount       INTEGER     NOT NULL DEFAULT 0,
    delete_yn    VARCHAR(1)  NOT NULL DEFAULT 'N',
    create_dt    TIMESTAMP   NOT NULL,
    update_dt    TIMESTAMP   NOT NULL
);

CREATE TABLE IF NOT EXISTS meeting_member (
    meeting_member_id BIGSERIAL PRIMARY KEY,
    user_id           BIGINT     NOT NULL REFERENCES users(user_id),
    meeting_id        BIGINT     NOT NULL REFERENCES meeting(meeting_id),
    role              VARCHAR(10) NOT NULL,
    withdrawal_yn     VARCHAR(1)  NOT NULL DEFAULT 'N',
    create_dt         TIMESTAMP   NOT NULL,
    update_dt         TIMESTAMP   NOT NULL
);

CREATE TABLE IF NOT EXISTS deposit (
    deposit_id        BIGSERIAL PRIMARY KEY,
    meeting_member_id BIGINT    NOT NULL REFERENCES meeting_member(meeting_member_id),
    deposit_amount    INTEGER   NOT NULL DEFAULT 0,
    create_dt         TIMESTAMP NOT NULL,
    update_dt         TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS withdrawal (
    withdrawal_id     BIGSERIAL PRIMARY KEY,
    meeting_member_id BIGINT    NOT NULL REFERENCES meeting_member(meeting_member_id),
    withdrawal_amount INTEGER   NOT NULL,
    create_dt         TIMESTAMP NOT NULL,
    update_dt         TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS automatic_payment (
    automatic_payment_id BIGSERIAL PRIMARY KEY,
    meeting_member_id    BIGINT    NOT NULL REFERENCES meeting_member(meeting_member_id),
    payment_amount       INTEGER   NOT NULL DEFAULT 0,
    payment_day          INTEGER   NOT NULL,
    active_yn            VARCHAR(1) NOT NULL DEFAULT 'N',
    create_dt            TIMESTAMP NOT NULL,
    update_dt            TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS one_time_payment (
    one_time_payment_id BIGSERIAL PRIMARY KEY,
    meeting_id          BIGINT    NOT NULL REFERENCES meeting(meeting_id),
    meeting_member_id   BIGINT    NOT NULL REFERENCES meeting_member(meeting_member_id),
    amount              INTEGER   NOT NULL,
    deadline            DATE      NOT NULL,
    create_dt           TIMESTAMP NOT NULL,
    update_dt           TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS one_time_payment_member (
    one_time_payment_member_id BIGSERIAL PRIMARY KEY,
    one_time_payment_id        BIGINT    NOT NULL REFERENCES one_time_payment(one_time_payment_id),
    meeting_member_id          BIGINT    NOT NULL REFERENCES meeting_member(meeting_member_id),
    create_dt                  TIMESTAMP NOT NULL,
    update_dt                  TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS payment_history (
    payment_history_id         BIGSERIAL PRIMARY KEY,
    meeting_member_id          BIGINT     NOT NULL REFERENCES meeting_member(meeting_member_id),
    automatic_payment_id       BIGINT     REFERENCES automatic_payment(automatic_payment_id),
    one_time_fee_member_id     BIGINT     REFERENCES one_time_payment_member(one_time_payment_member_id),
    amount                     INTEGER    NOT NULL DEFAULT 0,
    payment_status             VARCHAR(10),
    create_dt                  TIMESTAMP  NOT NULL,
    update_dt                  TIMESTAMP  NOT NULL
);
