package eu.artofcoding.example.jta;

import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

public class FacesHelper {

    /**
     * Get actual HTTP request.
     * @return HttpServletRequest.
     */
    @Produces
    @Current
    public HttpServletRequest getHttpServletRequest() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return (HttpServletRequest) facesContext.getExternalContext().getRequest();
    }

}
