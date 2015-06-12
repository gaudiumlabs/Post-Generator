package test;

import java.util.Iterator;
import java.util.List;
 
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class TestEmployeeHibernate {
	 
	  
	private static ServiceRegistry serviceRegistry;
	private static SessionFactory sessionFactory;
	 
	public static void main(String[] args) {
	  try{
	    // This step will read hibernate.cfg.xml and prepare hibernate for use
	    Configuration cfg=new Configuration().configure();
	    serviceRegistry = new ServiceRegistryBuilder().applySettings(
	    cfg.getProperties()).buildServiceRegistry();
	    sessionFactory = cfg.buildSessionFactory(serviceRegistry);
	    //Create employee objects to be persisted
	    Employee emp1=new Employee(1,"Sourabh","Soni","admin@sourabhsoni.com");
	    Employee emp2=new Employee(2,"test","User1","testUser1@sourabhsoni.com");
	    Employee emp3=new Employee(3,"test","User2","testUser2@sourabhsoni.com");
	    
	    //Create a handler to access methods
	    TestEmployeeHibernate handler=new TestEmployeeHibernate();  
	    //insert records
	    handler.insert_record(emp1);
	    handler.insert_record(emp2);
	    handler.insert_record(emp3); 
	    //Retrieve these records
	    handler.getAllEmployees();
	    //Update a record
	    handler.updateEmployee(2, "testUser_01@sourabhsoni.com");
	    //Check if the record is updated
	    handler.getEmployee(2); 
	  }catch(Exception e){
	    System.out.println(e.getMessage());
	    e.printStackTrace();
	  }finally{
	    // Actual contact insertion will happen at this step
	  }
	}
	 
	public void insert_record(Employee emp)
	{
	  Session session=null;
	  Transaction tx=null;
	  try
	  {
	    session =sessionFactory.openSession();
	    tx=session.getTransaction();
	    tx.begin();
	    session.save(emp);
	    tx.commit();
	    session.flush();
	  }catch(HibernateException he){
	    if(tx!=null)tx.rollback();
	    System.out.println("Not able to open session");
	    he.printStackTrace();
	  }
	  catch(Exception e){
	    e.printStackTrace();
	  }finally{
	    if(session!=null)
	    session.close();
	  }
	}
	 
	public void getEmployee(long id)
	{
	  Session session=null;
	  Transaction tx=null;
	  try
	  {
	    session=sessionFactory.openSession();
	    tx=session.beginTransaction();
	    Employee emp=(Employee)session.get(Employee.class, id);
	    System.out.println("Name:"+emp.getFirstName()+" "+emp.getLastName());
	    System.out.println("Email:"+emp.getEmail());
	    tx.commit();
	  }catch(HibernateException he){
	    if(tx!=null)tx.rollback();
	    he.printStackTrace();
	  }finally{
	    if(session!=null)
	    session.close();
	  }
	}
	public void getAllEmployees()
	{
	  Session session=null;
	  Transaction tx=null;
	  try
	  {
	    session=sessionFactory.openSession();
	    tx=session.beginTransaction();
	    String SQL_Query="FROM Employee";
	    Query query=session.createQuery(SQL_Query);
	    List employees=query.list();
	    Iterator itr=employees.iterator();
	    while(itr.hasNext())
	    {
	      Employee emp=(Employee)itr.next();
	      System.out.println("Employee id:"+emp.getId());
	      System.out.println("Name:"+emp.getFirstName()+" "+emp.getLastName());
	      System.out.println("Email:"+emp.getEmail());
	    }
	    tx.commit();
	  }catch(HibernateException he){
	    if(tx!=null)tx.rollback();
	    he.printStackTrace();
	  }finally{
	    if(session!=null)
	    session.close();
	  }
	}
	 
	public void updateEmployee(long id,String email)
	{
	  Session session=null;
	  Transaction tx=null;
	  try
	  {
	    session=sessionFactory.openSession();
	    tx=session.beginTransaction();
	    Employee emp=(Employee)session.get(Employee.class, id);
	    emp.setEmail(email);
	    session.update(emp);
	    tx.commit();
	  }catch(HibernateException he){
	    if(tx!=null)tx.rollback();
	    he.printStackTrace();
	  }finally{
	    if(session!=null)
	    session.close();
	  }
	}
	 
	}