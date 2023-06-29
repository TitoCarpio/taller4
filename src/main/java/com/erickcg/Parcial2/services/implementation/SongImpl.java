package com.erickcg.Parcial2.services.implementation;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
//import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.erickcg.Parcial2.models.dtos.SearchSongDTO;
import com.erickcg.Parcial2.models.dtos.SongDto;
import com.erickcg.Parcial2.models.entities.Song;
import com.erickcg.Parcial2.repositories.SongRepository;
import com.erickcg.Parcial2.services.ISong;

@Service
public class SongImpl implements ISong {

	@Autowired
	SongRepository songRepo;

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

	@Override
	public Page<Song> allSong(SearchSongDTO info, int page, int size) {
		
		//declaro una lista de tipo page que sera donde guarde lo que me devuelve la lista 
//		Page<Song> songs;
		Pageable pageable = PageRequest.of(page, size, Sort.by("title"));
		//cambie la condicion del if
		if (info.getTitle().isEmpty()) {
			 
			return songRepo.findAll(pageable);
			
		} else {
//			Pageable pageable = PageRequest.of(page, size); 
//			List<Song> song;
			
//			List<SongDto> transformation = recorrerLista(song);
			return songRepo.findByTitleContains(info.getTitle(), pageable);
			
		}

	}

//	@Override
//	public Page<Song> page(int page, int size) {
//		Pageable pageable = PageRequest.of(page, size, Sort.by("code").descending());
//		return songRepo.findAll(pageable);
//	}
	
	

}
