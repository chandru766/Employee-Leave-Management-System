package com.project.elms.repository;


import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.project.elms.LoginTypes; 
import com.project.elms.entity.User;

public interface  UserRepository  extends JpaRepository<User, Integer> {
	User	findById(int id); 
    List<User>  findAllByOrderByIdDesc(Pageable p);  
	List<User>   findByTypeOrderByIdDesc(String type);  
	User findByUsername(String un);
	User findByEmail(String email);
	User findByContact(String contact);
	List<User> findByFullnameContaining(String key);
	List<User> findByTypeAndFullnameContaining(String type, String key);
	List<User> findAllByOrderByIdDesc();
	
	List<User> findByStatusOrderByIdDesc(int status);
	List<User> findByStatusAndTypeOrderByIdDesc(int i, String string);
	List<User> findByStatusAndFullnameContaining(int i, String key);
	List<User> findByStatusAndTypeAndFullnameContaining(int i, String type, String key);
	List<User> findByStatusAndTypeOrderByFullname(int i, String string); 
	
}
