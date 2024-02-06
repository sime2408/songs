-- Create the 'artists' table
CREATE TABLE IF NOT EXISTS artists (
                                       id INT AUTO_INCREMENT PRIMARY KEY,
                                       name VARCHAR(255),
    artistic_name VARCHAR(255),
    date_of_birth DATE,
    nationality VARCHAR(255)
    );

-- Insert dummy artists
INSERT INTO artists (name, artistic_name, date_of_birth, nationality)
VALUES ('John Doe', 'Johnny', '1975-04-23', 'Macedonian'),
       ('Jane Smith', 'Janie', '1985-12-11', 'Macedonian');

-- Create the 'genres' table
CREATE TABLE IF NOT EXISTS genres (
                                      id INT AUTO_INCREMENT PRIMARY KEY,
                                      name VARCHAR(255)
    );

-- Insert dummy genres
INSERT INTO genres (name)
VALUES ('Pop'),
       ('Rock'),
       ('Jazz'),
       ('Classical');

-- Create the 'songs' table
CREATE TABLE IF NOT EXISTS songs (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     title VARCHAR(255),
    duration INT,
    release_date DATE,
    genre_id INT,
    artist_id INT
    );

-- Insert dummy songs
INSERT INTO songs (title, duration, release_date, genre_id, artist_id)
VALUES ('New Song', 4, '2021-07-21', (SELECT id FROM genres WHERE name = 'Pop'), (SELECT id FROM artists WHERE artistic_name = 'Johnny')),
       ('Another Song', 5, '2022-01-15', (SELECT id FROM genres WHERE name = 'Rock'), (SELECT id FROM artists WHERE artistic_name = 'Janie')),
       ('Song Three', 6, '2020-06-30', (SELECT id FROM genres WHERE name = 'Jazz'), (SELECT id FROM artists WHERE artistic_name = 'Johnny'));

-- Create the 'playlists' table
CREATE TABLE IF NOT EXISTS playlists (
                                         id INT AUTO_INCREMENT PRIMARY KEY,
                                         name VARCHAR(255),
    date_of_creation DATE,
    status VARCHAR(255)
    );

-- Insert dummy playlists
INSERT INTO playlists (name, date_of_creation, status)
VALUES ('My Playlist', '2021-08-01', 'public'),
       ('Chill Vibes', '2022-02-14', 'private');

-- Create the 'playlist_songs' table (assuming a many-to-many relationship between songs and playlists)
CREATE TABLE IF NOT EXISTS playlist_songs (
                                              playlist_id INT,
                                              song_id INT,
                                              PRIMARY KEY (playlist_id, song_id)
    );

-- Insert dummy relations (adjust according to your join table name and structure)
INSERT INTO playlist_songs (playlist_id, song_id)
VALUES ((SELECT id FROM playlists WHERE name = 'My Playlist'), (SELECT id FROM songs WHERE title = 'New Song')),
       ((SELECT id FROM playlists WHERE name = 'Chill Vibes'), (SELECT id FROM songs WHERE title = 'Another Song')),
       ((SELECT id FROM playlists WHERE name = 'My Playlist'), (SELECT id FROM songs WHERE title = 'Song Three'));
