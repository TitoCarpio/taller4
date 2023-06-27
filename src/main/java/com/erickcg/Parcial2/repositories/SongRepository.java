package com.erickcg.Parcial2.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.repository.PagingAndSortingRepository;

import com.erickcg.Parcial2.models.entities.Song;

public interface SongRepository extends JpaRepository<Song, UUID> {

	// cambio el tipo de lista a page para hacer paginacion
	Page<Song> findByTitleContains(String word, Pageable page);

	Song findByCode(UUID code);
//	Page<Song> findAll(Pageable pageable);

}
