/* In this file are defined the tables of the database */

-- Table: roles
CREATE TABLE roles (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    status BINARY(1) NOT NULL DEFAULT 1
);

-- Table: permissions
CREATE TABLE permissions (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    status BINARY(1) NOT NULL DEFAULT 1
);

-- Table: roles_permissions
CREATE TABLE roles_permissions (
    role_id INT NOT NULL,
    permission_id INT NOT NULL,
    FOREIGN KEY (role_id) REFERENCES roles(id),
    FOREIGN KEY (permission_id) REFERENCES permissions(id),
    PRIMARY KEY (role_id, permission_id)
    status BINARY(1) NOT NULL DEFAULT 1
);

-- Table: musical_bands
CREATE TABLE musical_bands (
    id BINARY (16) PRIMARY KEY DEFAULT (UUID_TO_BIN(UUID())),
    name VARCHAR(100) NOT NULL UNIQUE,
    logo VARCHAR(255) NOT NULL DEFAULT '',
    address VARCHAR(255) NOT NULL DEFAULT '',
    phone VARCHAR(25) NOT NULL DEFAULT '',
    email VARCHAR(50) NOT NULL UNIQUE,
    status BINARY(1) NOT NULL DEFAULT 1
);

-- Table: musical_roles
CREATE TABLE musical_roles (
    id INT PRIMARY KEY AUTO_INCREMENT,
    musical_band_id BINARY(16) NOT NULL,
    FOREIGN KEY (musical_band_id) REFERENCES musical_bands(id),
    name VARCHAR(100) NOT NULL,
    status BINARY(1) NOT NULL DEFAULT 1
);
CREATE INDEX nusical_role_name_index ON musical_roles(name);

-- Table: users
CREATE TABLE users (
    id BINARY (16) PRIMARY KEY DEFAULT (UUID_TO_BIN(UUID())),
    role_id INT NOT NULL,
    FOREIGN KEY (role_id) REFERENCES roles(id),
    name VARCHAR(100) NOT NULL DEFAULT '',
    lastname VARCHAR(100) NOT NULL DEFAULT '',
    phone VARCHAR(25) NOT NULL DEFAULT '',
    photo VARCHAR(255) NOT NULL DEFAULT '',
    email VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL,
    status BINARY(1) NOT NULL DEFAULT 1
);

-- Table: musical_roles_users
CREATE TABLE musical_roles_users (
    musical_role_id INT NOT NULL,
    user_id BINARY(16) NOT NULL,
    musical_band_id BINARY(16) NOT NULL,
    FOREIGN KEY (musical_role_id) REFERENCES musical_roles(id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (musical_band_id) REFERENCES musical_bands(id),
    PRIMARY KEY (musical_role_id, user_id, musical_band_id),
    status BINARY(1) NOT NULL DEFAULT 1
);

-- Table: users_musical_bands
CREATE TABLE users_musical_bands (
    user_id BINARY(16) NOT NULL,
    musical_band_id BINARY(16) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (musical_band_id) REFERENCES musical_bands(id),
    PRIMARY KEY (user_id, musical_band_id),
    status BINARY(1) NOT NULL DEFAULT 1,
);

-- Table: Artists
CREATE TABLE artists (
    id INT PRIMARY KEY AUTO_INCREMENT,
    musical_band_id BINARY(16) NOT NULL,
    FOREIGN KEY (musical_band_id) REFERENCES musical_bands(id),
    name VARCHAR(100) NOT NULL UNIQUE,
    status BINARY(1) NOT NULL DEFAULT 1
);
CREATE INDEX artist_name_index ON artists(name);

-- Table: musical_genres
CREATE TABLE musical_genres (
    id INT PRIMARY KEY AUTO_INCREMENT,
    musical_band_id BINARY(16) NOT NULL,
    FOREIGN KEY (musical_band_id) REFERENCES musical_bands(id),
    name VARCHAR(100) NOT NULL UNIQUE,
    status BINARY(1) NOT NULL DEFAULT 1
);
CREATE INDEX musical_genre_name_index ON musical_genres(name);

-- Table: songs
CREATE TABLE songs (
    id INT PRIMARY KEY AUTO_INCREMENT,
    artist_id INT NOT NULL,
    musicak_genre_id INT NOT NULL,
    musical_band_id BINARY(16) NOT NULL,
    FOREIGN KEY (artist_id) REFERENCES artists(id),
    FOREIGN KEY (musical_genre_id) REFERENCES musical_genres(id),
    FOREIGN KEY (musical_band_id) REFERENCES musical_bands(id),
    tonality VARCHAR(25) NOT NULL DEFAULT '',
    link VARCHAR(255) NOT NULL DEFAULT '',
    sheet_music VARCHAR(255) NOT NULL DEFAULT '',
    description TEXT NOT NULL DEFAULT '',
    name VARCHAR(100) NOT NULL,
    status BINARY(1) NOT NULL DEFAULT 1
);
CREATE INDEX song_name_index ON songs(name);

-- Table: repertoires
CREATE TABLE repertoires (
    id BINARY (16) PRIMARY KEY DEFAULT (UUID_TO_BIN(UUID())),
    musical_band_id BINARY(16) NOT NULL,
    FOREIGN KEY (musical_band_id) REFERENCES musical_bands(id),
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT NOT NULL DEFAULT '',
    link VARCHAR(255) NOT NULL DEFAULT '',
    status BINARY(1) NOT NULL DEFAULT 1
);

-- Table: repertoires_songs
CREATE TABLE repertoires_songs (
    repertoire_id BINARY(16) NOT NULL,
    song_id INT NOT NULL,
    user_id BINARY(16) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (repertoire_id) REFERENCES repertoires(id),
    FOREIGN KEY (song_id) REFERENCES songs(id),
    PRIMARY KEY (repertoire_id, song_id, user_id),
    status BINARY(1) NOT NULL DEFAULT 1
);

-- Table: events
CREATE TABLE events (
    id BINARY (16) PRIMARY KEY DEFAULT (UUID_TO_BIN(UUID())),
    musical_band_id BINARY(16) NOT NULL,
    repertoire_id BINARY(16) NOT NULL,
    FOREIGN KEY (musical_band_id) REFERENCES musical_bands(id),
    FOREIGN KEY (repertoire_id) REFERENCES repertoires(id),
    name VARCHAR(100) NOT NULL,
    description TEXT NOT NULL DEFAULT '',
    date DATETIME NOT NULL,
    place VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL DEFAULT '',
    status BINARY(1) NOT NULL DEFAULT 1
);

-- Table: Absences
CREATE TABLE absences (
    id BINARY (16) PRIMARY KEY DEFAULT (UUID_TO_BIN(UUID())),
    user_id BINARY(16) NOT NULL,
    musical_band_id BINARY(16) NOT NULL,
    FOREIGN KEY (musical_band_id) REFERENCES musical_bands(id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    date_from DATETIME NOT NULL,
    date_to DATETIME NOT NULL,
    description TEXT NOT NULL DEFAULT '',
    status BINARY(1) NOT NULL DEFAULT 1
);