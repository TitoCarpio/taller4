package com.erickcg.Parcial2.services;

import org.springframework.data.domain.Page;

import com.erickcg.Parcial2.models.dtos.SaveUserDto;
import com.erickcg.Parcial2.models.dtos.SearchPlaylistDTO;
import com.erickcg.Parcial2.models.entities.Playlist;
import com.erickcg.Parcial2.models.entities.Token;
import com.erickcg.Parcial2.models.entities.User;


public interface IUser {
	void save(SaveUserDto info) throws Exception;
	Page<Playlist> searchUserPlaylist(SearchPlaylistDTO info, String username, int page, int size);
	
//	metodos del taller
	Token registerToken(User user) throws Exception;
	Boolean isTokenValid(User user, String token);
	void cleanTokens(User user) throws Exception;
	
	User findOneByIdentifier(String identifier);
	User findOneByUsernameOrEmail(String username, String email);
	Boolean comparePassword(String toCompare, String current);
	
	//busca el usuario autentificado
	User findUserAuthenticated();
}
