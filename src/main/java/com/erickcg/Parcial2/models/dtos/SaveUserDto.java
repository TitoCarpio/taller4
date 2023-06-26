package com.erickcg.Parcial2.models.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SaveUserDto {
	
	@NotEmpty(message = "El nombre de usuario es requerido")
	private String username;
	
	@NotEmpty(message = "El correo es obligatorio")
	@Email
	private String email;
	
	@NotEmpty(message = "La consena es obligatoria")
	@Pattern(regexp ="^(?=.*\\d)(?=.*[\\u0021-\\u002b\\u003c-\\u0040])(?=.*[A-Z])(?=.*[a-z])\\S{8,16}$", message = "La contra debe de tener una minuscula, una mayuscula, un numero, un simbolo y almenos 8 caracteres")
	private String password;

}
