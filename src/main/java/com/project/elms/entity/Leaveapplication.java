package com.project.elms.entity;

import java.sql.Date;
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
@Table(name="leave_applications") 
public class Leaveapplication  {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;   
	private int employee;
	private int leavetype;
	private String reason;
	private Date fromdate;
	private Date todate;
	private int type;
	private String status;
	@Column(nullable = true)
	private Object approvedby;
	@Column(nullable = true)
	private Timestamp approvedat;
	private float leavedays;
	private Timestamp   date_created;
	private Timestamp   date_updated;
	public void setApprovedby(Object object) {
		if(object!=null)
		   this.approvedby=(int) object;
		else
			this.approvedby=null;
	} 
	
}