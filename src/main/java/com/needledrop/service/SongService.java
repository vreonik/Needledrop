package com.needledrop.service;

import com.needledrop.entity.Album;
import com.needledrop.entity.Song;
import com.needledrop.repository.AlbumRepository;
import com.needledrop.repository.SongRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SongService {

    private final SongRepository songRepository;
    private final AlbumRepository albumRepository;

    // ADD THIS CONSTRUCTOR
    public SongService(SongRepository songRepository, AlbumRepository albumRepository) {
        this.songRepository = songRepository;
        this.albumRepository = albumRepository;
    }

    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }

    public Song getSongById(Long id) {
        return songRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Song not found with id: " + id));
    }

    public List<Song> getSongsByAlbum(Long albumId) {
        return songRepository.findByAlbumId(albumId);
    }

    @Transactional
    public Song createSong(Song song, Long albumId) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new RuntimeException("Album not found with id: " + albumId));

        song.setAlbum(album);
        return songRepository.save(song);
    }

    @Transactional
    public Song updateSong(Long id, Song songDetails) {
        Song song = getSongById(id);

        if (songDetails.getTitle() != null) {
            song.setTitle(songDetails.getTitle());
        }
        if (songDetails.getDuration() != null) {
            song.setDuration(songDetails.getDuration());
        }
        if (songDetails.getTrackNumber() != null) {
            song.setTrackNumber(songDetails.getTrackNumber());
        }

        return songRepository.save(song);
    }

    @Transactional
    public void deleteSong(Long id) {
        if (!songRepository.existsById(id)) {
            throw new RuntimeException("Song not found with id: " + id);
        }
        songRepository.deleteById(id);
    }
}