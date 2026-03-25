package ru.tet.jetty.starter;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class TetJettyServerOptions {

	int port = 8080;
	
	String contextPath = "/";
	
	//Путь к веб-проекту, который надо поднять
	String webAppProjectPath;
	
	//build.finalName веб проекта - имя под которым оно будет собираться в target 
	String webAppProjectFinalName;
	
	
	String[] welcomeFiles = new String[]{"index.html", "welcome.html"};

	//включить поддержку аннотаций и jsp
	//При использовании WebAppContext всё это включено по умолчанию.
	boolean annotationSupport = true;
	boolean jspSupport = true;
	
	//где находится статический контент в classpath-e 
	List<String> classLoaderBaseResources = new ArrayList<>(); 
	
	//где находится статический контент в файловой системе 
	List<Path> fileSystemBaseResources = new ArrayList<>(); 

	//дополнительные папки и jar-файлы, которые надо добавить в classpath
	List<Path> extraClasspathes = new ArrayList<>(); 
	
	
}
