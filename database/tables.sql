/* In this file are defined the tables of the database */
CREATE DATABASE IF NOT EXISTS bandsync;

USE bandsync;

-- Table: musical_bands
CREATE TABLE IF NOT EXISTS musical_bands (
    id BINARY(16) PRIMARY KEY DEFAULT(UUID_TO_BIN(UUID())),
    name VARCHAR(100) NOT NULL UNIQUE,
    hyphenated_name VARCHAR(100) NOT NULL UNIQUE,
    logo VARCHAR(255) NOT NULL DEFAULT '',
    address VARCHAR(255) NOT NULL DEFAULT '',
    phone VARCHAR(25) NOT NULL DEFAULT '',
    email VARCHAR(50) NOT NULL UNIQUE,
    status BIT NOT NULL DEFAULT 1
);

-- Table: roles
CREATE TABLE IF NOT EXISTS roles (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    musical_band_id BINARY(16) NOT NULL,
    FOREIGN KEY (musical_band_id) REFERENCES musical_bands (id),
    UNIQUE (musical_band_id, name),
    status BIT NOT NULL DEFAULT 1
);

-- table: types_permissions
CREATE TABLE IF NOT EXISTS types_permissions (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    status BIT NOT NULL DEFAULT 1
);

-- Table: permissions
CREATE TABLE IF NOT EXISTS permissions (
    id INT PRIMARY KEY AUTO_INCREMENT,
    type_permission_id INT NOT NULL,
    FOREIGN KEY (type_permission_id) REFERENCES types_permissions (id),
    name VARCHAR(100) NOT NULL UNIQUE,
    status BIT NOT NULL DEFAULT 1
);

-- Table: roles_permissions
CREATE TABLE IF NOT EXISTS roles_permissions (
    role_id INT NOT NULL,
    permission_id INT NOT NULL,
    FOREIGN KEY (role_id) REFERENCES roles (id),
    FOREIGN KEY (permission_id) REFERENCES permissions (id),
    PRIMARY KEY (role_id, permission_id),
    status BIT NOT NULL DEFAULT 1
);

-- Table: musical_roles
CREATE TABLE IF NOT EXISTS musical_roles (
    id INT PRIMARY KEY AUTO_INCREMENT,
    musical_band_id BINARY(16) NOT NULL,
    FOREIGN KEY (musical_band_id) REFERENCES musical_bands (id),
    name VARCHAR(100) NOT NULL,
    UNIQUE (musical_band_id, name),
    status BIT NOT NULL DEFAULT 1
);

-- Table: users
CREATE TABLE IF NOT EXISTS users (
    id BINARY(16) PRIMARY KEY DEFAULT(UUID_TO_BIN(UUID())),
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NULL,
    firstname VARCHAR(100) NOT NULL DEFAULT '',
    lastname VARCHAR(100) NOT NULL DEFAULT '',
    phone VARCHAR(25) NOT NULL DEFAULT '',
    photo VARCHAR(255) NOT NULL DEFAULT '',
    email VARCHAR(50) NOT NULL UNIQUE,
    status BIT NOT NULL DEFAULT 1,
    secret_2fa VARCHAR(255),
    is_2fa_enabled BIT DEFAULT 0
);

-- Table: users_roles
CREATE TABLE IF NOT EXISTS users_roles (
    user_id BINARY(16) NOT NULL,
    role_id INT NOT NULL,
    musical_band_id BINARY(16) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (role_id) REFERENCES roles (id),
    FOREIGN KEY (musical_band_id) REFERENCES musical_bands (id),
    PRIMARY KEY (
        user_id,
        role_id,
        musical_band_id
    ),
    status BIT NOT NULL DEFAULT 1
);

-- Table: musical_roles_users
CREATE TABLE IF NOT EXISTS musical_roles_users (
    musical_role_id INT NOT NULL,
    user_id BINARY(16) NOT NULL,
    musical_band_id BINARY(16) NOT NULL,
    FOREIGN KEY (musical_role_id) REFERENCES musical_roles (id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (musical_band_id) REFERENCES musical_bands (id),
    PRIMARY KEY (
        musical_role_id,
        user_id,
        musical_band_id
    ),
    status BIT NOT NULL DEFAULT 1
);

-- Table: users_musical_bands
CREATE TABLE IF NOT EXISTS users_musical_bands (
    user_id BINARY(16) NOT NULL,
    musical_band_id BINARY(16) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (musical_band_id) REFERENCES musical_bands (id),
    PRIMARY KEY (user_id, musical_band_id),
    status BIT NOT NULL DEFAULT 1
);

-- Table: invitations
CREATE TABLE IF NOT EXISTS invitations (
    id BINARY(16) PRIMARY KEY DEFAULT(UUID_TO_BIN(UUID())),
    musical_band_id BINARY(16) NOT NULL,
    invited_by BINARY(16) NOT NULL,
    token VARCHAR(500) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NOT NULL,
    FOREIGN KEY (musical_band_id) REFERENCES musical_bands (id),
    FOREIGN KEY (invited_by) REFERENCES users (id)
);

CREATE INDEX idx_invitations_email_band_status ON invitations (
    email,
    musical_band_id,
    status
);

-- Table: Artists
CREATE TABLE IF NOT EXISTS artists (
    id INT PRIMARY KEY AUTO_INCREMENT,
    musical_band_id BINARY(16) NOT NULL,
    FOREIGN KEY (musical_band_id) REFERENCES musical_bands (id),
    name VARCHAR(100) NOT NULL,
    UNIQUE (musical_band_id, name),
    status BIT NOT NULL DEFAULT 1
);

CREATE INDEX artist_name_index ON artists (name);

-- Table: musical_genres
CREATE TABLE IF NOT EXISTS musical_genres (
    id INT PRIMARY KEY AUTO_INCREMENT,
    musical_band_id BINARY(16) NOT NULL,
    FOREIGN KEY (musical_band_id) REFERENCES musical_bands (id),
    name VARCHAR(100) NOT NULL,
    UNIQUE (musical_band_id, name),
    status BIT NOT NULL DEFAULT 1
);

CREATE INDEX musical_genre_name_index ON musical_genres (name);

-- Table: songs
CREATE TABLE IF NOT EXISTS songs (
    id INT PRIMARY KEY AUTO_INCREMENT,
    artist_id INT NOT NULL,
    musical_genre_id INT NOT NULL,
    musical_band_id BINARY(16) NOT NULL,
    FOREIGN KEY (artist_id) REFERENCES artists (id),
    FOREIGN KEY (musical_genre_id) REFERENCES musical_genres (id),
    FOREIGN KEY (musical_band_id) REFERENCES musical_bands (id),
    tonality VARCHAR(25) NOT NULL DEFAULT '',
    link VARCHAR(255) NOT NULL DEFAULT '',
    sheet_music VARCHAR(255) NOT NULL DEFAULT '',
    name VARCHAR(100) NOT NULL,
    status BIT NOT NULL DEFAULT 1
);

CREATE INDEX song_name_index ON songs (name);

-- Table: repertoires
CREATE TABLE IF NOT EXISTS repertoires (
    id BINARY(16) PRIMARY KEY DEFAULT(UUID_TO_BIN(UUID())),
    musical_band_id BINARY(16) NOT NULL,
    FOREIGN KEY (musical_band_id) REFERENCES musical_bands (id),
    name VARCHAR(100) NOT NULL,
    UNIQUE (musical_band_id, name),
    description TEXT,
    link VARCHAR(255) NOT NULL DEFAULT '',
    status BIT NOT NULL DEFAULT 1
);

-- Table: repertoires_songs
CREATE TABLE IF NOT EXISTS repertoires_songs (
    repertoire_id BINARY(16) NOT NULL,
    song_id INT NOT NULL,
    FOREIGN KEY (repertoire_id) REFERENCES repertoires (id),
    FOREIGN KEY (song_id) REFERENCES songs (id),
    PRIMARY KEY (repertoire_id, song_id),
    status BIT NOT NULL DEFAULT 1
);

-- Table: events
CREATE TABLE IF NOT EXISTS events (
    id BINARY(16) PRIMARY KEY DEFAULT(UUID_TO_BIN(UUID())),
    musical_band_id BINARY(16) NOT NULL,
    repertoire_id BINARY(16) NOT NULL,
    FOREIGN KEY (musical_band_id) REFERENCES musical_bands (id),
    FOREIGN KEY (repertoire_id) REFERENCES repertoires (id),
    name VARCHAR(100) NOT NULL,
    description TEXT,
    date DATETIME NOT NULL,
    place VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL DEFAULT '',
    status BIT NOT NULL DEFAULT 1
);

-- Table: password_reset_tokens
CREATE TABLE IF NOT EXISTS password_reset_tokens (
    token BINARY(16) PRIMARY KEY DEFAULT(UUID_TO_BIN(UUID())),
    user_id BINARY(16) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id),
    expiration_date DATETIME NOT NULL,
    used BIT NOT NULL DEFAULT 0
);