package ru.tet;

import java.util.ServiceLoader;

import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletException;

/**
 * Проверка поиска сервисов через SPI.
 * Тут находится класс ServletContainerInitializerExample.
 * Контейнер сервлетов выполняет эту же операцию, после чего использует имплементации ServletContainerInitializer для инициализации веб-приложения.
 * 
 * @author tetsuma
 *
 */
public class SPIDemo {
	
	public static void main(String[] args) {
		
		ServiceLoader<ServletContainerInitializer> services = ServiceLoader.load(ServletContainerInitializer.class);
		
		 for (ServletContainerInitializer initializer : services) {
	            System.out.println("Найден сервис: " + initializer.getClass().getName());
	            try {
					initializer.onStartup(null, null);
				} catch (ServletException e) {
					e.printStackTrace();
				}
	        }		
		
		
		
		
		
	}
	
	
	
}
