package com.needledrop.config;

import com.needledrop.entity.User;
import com.needledrop.entity.Album;
import com.needledrop.entity.Song;
import com.needledrop.repository.UserRepository;
import com.needledrop.repository.AlbumRepository;
import com.needledrop.repository.SongRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AlbumRepository albumRepository;
    private final SongRepository songRepository;

    public DataLoader(UserRepository userRepository,
                      PasswordEncoder passwordEncoder,
                      AlbumRepository albumRepository,
                      SongRepository songRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.albumRepository = albumRepository;
        this.songRepository = songRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        System.out.println("=== Starting DataLoader ===");

        // Create users
        User admin = createAdminUser();
        User regularUser = createRegularUser();
        User musicFan = createMusicFanUser();
        User reviewer = createReviewerUser();

        // Create albums and songs for each user
        createMusicDataForUser(admin, "admin");
        createMusicDataForUser(regularUser, "user");
        createMusicDataForUser(musicFan, "musicfan");
        createMusicDataForUser(reviewer, "reviewer");

        System.out.println("=== DataLoader finished ===");
        printSummary();
    }

    private User createAdminUser() {
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@needledrop.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ROLE_ADMIN");
            admin.setEnabled(true);
            return userRepository.save(admin);
        }
        return userRepository.findByUsername("admin").orElseThrow();
    }

    private User createRegularUser() {
        if (!userRepository.existsByUsername("user")) {
            User user = new User();
            user.setUsername("user");
            user.setEmail("user@needledrop.com");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setRole("ROLE_USER");
            user.setEnabled(true);
            return userRepository.save(user);
        }
        return userRepository.findByUsername("user").orElseThrow();
    }

    private User createMusicFanUser() {
        if (!userRepository.existsByUsername("musicfan")) {
            User user = new User();
            user.setUsername("musicfan");
            user.setEmail("musicfan@needledrop.com");
            user.setPassword(passwordEncoder.encode("musicfan123"));
            user.setRole("ROLE_USER");
            user.setEnabled(true);
            return userRepository.save(user);
        }
        return userRepository.findByUsername("musicfan").orElseThrow();
    }

    private User createReviewerUser() {
        if (!userRepository.existsByUsername("reviewer")) {
            User user = new User();
            user.setUsername("reviewer");
            user.setEmail("reviewer@needledrop.com");
            user.setPassword(passwordEncoder.encode("reviewer123"));
            user.setRole("ROLE_USER");
            user.setEnabled(true);
            return userRepository.save(user);
        }
        return userRepository.findByUsername("reviewer").orElseThrow();
    }

    private void createMusicDataForUser(User user, String userType) {
        System.out.println("Creating music data for user: " + user.getUsername());

        switch (userType) {
            case "admin":
                // Admin has classic rock and jazz
                createAlbum(user, "The Dark Side of the Moon", "Pink Floyd", 1973, "Progressive Rock", Arrays.asList(
                        new SongData("Speak to Me", 1, 1, 13),
                        new SongData("Breathe", 2, 2, 43),
                        new SongData("On the Run", 3, 3, 30),
                        new SongData("Time", 4, 6, 53),
                        new SongData("The Great Gig in the Sky", 5, 4, 36)
                ));

                createAlbum(user, "Kind of Blue", "Miles Davis", 1959, "Jazz", Arrays.asList(
                        new SongData("So What", 1, 9, 22),
                        new SongData("Freddie Freeloader", 2, 9, 34),
                        new SongData("Blue in Green", 3, 5, 37)
                ));
                break;

            case "user":
                // Regular user has indie/alternative
                createAlbum(user, "OK Computer", "Radiohead", 1997, "Alternative Rock", Arrays.asList(
                        new SongData("Airbag", 1, 4, 44),
                        new SongData("Paranoid Android", 2, 6, 23),
                        new SongData("Subterranean Homesick Alien", 3, 4, 27)
                ));

                createAlbum(user, "Is This It", "The Strokes", 2001, "Indie Rock", Arrays.asList(
                        new SongData("Is This It", 1, 2, 31),
                        new SongData("The Modern Age", 2, 3, 28),
                        new SongData("Soma", 3, 2, 33)
                ));
                break;

            case "musicfan":
                // Music fan has hip-hop and electronic
                createAlbum(user, "The College Dropout", "Kanye West", 2004, "Hip-Hop", Arrays.asList(
                        new SongData("We Don't Care", 1, 3, 59),
                        new SongData("All Falls Down", 2, 3, 43),
                        new SongData("Jesus Walks", 3, 3, 13)
                ));

                createAlbum(user, "Random Access Memories", "Daft Punk", 2013, "Electronic", Arrays.asList(
                        new SongData("Give Life Back to Music", 1, 4, 35),
                        new SongData("The Game of Love", 2, 5, 22),
                        new SongData("Giorgio by Moroder", 3, 9, 4)
                ));
                break;

            case "reviewer":
                // Reviewer has pop and R&B
                createAlbum(user, "21", "Adele", 2011, "Pop/Soul", Arrays.asList(
                        new SongData("Rolling in the Deep", 1, 3, 48),
                        new SongData("Rumour Has It", 2, 3, 43),
                        new SongData("Set Fire to the Rain", 3, 4, 2)
                ));

                createAlbum(user, "Channel Orange", "Frank Ocean", 2012, "R&B", Arrays.asList(
                        new SongData("Thinkin Bout You", 1, 3, 20),
                        new SongData("Sierra Leone", 2, 2, 28),
                        new SongData("Sweet Life", 3, 4, 14)
                ));
                break;
        }
    }

    private void createAlbum(User user, String title, String artist, int year, String genre, List<SongData> songs) {
        // Check if album already exists for this user
        boolean exists = albumRepository.existsByTitleAndCreatedBy(title, user);
        if (exists) {
            System.out.println("Album already exists: " + title + " for user: " + user.getUsername());
            return;
        }

        Album album = new Album();
        album.setTitle(title);
        album.setArtist(artist);
        album.setReleaseYear(year);
        album.setGenre(genre);
        album.setCoverImageUrl("https://picsum.photos/300/300?random=" + title.hashCode());
        album.setCreatedBy(user);  // THIS IS HOW YOU SET THE USER WHO CREATED THE ALBUM
        albumRepository.save(album);

        // Create songs for the album
        for (SongData songData : songs) {
            Song song = new Song();
            song.setTitle(songData.title);
            song.setTrackNumber(songData.trackNumber);
            song.setDuration(Duration.ofMinutes(songData.minutes).plusSeconds(songData.seconds));
            song.setAlbum(album);  // THIS SETS THE ALBUM FOR THE SONG
            songRepository.save(song);
        }

        System.out.println("Created album: " + title + " with " + songs.size() + " songs for user: " + user.getUsername());
    }

    private void printSummary() {
        long userCount = userRepository.count();
        long albumCount = albumRepository.count();
        long songCount = songRepository.count();

        System.out.println("\n=== DataLoader Summary ===");
        System.out.println("Total Users: " + userCount);
        System.out.println("Total Albums: " + albumCount);
        System.out.println("Total Songs: " + songCount);

        // Print per-user breakdown
        List<User> users = userRepository.findAll();
        for (User user : users) {
            long userAlbumCount = albumRepository.countByCreatedBy(user);
            System.out.println("  - " + user.getUsername() + ": " + userAlbumCount + " albums");
        }
    }

    // Helper class for song data
    private static class SongData {
        String title;
        int trackNumber;
        int minutes;
        int seconds;

        SongData(String title, int trackNumber, int minutes, int seconds) {
            this.title = title;
            this.trackNumber = trackNumber;
            this.minutes = minutes;
            this.seconds = seconds;
        }
    }
}