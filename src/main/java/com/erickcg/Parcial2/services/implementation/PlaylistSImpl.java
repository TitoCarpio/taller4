package com.erickcg.Parcial2.services.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.erickcg.Parcial2.models.dtos.SavePlayListDTO;

import com.erickcg.Parcial2.models.entities.Playlist;

import com.erickcg.Parcial2.models.entities.User;
import com.erickcg.Parcial2.repositories.PlaylistRepository;
import com.erickcg.Parcial2.repositories.UserRepository;
import com.erickcg.Parcial2.services.IPlaylist;

import jakarta.transaction.Transactional;

@Service
public class PlaylistSImpl implements IPlaylist {

	@Autowired
	PlaylistRepository playRepo;
	@Autowired
	UserRepository userRepo;

	@Override
	@Transactional(rollbackOn = Exception.class)
	public boolean save(SavePlayListDTO info, String username) throws Exception {
//		User user = userRepo.findByEmail(info.getEmail());
//		String tokenHeader = request.getHeader("Authorization");
//		String token = null;
//		token = tokenHeader.substring(7);

		User user = userRepo.findOneByUsernameOrEmail(username, username);

		System.out.println(user);

		Playlist play = null;
		Playlist existe = playRepo.findByTitle(info.getTitle());

		// verifico que el usuario exista
		if (user == null) {
			return false;
		}

		// verifico si la playlist existe
		if (existe == null) {
			play = new Playlist(info.getTitle(), info.getDescription(), user);
			playRepo.save(play);
			play = null;
			existe = null;
			return true;
			// si la playlist existe verifico que el usuario sea diferente
		} else if (existe.getUser() != user) {
			play = new Playlist(info.getTitle(), info.getDescription(), user);
			playRepo.save(play);
			play = null;
			existe = null;
			return true;
		} else {
			return false;
		}

	}

	@Override
	public Page<Playlist> findPlaylist(User user, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("title"));
		Page<Playlist> pages = playRepo.findByUser(user, pageable);
		return pages;
	}

}
