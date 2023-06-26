package com.erickcg.Parcial2.contollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erickcg.Parcial2.models.dtos.MessageDTO;
import com.erickcg.Parcial2.models.dtos.SavePlayListDTO;
import com.erickcg.Parcial2.services.IPlaylist;
import com.erickcg.Parcial2.utils.JWTTools;
import com.erickcg.Parcial2.utils.RequestErrorHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/API/v1/")
public class PlaylistController {

	@Autowired IPlaylist playServices;
	@Autowired 
	private RequestErrorHandler errorHandler;
	
	//para obtener el usuario
	@Autowired
	JWTTools jwtTools;
	
	@PostMapping("playlist")
	public ResponseEntity<?> savePlaylist(@RequestBody @Valid SavePlayListDTO info,HttpServletRequest request,  BindingResult validations){
		
		// obtengo el toquen de los headers de la peticion
				String tokenHeader = request.getHeader("Authorization");
				String token = tokenHeader.substring(7);
				// obtengo el user del token
				String username = jwtTools.getUsernameFrom(token);
		
		
		if(validations.hasErrors()) {
			return new ResponseEntity<>(
					errorHandler.mapErrors(validations.getFieldErrors()), 
					HttpStatus.BAD_REQUEST);
		}
		
		try {
			
			if(playServices.save(info, username)) {
				return new ResponseEntity<>(
						new MessageDTO("PlayList created"), HttpStatus.CREATED);
			}else {
				return new ResponseEntity<>(
						new MessageDTO("Playlist no creada, el usuario no existe o la playlist ya existe"), HttpStatus.BAD_GATEWAY);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
	}
}
