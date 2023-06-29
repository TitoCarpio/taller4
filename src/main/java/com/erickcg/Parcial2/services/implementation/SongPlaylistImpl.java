package com.erickcg.Parcial2.services.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.erickcg.Parcial2.models.dtos.SongDto;
import com.erickcg.Parcial2.models.entities.Song;
import com.erickcg.Parcial2.models.entities.SongXPlaylist;
import com.erickcg.Parcial2.repositories.PlaylistRepository;
import com.erickcg.Parcial2.repositories.SongXPlaylistRepository;
import com.erickcg.Parcial2.services.ISongPlay;

import jakarta.transaction.Transactional;

@Service
public class SongPlaylistImpl implements ISongPlay {

	@Autowired
	SongXPlaylistRepository songplayRepo;
	@Autowired
	PlaylistRepository playRepo;

	@Override
	@Transactional(rollbackOn = Exception.class)
	public void save(SongXPlaylist info) {
		songplayRepo.save(info);

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
		// recorriendo la lista y llenando la newSong
		for (Song song : songs) {
			SongDto songDto = new SongDto();
			songDto.setCode(song.getCode());
			songDto.setTitle(song.getTitle());
			songDto.setDuration(convertirSegundos(song.getDuration()));
			newSong.add(songDto);
		}

		return newSong;
	}

	// funcion que recorre la lista de canciones y obtiene la duracion total
	public static String duracionTotal(List<Song> songs) {
		int duracionTotal = 0;
		for (Song song : songs) {
			duracionTotal += song.getDuration();
		}
		return convertirSegundos(duracionTotal);
	}

	@Override
	public Page<SongXPlaylist> findPlaylist(UUID codeplay, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("code")); 
		Page<SongXPlaylist> song = songplayRepo.findByPlaylistCode(codeplay,pageable);
		return song;

	}

}
