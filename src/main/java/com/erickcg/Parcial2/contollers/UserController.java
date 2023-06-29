package com.erickcg.Parcial2.contollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.erickcg.Parcial2.models.entities.User;
import com.erickcg.Parcial2.models.dtos.MessageDTO;
import com.erickcg.Parcial2.models.dtos.PageDTO;
import com.erickcg.Parcial2.models.dtos.SaveUserDto;
import com.erickcg.Parcial2.models.dtos.SearchPlaylistDTO;
import com.erickcg.Parcial2.models.dtos.TokenDTO;
import com.erickcg.Parcial2.models.dtos.UserLoginDTO;
import com.erickcg.Parcial2.models.entities.Playlist;
import com.erickcg.Parcial2.models.entities.Song;
import com.erickcg.Parcial2.models.entities.Token;
import com.erickcg.Parcial2.services.IUser;
import com.erickcg.Parcial2.utils.JWTTools;
import com.erickcg.Parcial2.utils.RequestErrorHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/API/v1/")
//@RequestMapping()
public class UserController {

	@Autowired
	IUser userServices;

	// para obtener el usuario
	@Autowired
	JWTTools jwtTools;

	@Autowired
	private RequestErrorHandler errorHandler;

	@PostMapping("/auth/singup")
	public ResponseEntity<?> saveUser(@RequestBody @Valid SaveUserDto info, BindingResult validations) {
		if (validations.hasErrors()) {
			return new ResponseEntity<>(errorHandler.mapErrors(validations.getFieldErrors()), HttpStatus.BAD_REQUEST);
		}

		try {
			userServices.save(info);
			return new ResponseEntity<>(new MessageDTO("User created"), HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(new MessageDTO("El usuario o correo ya existe"),
					HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	@GetMapping("/user/playlist")
	public ResponseEntity<?> playlistByUser(@RequestBody @Valid SearchPlaylistDTO info, HttpServletRequest request,
			BindingResult validations, @RequestParam(defaultValue = "0") int page, 
			@RequestParam(defaultValue = "5") int size) {
		// obtengo el toquen de los headers de la peticion
		String tokenHeader = request.getHeader("Authorization");
		String token = tokenHeader.substring(7);
		// obtengo el user del token
		String username = jwtTools.getUsernameFrom(token);
		if (validations.hasErrors()) {
			return new ResponseEntity<>(errorHandler.mapErrors(validations.getFieldErrors()), HttpStatus.BAD_REQUEST);
		}

		try {
			Page<Playlist> play = userServices.searchUserPlaylist(info, username, page, size);
			
			if (play == null) {
				return new ResponseEntity<>("El usuario no tiene listas creadas", HttpStatus.OK);
			}

			if (!play.isEmpty()) {
				//lleno el DTO 
				PageDTO<Song> response = new PageDTO<>(
						play.getContent(),
						play.getNumber(),
						play.getSize(),
						play.getTotalElements(),
						play.getTotalPages()
						);
				
				
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else if (play.isEmpty()) {
				return new ResponseEntity<>("El usuario no tiene listas creadas con ese nombre", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("No se encontraron coincidencias", HttpStatus.OK);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@ModelAttribute @Valid UserLoginDTO info, BindingResult validations) {
		if (validations.hasErrors()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		User user = userServices.findOneByUsernameOrEmail(info.getIdentifier(), info.getIdentifier());

		if (user == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		if (!userServices.comparePassword(info.getPassword(), user.getPassword())) {

			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		try {
			Token token = userServices.registerToken(user);

			return new ResponseEntity<>(new TokenDTO(token), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
