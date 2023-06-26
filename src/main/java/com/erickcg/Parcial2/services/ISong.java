package com.erickcg.Parcial2.services;

import java.util.List;

import com.erickcg.Parcial2.models.dtos.SearchSongDTO;
import com.erickcg.Parcial2.models.dtos.SongDto;

public interface ISong {
	List<SongDto> allSong(SearchSongDTO info);
}
