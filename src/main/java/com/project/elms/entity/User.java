package com.project.elms.entity;

import java.sql.Timestamp;
import java.util.Date;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
 


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="users") 
public class User  {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id; 
	private String   fullname; 
	private String   username;
	private String   password;
	private String email;
	private String contact; 
	private Timestamp   last_login;
	private String   type;
	private int status;
	private Timestamp   date_added;
	private Timestamp   date_updated; 
}