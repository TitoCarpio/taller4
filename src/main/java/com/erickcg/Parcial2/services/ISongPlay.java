package com.erickcg.Parcial2.services;

import java.util.List;
import java.util.UUID;

import com.erickcg.Parcial2.models.entities.SongXPlaylist;


public interface ISongPlay {
	void save(SongXPlaylist info) throws Exception ;
	List<SongXPlaylist> findPlaylist(UUID codeplay);

}
