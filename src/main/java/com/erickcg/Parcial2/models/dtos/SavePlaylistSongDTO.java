package com.erickcg.Parcial2.models.dtos;

import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class SavePlaylistSongDTO {
	@NotEmpty
	private UUID song;
	
}
