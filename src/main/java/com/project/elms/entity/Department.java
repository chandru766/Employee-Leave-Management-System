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
@Table(name="department_list") 
public class Department  {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;  
	private String name;
	private String description;
	private Timestamp   date_created;
	private Timestamp   date_updated; 
}