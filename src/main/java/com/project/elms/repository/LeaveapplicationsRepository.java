package com.project.elms.repository;

 
 
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
 
import com.project.elms.entity.Leaveapplication;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root; 

public interface  LeaveapplicationsRepository  extends JpaRepository<Leaveapplication, Integer> {
	Leaveapplication	  findById(int id);   
	List<Leaveapplication> findByEmployee(int id);   
	List<Leaveapplication> findByStatus(String status);
	List<Leaveapplication> findByEmployeeAndStatus(int id, String status);
	
	public default boolean isdateexists(EntityManager em,int emp,Date dt,int id)
	{
		
		List<Leaveapplication> objs=new ArrayList<>();
		try {  
		CriteriaBuilder cb =em.getCriteriaBuilder();
	    CriteriaQuery<Leaveapplication> q = cb.createQuery(Leaveapplication.class);
	    Root<Leaveapplication> ua = q.from(Leaveapplication.class);
	    List<Predicate> predicates = new ArrayList<>(); 
	    
	   if(id!=0) predicates.add(cb.notEqual(ua.get("id"), id)); 
	    predicates.add(cb.equal(ua.get("employee"), emp));
	    
	    predicates.add(cb.lessThanOrEqualTo(ua.get("fromdate"), dt));
	  
	    predicates.add(cb.greaterThanOrEqualTo(ua.get("todate"), dt));
	    
	    q.select(ua).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
	   
	    
	    objs=em.createQuery(q).getResultList();
	    
		}
		catch(Exception e)
		{
			
		}
		return objs.size()>0?true:false;
	}
	
	public default List<Leaveapplication> findByEmployeedates(EntityManager em, int employee,Date fromdate, Date todate)
	{
		List<Leaveapplication> objs=new ArrayList<>();
		try {  
			CriteriaBuilder cb =em.getCriteriaBuilder();
		    CriteriaQuery<Leaveapplication> q = cb.createQuery(Leaveapplication.class);
		    Root<Leaveapplication> ua = q.from(Leaveapplication.class);
		    List<Predicate> predicates1 = new ArrayList<>(); 
		     
		    predicates1.add(cb.greaterThanOrEqualTo(ua.get("fromdate"), fromdate));
		    
		    predicates1.add(cb.lessThanOrEqualTo(ua.get("fromdate"), todate));
		    
		    List<Predicate> predicates2 = new ArrayList<>(); 
		     
		    predicates2.add(cb.greaterThanOrEqualTo(ua.get("todate"), fromdate));
		    
		    predicates2.add(cb.lessThanOrEqualTo(ua.get("todate"), todate));
		    
		    Predicate predicateA=cb.and(predicates1.toArray(new Predicate[predicates1.size()]));
		    Predicate predicateB=cb.and(predicates2.toArray(new Predicate[predicates2.size()]));
		    
		     
		    
		    q.select(ua).where(cb.or(predicateA,predicateB));
		    
		    q.select(ua).where(cb.equal(ua.get("employee"),employee));
		    
		    objs=em.createQuery(q).getResultList();
		    
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
			} 
		
		return objs;
	}
	
	public default List<Leaveapplication> findBydates(EntityManager em,Date fromdate, Date todate)
	{
		List<Leaveapplication> objs=new ArrayList<>();
		try {  
			CriteriaBuilder cb =em.getCriteriaBuilder();
		    CriteriaQuery<Leaveapplication> q = cb.createQuery(Leaveapplication.class);
		    Root<Leaveapplication> ua = q.from(Leaveapplication.class);
		    List<Predicate> predicates1 = new ArrayList<>(); 
		     
		    predicates1.add(cb.greaterThanOrEqualTo(ua.get("fromdate"), fromdate));
		    
		    predicates1.add(cb.lessThanOrEqualTo(ua.get("fromdate"), todate));
		    
		    List<Predicate> predicates2 = new ArrayList<>(); 
		     
		    predicates2.add(cb.greaterThanOrEqualTo(ua.get("todate"), fromdate));
		    
		    predicates2.add(cb.lessThanOrEqualTo(ua.get("todate"), todate));
		    
		    Predicate predicateA=cb.and(predicates1.toArray(new Predicate[predicates1.size()]));
		    Predicate predicateB=cb.and(predicates2.toArray(new Predicate[predicates2.size()]));
		    
		    q.select(ua).where(cb.or(predicateA,predicateB));
		    
		    
		    objs=em.createQuery(q).getResultList();
		    
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
			} 
		
		return objs; 
	}
	
	
}
