package com.erickcg.Parcial2.services;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.erickcg.Parcial2.models.entities.SongXPlaylist;


public interface ISongPlay {
	void save(SongXPlaylist info) throws Exception ;
	Page<SongXPlaylist> findPlaylist(UUID codeplay, int page, int size);

}
