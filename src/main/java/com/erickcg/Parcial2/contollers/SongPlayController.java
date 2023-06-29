package com.erickcg.Parcial2.contollers;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.erickcg.Parcial2.models.dtos.MessageDTO;
import com.erickcg.Parcial2.models.dtos.PageDTO;
import com.erickcg.Parcial2.models.dtos.ResPlaylistDto;
import com.erickcg.Parcial2.models.dtos.SavePlaylistSongDTO;
import com.erickcg.Parcial2.models.dtos.SongDto;
import com.erickcg.Parcial2.models.entities.Playlist;
import com.erickcg.Parcial2.models.entities.Song;
import com.erickcg.Parcial2.models.entities.SongXPlaylist;
import com.erickcg.Parcial2.repositories.PlaylistRepository;
import com.erickcg.Parcial2.repositories.SongRepository;
import com.erickcg.Parcial2.repositories.SongXPlaylistRepository;
import com.erickcg.Parcial2.services.ISongPlay;
import com.erickcg.Parcial2.utils.RequestErrorHandler;

@RestController
@RequestMapping("/API/v1/")
public class SongPlayController {
	@Autowired ISongPlay songPlayServices;
	@Autowired PlaylistRepository playRepo;
	@Autowired SongRepository songRepo;
	@Autowired SongXPlaylistRepository songplayRepo;
	
	
	@Autowired 
	private RequestErrorHandler errorHandler;
	
	@PostMapping("playlist/{code}")
	public ResponseEntity<?> saveSongPlay(@PathVariable UUID code, @RequestBody SavePlaylistSongDTO info, BindingResult validations){
		if(validations.hasErrors()) {
			return new ResponseEntity<>(
					errorHandler.mapErrors(validations.getFieldErrors()), 
					HttpStatus.BAD_REQUEST);
		}
		
		try {
			
			if (code != null && info != null) {
				//verifico si ambos existen
				Song song = songRepo.findByCode(info.getSong());
				Playlist play = playRepo.findByCode(code);
				
				if(song != null && play != null) {
					//busco si esa cancion existe en la playlist
					SongXPlaylist songXPlay = songplayRepo.findByPlaylistCodeAndSongCode(code, info.getSong());
					if (songXPlay == null) {
						SongXPlaylist songPlay = new SongXPlaylist(
								new Date(),
								song,
								play
								);
						
						songPlayServices.save(songPlay);
						return new ResponseEntity<>(new MessageDTO("Song added to playlist"), HttpStatus.OK);
					}else {
						return new ResponseEntity<>(new MessageDTO("La cancion ya existe en la lista"), HttpStatus.ACCEPTED);
					}
					
				}else {
					return new ResponseEntity<>(
						new MessageDTO("Playlist o cancion no existe"), HttpStatus.ACCEPTED);
				}
				
			}
			return new ResponseEntity<>(
					new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
			
			
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

			//funcion que recorre la lista de canciones y obtiene la duracion total
			public static String duracionTotal(List<Song> songs) {
				int duracionTotal = 0;
				for (Song song : songs) {
					duracionTotal += song.getDuration();
				}
				return convertirSegundos(duracionTotal);
			}
	
	//funcion que recorre la lista de SongXPlaylist y obtiene las canciones
	public static List<Song> recorrerListaSongXPlaylist(List<SongXPlaylist> songs) {
		List<Song> newSong = new ArrayList<>();
		//recorriendo la lista y llenando la newSong
		for (SongXPlaylist song : songs) {
			Song songDto = new Song();
			songDto.setCode(song.getSong().getCode());
			songDto.setTitle(song.getSong().getTitle());
			songDto.setDuration(song.getSong().getDuration());
			newSong.add(songDto);
		}

		return newSong;
	}
	
	
	//fucnion que recorre todas las paginaciones de la lista
	public List<Song> canciones (UUID playlist){
		List<Song> song = new ArrayList<>();
		int page =0, size = 5;
		Page<SongXPlaylist> songxPla = songPlayServices.findPlaylist(playlist, page, size);
		
		//obtengo la cantidad de paginas
		int paginas =songxPla.getTotalPages();
		
		while (page <= paginas) {
			Page<SongXPlaylist> songxPlay = songPlayServices.findPlaylist(playlist, page, size);
			song.addAll(recorrerListaSongXPlaylist(songxPlay.getContent()));
			page++;
		}
		return song;
	}

	
	
	
	@GetMapping("playlist/{playlist}")
	public ResponseEntity<?> findPlaylist(@PathVariable UUID playlist, @RequestParam(defaultValue = "0") int page, 
			@RequestParam(defaultValue = "5") int size){
		try {
			Playlist play = playRepo.findByCode(playlist);
			if (play != null) {
				
				List<Song> songs = canciones(playlist);
				
				
				
				
				Page<SongXPlaylist> songxPla = songPlayServices.findPlaylist(playlist, page, size);
				
				
				
				
				//obteniendo las canciones
				List<Song> son = recorrerListaSongXPlaylist(songxPla.getContent());
				//obteniendo la duracion totla de la Playlist
				String duration = duracionTotal(songs);
				//transformo el formato de la duracion a inutos y segundos
				List<SongDto> transformation = recorrerLista(son);
				
				
				PageDTO<SongDto> response = new PageDTO<>(
						transformation,
						songxPla.getNumber(),
						songxPla.getSize(),
						songxPla.getTotalElements(),
						songxPla.getTotalPages()
						);
				
				ResPlaylistDto info = new ResPlaylistDto(
						play.getTitle(),
						play.getDescription(),
						duration,
						response
						);
				
//				
				
				return new ResponseEntity<>(info, HttpStatus.ACCEPTED);
			}else {
				return new ResponseEntity<>("La playlist no existe", HttpStatus.CONFLICT);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	
		
	}
	
}
