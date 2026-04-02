тестовое веб-приложение

------------------------------
запускается через: 
run as -> maven build
jetty:run

либо через

JettyEmbeddedStarter1

------------------------------

Содержит:
1)js библиотеки: jquery, jstree, moment 
2)сервлеты, ServletContextListener, ServletContainerInitializer.
HelloServlet - подключается через web.xml
OkaeriServlet - через аннотацию.

3)jsp, jsp библиотеки

md2_index.jsp - главная страница, точка входа

делает упаковку в том числе и в zip-формате.
Это позволяет импортировать содержимое в другие проекты как overlay.
см. web-app-ex2


