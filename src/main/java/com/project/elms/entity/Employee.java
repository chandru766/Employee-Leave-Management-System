package com.project.elms.entity;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

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
@Table(name="employee") 
public class Employee  {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id; 
	private String empid; 
	private int gender;
	private Date dob;
	private Date doj;
	private int department;
	private int designation;
	private int reportingto;
	private String address; 
	private int resigned;
	private Date dor;
	private Timestamp   date_added;
	private Timestamp   date_updated;  
	
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user", referencedColumnName = "id")
	    private User user;
	
}