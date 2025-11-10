-- V1__create_vacancy.sql
CREATE SEQUENCE IF NOT EXISTS vacancy_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS vacancy (
    id BIGINT PRIMARY KEY DEFAULT nextval('vacancy_seq'),
    title VARCHAR(255) NOT NULL,
    description TEXT,
    location VARCHAR(150),
    job_format VARCHAR(20),
    job_type VARCHAR(20),
    status VARCHAR(20)
    );