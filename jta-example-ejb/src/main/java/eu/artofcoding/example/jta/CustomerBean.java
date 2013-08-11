package eu.artofcoding.example.jta;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static javax.ejb.TransactionAttributeType.REQUIRED;

@Stateless
@Remote(CustomerFacade.class)
public class CustomerBean implements CustomerFacade {

    @PersistenceContext
    private EntityManager em;

    public CustomerBean() {
    }
    
    @Override
    @TransactionAttribute(REQUIRED)
    public Customer findCustomer(Long id) {
        return em.find(Customer.class, id);
    }

    @Override
    @TransactionAttribute(REQUIRED)
    public Object createCustomer(Customer customer) {
        em.persist(customer);
        Object id = em.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(customer);
        return id;
    }

    @Override
    @TransactionAttribute(REQUIRED)
    public void editCustomer(Customer customer) {
        em.merge(customer);
    }

    @Override
    @TransactionAttribute(REQUIRED)
    public Customer changeName(Long id, String firstname, String lastname) {
        Customer customer = findCustomer(1L);
        customer.setFirstname(firstname);
        customer.setLastname(lastname);
        //em.merge(customer); is done automatically through JTA
        return customer;
    }

    @Override
    @TransactionAttribute(REQUIRED)
    public Customer rollbackChangeName(Long id, String firstname, String lastname) {
        Customer customer = changeName(id, firstname, lastname);
        editCustomer(customer); // em.merge()
        em.flush(); // Submit SQL to database
        throw new RuntimeException("Rollback!");
    }

}
