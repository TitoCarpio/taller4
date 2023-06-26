package com.erickcg.Parcial2.models.dtos;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SearchPlaylistDTO {
	
//	@NotEmpty(message = "El correo del usuario no puede estar vacio")
//	@Email(message = "No cumple con el formato de correo electronico")
//	private String email;
	
	@Size(min= 0, max = 64)
	private String title;
}
