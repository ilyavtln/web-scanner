import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

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

            while (curGetNews < numberToGet ) {
                // Заголовок страницы
                String title = doc.title();
                System.out.println("Title: " + title + "\n");

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
                    System.out.println("Id: " + curGetNews);
                    System.out.println("Текст: " + elem.text());
                    System.out.println("Дата: " + dataElem.text());

                    // Добавляем новость в список
                    newsList.add(new News(elem.text(), dataElem.text()));

                    if (curGetNews >= numberToGet) {
                        break;
                    }
                }

                // Если количество новостей не совпало с нашим желаемым числом, переходим на следующую страницу
                queryNum++;
                queryStr = Integer.toString(queryNum);

                // Получаем HTML-документ со страницы
                doc = Jsoup.connect(URL).data("category", "1").data("page", queryStr).userAgent("Mozilla").timeout(getTimeout()).get();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int getTimeout() {
        // Генерируем случайное число для таймаута
        Random random = new Random();

        return random.nextInt(1000) + 1000;
    }

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
}