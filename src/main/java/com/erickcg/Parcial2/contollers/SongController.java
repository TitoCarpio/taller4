package com.erickcg.Parcial2.contollers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.erickcg.Parcial2.models.dtos.MessageDTO;
import com.erickcg.Parcial2.models.dtos.PageDTO;
import com.erickcg.Parcial2.models.dtos.SearchSongDTO;
import com.erickcg.Parcial2.models.dtos.SongDto;
import com.erickcg.Parcial2.models.entities.Song;
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
	public ResponseEntity<?> allSong(@RequestParam(defaultValue = "0") int page, 
			@RequestParam(defaultValue = "5") int size,
			@RequestBody @Valid SearchSongDTO info, 
			BindingResult validations){
		
		if(validations.hasErrors()) {
			return new ResponseEntity<>(
					errorHandler.mapErrors(validations.getFieldErrors()), 
					HttpStatus.BAD_REQUEST);
		}
		
		try {
			Page<Song> songs = songServices.allSong(info, page, size);
			if (songs.isEmpty()) {
				return new ResponseEntity<>("No se encontraron canciones", HttpStatus.OK);
			}
			
			List<SongDto> songsFormat =recorrerLista(songs.getContent());
			PageDTO<Song> response = new PageDTO<>(
					songsFormat,
					songs.getNumber(),
					songs.getSize(),
					songs.getTotalElements(),
					songs.getTotalPages()
					);
			return new ResponseEntity<>(response, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
	}
	
	
	// funcion que me da el formato de segundos a MM:ss
		public static String convertirSegundos(int segundos) {
			int minutos = segundos / 60;
			int segundosRestantes = segundos % 60;
			return String.format("%02d:%02d", minutos, segundosRestantes);
		}

		// funcion que recorre los elementos de una lista y los convierte a MM:ss
		public static List<SongDto> recorrerLista(List<Song> songs) {
			List<SongDto> newSong = new ArrayList<>();
			//recorriendo la lista y llenando la newSong
			for (Song song : songs) {
				SongDto songDto = new SongDto();
				songDto.setCode(song.getCode());
				songDto.setTitle(song.getTitle());
				songDto.setDuration(convertirSegundos(song.getDuration()));
				newSong.add(songDto);
			}

			return newSong;
		}
}
