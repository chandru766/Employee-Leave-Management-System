package com.project.elms.controller;
  
import com.project.elms.CommonFuns;
import com.project.elms.LeaveStatus;
import com.project.elms.LoginTypes;
import com.project.elms.repository.*; 
import com.project.elms.entity.*;
import com.project.elms.service.UserloginsService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.FileSystemResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest; 
 

@RestController
@RequestMapping(path="/employee/JSON", produces="application/json")
@CrossOrigin(origins="*")
public class EmployeeRestJsonResponse { 
	
	@Resource	private UserloginsService logins;    
	@Resource   private UserRepository users;  
	@Resource   private DepartmentRepository departments;  
	@Resource   private DesignationRepository designations;  
	@Resource   private LeavetypesRepository leavetypes; 
	@Resource   private HolidayRepository holidays;  
	@Resource   private EmployeeRepository employees;  
	@Resource   private EmpleavecreditsRepository elcredits;
	@Resource   private LeaveapplicationsRepository eleaves;  
	@PersistenceContext
    private EntityManager em; 
	
	@PostMapping("/holidays")
	public Hashtable<String,Object>  holidays(HttpServletRequest request)
	{
		Hashtable<String,Object> arr=new Hashtable<String,Object>(); 
		String action=request.getParameter("action");
		arr.put("success", 0);  
		switch(action)
		{
		case "readall":
			arr.put("success", 1);  
			int year=Integer.parseInt(request.getParameter("year"));
			arr.put("data" ,holidays.findByYearOrderByDate(year));
			break;  
		}
		return arr;
	}
	
	@PostMapping("/users")
	public Hashtable<String,Object>  users(HttpServletRequest request)
	{
		Hashtable<String,Object> arr=new Hashtable<String,Object>();
		Hashtable<String,Object> obj=new Hashtable<String,Object>();
		String action=request.getParameter("action");
		arr.put("success", 0);
		User cobj;
		String name,username,contact,email, password,type,sts;
		int id;
		switch(action)
		{
		case "readall":
			arr.put("success", 1);   
			int status=CommonFuns.cint( request.getParameter("status"));
			arr.put("data" ,users.findByStatusOrderByIdDesc(status));
			break; 
		case "search":
			String key=request.getParameter("key");
			  type=request.getParameter("type");
			List<User> objs;
			if(type==null)
			  objs=users.findByStatusAndFullnameContaining(1,key); 
			else
				objs=users.findByStatusAndTypeAndFullnameContaining(1,type,key);
			
			
			arr.put("data" ,new ArrayList<Hashtable<String,Object>>());
			for(User c:objs)
			{
				obj=new Hashtable<String,Object>(); 
	        	obj.put("id",c.getId());
	        	obj.put("label", c.getFullname()+" - "+c.getType()); 
	        	obj.put("value", c.getFullname()+" - "+c.getType()); 
				((List<Hashtable<String,Object>>)arr.get("data")).add(obj);
			} 
			break;
		case "read":			
			 id=Integer.parseInt(request.getParameter("id"));
			cobj= users.findById(id);
			if(cobj==null)
			{
				arr.put("success", 0);
				arr.put("message", "Not Exists!");
			}
			else
			{
				arr.put("success", 1);
			   arr.put("data",cobj);
			}
			break;
		    case "add":
		    	  name=request.getParameter("fullname");
			      email=request.getParameter("email"); 
			      type=request.getParameter("type");
			      contact=request.getParameter("contact"); 
			      username=request.getParameter("username"); 
		   	      password=request.getParameter("password"); 
		   	      sts=request.getParameter("status");
			      
			    List<String> errors=new ArrayList<String>();
			    
			    
			    if(username.isBlank())
			    	errors.add("Contact");
			    else
			    	if(users.findByUsername(username)!=null)
			    	     errors.add("Username exists!");
			    
			    if(email.isBlank())
			    	errors.add("Email");
			    else
			    	if(users.findByEmail(email)!=null)
			    	     errors.add("Email exists!");
			    
			    if(contact.isBlank())
			    	errors.add("Contact");
			    else
			    	if(users.findByContact(contact)!=null)
			    	     errors.add("Contact exists!");
			    
			    if(!logins.validation_username(username))
			    	errors.add("Inavlid Username!");
			    
			    if(!logins.validation_Password(password))   errors.add("A valid Password");
			  
		    		
			    if(name.isBlank()) errors.add("Full name");
			    if(type.isBlank()) errors.add("type");
			    
			    if(errors.size()==0)
			    {
			    	  cobj=new User();
			    	  cobj.setFullname(name);
			    	  cobj.setEmail(email);
			    	  cobj.setContact(contact);
			    	  cobj.setUsername(username);
			    	  cobj.setType(type);
			    	  cobj.setStatus(CommonFuns.cint(sts));
			    	  cobj.setPassword("Test@123"); 
			    	users.save(cobj);
			    	logins.changeUserPassword(cobj, password);
			    	arr.put("success", 1);
			        arr.put("message","User Created successfully!");
			    }
			    else
			        arr.put("message","The following fields contains errors: "+errors.stream().collect(Collectors.joining("<br/> ")));
			break;
		    case "edit":
		    	id=Integer.parseInt(request.getParameter("id")); 
		    	name=request.getParameter("fullname");
			      email=request.getParameter("email"); 
			      type=request.getParameter("type");
			      contact=request.getParameter("contact"); 
			      username=request.getParameter("username"); 
		   	      password=request.getParameter("password");     
		   	   sts=request.getParameter("status");
			     errors=new ArrayList<String>();
			      
			     errors=new ArrayList<String>();
			    
			    User user =users.findById(id);
			    
			    if(user==null) errors.add("Not exists!");
			    if(name.isBlank()) errors.add("Full name");  
			    
			    if(email.isBlank()) errors.add("Email"); 
			    else
			    {
			    	User user1=users.findByEmail(email);
			    	if(user1!=null) 
			    	if(user.getId()!=user1.getId())
			    		errors.add("Email already exists!");
			    }
			    
			    if(contact.isBlank()) errors.add("Contact"); 
			    else
			    {
			    	User user1=users.findByContact(contact);
			    	if(user1!=null) 
			    	if(user.getId()!=user1.getId())
			    		errors.add("Contact already exists!");
			    }
			    
			    if(username.isBlank()) errors.add("Username"); 
			    else
			    { 
				    if(!logins.validation_username(username))
				    	errors.add("Inavlid Username!");
			    	User user1=users.findByUsername(username);
			    	if(user1!=null) 
			    	if(user.getId()!=user1.getId())
			    		errors.add("Username already exists!");
			    	
			    }
			    
			    if(!password.isBlank())
			    {
			    	 if(!logins.validation_Password(password))   errors.add("A valid Password");
			    	 
			    }
			    
			    if(errors.size()==0)
			    { 
			    	cobj =users.findById(id);
			    	 cobj.setFullname(name);
			    	  cobj.setEmail(email);
			    	  cobj.setContact(contact);
			    	  cobj.setUsername(username);
			    	  cobj.setStatus(CommonFuns.cint(sts));
			    	  cobj.setType(type);
			    	users.save(cobj); 
			    	 if(!password.isBlank())
			    		 logins.changeUserPassword(user, password);
			    	arr.put("success", 1);
			        arr.put("message","Saved!");
			    }
			    else
			        arr.put("message","The following fields contains errors: "+errors.stream().collect(Collectors.joining("<br/> ")));

		    	break;
		    
		}
		return arr;
	}
	
	@PostMapping("/employees")
	public Hashtable<String,Object>  emps(MultipartHttpServletRequest request) throws Exception
	{
		Hashtable<String,Object> arr=new Hashtable<String,Object>();
		Hashtable<String,Object> obj=new Hashtable<String,Object>();
		String action=request.getParameter("action");
		arr.put("success", 0);
		Employee cobj; 
		String resigned;
		int user;  
		switch(action)
		{
		case "useremployees": 
			List<User>  objs=users.findByStatusAndTypeOrderByIdDesc(1,LoginTypes.EMPLOYEE.toString());
			arr.put("data" ,new ArrayList<Hashtable<String,Object>>());
			for(User c:objs)
			{
				arr.put("success", 1);   
				cobj=employees.findByUser(c);
				obj=new Hashtable<String,Object>(); 
	        	obj.put("user",c.getId()); 
	        	obj.put("username",c.getUsername()); 
	        	obj.put("name",c.getFullname());  
	        	 obj.put("empid","");
	        	  obj.put("department","");
	        	  obj.put("designation","");
	        	if(cobj!=null)
	        	{
	        	  obj.put("empid",cobj.getEmpid());
	        	  obj.put("resigned",cobj.getResigned());
	        	  obj.put("department",departments.findById(cobj.getDepartment()).getName());
	        	  obj.put("designation",designations.findById(cobj.getDesignation()).getName());
	        	}
	        	((List<Hashtable<String,Object>>)arr.get("data")).add(obj);
			} 
			
			break;
		case "report": 
			User uobj = logins.findByUsername(
	    		      SecurityContextHolder.getContext().getAuthentication().getName());
			List<Employee> objs1=employees.findByReportingtoAndStatus(em,uobj.getId(),1);
			arr.put("data" ,new ArrayList<Hashtable<String,Object>>());
			arr.put("success", 1);   
			for(Employee cobj1:objs1)
			{
				arr.put("success", 1);   
		        User c= cobj1.getUser();
				obj=new Hashtable<String,Object>(); 
	        	obj.put("user",c.getId()); 
	        	obj.put("username",c.getUsername()); 
	        	obj.put("name",c.getFullname());     
	        	  obj.put("empid",cobj1.getEmpid());
	        	  obj.put("resigned",cobj1.getResigned());
	        	  obj.put("department",departments.findById(cobj1.getDepartment()).getName());
	        	  obj.put("designation",designations.findById(cobj1.getDesignation()).getName());
	        	  obj.put("doj",cobj1.getDoj());
	        	  obj.put("dob",cobj1.getDob()!=null?cobj1.getDob():"");
	        	  obj.put("gender",cobj1.getGender()==1?"MALE":"FEMALE"); 
	        	((List<Hashtable<String,Object>>)arr.get("data")).add(obj);
			} 
			
			break;	
		case "readall":
			arr.put("success", 1);   
			resigned= request.getParameter("resigned");
			arr.put("data" ,employees.findByResignedOrderByIdDesc(CommonFuns.cint(resigned)));
			break; 
		 
		case "read":			
			user=Integer.parseInt(request.getParameter("user"));
			cobj= employees.findByUser(users.findById(user));
			if(cobj==null)
			{
				arr.put("success", 0);
				arr.put("message", "Not Exists!");
			}
			else
			{
				arr.put("success", 1);
			   arr.put("data",cobj);
			}
			break;
		    
		}
		return arr;
	}
	
	@PostMapping("/leavebalances")
	public Hashtable<String,Object>  leavebalances(HttpServletRequest request) throws Exception
	{
		Hashtable<String,Object> arr=new Hashtable<String,Object>();
		Hashtable<String,Object> obj=new Hashtable<String,Object>();
		String action=request.getParameter("action");
		arr.put("success", 0);
		Employee cobj;  
		switch(action)
		{ 
	 
		case "read":			
			User user = logins.findByUsername(
	    		      SecurityContextHolder.getContext().getAuthentication().getName());
			cobj= employees.findByUser(user);
			arr.put("success", 0);   
			if(cobj!=null)
        	{ 
				arr.put("success", 1);   
        		obj=new Hashtable<String,Object>(); 
        		User c=user;
	        	obj.put("user",c.getId());  
	        	obj.put("name",c.getFullname());  
        	    obj.put("empid",cobj.getEmpid());
        	    obj.put("eid",cobj.getId());
        	    obj.put("doj",cobj.getDoj());
        	    obj.put("department",departments.findById(cobj.getDepartment()).getName());
        	    obj.put("designation",designations.findById(cobj.getDesignation()).getName()); 
        	   // List<EmpLeaveCredits> leaves=elcreadits.findByEmployee(cobj.getId());
        	    List<Hashtable<String,Object>> leavebal=new ArrayList<Hashtable<String,Object>>(); 
        	    Hashtable<String,Object> obj1=new  Hashtable<String,Object> (); 
        	    	for(Leavetype l:leavetypes.findByStatus(1))
        	    	{
        	    		obj1=new  Hashtable<String,Object> (); 
        	    		obj1.put("leavetype", l.getCode());
        	    		obj1.put("id",l.getId());
        	    		EmpLeaveCredits obj2 = elcredits.findByEmployeeAndLeavetype(cobj.getId(),l.getId());
        	    		if(obj2!=null)
        	    			obj1.put("bal",obj2); 
        	    		else {
        	    		    obj2=new EmpLeaveCredits();
        	    		    obj2.setLeavetype(l.getId()); 
        	    		    obj2.setOpeningbal(0);
        	    		    obj2.setEmployee(0);
        	    		    obj2.setBal(0);
        	    		    obj2.setUsed(0);
        	    		}
        	    		obj1.put("bal",obj2); 
        	    		leavebal.add(obj1);
        	    	} 
        	 obj.put("leavebal",leavebal);
        	 arr.put("data", obj);
        	}
			else
				arr.put("message", "Employee detailes not  found");   
			break; 
		}
		return arr;
	}
	@PostMapping("/empleaves")
	public Hashtable<String,Object>  empleaves(HttpServletRequest request) throws Exception
	{
		Hashtable<String,Object> arr=new Hashtable<String,Object>();
		Hashtable<String,Object> obj=new Hashtable<String,Object>();
		String action=request.getParameter("action");
		arr.put("success", 0);
		Employee cobj; 
		 User user = logins.findByUsername(
	  		      SecurityContextHolder.getContext().getAuthentication().getName());
		Leaveapplication application;
		String  datestart,dateend,reason,leavetype,leavedays,dtype; 
		User uobj;
		switch(action)
		{
		case "readall": 
			String status= request.getParameter("status");
			 cobj= employees.findByUser(user);
			List<Leaveapplication>  objs=eleaves.findByEmployeeAndStatus(cobj.getId(),status);
			arr.put("data" ,new ArrayList<Hashtable<String,Object>>());
			arr.put("success", 1);   
			for(Leaveapplication c:objs)
			{ 
	        		obj=new Hashtable<String,Object>(); 
	        		cobj=employees.findById(c.getEmployee());
	        		uobj= cobj.getUser(); 
		        	obj.put("name",uobj.getFullname());  
	        	    obj.put("empid",cobj.getEmpid());
	        	    obj.put("fromdate",c.getFromdate());
	        	    obj.put("id",c.getId());
	        	    obj.put("leavetype",leavetypes.findById(c.getLeavetype()).getCode());
	        	    obj.put("days", c.getLeavedays());
	        	    obj.put("status",c.getStatus()); 
	        	    if(c.getStatus().compareTo(LeaveStatus.APPROVED.toString())==0) 
	        	    	obj.put("statusclass","badge badge-success");
	        	    else if(c.getStatus().compareTo(LeaveStatus.DENIED.toString())==0) 
	        	    	obj.put("statusclass","badge badge-danger");
	        	    else if(c.getStatus().compareTo(LeaveStatus.CANCELLED.toString())==0) 
	        	    	obj.put("statusclass","badge badge-danger");
					else
						obj.put("statusclass","badge badge-primary"); 
	        	    
	        	    obj.put("editable",c.getStatus().compareTo(LeaveStatus.PENDING.toString())==0);
	        	    ((List<Hashtable<String,Object>>)arr.get("data")).add(obj); 
			} 
			
			break;
		case "readteamall": 
			  status= request.getParameter("status");
			List<Employee>   objs1= employees.findByReportingtoAndStatus(em, user.getId(),1);
			arr.put("data" ,new ArrayList<Hashtable<String,Object>>());
			arr.put("success", 1); 
			for(Employee cobj1:objs1)
			{
			  objs=eleaves.findByEmployeeAndStatus(cobj1.getId(),status); 
			for(Leaveapplication c:objs)
			{ 
	        		obj=new Hashtable<String,Object>();  
	        		uobj= cobj1.getUser(); 
		        	obj.put("name",uobj.getFullname());  
	        	    obj.put("empid",cobj1.getEmpid());
	        	    obj.put("fromdate",c.getFromdate());
	        	    obj.put("id",c.getId());
	        	    obj.put("leavetype",leavetypes.findById(c.getLeavetype()).getCode());
	        	    obj.put("days", c.getLeavedays());
	        	    obj.put("status",c.getStatus()); 
	        	    if(c.getStatus().compareTo(LeaveStatus.APPROVED.toString())==0) 
	        	    	obj.put("statusclass","badge badge-success");
	        	    else if(c.getStatus().compareTo(LeaveStatus.DENIED.toString())==0) 
	        	    	obj.put("statusclass","badge badge-danger");
	        	    else if(c.getStatus().compareTo(LeaveStatus.CANCELLED.toString())==0) 
	        	    	obj.put("statusclass","badge badge-danger");
					else
						obj.put("statusclass","badge badge-primary"); 
	        	    
	        	    obj.put("editable",c.getStatus().compareTo(LeaveStatus.PENDING.toString())==0);
	        	    ((List<Hashtable<String,Object>>)arr.get("data")).add(obj); 
			} 
			}
			break;

		case "report":
			String fromdate= request.getParameter("fromdate");
			String todate= request.getParameter("todate");
			arr.put("success", 0);   
			if(!fromdate.isBlank())
			{
				if(!todate.isBlank())
				{
				  	if(Date.valueOf(fromdate).compareTo(Date.valueOf(todate))<0)
				  	{
				  		cobj=employees.findByUser(user);
				  		objs=eleaves.findByEmployeedates(em,cobj.getId(), Date.valueOf(fromdate),Date.valueOf(todate));
						arr.put("data" ,new ArrayList<Hashtable<String,Object>>());
						arr.put("success", 1);   
						for(Leaveapplication c:objs)
						{ 
							obj=new Hashtable<String,Object>(); 
			        		cobj=employees.findById(c.getEmployee());
			        		uobj= cobj.getUser(); 
				        	obj.put("name",uobj.getFullname());  
			        	    obj.put("empid",cobj.getEmpid());
			        	    obj.put("fromdate",c.getFromdate());
			        	    obj.put("todate",c.getTodate());
			        	    obj.put("reason",c.getReason());
			        	    obj.put("id",c.getId());
			        	    obj.put("leavetype",leavetypes.findById(c.getLeavetype()).getCode());
			        	    obj.put("days", c.getLeavedays());
			        	    obj.put("status",c.getStatus()); 
			        	    if(c.getStatus().compareTo(LeaveStatus.APPROVED.toString())==0) 
			        	    	obj.put("statusclass","text-success");
			        	    else if(c.getStatus().compareTo(LeaveStatus.DENIED.toString())==0) 
			        	    	obj.put("statusclass","text-danger");
			        	    else if(c.getStatus().compareTo(LeaveStatus.CANCELLED.toString())==0) 
			        	    	obj.put("statusclass","text-danger");
							else
								obj.put("statusclass","text-primary"); 
			        	    
			        	    obj.put("editable",c.getStatus().compareTo(LeaveStatus.PENDING.toString())==0);
			        	    ((List<Hashtable<String,Object>>)arr.get("data")).add(obj); 
						}
					   
				  	}
				  	else
						arr.put("message","Inavlid To & From date");
				}
				else
					arr.put("message","Inavlid To date");
			}
			else
				arr.put("message","Inavlid From date");
			break;
		
		case "teamreport":
			  fromdate= request.getParameter("fromdate");
			  todate= request.getParameter("todate");
			arr.put("success", 0);   
			if(!fromdate.isBlank())
			{
				if(!todate.isBlank())
				{
				  	if(Date.valueOf(fromdate).compareTo(Date.valueOf(todate))<0)
				  	{
				  		objs1=employees.findByReportingtoAndStatus(em,user.getId(),1);
						arr.put("data" ,new ArrayList<Hashtable<String,Object>>());
						arr.put("success", 1);   
				  		for(Employee e:objs1)
				  		{
						cobj=e;
				  		objs=eleaves.findByEmployeedates(em,cobj.getId(), Date.valueOf(fromdate),Date.valueOf(todate));
						 
						arr.put("success", 1);   
						for(Leaveapplication c:objs)
						{ 
							obj=new Hashtable<String,Object>(); 
			        		cobj=employees.findById(c.getEmployee());
			        		uobj= cobj.getUser(); 
				        	obj.put("name",uobj.getFullname());  
			        	    obj.put("empid",cobj.getEmpid());
			        	    obj.put("fromdate",c.getFromdate());
			        	    obj.put("todate",c.getTodate());
			        	    obj.put("reason",c.getReason());
			        	    obj.put("id",c.getId());
			        	    obj.put("leavetype",leavetypes.findById(c.getLeavetype()).getCode());
			        	    obj.put("days", c.getLeavedays());
			        	    obj.put("status",c.getStatus()); 
			        	    if(c.getStatus().compareTo(LeaveStatus.APPROVED.toString())==0) 
			        	    	obj.put("statusclass","text-success");
			        	    else if(c.getStatus().compareTo(LeaveStatus.DENIED.toString())==0) 
			        	    	obj.put("statusclass","text-danger");
			        	    else if(c.getStatus().compareTo(LeaveStatus.CANCELLED.toString())==0) 
			        	    	obj.put("statusclass","text-danger");
							else
								obj.put("statusclass","text-primary"); 
			        	    
			        	    obj.put("editable",c.getStatus().compareTo(LeaveStatus.PENDING.toString())==0);
			        	    ((List<Hashtable<String,Object>>)arr.get("data")).add(obj); 
						}
				  		}
				  	}
				  	else
						arr.put("message","Inavlid To & From date");
				}
				else
					arr.put("message","Inavlid To date");
			}
			else
				arr.put("message","Inavlid From date");
			break;
			
		case "readleaveapplication":
			int id=Integer.parseInt(request.getParameter("id")); 
			application= eleaves.findById(id);
			if(application==null)
			{
				arr.put("success", 0);
				arr.put("message", "Not Exists!");
			}
			else
			{ 
				if(application.getStatus().compareTo(LeaveStatus.PENDING.toString())!=0)
				{
					arr.put("success", 0);
					arr.put("message", "Not Editable");
				}
				else
				{
					arr.put("success", 1);
					arr.put("data", application);
					cobj=employees.findById(application.getEmployee());
	        		User c= cobj.getUser();
					arr.put("user",c); 
				}
			}
	    break;
		case "read":			
			  id=Integer.parseInt(request.getParameter("id"));
			application= eleaves.findById(id);
			if(application==null)
			{
				arr.put("success", 0);
				arr.put("message", "Not Exists!");
			}
			else
			{ 
				arr.put("success", 1);   
        		obj=new Hashtable<String,Object>(); 
        		cobj=employees.findById(application.getEmployee());
        		User c= cobj.getUser();
	        	obj.put("user",c.getId());  
	        	obj.put("name",c.getFullname());  
        	    obj.put("empid",cobj.getEmpid());
        	    obj.put("fromdate",application.getFromdate());
        	    obj.put("todate",application.getTodate());
        	    obj.put("reason",application.getReason());
        	    if(application.getApprovedby()!=null)
        	    {
        	    	obj.put("approvedby", users.findById((int)application.getApprovedby()).getFullname());
        	    	obj.put("approvedat", application.getApprovedat());
        	    }
        	    obj.put("reason",application.getReason());
        	    obj.put("id",c.getId());
        	    obj.put("leavetype",leavetypes.findById(application.getLeavetype()).getCode());
        	    obj.put("days",application.getLeavedays());
        	    obj.put("status",application.getStatus()); 
        	    if(application.getStatus().compareTo(LeaveStatus.APPROVED.toString())==0) 
        	    	obj.put("statusclass","badge badge-success");
        	    else if(application.getStatus().compareTo(LeaveStatus.DENIED.toString())==0) 
        	    	obj.put("statusclass","badge badge-danger");
        	    else if(application.getStatus().compareTo(LeaveStatus.CANCELLED.toString())==0) 
        	    	obj.put("statusclass","badge badge-danger");
				else
					obj.put("statusclass","badge badge-primary"); 
        	    arr.put("data", obj);
        	} 
			break;
		   case "add":  
			      leavetype=request.getParameter("leavetype");
			      dtype=request.getParameter("type");
			      datestart=request.getParameter("datestart");
			      leavedays=request.getParameter("leavedays");
			      dateend=request.getParameter("dateend");
			      reason=request.getParameter("reason");
			      cobj= employees.findByUser(user);
			   	  arr.put("success", 0);   
			   	  List<String> errors=new ArrayList<String>();
				  
			   	  if(cobj==null)
		        	errors.add("Employee Not exists!");
			   	  else
			   	  { 
			   		if(leavedays.isBlank()) 
			   			errors.add("Invalid leave days");
			   		if(datestart.isBlank())
			   			errors.add("Invalid Day start");
			   		else
			   			if(eleaves.isdateexists(em, cobj.getId(),Date.valueOf(datestart) ,0))
			   				errors.add("Day start already exists in records!");
			   	  
			   		if(dateend.isBlank())
			   			errors.add("Invalid Day end");
			   		else
			   			if(eleaves.isdateexists(em, cobj.getId(),Date.valueOf(dateend) ,0))
			   				errors.add("Day end already exists in records!");
			   		
			   		
			   		if(Date.valueOf(datestart).compareTo(Date.valueOf(dateend))>0)
			   			 errors.add("Invalid Dates");
			   		 
			   		if(reason.isBlank())
			   			errors.add("Reason required");
			   		
			     	EmpLeaveCredits el=elcredits.findByEmployeeAndLeavetype(cobj.getId(),CommonFuns.cint(leavetype) );
			     	if(el==null)
			   	      errors.add("No Leave balance");
			     	else
			     	  if(el.getBal()<CommonFuns.cfloat(leavedays))
			     			 errors.add("No Leave balance");
			       	  
			   	  }
			      if(errors.size()==0)
				  {
			    	  application=new Leaveapplication();
			    	  application.setEmployee(cobj.getId());
			    	  application.setLeavetype(CommonFuns.cint(leavetype));
			    	  application.setType(CommonFuns.cint(dtype));
			    	  application.setFromdate(Date.valueOf(datestart));
			    	  application.setTodate(Date.valueOf(dateend));
			    	  application.setReason(reason);
			    	  application.setStatus(LeaveStatus.PENDING.toString());
			    	  application.setLeavedays(CommonFuns.cfloat(leavedays));
			    	  eleaves.save(application);  
			   	arr.put("success", 1);
		        arr.put("message","Employee Leave Application created successfully!");
		    }
		    else
		        arr.put("message","The following fields contains errors: "+errors.stream().collect(Collectors.joining("<br/> ")));
 
			   	  break;  
		   case "edit": 
				   id=Integer.parseInt(request.getParameter("id"));
					application= eleaves.findById(id); 
					 
			      leavetype=request.getParameter("leavetype");
			      dtype=request.getParameter("type");
			      datestart=request.getParameter("datestart");
			      leavedays=request.getParameter("leavedays");
			      dateend=request.getParameter("dateend");
			      reason=request.getParameter("reason");
			      cobj= employees.findByUser(user);
			   	  
			      
			   	   errors=new ArrayList<String>();
			   	  if(application==null)
			   		errors.add("Application Not exists!");
			   	  
			   	  if(cobj==null)
		        	errors.add("Employee Not exists!");
			   	  else
			   	  { 
			   		if(leavedays.isBlank()) 
			   			errors.add("Invalid leave days");
			   		if(datestart.isBlank())
			   			errors.add("Invalid Day start");
			   		else
			   			if(eleaves.isdateexists(em, cobj.getId(),Date.valueOf(datestart) ,id))
			   				errors.add("Day start already exists in records!");
			   	  
			   		if(dateend.isBlank())
			   			errors.add("Invalid Day end");
			   		else
			   			if(eleaves.isdateexists(em, cobj.getId(),Date.valueOf(dateend),id ))
			   				errors.add("Day end already exists in records!");
			   		

			   		if(Date.valueOf(datestart).compareTo(Date.valueOf(dateend))>0)
			   			 errors.add("Invalid Dates");
			   		
			   		if(reason.isBlank())
			   			errors.add("Reason required");
			   		
			     	EmpLeaveCredits el=elcredits.findByEmployeeAndLeavetype(cobj.getId(),CommonFuns.cint(leavetype) );
			     	if(el==null)
			   	      errors.add("No Leave balance");
			     	else
			     	  if(el.getBal()<CommonFuns.cfloat(leavedays))
			     			 errors.add("No Leave balance");
			       	  
			   	  }
			      if(errors.size()==0)
				  {
			    	  application= eleaves.findById(id); 
			    	  application.setEmployee(cobj.getId());
			    	  application.setLeavetype(CommonFuns.cint(leavetype));
			    	  application.setType(CommonFuns.cint(dtype));
			    	  application.setFromdate(Date.valueOf(datestart));
			    	  application.setTodate(Date.valueOf(dateend));
			    	  application.setReason(reason);
			    	  application.setStatus(LeaveStatus.PENDING.toString());
			    	  application.setLeavedays(CommonFuns.cfloat(leavedays));
			    	  eleaves.save(application);  
			   	arr.put("success", 1);
		        arr.put("message","Employee Leave Application Updated successfully!");
		    }
		    else
		        arr.put("message","The following fields contains errors: "+errors.stream().collect(Collectors.joining("<br/> ")));

			   	  break;  
		     case "changestatus":
		    	  id=Integer.parseInt(request.getParameter("id"));
		    	  status=request.getParameter("status");
		    	application= eleaves.findById(id);
				if(application==null)
				{
					arr.put("success", 0);
					arr.put("message", "Not Exists!");
				}
				else
				{ 
					arr.put("success", 1);
					cobj=employees.findById(application.getEmployee());
					EmpLeaveCredits obj2=elcredits.findByEmployeeAndLeavetype(cobj.getId(),application.getLeavetype());
				    
	                if(application.getStatus().compareTo(LeaveStatus.APPROVED.toString())==0 && status.compareTo(LeaveStatus.APPROVED.toString() )!=0)
					{
			  		    obj2.setBal(obj2.getBal()+application.getLeavedays());
					    obj2.setUsed(obj2.getUsed()-application.getLeavedays());
					    application.setApprovedat(null);
					    application.setApprovedby(null);
					    elcredits.save(obj2);
					}
					else if(status.compareTo(LeaveStatus.APPROVED.toString())==0)
					{
						if(obj2!=null)
					    {
					    	if(obj2.getBal()<application.getLeavedays())
					    	{
					    		arr.put("success", 0);
								arr.put("message", "No Leave Balance!");
								return arr;
					    	}
					    }
					    else 
					    {
					    	arr.put("success", 0);
							arr.put("message", "No Leave Balance!");
							return arr;
					    }
						obj2.setBal(obj2.getBal()-application.getLeavedays());
					    obj2.setUsed(obj2.getUsed()+application.getLeavedays());
					    elcredits.save(obj2);
					    application.setApprovedat(Timestamp.valueOf(LocalDateTime.now()));
					    
					    User employee = logins.findByUsername(
				  		      SecurityContextHolder.getContext().getAuthentication().getName());
					    application.setApprovedby(employee.getId());
					} 
						application.setStatus(status);
						eleaves.save(application); 
					arr.put("message", "Changed Successfully!");
				}
		    	break;	   	  
			   	  
		   case "delete":
		    	  id=Integer.parseInt(request.getParameter("id"));
		    	application= eleaves.findById(id);
				if(application==null)
				{
					arr.put("success", 0);
					arr.put("message", "Not Exists!");
				}
				else
				{ 
					
					if(application.getStatus().compareTo(LeaveStatus.APPROVED.toString())==0)
					{
						arr.put("success", 0);
						arr.put("message", "Approved Leaves cannot be deleted!");
					}
					else
					{
						arr.put("success", 1);
					eleaves.delete(application); 
					arr.put("message", "Deleted Successfully!");
					}
				}
		    	break;
		}
		return arr;
	}
	
}
