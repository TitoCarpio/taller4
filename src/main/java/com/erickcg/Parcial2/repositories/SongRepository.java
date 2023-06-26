package com.erickcg.Parcial2.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.ListCrudRepository;

import com.erickcg.Parcial2.models.entities.Song;

public interface SongRepository 
	extends ListCrudRepository<Song, UUID> {
	List<Song> findByTitleContains(String word);
	Song findByCode(UUID code);

}
