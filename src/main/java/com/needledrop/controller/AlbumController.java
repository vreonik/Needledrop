package com.needledrop.controller;

import com.needledrop.dto.CreateAlbumRequest;
import com.needledrop.dto.UpdateAlbumRequest;
import com.needledrop.entity.Album;
import com.needledrop.service.AlbumService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/albums")
public class AlbumController {

    private final AlbumService albumService;

    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping
    public ResponseEntity<List<Album>> getAllAlbums() {
        return ResponseEntity.ok(albumService.getAllAlbums());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Album> getAlbumById(@PathVariable Long id) {
        return albumService.getAlbumById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Album>> getAlbumsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(albumService.getAlbumsByUserId(userId));
    }

    // Authenticated convenience endpoint
    @GetMapping("/my-albums")
    public ResponseEntity<?> getMyAlbums(Authentication authentication) {
        try {
            String username = authentication.getName();
            return ResponseEntity.ok(albumService.getAlbumsByUsername(username));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Failed to get albums: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createAlbum(@RequestBody CreateAlbumRequest request,
                                         Authentication authentication) {
        try {
            String username = authentication.getName();
            Album album = albumService.createAlbum(request, username);
            return ResponseEntity.status(HttpStatus.CREATED).body(album);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAlbum(@PathVariable Long id,
                                         @RequestBody UpdateAlbumRequest request,
                                         Authentication authentication) {
        try {
            String username = authentication.getName();
            Album updatedAlbum = albumService.updateAlbum(id, request, username);
            return ResponseEntity.ok(updatedAlbum);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAlbum(@PathVariable Long id,
                                         Authentication authentication) {
        try {
            String username = authentication.getName();
            albumService.deleteAlbum(id, username);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", e.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Album>> searchAlbums(@RequestParam String query) {
        return ResponseEntity.ok(albumService.searchAlbums(query));
    }

    @GetMapping("/advanced-search")
    public ResponseEntity<List<Album>> advancedSearch(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String artist,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Integer yearFrom,
            @RequestParam(required = false) Integer yearTo) {

        return ResponseEntity.ok(albumService.advancedSearch(title, artist, genre, yearFrom, yearTo));
    }

    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<Album>> getAlbumsByGenre(@PathVariable String genre) {
        return ResponseEntity.ok(albumService.getAlbumsByGenre(genre));
    }

    @GetMapping("/artist/{artist}")
    public ResponseEntity<List<Album>> getAlbumsByArtist(@PathVariable String artist) {
        return ResponseEntity.ok(albumService.getAlbumsByArtist(artist));
    }

    @GetMapping("/year-range")
    public ResponseEntity<List<Album>> getAlbumsByYearRange(
            @RequestParam Integer from,
            @RequestParam Integer to) {
        return ResponseEntity.ok(albumService.getAlbumsByYearRange(from, to));
    }
}
