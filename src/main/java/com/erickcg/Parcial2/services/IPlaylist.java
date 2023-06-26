package com.erickcg.Parcial2.services;

import java.util.List;

import com.erickcg.Parcial2.models.dtos.SavePlayListDTO;
import com.erickcg.Parcial2.models.entities.Playlist;
import com.erickcg.Parcial2.models.entities.User;

public interface IPlaylist {
	boolean save(SavePlayListDTO info, String username) throws Exception;
	List<Playlist> findPlaylist(User user);

}
