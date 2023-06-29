package com.erickcg.Parcial2.services.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.erickcg.Parcial2.models.dtos.SaveUserDto;
import com.erickcg.Parcial2.models.dtos.SearchPlaylistDTO;
import com.erickcg.Parcial2.models.entities.Playlist;
import com.erickcg.Parcial2.models.entities.Token;
import com.erickcg.Parcial2.models.entities.User;
import com.erickcg.Parcial2.repositories.PlaylistRepository;
import com.erickcg.Parcial2.repositories.TokenRepository;
import com.erickcg.Parcial2.repositories.UserRepository;
import com.erickcg.Parcial2.services.IUser;
import com.erickcg.Parcial2.utils.JWTTools;

import jakarta.transaction.Transactional;

@Service
public class UserSImpl implements IUser {
	@Autowired
	public PasswordEncoder passwordEncoder;
	
	@Autowired UserRepository userRepo;
	
	@Autowired PlaylistRepository playRepo;
	
//	implementaciones del taller
	@Autowired
	private JWTTools jwtTools;
	
	@Autowired
	private TokenRepository tokenRepository;
	
	// hasta aca llega la implementacion del taller

	@Override
	@Transactional(rollbackOn = Exception.class)
	public void save(SaveUserDto info) {
		User user =  new User(
				info.getUsername(),
				info.getEmail(),
				passwordEncoder.encode(info.getPassword())
				);
		userRepo.save(user);
	}

	@Override
	public Page<Playlist> searchUserPlaylist(SearchPlaylistDTO info, String username, int page, int size) {
		
		
		User user = userRepo.findOneByUsernameOrEmail(username, username);
		if (user != null && info.getTitle().isEmpty()) {
			//agrego la paginacion
			Pageable pageable = PageRequest.of(page, size, Sort.by("title"));
			
			
			Page<Playlist> play = playRepo.findByUser(user, pageable);
			
//			System.out.println(playRepo.findByUserAndTitleContains(user, "prueba", pageable));
			
			
			
			return play;
		} else if (!info.getTitle().isEmpty() && user != null) {
			Pageable pageable = PageRequest.of(page, size, Sort.by("title"));
			return playRepo.findByUserAndTitleContains(user, info.getTitle(), pageable);
		}else {
			return null;
		}
		
	}
	
//	metodos del taller
	
	@Override
	@Transactional(rollbackOn = Exception.class)
	public Token registerToken(User user) throws Exception {
		cleanTokens(user);
		
		String tokenString = jwtTools.generateToken(user);
		Token token = new Token(tokenString, user);
		
		tokenRepository.save(token);
		
		return token;
	}

	@Override
	public Boolean isTokenValid(User user, String token) {
		try {
			cleanTokens(user);
			List<Token> tokens = tokenRepository.findByUserAndActive(user, true);
			
			tokens.stream()
				.filter(tk -> tk.getContent().equals(token))
				.findAny()
				.orElseThrow(() -> new Exception());
			
			return true;
		} catch (Exception e) {
			return false;
		}		
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public void cleanTokens(User user) throws Exception {
		List<Token> tokens = tokenRepository.findByUserAndActive(user, true);
		
		tokens.forEach(token -> {
			if(!jwtTools.verifyToken(token.getContent())) {
				token.setActive(false);
				tokenRepository.save(token);
			}
		});
		
	}
	
	@Override
	public User findOneByIdentifier(String identifier) {
		return userRepo.findOneByUsernameOrEmail(identifier, identifier);
	}
	
	@Override
	public Boolean comparePassword(String toCompare, String current) {
		return passwordEncoder.matches(toCompare, current);
	}

	@Override
	public User findOneByUsernameOrEmail(String username, String email) {
		return userRepo.findOneByUsernameOrEmail(username, email);
	}


	@Override
	public User findUserAuthenticated() {
		String username = SecurityContextHolder
			.getContext()
			.getAuthentication()
			.getName();
		
		return userRepo.findOneByUsernameOrEmail(username, username);
	}
	
	

	
	
}
