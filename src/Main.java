import java.io.IOException;

/*
Для запуска программы в консоли необходимо
1. Собрать проект - команда:
javac -cp "lib/jsoup-1.17.2.jar;lib/opencsv-5.9.jar" src/News.java src/Scrapper.java src/Main.java -d out
2. Запустить проект - команда:
java -cp "lib/jsoup-1.17.2.jar;lib/opencsv-5.9.jar;out" Main 10 test
3. Первое значение в параметрах - количество новостей (int)
4. Второе значение в параметрах - имя выходного файла (String)
*/
public class Main {
    // arg1 - (int) количество новостей для скачивания
    // arg2 - (string) имя выходного файла .scv
    public static void main(String[] args) throws IOException {
        // Проверяем, что передано ровно 2 аргумента в консоли
        if (args.length != 2) {
            if (args.length > 2) {
                System.out.println("Введено слишком много аргументов");
            } else {
                System.out.println("Введено слишком мало аргументов");
            }
            System.exit(1);
        }

        // Количество новостей, которое предстоит сохранить
        int numberToGet = 0;
        // Пытаемcя конвертировать первый аргумент в int
        try {
            numberToGet = Integer.parseInt(args[0]);
            // Делаем проверку, что число новостей не меньше 0
            if (numberToGet < 0) {
                System.out.println("Введено некорректное число");
                System.exit(1);
            }
            // Если с числом все хорошо, то выводим его в консоль
            System.out.println("Переданное число: " + numberToGet);
        } catch (Exception e) {
            // Если переданный аргумент не является число, выходим
            System.out.println("Первый аргумент не является числом");
            System.exit(1);
        }

        // Второй аргумент забираем и выводим
        String fileName = args[1];
        System.out.println("Переданное имя файла: " + fileName);

        // Определяем ссылку, с которой будем брать новости
        String URL = "https://zabgu.ru/php/news.php";

        Scrapper newsDownloader = new Scrapper(numberToGet, fileName);
        newsDownloader.downloadNews(URL);
    }
}