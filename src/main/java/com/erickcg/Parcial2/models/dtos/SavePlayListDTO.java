package com.erickcg.Parcial2.models.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class SavePlayListDTO {
	@NotEmpty(message = "EL titulo de la lista es necesario")
	@Size(min = 1, max = 64)
	private String title;
	
	@NotEmpty(message = "La descripcion es necesaria")
	@Size(min = 1, max = 64)
	private String description;
	
//	@NotEmpty(message = "El email de usuario es necesario")
//	@Email
//	private String email;
	
	//private TokenDTO token;

}
