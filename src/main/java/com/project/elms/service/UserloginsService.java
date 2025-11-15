package com.project.elms.service;
 
 
import com.project.elms.entity.User;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

public interface UserloginsService {
    void saveUser(User user);

    User findByUsername(String un);

    List<User> findAllUsers();

	boolean checkIfValidOldPassword(User user, String password);

	void changeUserPassword(User user, String newpassword);

	boolean validation_Password(String password);
	
	boolean validation_username(String username);

	Collection<? extends GrantedAuthority> getAuthorities(String role);
}
