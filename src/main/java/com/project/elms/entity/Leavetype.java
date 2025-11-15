package com.project.elms.entity;

import java.sql.Timestamp; 

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
@Table(name="leave_types") 
public class Leavetype  {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;  
	private String code;
	private String name;
	private String description;
	private float default_credit;
	private int status;
	private Timestamp   date_created;
	private Timestamp   date_updated; 
}