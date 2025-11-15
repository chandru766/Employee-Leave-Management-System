package com.project.elms.repository;

 
 
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
 
import com.project.elms.entity.Leavetype;

public interface  LeavetypesRepository  extends JpaRepository<Leavetype, Integer> {
	Leavetype	findById(int id);  
	Leavetype	findByCode(String cd);  
	List<Leavetype>   findByStatus(int st);
	Leavetype   findByName(String name); 
}
