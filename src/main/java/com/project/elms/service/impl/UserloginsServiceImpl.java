package com.project.elms.service.impl;
 
 
 
import com.project.elms.entity.User; 
import com.project.elms.repository.UserRepository;
import com.project.elms.service.UserloginsService;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserloginsServiceImpl implements UserloginsService {

    private UserRepository userRepository; 
    private PasswordEncoder passwordEncoder;

    public UserloginsServiceImpl(UserRepository userRepository, 
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository; 
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(User user) { 
         
        userRepository.save(user);
    }

    @Override
    public User findByUsername(String un) {
        return userRepository.findByUsername(un);
    }

    @Override
    public List<User> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .collect(Collectors.toList());
    }
 
	@Override
	public boolean checkIfValidOldPassword(User user, String password) {
		if(passwordEncoder.matches(password, user.getPassword()))
            return true;
		else
			return false;
	}

	@Override
	public void changeUserPassword(User user, String newpassword) {
		user.setPassword(passwordEncoder.encode(newpassword));
        
        userRepository.save(user);
	}
	
	@Override
	//______________________________________________________________________________
	/**
	 * Validation Password     */
	//______________________________________________________________________________
	public   boolean validation_Password(final String PASSWORD_Arg)    {
	    boolean result = false;
	    try {
	        if (PASSWORD_Arg!=null) {
	            //_________________________
	            //Parameteres
	            final String MIN_LENGHT="8";
	            final String MAX_LENGHT="20"; 

	            //_________________________
	            //Modules
	            final String ONE_DIGIT = "(?=.*[0-9])";  //(?=.*[0-9]) a digit must occur at least once
	            final String LOWER_CASE = "(?=.*[a-z])";  //(?=.*[a-z]) a lower case letter must occur at least once
	            final String UPPER_CASE = "(?=.*[A-Z])";  //(?=.*[A-Z]) an upper case letter must occur at least once
	            final String NO_SPACE = "(?=\\S+$)";  //(?=\\S+$) no whitespace allowed in the entire string
	            //final String MIN_CHAR = ".{" + MIN_LENGHT + ",}";  //.{8,} at least 8 characters
	            final String MIN_MAX_CHAR = ".{" + MIN_LENGHT + "," + MAX_LENGHT + "}";  //.{5,10} represents minimum of 5 characters and maximum of 10 characters

	            final String SPECIAL_CHAR;
	             SPECIAL_CHAR= "(?=.*[@#$%^&+=])"; //(?=.*[@#$%^&+=]) a special character must occur at least once
	             
	            //_________________________
	            //Pattern
	            //String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
	            final String PATTERN = ONE_DIGIT + LOWER_CASE + UPPER_CASE + SPECIAL_CHAR + NO_SPACE + MIN_MAX_CHAR;
	            //_________________________
	            result = PASSWORD_Arg.matches(PATTERN);
	            //_________________________
	        }    

	    } catch (Exception ex) {
	        result=false;
	    }

	    return result;
	}
	
	  @Override
	  public Collection < ? extends GrantedAuthority> getAuthorities(String role ) {
	    	HashSet<GrantedAuthority> obj= new HashSet<GrantedAuthority>();
	    	obj.add(new SimpleGrantedAuthority(role));
	    	return obj;
	    }

	@Override
	public boolean validation_username(String username) {
		 boolean result = false;
		    try {
		        if (username!=null) {
		                   
		            final String PATTERN ="^[a-zA-Z][a-zA-Z0-9_]{4,19}$";
		            //_________________________
		            result = username.matches(PATTERN);
		            //_________________________
		        }    

		    } catch (Exception ex) {
		        result=false;
		    }

		    return result;
	}
	     
	 
}
