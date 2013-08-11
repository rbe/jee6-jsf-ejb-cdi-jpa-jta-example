package eu.artofcoding.example.jta.servlet;

import eu.artofcoding.example.jta.Customer;
import eu.artofcoding.example.jta.CustomerFacade;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/createCustomer"})
public class CreateCustomerServlet extends HttpServlet {

    @EJB(lookup = "java:global/jta-example-ejb-1.0.0-SNAPSHOT/CustomerBean")
    private CustomerFacade customerBean;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRequest(request, response);
    }

    private void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Customer customer = new Customer();
        customer.setFirstname("Firstname");
        customer.setLastname("Lastname");
        customerBean.createCustomer(customer);
    }

}
