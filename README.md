Для запуска можно написать в в терминале из корневой папки проекта
![image](https://github.com/ilyavtln/web-scanner/assets/108632891/4acc7ca3-77b6-4c25-9890-5f2f34a2487a)

<!--Собираем класс и библиотеку-->
javac -cp "lib/jsoup-1.17.2.jar" src/Main.java -d out
<!--Запускаем и передаем аргументы-->
java -cp "lib/jsoup-1.17.2.jar;out" Main 1000 test 
