
логгирование с использованием JUL.

Мы подключаем артефакт slf4j-jdk14.
Это SLF4J Binding выводящий SLF4J сообщения в JUL — java.util.logging (Java Logging API )

В класспазе должен быть только один такой Binding!

для конфигурирования JUL используется класс LoggingUtil.
он считывает конфигурацию из logging.properties.

По умолчанию JUL использует ConsoleHandler, которые выводит все сообщения в System.err.
Так что мы подключаем свой обработчик: SystemOutHandler

Уровни логов в порядке приоритетности:
ALL, FINEST, FINER, FINE, CONFIG, INFO, WARNING, SEVERE, OFF

-------------
WebAppTest
  тестирование веб-сервера через junit
 
BasicServer_jdk_logging
  простой веб-сервер





