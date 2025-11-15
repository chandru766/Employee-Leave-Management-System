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
@Table(name="employee_leave_credits") 
public class EmpLeaveCredits  {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;  
	private int employee;  
	private int leavetype; 
	private float openingbal;
	private float bal;
	private float used;
	private Timestamp   date_added;
	private Timestamp   date_updated; 
}