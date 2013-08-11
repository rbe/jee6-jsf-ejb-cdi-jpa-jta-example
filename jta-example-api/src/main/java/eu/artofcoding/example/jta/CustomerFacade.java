package eu.artofcoding.example.jta;

/**
 * Business interface for customers.
 */
public interface CustomerFacade {

    Object createCustomer(Customer customer);

    void editCustomer(Customer customer);

    Customer findCustomer(Long id);

    Customer changeName(Long id, String firstname, String lastname);

    Customer rollbackChangeName(Long id, String firstname, String lastname);

}
