package com.erickcg.Parcial2.contollers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erickcg.Parcial2.models.dtos.MessageDTO;
import com.erickcg.Parcial2.models.dtos.SearchSongDTO;
import com.erickcg.Parcial2.models.dtos.SongDto;
import com.erickcg.Parcial2.services.ISong;
import com.erickcg.Parcial2.utils.RequestErrorHandler;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/API/v1/")
public class SongController {
	
	@Autowired ISong songServices;
	
	@Autowired 
	private RequestErrorHandler errorHandler;
	
	@GetMapping("song/")
	public ResponseEntity<?> allSong(@RequestBody @Valid SearchSongDTO info, BindingResult validations){
		
		if(validations.hasErrors()) {
			return new ResponseEntity<>(
					errorHandler.mapErrors(validations.getFieldErrors()), 
					HttpStatus.BAD_REQUEST);
		}
		
		try {
			List<SongDto> songs = songServices.allSong(info);
			if (songs.isEmpty()) {
				return new ResponseEntity<>("No se encontraron canciones", HttpStatus.OK);
			}
			
			return new ResponseEntity<>(songs, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
	}

}
