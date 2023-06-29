package com.erickcg.Parcial2.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;

import com.erickcg.Parcial2.models.entities.SongXPlaylist;

public interface SongXPlaylistRepository 
		extends ListCrudRepository<SongXPlaylist, UUID>{
		SongXPlaylist findByPlaylistCodeAndSongCode(UUID playlist, UUID song);
		
		//obtiene todas las canciones de una lista
		Page<SongXPlaylist> findByPlaylistCode(UUID playlist, Pageable pageable);
		
	
		
		
		
		

}
