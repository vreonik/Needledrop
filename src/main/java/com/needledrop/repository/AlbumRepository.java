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

    // Ownership / user-scoped queries
    boolean existsByTitleAndCreatedBy(String title, User createdBy);
    long countByCreatedBy(User createdBy);
    List<Album> findByCreatedById(Long userId);

    // Simple search (title or artist)
    List<Album> findByTitleContainingIgnoreCaseOrArtistContainingIgnoreCase(String title, String artist);

    // Flexible filtering
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

    // Convenience filters
    List<Album> findByGenreIgnoreCase(String genre);
    List<Album> findByArtistIgnoreCase(String artist);
    List<Album> findByReleaseYearBetween(Integer startYear, Integer endYear);
}
