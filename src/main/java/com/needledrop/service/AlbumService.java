package com.needledrop.service;

import com.needledrop.dto.CreateAlbumRequest;
import com.needledrop.dto.UpdateAlbumRequest;
import com.needledrop.entity.Album;
import com.needledrop.entity.Song;
import com.needledrop.entity.User;
import com.needledrop.repository.AlbumRepository;
import com.needledrop.repository.SongRepository;
import com.needledrop.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final SongRepository songRepository;
    private final UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public AlbumService(AlbumRepository albumRepository,
                        SongRepository songRepository,
                        UserRepository userRepository) {
        this.albumRepository = albumRepository;
        this.songRepository = songRepository;
        this.userRepository = userRepository;
    }

    // GET all albums
    public List<Album> getAllAlbums() {
        return albumRepository.findAll();
    }

    // GET album by ID
    public Optional<Album> getAlbumById(Long id) {
        return albumRepository.findById(id);
    }

    // GET albums by user ID
    public List<Album> getAlbumsByUserId(Long userId) {
        return albumRepository.findByCreatedById(userId);
    }

    // CREATE album
    @Transactional
    public Album createAlbum(CreateAlbumRequest request, String username) {
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        Album album = new Album();
        album.setTitle(request.getTitle());
        album.setArtist(request.getArtist());
        album.setReleaseYear(request.getReleaseYear());
        album.setGenre(request.getGenre());
        album.setCreatedBy(currentUser);
        album.setCreatedAt(LocalDateTime.now());
        album.setUpdatedAt(LocalDateTime.now());

        return albumRepository.save(album);
    }

    // In AlbumService.java

    @Transactional
    public Album updateAlbum(Long id, UpdateAlbumRequest request, String username) {
        System.out.println("=== UPDATE ALBUM ===");
        System.out.println("Album ID: " + id);
        System.out.println("Requested by user: " + username);

        // 1. Find the user by username
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        System.out.println("User found: " + currentUser.getUsername() +
                ", Role: " + currentUser.getRole());

        // 2. Find the album
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Album not found"));

        System.out.println("Album found: " + album.getTitle() +
                ", Created by: " + album.getCreatedBy().getUsername());

        // 3. Check permissions
        boolean isOwner = album.getCreatedBy().getId().equals(currentUser.getId());
        boolean isAdmin = "ROLE_ADMIN".equals(currentUser.getRole());

        System.out.println("Is owner? " + isOwner);
        System.out.println("Is admin? " + isAdmin);

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("You can only update your own albums");
        }

        // 4. Update fields if provided
        if (request.getTitle() != null) {
            album.setTitle(request.getTitle());
        }
        if (request.getArtist() != null) {
            album.setArtist(request.getArtist());
        }
        if (request.getReleaseYear() != null) {
            album.setReleaseYear(request.getReleaseYear());
        }
        if (request.getGenre() != null) {
            album.setGenre(request.getGenre());
        }

        album.setUpdatedAt(LocalDateTime.now());

        Album savedAlbum = albumRepository.save(album);
        System.out.println("Album updated successfully: " + savedAlbum.getTitle());

        return savedAlbum;
    }

    @Transactional
    public void deleteAlbum(Long id, String username) {
        System.out.println("=== DELETE ALBUM ===");
        System.out.println("Album ID: " + id);
        System.out.println("Username from JWT: " + username);

        // Find user by username
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        System.out.println("User found in DB: " + currentUser.getUsername() +
                ", Role: " + currentUser.getRole());

        // Find album
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Album not found"));

        System.out.println("Album found: " + album.getTitle() +
                ", Created by: " + album.getCreatedBy().getUsername());

        // Check permissions
        boolean isOwner = album.getCreatedBy().getId().equals(currentUser.getId());
        boolean isAdmin = "ROLE_ADMIN".equals(currentUser.getRole());

        System.out.println("Is owner? " + isOwner);
        System.out.println("Is admin? " + isAdmin);

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("You can only delete your own albums");
        }

        // Delete songs
        System.out.println("Deleting songs for album ID: " + id);
        songRepository.deleteByAlbumIdNative(id);
        System.out.println("Songs deleted");

        // Delete album
        System.out.println("Deleting album: " + album.getTitle());
        albumRepository.delete(album);
        System.out.println("Album deleted");
    }

    // SEARCH albums
    public List<Album> searchAlbums(String query) {
        return albumRepository.findByTitleContainingIgnoreCaseOrArtistContainingIgnoreCase(query, query);
    }

    public List<Album> advancedSearch(String title, String artist, String genre, Integer yearFrom, Integer yearTo) {
        if (yearFrom == null) yearFrom = 1900;
        if (yearTo == null) yearTo = LocalDateTime.now().getYear();

        return albumRepository.advancedSearch(title, artist, genre, yearFrom, yearTo);
    }

    // Get albums by genre
    public List<Album> getAlbumsByGenre(String genre) {
        return albumRepository.findByGenreIgnoreCase(genre);
    }

    // Get albums by artist
    public List<Album> getAlbumsByArtist(String artist) {
        return albumRepository.findByArtistIgnoreCase(artist);
    }

    // Get albums by year range
    public List<Album> getAlbumsByYearRange(Integer from, Integer to) {
        return albumRepository.findByReleaseYearBetween(from, to);
    }
}