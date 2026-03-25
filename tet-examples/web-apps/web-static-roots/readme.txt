тестовые статические ресурсы

Эти попадают в jar-файл:

src/main/resources/static-root1
src/main/resources/static-root2


Подключаются через ClassLoader:

ResourceFactory resourceFactory = ResourceFactory.of(context);
Resource baseResource = resourceFactory.newClassLoaderResource("static-root2");
if (!Resources.isReadableDirectory(baseResource))
	throw new FileNotFoundException("Unable to find base-resource for [static-root2]");
context.setBaseResource(baseResource);



------------------------------

Эти просто лежат отдельно:

src/webapps/alt-root
src/webapps/webapp-a
src/webapps/webapp-b


Можно подключить через ResourceServlet:

Path altPath = Paths.get("../../web-apps/web-static-roots/src/webapps/alt-root/").toRealPath();

ServletHolder holderAlt = new ServletHolder("static-alt", ResourceServlet.class);
holderAlt.setInitParameter("baseResource", altPath.toUri().toASCIIString());
holderAlt.setInitParameter("dirAllowed", "true");
holderAlt.setInitParameter("pathInfoOnly", "true");
context.addServlet(holderAlt, "/alt/*");


		
		
		
		