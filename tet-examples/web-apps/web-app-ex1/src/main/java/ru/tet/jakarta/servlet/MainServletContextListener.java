package ru.tet.jakarta.servlet;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class MainServletContextListener implements ServletContextListener {

	private final static Logger logger = Logger.getLogger(MainServletContextListener.class.getName());

	@Override
	public void contextInitialized(ServletContextEvent sce) {

		LogManager.getLogManager().getLogger(Logger.GLOBAL_LOGGER_NAME).setLevel(Level.FINE);

		logger.info("MainServletContextListener: MainServletContextListener");



	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}
}