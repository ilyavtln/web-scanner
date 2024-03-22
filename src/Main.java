import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;

import com.opencsv.CSVWriter;

/*
Для запуска программы в консоли необходимо
1. Собрать проект - команда: javac -cp "lib/jsoup-1.17.2.jar;lib/opencsv-5.9.jar" src/Main.java -d out
2. Запустить проект - команда: java -cp "lib/jsoup-1.17.2.jar;lib/opencsv-5.9.jar;out" Main 1 test
3. Первое значение в параметрах - количество новостей (int)
4. Второе значение в параметрах - имя выходного файла (String)
*/
public class Main {
    // arg1 - (int) количество новостей для скачивания
    // arg2 - (string) имя выходного файла .scv
    public static void main(String[] args) throws IOException {
        // Массив для хранения данных из новостей
        ArrayList<News> newsList = new ArrayList<>();
        // Количество новостей, которое предстоит сохранить
        int numberToGet = 0;
        // Проверяем, что передано ровно 2 аргумента в консоли
        if (args.length != 2) {
            if (args.length > 2) {
                System.out.println("Введено слишком много аргументов");
            }
            else {
                System.out.println("Введено слишком мало аргументов");
            }
            System.exit(1);
        }
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
        }
        catch (Exception e){
            // Если переданный аргумент не является число, выходим
            System.out.println("Первый аргумент не является числом");
            System.exit(1);
        }

        // Второй аргумент забираем и выводим
        String fileName = args[1];
        System.out.println("Переданное имя файла: " + fileName);

        // Определяем ссылку, с которой будем брать новости
        String URL = "https://zabgu.ru/php/news.php";

        // Создаем текущую переменную, которая будет хранить полученное число новостей
        int curGetNews = 0;
        // Пытаемся подключиться в сайта
        try {
            // Устанавливаем счетчик query запросов (по умолчанию = 1)
            int queryNum = 1;
            String queryStr = Integer.toString(queryNum);
            // Получаем HTML-документ со страницы
            Document doc = Jsoup.connect(URL).data("category", "1").data("page", queryStr).userAgent("Mozilla").timeout(getTimeout()).get();

            // Заголовок страницы
            String title = doc.title();
            System.out.println("Title: " + title + "\n");

            while (curGetNews < numberToGet ) {
                // Получаем все новости со страницы
                Elements divs = doc.select("div");
                // Выбираем карточки новстей
                Elements elements = divs.select(".preview_new, .preview_new_end");
                Elements dataText = elements.select(".dateOnImage");
                Elements newsText = elements.select(".headline");

                // Перебор коллекции всех найденных элементов
                for (int i = 0; i < newsText.size(); i++) {
                    Element elem = newsText.get(i);
                    Element dataElem = dataText.get(i);

                    // Выводим информацию о новости
                    curGetNews++;

                    // Вывод новостей в консоль, при необходимости можно включить
                    //consoleOut(curGetNews, elem.text(), dataElem.text());

                    // Добавляем новость в список
                    newsList.add(new News(elem.text(), dataElem.text()));

                    // Проверяем, что мы не достигли нужного числа новостей
                    if (curGetNews >= numberToGet) {
                        break;
                    }
                }

                // Если количество новостей не совпало с нашим желаемым числом, переходим на следующую страницу
                queryNum++;
                queryStr = Integer.toString(queryNum);

                // Получаем HTML-документ с новой страницы
                doc = Jsoup.connect(URL).data("category", "1").data("page", queryStr).userAgent("Mozilla").timeout(getTimeout()).get();
            }

            // Вызов функции для вывода новостей в файл
            output(newsList, fileName, title);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int getTimeout() {
        // Генерируем случайное число для таймаута
        Random random = new Random();

        return Math.abs(random.nextInt(5000) + 2000);
    }

    // Класс для хранения информации о новостях
    private static class News {
        private final String text;
        private final String date;

        public News(String text, String date) {
            this.text = text;
            this.date = date;
        }

        public String getText() {
            return text;
        }

        public String getDate() {
            return date;
        }
    }

    // Функция для вывода новостей в консоль
    private static void consoleOut(int id, String text, String data) {
        System.out.println("Id: " + id);
        System.out.println("Дата: " + data);
        System.out.println("Текст: " + text);
        System.out.println("\n");
    }

    // Функция для вывода новостей в файл .csv
    private static void output(ArrayList<News> newsList, String file, String title) {
        // Добавляем к имени файла его расширение
        String fileName = file + ".csv";
        // Пытаемся записать данные в файл
        try (CSVWriter writer = new CSVWriter(new FileWriter(fileName, StandardCharsets.UTF_8))) {
            // Добавляем заголовок страницы в начало файла
            String[] titleArray = {title};
            // Записываем заголовок в файл
            writer.writeNext(titleArray);
            // Записываем заголовки
            String[] header = {"id", "Дата", "Текст"};
            writer.writeNext(header);
            // Записываем данные
            for (int i = 0; i < newsList.size(); i++) {
                // Достаем по 1й новости из массива
                News news = newsList.get(i);
                // Добавляем в массив данные, которые необходимо записать
                String[] data = {Integer.toString(i + 1), news.getDate(), news.getText()};
                // Записываем в файл
                writer.writeNext(data);
            }
            System.out.println("Данные успешно записаны в файл " + fileName);
        }
        catch (IOException e) {
            System.err.println("Ошибка при записи в файл " + fileName);
            e.printStackTrace();
        }
    }
}