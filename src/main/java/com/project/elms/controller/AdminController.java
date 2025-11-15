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

@Controller
public class AdminController { 
	
 
	 
	@Autowired 
	private UserloginsService adminService;  
	
	@Autowired 
	private UserRepository users;
	 
    
	@GetMapping("/")
    public String index(Authentication auth)
    { 
		   //adminService.changeUserPassword(adminService.findByUsername("admin"), "Test@123");
		   if(auth!=null)
		    if(auth.isAuthenticated())
		    {
		    	if(!auth.getAuthorities().isEmpty())
		    	{
		    		for(LoginTypes l:LoginTypes.values())
		    		if(auth.getAuthorities().contains(new SimpleGrantedAuthority(l.toString())))
		              	return "redirect:/"+l.toString().toLowerCase()+"/dashboard"; 
		    	}
		    }
    		return "login";
    }
	
	 @GetMapping("/login")
	    public String login(Model model) {  
	        return "login";
	    } 
	
    @GetMapping("/admin")
    public String user(Model model) {  
        return "redirect:admin/dashboard";
    } 
    
     
 
    @GetMapping("/admin/dashboard") 
    public String dashboard(Model model){
        //model.addAttribute("users", users.findByTypeOrderByIdDesc(LoginTypes.EMPLOYEE.toString()));
        return "admin/index";
    } 
    
    @GetMapping("/admin/departments") 
    public String departments(Model model){ 
        return "admin/departments";
    } 
    
    @GetMapping("/admin/designations") 
    public String designations(Model model){ 
        return "admin/designations";
    }
    
    @GetMapping("/admin/leavetypes") 
    public String leavetypes(Model model){ 
        return "admin/leavetypes";
    }
    
    @GetMapping("/admin/employees") 
    public String employees(Model model){ 
        return "admin/employees";
    }
    
    @GetMapping("/admin/leavebalances") 
    public String leavebalances(Model model){ 
        return "admin/leavebalances";
    }
    
    @GetMapping("/admin/leaveapplications") 
    public String leaveapplications(Model model){  
    	model.addAttribute("statuses",LeaveStatus.values());
        return "admin/leaveapplications";
    }
     
    
    @GetMapping("/admin/holidays") 
    public String holidays(Model model){ 
        return "admin/holidays";
    }
    
    
    @GetMapping("/admin/changepassword") 
    public String changepassword(Model model){ 
        return "admin/changepassword";
    } 
     
    
    @PostMapping("/admin/updatepassword")
    public String updatepassword(HttpServletRequest request,Model m)
    {
    	String password=request.getParameter("password");
    	String newpassword=request.getParameter("newpassword");
    	String confirmpassword=request.getParameter("confirmpassword");
    	
    	User user = adminService.findByUsername(
    		      SecurityContextHolder.getContext().getAuthentication().getName());
    		    if(newpassword.compareTo(confirmpassword)>0)
    		    {
    		    	m.addAttribute("updatepassword_errors", "New Password and Confirm Password Field do not match  !!");
    		    }
    		    else if (!adminService.checkIfValidOldPassword(user, password)) {
    		        m.addAttribute("updatepassword_errors", "Inavlid Current Password");
    		    }
    		    else if(!adminService.validation_Password(newpassword))
    		    {
    		    	  m.addAttribute("updatepassword_errors", "Inavlid New Password.Please follow specified policies.");
    		    }
    		    else
    		    {
    		    	adminService.changeUserPassword(user, newpassword);
    		      m.addAttribute("updatepassword_success", "Password updated successfully");
    		    }
    	return changepassword(m);
    } 
    
    @GetMapping("/avatars/{un}")
    @ResponseBody
    public ResponseEntity<InputStreamResource>  avatars(@PathVariable("un") String un,HttpServletResponse response) throws Exception
    	{ 
    	    
    	  InputStreamResource file;
    	   String fpath = (new FileSystemResource("")).getFile().getAbsolutePath()+"\\avatars\\"; 
    	   if(new File(fpath+un).exists())
    	        file=  new InputStreamResource(new FileInputStream(new File(fpath+un)));
    	   else 
    		     file=  new InputStreamResource(new FileInputStream(new File(fpath+"boy.png")));
    	   
    	   return ResponseEntity.ok()
    			   .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;")
    			   .body(file); 
    	} 
 
    
    @GetMapping("/stravatars/{un}")
    @ResponseBody
    public ResponseEntity<InputStreamResource> stravatars(@PathVariable("un") String un,HttpServletResponse response) throws Exception
    	{ 
    	   
    	  User user=adminService.findByUsername(un);
    	  InputStreamResource file;
    	   String fpath = (new FileSystemResource("")).getFile().getAbsolutePath()+"\\avatars\\"; 
    	   if(new File(fpath+user.getId()).exists())
    		   fpath=  fpath+user.getId();
    	   else 
    		     fpath=fpath+"boy.png";
    	    
    	   file=new InputStreamResource(new FileInputStream(new File(fpath)));
    	   return ResponseEntity.ok()
    			   .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;")
    			   .body(file); 
    	} 
    
    @GetMapping("/admin/profile") 
    public String profile(Model model){
    	User obj = adminService.findByUsername(
  		      SecurityContextHolder.getContext().getAuthentication().getName());
    	model.addAttribute("obj", obj); 
        return "admin/profile";
    } 
    
    @PostMapping("/admin/updateprofile")
    @ResponseBody
    public Map<String,Object> updateprofile(HttpServletRequest request)
    {
    	Map<String,Object> arr=new HashMap<>();
    	arr.put("success", 0);  
   	    String email=request.getParameter("email");
	    String fullname=request.getParameter("fullname");     
	    String contact=request.getParameter("contact");     
	    String username=request.getParameter("username");     
	    List<String> errors=new ArrayList<String>();
	    
	    User user = adminService.findByUsername(
  		      SecurityContextHolder.getContext().getAuthentication().getName());
	    
	    
	    if(fullname.isBlank()) errors.add("Full name");  
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
	    
	    
	    if(!adminService.validation_username(username))
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
	    	user.setFullname(fullname);
	    	user.setUsername(username);
	    	user.setEmail(email);
	    	user.setContact(contact); 
	    	users.save(user); 
	    }
	    else
	        arr.put("message","The following fields contains errors: "+errors.stream().collect(Collectors.joining(", ")));
	    	
	    return   arr; 	
    }
    
    @GetMapping("/admin/users") 
    public String users(Model model){ 
    	model.addAttribute("types",LoginTypes.values());
        return "admin/users";
    } 
    
    @Resource   private EmployeeRepository employees;  
    @Resource   private DepartmentRepository departments;  
    @Resource   private DesignationRepository designations;

	  @Resource   private LeavetypesRepository leavetypes;  
    
    @GetMapping("/admin/editemployee/{id}") 
    public String editemployee(@PathVariable("id")int id,Model model){  
    	User uobj=users.findById(id);
    	model.addAttribute("uobj",uobj);
    	Employee e=employees.findByUser(uobj);
    	if(e==null)
    	{
    		e=new Employee();
    		model.addAttribute("reportingtoname","");
    	}
        else
        {
        	User c=users.findById(e.getReportingto());
        	if(c!=null)
        	  model.addAttribute("reportingtoname",c.getFullname()+" - "+c.getType());
        	else if(e.getReportingto()==0)
        		 model.addAttribute("reportingtoname","Not Applicable");
        }

    	model.addAttribute("eobj",e);
    	model.addAttribute("departments",departments.findAll(Sort.by(Direction.ASC, "name")));
    	model.addAttribute("designations",designations.findAll(Sort.by(Direction.ASC, "name")));
        return "admin/editemployee";
    } 
    
    @GetMapping("/admin/viewemployee/{id}") 
    public String viewemployee(@PathVariable("id")int id,Model model){ 
    	User uobj=users.findById(id);
    	model.addAttribute("uobj",uobj);
    	Employee e=employees.findByUser(uobj);
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
        	model.addAttribute("reportingtoname",c.getFullname()+" - "+c.getType());
        	model.addAttribute("department",departments.findById(e.getDepartment()).getName());
        	model.addAttribute("designation",designations.findById(e.getDesignation()).getName());
        }

    	model.addAttribute("eobj",e);
        return "admin/viewemployee";
    } 
     
   
    @GetMapping("/admin/newleaveapplication") 
    public String newleaveapplication(Model model){   
    	model.addAttribute("leavetypes", leavetypes.findByStatus(1));
        return "admin/newleaveapplication";
    } 
    
    @GetMapping("/admin/editleaveapplication/{id}") 
    public String newleaveapplication(@PathVariable("id")int id,Model model){   
    	model.addAttribute("id", id);
    	model.addAttribute("leavetypes", leavetypes.findByStatus(1));
        return "admin/editleaveapplication";
    } 
    
    
    @GetMapping("/admin/empreport") 
    public String empreport(Model model){    
        return "admin/empreport";
    } 
    
    @GetMapping("/admin/empleavesreport") 
    public String empleavesreport(Model model){    
        return "admin/empleavesreport";
    } 
    
     
    
}