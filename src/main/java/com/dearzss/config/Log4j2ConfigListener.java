package com.dearzss.config;

import org.apache.logging.log4j.core.config.Configurator;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Enumeration;

public class Log4j2ConfigListener implements ServletContextListener {
    private static final String KEY = "log4jConfigLocation";

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        String fileName = getContextParam(servletContextEvent);
        Configurator.initialize("Log4j2", fileName);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    private String getContextParam(ServletContextEvent event) {
        Enumeration<String> names = event.getServletContext().getInitParameterNames();
        while (names.hasMoreElements()){
            String name = names.nextElement();
            String value = event.getServletContext().getInitParameter(name);
            if(name.trim().equals(KEY)){
                return value;
            }
        }
        return null;
    }
}
