package com.project.elms.repository;

 
 
import java.util.ArrayList;
import java.util.List;
 
import org.springframework.data.jpa.repository.JpaRepository;
 
import com.project.elms.entity.Employee;
import com.project.elms.entity.User; 

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public interface  EmployeeRepository  extends JpaRepository<Employee, Integer> {
	Employee	  findById(int id);  
	Employee   findByUser(User user);  
	List<Employee>  findByResignedOrderByIdDesc(int status);
	Employee findByEmpid(String empid);
	default List<Employee> findByReportingtoAndStatus(EntityManager em, int user, int status)
	{
		List<Employee> objs=new ArrayList<Employee>();
		
		try {  
			CriteriaBuilder cb =em.getCriteriaBuilder();
		    CriteriaQuery<Employee> q = cb.createQuery(Employee.class);
		  
		    Root<Employee> ua = q.from(Employee.class); 
		    
		    Join<Employee, User> euser = ua.join("user"); 
		    
		    
		    List<Predicate> predicates = new ArrayList<>(); 
		     
		    predicates.add(cb.equal(ua.get("reportingto"), user)); 
		    
		    predicates.add(cb.equal(euser.get("status"),status)); 
		    
		    q.select(ua).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		    
		  
		    
		    objs=em.createQuery(q).getResultList();
		    
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
			} 
		
		return objs;
	}  
	
}
