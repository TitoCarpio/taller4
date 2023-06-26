package com.erickcg.Parcial2.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.ListCrudRepository;

import com.erickcg.Parcial2.models.entities.Playlist;
import com.erickcg.Parcial2.models.entities.User;

public interface PlaylistRepository
extends ListCrudRepository<Playlist, UUID>{
	List<Playlist> findByUser(User user);
	Playlist findByTitle(String title);
	List<Playlist> findByUserAndTitleContains(User user, String title);
	Playlist findByCode(UUID code);
}
