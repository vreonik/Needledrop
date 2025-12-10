package com.needledrop.controller;

import com.needledrop.dto.CreateAlbumRequest;
import com.needledrop.dto.DeleteAlbumRequest;
import com.needledrop.dto.UpdateAlbumRequest;
import com.needledrop.entity.Album;
import com.needledrop.entity.User;
import com.needledrop.repository.SongRepository;
import com.needledrop.service.AlbumService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/albums")
public class AlbumController {

    private final AlbumService albumService;

    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    // GET all albums
    @GetMapping
    public ResponseEntity<List<Album>> getAllAlbums() {
        List<Album> albums = albumService.getAllAlbums();
        return ResponseEntity.ok(albums);
    }

    // GET album by ID
    @GetMapping("/{id}")
    public ResponseEntity<Album> getAlbumById(@PathVariable Long id) {
        return albumService.getAlbumById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET albums by user ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Album>> getAlbumsByUser(@PathVariable Long userId) {
        List<Album> albums = albumService.getAlbumsByUserId(userId);
        return ResponseEntity.ok(albums);
    }

    // GET current user's albums
    @GetMapping("/my-albums")
    public ResponseEntity<List<Album>> getMyAlbums(Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        List<Album> albums = albumService.getAlbumsByUserId(currentUser.getId());
        return ResponseEntity.ok(albums);
    }

    @PostMapping
    public ResponseEntity<Album> createAlbum(@RequestBody CreateAlbumRequest request,
                                             Authentication authentication) {
        String username = authentication.getName();
        Album album = albumService.createAlbum(request, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(album);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Album> updateAlbum(@PathVariable Long id,
                                             @RequestBody UpdateAlbumRequest request,
                                             Authentication authentication) {

        String username = authentication.getName();
        System.out.println("Updating album " + id + " for user: " + username);

        Album updatedAlbum = albumService.updateAlbum(id, request, username);
        return ResponseEntity.ok(updatedAlbum);
    }

    // DELETE album
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlbum(@PathVariable Long id,
                                            Authentication authentication) {
        String username = authentication.getName();
        System.out.println("Username from JWT: " + username);
        albumService.deleteAlbum(id, username);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/test/{id}")
    public ResponseEntity<Void> testDelete(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("TEST - Auth class: " + auth.getClass());
        System.out.println("TEST - Principal class: " + auth.getPrincipal().getClass());
        System.out.println("TEST - Username: " + auth.getName());

        // Force it to work:
        albumService.deleteAlbum(id, auth.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Album>> searchAlbums(@RequestParam String query) {
        List<Album> albums = albumService.searchAlbums(query);
        return ResponseEntity.ok(albums);
    }

    @GetMapping("/advanced-search")
    public ResponseEntity<List<Album>> advancedSearch(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String artist,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Integer yearFrom,
            @RequestParam(required = false) Integer yearTo) {

        List<Album> albums = albumService.advancedSearch(title, artist, genre, yearFrom, yearTo);
        return ResponseEntity.ok(albums);
    }

    // Get albums by genre
    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<Album>> getAlbumsByGenre(@PathVariable String genre) {
        List<Album> albums = albumService.getAlbumsByGenre(genre);
        return ResponseEntity.ok(albums);
    }

    // Get albums by artist
    @GetMapping("/artist/{artist}")
    public ResponseEntity<List<Album>> getAlbumsByArtist(@PathVariable String artist) {
        List<Album> albums = albumService.getAlbumsByArtist(artist);
        return ResponseEntity.ok(albums);
    }

    // Get albums by year range
    @GetMapping("/year-range")
    public ResponseEntity<List<Album>> getAlbumsByYearRange(
            @RequestParam Integer from,
            @RequestParam Integer to) {
        List<Album> albums = albumService.getAlbumsByYearRange(from, to);
        return ResponseEntity.ok(albums);
    }
}