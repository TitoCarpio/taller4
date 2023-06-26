package com.erickcg.Parcial2.models.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResPlaylistDto {
	private String title;
	private String description;
	private String durationList;
	private List<SongDto> songs;
}
