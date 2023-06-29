package com.erickcg.Parcial2.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;

import com.erickcg.Parcial2.models.entities.Playlist;
import com.erickcg.Parcial2.models.entities.User;

public interface PlaylistRepository
extends ListCrudRepository<Playlist, UUID>{
	Page<Playlist> findByUser(User user, Pageable pageable);
	Playlist findByTitle(String title);
	Page<Playlist> findByUserAndTitleContains(User user, String title, Pageable pageable);
	Playlist findByCode(UUID code);
}
