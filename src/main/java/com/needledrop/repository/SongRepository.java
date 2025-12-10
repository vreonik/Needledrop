package com.needledrop.repository;

import com.needledrop.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {

    List<Song> findByAlbumId(Long albumId);


    @Transactional
    @Modifying
    void deleteByAlbumId(Long albumId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Song s WHERE s.album.id = :albumId")
    void deleteByAlbumIdCustom(@Param("albumId") Long albumId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM songs WHERE album_id = :albumId", nativeQuery = true)
    void deleteByAlbumIdNative(@Param("albumId") Long albumId);
}