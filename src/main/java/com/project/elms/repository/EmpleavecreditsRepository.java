package com.project.elms.repository;

 
 
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
 
import com.project.elms.entity.EmpLeaveCredits;

public interface  EmpleavecreditsRepository  extends JpaRepository<EmpLeaveCredits, Integer> {
	EmpLeaveCredits	findById(int id);  
	List<EmpLeaveCredits>   findByEmployee(int emp);   
	EmpLeaveCredits   findByEmployeeAndLeavetype(int emp,int type);
}
