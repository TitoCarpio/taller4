package com.erickcg.Parcial2.models.dtos;

import java.util.UUID;

import lombok.Data;

@Data
public class SongDto {
	
	private UUID code;
	private String title;
	private String duration;

}
