package com.project.elms.controller;
  
import com.project.elms.LeaveStatus;
import com.project.elms.LoginTypes;

import com.project.elms.entity.*;
import com.project.elms.repository.*; 
import com.project.elms.service.UserloginsService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;  
import java.util.HashMap; 
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;  
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest; 

@Controller
public class EmployeeController { 
	
 
	 
	@Autowired 
	private UserloginsService employeeService;  
	
	@Autowired 
	private UserRepository users;
	  
	
	 
    @GetMapping("/employee")
    public String user(Model model) {  
        return "redirect:employee/dashboard";
    } 
     
 
    @GetMapping("/employee/dashboard") 
    public String dashboard(Model model){
        //model.addAttribute("users", users.findByTypeOrderByIdDesc(LoginTypes.EMPLOYEE.toString()));
        return "redirect:/employee/myleaveapplications";
    } 
     
    
    @GetMapping("/employee/myleavebalance") 
    public String myleavebalance(Model model){ 
        return "employee/myleavebalance";
    }
    
    @GetMapping("/employee/myleaveapplications") 
    public String leaveapplications(Model model){  
    	model.addAttribute("statuses",LeaveStatus.values());
        return "employee/myleaveapplications";
    }
     
 
    
    @GetMapping("/employee/holidays") 
    public String holidays(Model model){ 
        return "employee/holidays";
    }
    
    @GetMapping("/employee/teamreport") 
    public String teamreport(Model model){ 
        return "employee/teamreport";
    }
    
    
    @GetMapping("/employee/changepassword") 
    public String changepassword(Model model){ 
        return "employee/changepassword";
    } 
     
    
    @PostMapping("/employee/updatepassword")
    public String updatepassword(HttpServletRequest request,Model m)
    {
    	String password=request.getParameter("password");
    	String newpassword=request.getParameter("newpassword");
    	String confirmpassword=request.getParameter("confirmpassword");
    	
    	User user = employeeService.findByUsername(
    		      SecurityContextHolder.getContext().getAuthentication().getName());
    		    if(newpassword.compareTo(confirmpassword)>0)
    		    {
    		    	m.addAttribute("updatepassword_errors", "New Password and Confirm Password Field do not match  !!");
    		    }
    		    else if (!employeeService.checkIfValidOldPassword(user, password)) {
    		        m.addAttribute("updatepassword_errors", "Inavlid Current Password");
    		    }
    		    else if(!employeeService.validation_Password(newpassword))
    		    {
    		    	  m.addAttribute("updatepassword_errors", "Inavlid New Password.Please follow specified policies.");
    		    }
    		    else
    		    {
    		    	employeeService.changeUserPassword(user, newpassword);
    		      m.addAttribute("updatepassword_success", "Password updated successfully");
    		    }
    	return changepassword(m);
    } 
  
    
    
    @GetMapping("/employee/profile") 
    public String profile(Model model){
    	User obj = employeeService.findByUsername(
  		      SecurityContextHolder.getContext().getAuthentication().getName());
    	model.addAttribute("obj", obj); 
        return "employee/profile";
    } 

    @Resource   private EmployeeRepository employees;  
    @Resource   private DepartmentRepository departments;  
    @Resource   private DesignationRepository designations;
    

	  @Resource   private LeavetypesRepository leavetypes;  
    
    @GetMapping("/employee/mydetails") 
    public String mydetails(Model model){
    	User obj = employeeService.findByUsername(
  		      SecurityContextHolder.getContext().getAuthentication().getName());
    	model.addAttribute("uobj",obj);
    	Employee e=employees.findByUser(obj);
    	if(e==null)
    	{
    		e=new Employee();
    		model.addAttribute("reportingtoname","");

        	model.addAttribute("department","");
        	model.addAttribute("designation","");
    	}
        else
        {
        	User c=users.findById(e.getReportingto());
        	if(c!=null)
          	  model.addAttribute("reportingtoname",c.getFullname());
          	else if(e.getReportingto()==0)
          		 model.addAttribute("reportingtoname","Not Applicable");
        	model.addAttribute("department",departments.findById(e.getDepartment()).getName());
        	model.addAttribute("designation",designations.findById(e.getDesignation()).getName());
        }

    	model.addAttribute("eobj",e);
        return "employee/mydetails";
    } 
    
    @PostMapping("/employee/updateprofile")
    @ResponseBody
    public Map<String,Object> updateprofile(MultipartHttpServletRequest request) throws Exception
    {
    	String fpath = (new FileSystemResource("")).getFile().getAbsolutePath()+"\\avatars\\"; 
    	Map<String,Object> arr=new HashMap<>();
    	arr.put("success", 0);  
   	    String email=request.getParameter("email");   
	    String contact=request.getParameter("contact");     
	    String username=request.getParameter("username");     
	    MultipartFile img=request.getFile("img");
	    List<String> errors=new ArrayList<String>();
	    
	    User user = employeeService.findByUsername(
  		      SecurityContextHolder.getContext().getAuthentication().getName());
	    
	     
	    if(email.isBlank()) errors.add("Email"); 
	    if(username.isBlank()) errors.add("Username"); 
	    if(contact.isBlank()) errors.add("Contact"); 
	    User cobj;
	    cobj=users.findByUsername(username);
	    if(cobj!=null)
	    	if(cobj.getId()!=user.getId()) errors.add("Username already Exists!");
	    
	    cobj=users.findByEmail(email);
	    if(cobj!=null)
	    	if(cobj.getId()!=user.getId()) errors.add("Email already Exists!");
	    
	    
	    if(!employeeService.validation_username(username))
	    	errors.add("Inavlid Username!");
	    
	    cobj=users.findByContact(contact);
	    if(cobj!=null)
	    	if(cobj.getId()!=user.getId()) errors.add("Contact already Exists!");
	    
	    if(errors.size()==0)
	    { 
	    	arr.put("success", 1);
	        arr.put("message","Saved!"); 
	    	if(user.getUsername().compareTo(username)!=0)
	    	{
	    		arr.put("success", 2);
		        arr.put("message","Saved!,as username changed please login again"); 
		        
	    	} 
	    	user.setUsername(username);
	    	user.setEmail(email);
	    	user.setContact(contact); 
	    	users.save(user); 
	    	if(img.getSize()>0)
	    	{
	    		(new File(fpath+cobj.getId())).createNewFile();
	             FileOutputStream fl;
	             fl=new FileOutputStream(fpath+user.getId()); 
	             fl.write(img.getBytes());                       
	             fl.close();  
	    	}
	    }
	    else
	        arr.put("message","The following fields contains errors: "+errors.stream().collect(Collectors.joining(", ")));
	    	
	    return   arr; 	
    }
    
    
    @GetMapping("/employee/newleaveapplication") 
    public String newleaveapplication(Model model){   
    	model.addAttribute("leavetypes", leavetypes.findByStatus(1));
        return "employee/newleaveapplication";
    } 
    
    @GetMapping("/employee/editleaveapplication/{id}") 
    public String newleaveapplication(@PathVariable("id")int id,Model model){   
    	model.addAttribute("id", id);
    	model.addAttribute("leavetypes", leavetypes.findByStatus(1));
        return "employee/editleaveapplication";
    } 
    
    
    @GetMapping("/employee/teamleaveapplications") 
    public String teamleaveapplications(Model model){  
    	model.addAttribute("statuses",LeaveStatus.values());
        return "employee/teamleaveapplications";
    }
    
    @GetMapping("/employee/myleavesreport") 
    public String myleavesreport(Model model){    
        return "employee/myleavesreport";
    } 
    @GetMapping("/employee/teamleavesreport") 
    public String teamleavesreport(Model model){    
        return "employee/teamleavesreport";
    } 
}