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
import java.io.PrintWriter;

@WebServlet(urlPatterns = {"/showCustomer"})
public class ShowCustomerServlet extends HttpServlet {

    @EJB(lookup = "java:global/jta-example-ejb-1.0.0-SNAPSHOT/CustomerBean")
    private CustomerFacade customerBean;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRequest(request, response);
    }

    private void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Customer customer = customerBean.findCustomer(1L);
        PrintWriter writer = response.getWriter();
        writer.println("<html>");
        writer.println("<body>");
        writer.println("ID:" + customer.getId() + "<br/>");
        writer.println("Version:" + customer.getVersion() + "<br/>");
        writer.println("Firstname:" + customer.getFirstname() + "<br/>");
        writer.println("Lastname:" + customer.getLastname() + "<br/>");
        writer.println("</body>");
        writer.println("</html>");
    }

}
