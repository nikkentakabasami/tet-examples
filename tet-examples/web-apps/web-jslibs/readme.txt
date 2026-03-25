
Проект для добавления js-библиотек в веб-проекты. 
Альтернатива подключению веб-проекта.

Подключается примерно так:

		<dependency>
			<groupId>ru.tetsu.examples</groupId>
			<artifactId>web-jslibs</artifactId>
			<version>${project.version}</version>
			<type>zip</type>
			<scope>runtime</scope>
		</dependency>

  
  
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-war-plugin</artifactId>
				  <version>3.4.0</version>
				<configuration>
					<overlays>
						<overlay>
							<groupId>ru.tetsu.examples</groupId>
							<artifactId>web-jslibs</artifactId>
							<type>zip</type>
						</overlay>
						<overlay>
						</overlay>
					</overlays>
				</configuration>
			</plugin>



