package edu.kit.ipd.sdq.kampws.server.util;

import java.util.Set;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

public class ContextFinalizer implements ServletContextListener {

	private static final Logger LOGGER = Logger.getLogger(ContextFinalizer.class);
	
	@Override
    public void contextInitialized(ServletContextEvent sce) {
    }

    @SuppressWarnings("deprecation")
	@Override
    public void contextDestroyed(ServletContextEvent sce) {
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        Thread[] threadArray = threadSet.toArray(new Thread[threadSet.size()]);
        for(Thread t:threadArray) {        	
            if(t.getName().contains("EMF Reference Cleaner") || t.getName().contains("Finalizer")) {
            	LOGGER.debug("Stopping thread " + t.getName() + " manually.");
                synchronized(t) {
                   t.stop(); //don't complain, it works
                }
            }
        }
    }

}
