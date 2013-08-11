package eu.artofcoding.example.jta;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Abstract locating remote services through CDI.
 * Uses portable JNDI names to locate resources.
 */
public class PortableServiceLocator {

    @Produces
    public CustomerFacade getCustomerFacade(InjectionPoint p) throws NamingException {
        InitialContext ctx = new InitialContext();
        return (CustomerFacade) ctx.lookup("java:global/jta-example-ejb-1.0.0-SNAPSHOT/CustomerBean");
    }

    public static String getGlobalEnvironmentEntry(String envKey) throws NamingException {
        InitialContext ctx = new InitialContext();
        String _envKey = String.format("java:global/%s", envKey);
        return (String) ctx.lookup(_envKey);
    }

    public static String getCompEnvironmentEntry(String envKey) throws NamingException {
        InitialContext ctx = new InitialContext();
        String _envKey = String.format("java:comp/env/%s", envKey);
        return (String) ctx.lookup(_envKey);
    }

    public static String getEnvironmentEntry(String envKey) throws NamingException {
        String val = null;
        try {
            val = getCompEnvironmentEntry(envKey);
        } catch (NamingException e) {
            // ignore, try global
        }
        if (null == val) {
            val = getGlobalEnvironmentEntry(envKey);
        }
        return val;
    }

}
