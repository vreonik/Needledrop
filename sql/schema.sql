-- USERS TABLE
CREATE TABLE users (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       username VARCHAR(20) UNIQUE NOT NULL,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(20) DEFAULT 'ROLE_USER',
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       enabled BOOLEAN DEFAULT TRUE
);

-- USER_PROFILES TABLE
CREATE TABLE user_profiles (
                               id BIGINT PRIMARY KEY AUTO_INCREMENT,
                               user_id BIGINT UNIQUE,
                               display_name VARCHAR(255),
                               bio VARCHAR(500),
                               avatar_url VARCHAR(500),
                               member_since TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- ALBUMS TABLE
CREATE TABLE albums (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        title VARCHAR(255) NOT NULL,
                        artist VARCHAR(255) NOT NULL,
                        release_date DATE,
                        created_by BIGINT,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        FOREIGN KEY (created_by) REFERENCES users(id)
);

-- SONGS TABLE
CREATE TABLE songs (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       title VARCHAR(255) NOT NULL,
                       duration INT,
                       track_number INT,
                       album_id BIGINT,
                       FOREIGN KEY (album_id) REFERENCES albums(id) ON DELETE CASCADE
);

-- PLAYLISTS TABLE
CREATE TABLE playlists (
                           id BIGINT PRIMARY KEY AUTO_INCREMENT,
                           name VARCHAR(255) NOT NULL,
                           description VARCHAR(1000),
                           is_public BOOLEAN DEFAULT TRUE,
                           is_collaborative BOOLEAN DEFAULT FALSE,
                           created_by BIGINT,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           FOREIGN KEY (created_by) REFERENCES users(id)
);

-- REVIEWS TABLE
CREATE TABLE reviews (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         rating INT CHECK (rating BETWEEN 1 AND 5),
                         comment TEXT,
                         album_id BIGINT,
                         author_id BIGINT,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         FOREIGN KEY (album_id) REFERENCES albums(id),
                         FOREIGN KEY (author_id) REFERENCES users(id)
);

-- PLAYLIST_SONGS (Many-to-Many)
CREATE TABLE playlist_songs (
                                playlist_id BIGINT,
                                song_id BIGINT,
                                added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                PRIMARY KEY (playlist_id, song_id),
                                FOREIGN KEY (playlist_id) REFERENCES playlists(id) ON DELETE CASCADE,
                                FOREIGN KEY (song_id) REFERENCES songs(id) ON DELETE CASCADE
);

-- USER_FAVORITE_GENRES
CREATE TABLE user_favorite_genres (
                                      user_profile_id BIGINT,
                                      genre VARCHAR(100),
                                      PRIMARY KEY (user_profile_id, genre),
                                      FOREIGN KEY (user_profile_id) REFERENCES user_profiles(id) ON DELETE CASCADE
);