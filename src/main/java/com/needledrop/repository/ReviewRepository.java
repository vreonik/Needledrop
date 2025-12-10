package com.needledrop.repository;

import com.needledrop.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByAlbumId(Long albumId);
    List<Review> findByAuthorId(Long userId);
}