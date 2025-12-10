package com.needledrop.repository;

import com.needledrop.entity.Album;
import com.needledrop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

    // Add this method
    boolean existsByTitleAndCreatedBy(String title, User createdBy);

    // Also add this method that's referenced in printSummary()
    long countByCreatedBy(User createdBy);

    // Find albums by user ID
    List<Album> findByCreatedById(Long userId);

    // Search albums
    List<Album> findByTitleContainingIgnoreCaseOrArtistContainingIgnoreCase(String title, String artist);

    // Advanced search
    @Query("SELECT a FROM Album a WHERE " +
            "(:title IS NULL OR LOWER(a.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
            "(:artist IS NULL OR LOWER(a.artist) LIKE LOWER(CONCAT('%', :artist, '%'))) AND " +
            "(:genre IS NULL OR LOWER(a.genre) LIKE LOWER(CONCAT('%', :genre, '%'))) AND " +
            "a.releaseYear BETWEEN :yearFrom AND :yearTo")
    List<Album> advancedSearch(@Param("title") String title,
                               @Param("artist") String artist,
                               @Param("genre") String genre,
                               @Param("yearFrom") Integer yearFrom,
                               @Param("yearTo") Integer yearTo);

    // Find by genre
    List<Album> findByGenreIgnoreCase(String genre);

    // Find by artist
    List<Album> findByArtistIgnoreCase(String artist);

    // Find by year range
    List<Album> findByReleaseYearBetween(Integer startYear, Integer endYear);
}