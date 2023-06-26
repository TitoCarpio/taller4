package com.erickcg.Parcial2.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.ListCrudRepository;

import com.erickcg.Parcial2.models.entities.SongXPlaylist;

public interface SongXPlaylistRepository 
		extends ListCrudRepository<SongXPlaylist, UUID>{
		SongXPlaylist findByPlaylistCodeAndSongCode(UUID playlist, UUID song);
		List<SongXPlaylist> findByPlaylistCode(UUID playlist);
		
		
		

}
