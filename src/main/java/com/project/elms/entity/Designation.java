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
@Table(name="designation_list") 
public class Designation  {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;  
	private String name;
	private String description;
	private Timestamp   date_created;
	private Timestamp   date_updated; 
}