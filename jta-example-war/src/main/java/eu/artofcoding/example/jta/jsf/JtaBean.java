package eu.artofcoding.example.jta.jsf;

import eu.artofcoding.example.jta.*;
import org.slf4j.Logger;
import org.slf4j.MDC;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.FacesException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

@Named
@SessionScoped
public class JtaBean implements Serializable {

    @Inject
    private Logger log;

    @Inject
    @Current
    private transient HttpServletRequest request;

    //@EJB(lookup = "java:global/jta-example-ejb-1.0.0-SNAPSHOT/CustomerBean")
    @Inject
    private CustomerFacade customerBean;

    private Customer customer;

    /**
     * name = relative to java:comp/env/
     * lookup = absolute, e.g. java:global/hello or java:comp/env/hello
     */
    @Resource(name = "hello")
    private String helloFromModuleConfig;

    @PostConstruct
    private void postConstruct() {
        String user = String.format("[%s]:%d", request.getRemoteHost(), request.getRemotePort());
        log.info("postConstruct: " + user);
        MDC.put("user", user);
        customer = new Customer();
    }

    //<editor-fold desc="Configuration">

    /**
     * Get previously injected value from configuration.
     * @return Value for key "hello".
     */
    public String getHelloFromModuleConfig() {
        return helloFromModuleConfig;
    }

    /**
     * Get configuration value from component or global configuration.
     * @param envKey The configuration key.
     * @return Value or null.
     * @throws NamingException
     */
    public String getFromConfig(String envKey) throws NamingException {
        return PortableServiceLocator.getEnvironmentEntry(envKey);
    }

    //</editor-fold>

    //<editor-fold desc="Delegate getter/setter to entity">

    public Long getId() {
        return customer.getId();
    }

    public void setId(Long id) {
        customer.setId(id);
    }

    public String getFirstname() {
        return customer.getFirstname();
    }

    public void setFirstname(String firstname) {
        customer.setFirstname(firstname);
    }

    public String getLastname() {
        return customer.getLastname();
    }

    public void setLastname(String lastname) {
        customer.setLastname(lastname);
    }

    public Long getVersion() {
        return customer.getVersion();
    }

    //</editor-fold>

    //<editor-fold desc="Business Delegates">

    /**
     * User pushed button "Find", call business delegate.
     * Lookup customer by its ID or create an empty one.
     */
    public void findCustomer() {
        LoggerHelper.trace(log, "findCustomer", "");
        customer = customerBean.findCustomer(getId());
        if (null == customer) {
            customer = new Customer();
        }
    }

    /**
     * User pushed button "Create", call business delegate.
     * Create a customer and use its primary key to refresh our entity.
     */
    public void createCustomer() {
        LoggerHelper.trace(log, "createCustomer", "");
        Long id = (Long) customerBean.createCustomer(customer);
        this.customer = customerBean.findCustomer(id);
    }

    /**
     * User pushed button "Edit", call business delegate.
     * Edit customer's data (merge our entity through EntityManager).
     */
    public void editCustomer() {
        customerBean.editCustomer(customer);
    }

    /**
     * User pushed button "Change name", call business delegate.
     */
    public void changeName() {
        customerBean.changeName(getId(), getFirstname(), getLastname());
    }

    /**
     * User pushed button "Change name w/ rollback", call business delegate.
     * Be prepared for EJBException.
     */
    public void rollbackChangeName() {
        try {
            customerBean.rollbackChangeName(getId(), getFirstname(), getLastname());
        } catch (EJBException e) {
            throw new FacesException(e);
        }
    }

    //</editor-fold>

}
