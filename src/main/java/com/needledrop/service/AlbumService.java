package com.needledrop.service;

import com.needledrop.dto.CreateAlbumRequest;
import com.needledrop.dto.UpdateAlbumRequest;
import com.needledrop.entity.Album;
import com.needledrop.entity.User;
import com.needledrop.repository.AlbumRepository;
import com.needledrop.repository.SongRepository;
import com.needledrop.repository.UserRepository;
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

    public AlbumService(AlbumRepository albumRepository,
                        SongRepository songRepository,
                        UserRepository userRepository) {
        this.albumRepository = albumRepository;
        this.songRepository = songRepository;
        this.userRepository = userRepository;
    }

    public List<Album> getAllAlbums() {
        return albumRepository.findAll();
    }

    public Optional<Album> getAlbumById(Long id) {
        return albumRepository.findById(id);
    }

    public List<Album> getAlbumsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        return albumRepository.findByCreatedById(user.getId());
    }

    public List<Album> getAlbumsByUserId(Long userId) {
        return albumRepository.findByCreatedById(userId);
    }

    @Transactional
    public Album createAlbum(CreateAlbumRequest request, String username) {
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        Album album = new Album();
        album.setTitle(request.getTitle());
        album.setArtist(request.getArtist());
        album.setReleaseYear(request.getReleaseYear());
        album.setGenre(request.getGenre());
        album.setCoverImageUrl(request.getCoverImageUrl());
        album.setCreatedBy(currentUser);

        // createdAt/updatedAt handled by @PrePersist/@PreUpdate in entity
        return albumRepository.save(album);
    }

    @Transactional
    public Album updateAlbum(Long id, UpdateAlbumRequest request, String username) {
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Album not found"));

        assertOwnerOrAdmin(currentUser, album);

        // Partial updates
        if (request.getTitle() != null) album.setTitle(request.getTitle());
        if (request.getArtist() != null) album.setArtist(request.getArtist());
        if (request.getReleaseYear() != null) album.setReleaseYear(request.getReleaseYear());
        if (request.getGenre() != null) album.setGenre(request.getGenre());
        if (request.getCoverImageUrl() != null) album.setCoverImageUrl(request.getCoverImageUrl());

        // updatedAt handled by @PreUpdate
        return albumRepository.save(album);
    }

    @Transactional
    public void deleteAlbum(Long id, String username) {
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Album not found"));

        assertOwnerOrAdmin(currentUser, album);

        // Songs are deleted first to avoid FK issues
        songRepository.deleteByAlbumIdNative(id);

        albumRepository.delete(album);
    }

    public List<Album> searchAlbums(String query) {
        return albumRepository.findByTitleContainingIgnoreCaseOrArtistContainingIgnoreCase(query, query);
    }

    public List<Album> advancedSearch(String title, String artist, String genre, Integer yearFrom, Integer yearTo) {
        if (yearFrom == null) yearFrom = 1900;
        if (yearTo == null) yearTo = LocalDateTime.now().getYear();

        return albumRepository.advancedSearch(title, artist, genre, yearFrom, yearTo);
    }

    public List<Album> getAlbumsByGenre(String genre) {
        return albumRepository.findByGenreIgnoreCase(genre);
    }

    public List<Album> getAlbumsByArtist(String artist) {
        return albumRepository.findByArtistIgnoreCase(artist);
    }

    public List<Album> getAlbumsByYearRange(Integer from, Integer to) {
        return albumRepository.findByReleaseYearBetween(from, to);
    }

    private void assertOwnerOrAdmin(User currentUser, Album album) {
        boolean isOwner = album.getCreatedBy() != null && album.getCreatedBy().getId().equals(currentUser.getId());
        boolean isAdmin = "ROLE_ADMIN".equals(currentUser.getRole());

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("You can only modify your own albums");
        }
    }
}
