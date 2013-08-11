package eu.artofcoding.example.jta;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

public class LoggerHelper {

    @Inject
    @Current
    private HttpServletRequest request;

    @Produces
    public Logger getLogger(InjectionPoint p) {
        Class klass = p.getMember().getDeclaringClass();
        Logger log = LoggerFactory.getLogger(klass);
        return log;
    }

    public static void info(Logger logger, String task, String text) {
        if (logger.isInfoEnabled()) {
            MDC.put("task", task);
            logger.info(text);
            MDC.remove("task");
        }
    }

    public static void warning(Logger logger, String task, String text) {
        if (logger.isWarnEnabled()) {
            MDC.put("task", task);
            logger.warn(text);
            MDC.remove("task");
        }
    }

    public static void error(Logger logger, String task, String text) {
        if (logger.isErrorEnabled()) {
            MDC.put("task", task);
            logger.error(text);
            MDC.remove("task");
        }
    }

    public static void debug(Logger logger, String task, String text) {
        if (logger.isDebugEnabled()) {
            MDC.put("task", task);
            logger.debug(text);
            MDC.remove("task");
        }
    }

    public static void trace(Logger logger, String task, String text) {
        if (logger.isTraceEnabled()) {
            MDC.put("task", task);
            logger.trace(text);
            MDC.remove("task");
        }
    }

}
