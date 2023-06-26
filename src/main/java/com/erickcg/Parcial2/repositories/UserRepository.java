package com.erickcg.Parcial2.repositories;

import java.util.UUID;

import org.springframework.data.repository.ListCrudRepository;

import com.erickcg.Parcial2.models.entities.User;

public interface UserRepository 
	extends ListCrudRepository<User, UUID> {
	User findByEmail(String email);
	User findByCode(UUID code);
	User findOneByUsernameOrEmail(String username, String email);

}
