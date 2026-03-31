простейшее веб-приложение, которое импортирует весь контент из web-app-ex1.

Делается это через overlay.
см.
maven-war-plugin
  overlays
    overlay


Запуск:

1)JettyEmbeddedStarter10
  Пример запуска в jetty embedded режиме
Работает плохо: не импортирует html-файлы.


2)jetty-ee10-maven-plugin

запускается через: 
run as -> maven build
jetty:run

По какой то причине jetty-плагин кидает ошибки сборки, если подключить overlay как war.
С zip-форматом это работает, но не обновляет изменения динамически.

