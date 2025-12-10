package com.needledrop.dto;

public class DeleteAlbumRequest {
    private boolean deleteSongs = true;

    // Getters and setters
    public boolean isDeleteSongs() {
        return deleteSongs;
    }

    public void setDeleteSongs(boolean deleteSongs) {
        this.deleteSongs = deleteSongs;
    }
}