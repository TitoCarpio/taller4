package com.erickcg.Parcial2.services;

import org.springframework.data.domain.Page;

import com.erickcg.Parcial2.models.dtos.SearchSongDTO;
import com.erickcg.Parcial2.models.entities.Song;

public interface ISong {
	Page<Song> allSong(SearchSongDTO info, int page, int size);
}
