package com.erickcg.Parcial2.services;

import org.springframework.data.domain.Page;

import com.erickcg.Parcial2.models.dtos.SavePlayListDTO;
import com.erickcg.Parcial2.models.entities.Playlist;
import com.erickcg.Parcial2.models.entities.User;

public interface IPlaylist {
	boolean save(SavePlayListDTO info, String username) throws Exception;
	Page<Playlist> findPlaylist(User user, int page, int size);

}
