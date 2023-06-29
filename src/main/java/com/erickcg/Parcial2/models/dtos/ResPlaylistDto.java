package com.erickcg.Parcial2.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResPlaylistDto {
	private String title;
	private String description;
	private String durationList;
	private PageDTO<?> page;
}
