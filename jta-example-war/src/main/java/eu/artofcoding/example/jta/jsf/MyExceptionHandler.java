package eu.artofcoding.example.jta.jsf;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import java.util.Iterator;

public class MyExceptionHandler extends ExceptionHandlerWrapper {

    private ExceptionHandler wrapped;

    public MyExceptionHandler(ExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return wrapped;
    }

    @Override
    public void handle() throws FacesException {
        Iterator<ExceptionQueuedEvent> iterator = getUnhandledExceptionQueuedEvents().iterator();
        while (iterator.hasNext()) {
            ExceptionQueuedEvent event = iterator.next();
            ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();
            Throwable throwable = context.getException();
            //if (throwable instanceof FacesException) {
            FacesContext fc = FacesContext.getCurrentInstance();
            NavigationHandler navigationHandler = fc.getApplication().getNavigationHandler();
            try {
                // Find root cause
                Throwable cause = throwable.getCause();
                if (null != cause) {
                    while (null != cause.getCause()) {
                        cause = cause.getCause();
                    }
                } else {
                    cause = throwable;
                }
                FacesMessage facesMessage =
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Es ist ein Fehler aufgetreten, die Daten wurden nicht gespeichert",
                                cause.getLocalizedMessage());
                fc.addMessage(null, facesMessage);
                //navigationHandler.handleNavigation(fc, null, "/admin/index.xhtml");
                fc.renderResponse();
            } finally {
                iterator.remove();
            }
            //}
        }
        getWrapped().handle();
    }

}
