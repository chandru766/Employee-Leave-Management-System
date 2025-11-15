package com.project.elms.repository;

 
 
import org.springframework.data.jpa.repository.JpaRepository;
 
import com.project.elms.entity.Designation;

public interface  DesignationRepository  extends JpaRepository<Designation, Integer> {
	Designation	findById(int id);  
	Designation findByName(String un);  
}
