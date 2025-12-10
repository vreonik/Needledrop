package com.needledrop.repository;

import com.needledrop.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findByCreatedById(Long userId);
    List<Playlist> findByIsPublicTrue();
}