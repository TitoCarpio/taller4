package com.erickcg.Parcial2.services.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

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
	public List<SongDto> allSong(SearchSongDTO info) {
		List<Song> songs;

		if (info.getTitle() == null) {
			songs = songRepo.findAll();

			List<SongDto> transformation = recorrerLista(songs);
			return transformation;
			
		} else {
			songs = songRepo.findByTitleContains(info.getTitle());
			List<SongDto> transformation = recorrerLista(songs);
			return transformation;
			
		}

	}

}
