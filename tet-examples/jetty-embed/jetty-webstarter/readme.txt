Приложение для запуска других веб-приложений с помощью класса TetJettyServer.

Содержит:
MainTetJettyServer1.java
MainTetJettyServer10.java
MainTetJettyServer11.java
MainTetJettyServer2.java

гораздо удобнее запускать jetty embedded прямо из веб-приложений.
В этом случае не надо указывать путь к веб приложению и доп. класспаз.

Такой подход используется классами: 
JettyEmbeddedStarter1
JettyEmbeddedStarter2
...

jetty-библиотеки в этом случае указываются как provided и не цепляются к финальному веб-приложению.


