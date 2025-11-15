package com.project.elms.repository;

 
 
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
 
import com.project.elms.entity.Holiday; 
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public interface  HolidayRepository  extends JpaRepository<Holiday, Integer> {
	Holiday	findById(int id);  
	Holiday findByDate(Date dt);
	List<Holiday> findByYear(int year);
	List<Holiday> findByYearOrderByDate(int year);  
	
	public default boolean isholidaybetween(EntityManager em,Date fdt,Date tdt)
	{
		
		List<Holiday> objs=new ArrayList<>();
		try {  
		CriteriaBuilder cb =em.getCriteriaBuilder();
	    CriteriaQuery<Holiday> q = cb.createQuery(Holiday.class);
	    Root<Holiday> ua = q.from(Holiday.class);
	    List<Predicate> predicates = new ArrayList<>(); 
	     
	    
	    predicates.add(cb.greaterThanOrEqualTo(ua.get("date"), fdt));
	    
	    predicates.add(cb.lessThanOrEqualTo(ua.get("date"), tdt));
	    
	    q.select(ua).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
	   
	    
	    objs=em.createQuery(q).getResultList();
	    
		}
		catch(Exception e)
		{
			
		}
		return objs.size()>0?true:false;
	}
	
}
