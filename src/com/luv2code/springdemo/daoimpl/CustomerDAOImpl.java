package com.luv2code.springdemo.daoimpl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.luv2code.springdemo.dao.CustomerDAO;
import com.luv2code.springdemo.entity.Customer;

@Repository
public class CustomerDAOImpl implements CustomerDAO {
	
	@Autowired
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	private Session getSession() {
		return this.sessionFactory.getCurrentSession();
	}

	@Override
	public List<Customer> getCustomers() {
		Query<Customer> theQuery = getSession().createQuery("from Customer order by lastName", 
																Customer.class);
		List<Customer> customers = theQuery.getResultList();
		return customers;
	}

	@Override
	public void addCustomer(Customer theCustomer) {
		getSession().saveOrUpdate(theCustomer);
	}

	@Override
	public Customer getCustomer(int theId) {
		Customer theCustomer = getCustomerById(theId);
		return theCustomer;
	}

	@Override
	public void deleteCustomer(int theId) {
		Customer theCustomer = getCustomerById(theId);
		getSession().delete(theCustomer);
	}

	private Customer getCustomerById(int theId) {
		Customer theCustomer = getSession().get(Customer.class, theId);
		return theCustomer;
	}
	
	@Override
    public List<Customer> searchCustomers(String theSearchName) {
        Query<Customer> theQuery = null;
        if (theSearchName != null && theSearchName.trim().length() > 0) {
            theQuery =getSession().createQuery("from Customer where lower(firstName) like :theName or lower(lastName) like :theName", Customer.class);
            theQuery.setParameter("theName", "%" + theSearchName.toLowerCase() + "%");
        }
        else 
            theQuery =getSession().createQuery("from Customer", Customer.class);            
  
        List<Customer> customers = theQuery.getResultList();     
        return customers;
        
    }

}
