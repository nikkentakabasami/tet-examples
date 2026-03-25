package ru.tet.jakarta.servlet;


import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;
import jakarta.servlet.annotation.HandlesTypes;
import ru.tet.beans.Page;

/**
 * Демка использования ServletContainerInitializer
 * 
 * это фича, которая появилась в Servlets 3.0
 * 
 * Имплементация ServletContainerInitializer - объект, который будет выполнять инициализацию веб-приложения.
 * Он запускается ещё до создания сервлетного контекста.
 * Позволяет програмно добавить сервлеты, фильтры, слушатели.
 * Для работы его необходимо сконфигурировать через SPI (папка META-INF/services/)
 * Такой проект не нуждается в файле web.xml
 * 
 * 
 * ServletContainerInitializerExample:
 * 1)Находит все имплементации интерфейса Page.
 * 2)Цепляет сервлет PagesServlet.
 * 3)Для каждой найденной страницы добавляет для PagesServlet новый мэппинг. 
 * 
 * Например:
 * http://localhost:8080/tdWeb2/pageOne
 * 
 * @author tetsuma
 *
 */
@HandlesTypes({Page.class})
public class ServletContainerInitializerExample implements ServletContainerInitializer {
    @Override
    public void onStartup (Set<Class<?>> pageClasses, ServletContext ctx)
              throws ServletException {
        List<Page> pages = new ArrayList<>();
        
        System.out.println("start ServletContainerInitializerExample");
        if (pageClasses != null) {
            System.out.format("found %d pages",pageClasses.size());
            for (Class<?> pageClass : pageClasses) {
                if (!pageClass.isInterface() &&
                          !Modifier.isAbstract(pageClass.getModifiers())) {
                    try {
                    	Page page = (Page) pageClass.getConstructor().newInstance();
//                        Page page = (Page) pageClass.newInstance();
                        pages.add(page);
                    } catch (Throwable ex) {
                        throw new ServletException(
                                  "Failed to instantiate WebApplicationInitializer" +
                                            " class", ex);
                    }
                }
            }
        }
        if (pages.size() > 0) {
            ctx.setAttribute("pages", pages);
            ServletRegistration.Dynamic servletRegistration = ctx.addServlet("appController",
                                                                             PagesServlet.class);
            pages.forEach(p -> {
                System.out.format("add mapping %s", p.getPath());
                servletRegistration.addMapping(p.getPath());
            });
        }
    }
}