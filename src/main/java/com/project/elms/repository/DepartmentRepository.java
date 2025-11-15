package com.project.elms.repository;

 
 
import org.springframework.data.jpa.repository.JpaRepository;
 
import com.project.elms.entity.Department;

public interface  DepartmentRepository  extends JpaRepository<Department, Integer> {
	Department	findById(int id);  
	Department findByName(String un);  
}
