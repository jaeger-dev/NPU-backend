-- Drop existing tables if they exist
DROP TABLE IF EXISTS ratings CASCADE;
DROP TABLE IF EXISTS creation_elements CASCADE;
DROP TABLE IF EXISTS creations CASCADE;
DROP TABLE IF EXISTS elements CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- Create tables

CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS elements (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    color VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS creations (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    image_url TEXT,
    user_id BIGINT REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE creation_elements (
    creation_id BIGINT REFERENCES creations(id) ON DELETE CASCADE,
    element_id BIGINT REFERENCES elements(id) ON DELETE CASCADE,
    PRIMARY KEY (creation_id, element_id)
);

CREATE TABLE ratings (
    id BIGSERIAL PRIMARY KEY,
    creativity_score INTEGER NOT NULL CHECK (creativity_score BETWEEN 1 AND 5),
    uniqueness_score INTEGER NOT NULL CHECK (uniqueness_score BETWEEN 1 AND 5),
    rated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    creation_id BIGINT REFERENCES creations(id) ON DELETE CASCADE,
    rated_by BIGINT REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE (creation_id, rated_by)
);