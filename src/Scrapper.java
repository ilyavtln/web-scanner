import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Scrapper {
    private final int numberToGet;
    // Массив для хранения данных из новостей
    ArrayList<News> newsList = new ArrayList<>();
    // Title страницы
    String title;

    // Получаем через конструктор число новостей и имя файла
    public Scrapper(int numberToGet) {
        this.numberToGet = numberToGet;
    }

    // Основная функция класса, которая получает и сохраняет новости в файл
    public void downloadNews(String URL) {
        // Создаем текущую переменную, которая будет хранить полученное число новостей
        int curGetNews = 0;

        // Пытаемся подключиться в сайта
        try {
            // Устанавливаем счетчик query запросов (по умолчанию = 1)
            int queryNum = 1;
            String queryStr = Integer.toString(queryNum);
            // Получаем HTML-документ со страницы
            // Подключаемся к url с query параметрами category=1&page=queryStr
            // Переменная queryStr позволяет делать постраничную навигацию при получения 9 новостей с текущей
            // userAgent: Устанавливаем User-Agent
            // timeout: Устанавливаем таймаут случайным значением
            Document doc = Jsoup.connect(URL).data("category", "1").data("page", queryStr).userAgent("Mozilla").timeout(getTimeout()).get();

            // Заголовок страницы
            title = doc.title();

            // Выводим ссылку на сайт и его заголовок
            System.out.println("\n" + "Новости с сайта " + URL);
            System.out.println("Title: " + title + "\n");

            // Выполняем цикл до тех пор, пока не получим нужное количество новостей
            while (curGetNews < numberToGet ) {
                // Получаем все новости со страницы
                Elements divs = doc.select("div");
                // Выбираем карточки новстей и из них текст и дату
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
                    // consoleOut(curGetNews, elem.text(), dataElem.text());

                    // Добавляем новость в список
                    newsList.add(new News(elem.text(), dataElem.text()));

                    // Проверяем, что мы не достигли нужного числа новостей
                    if (curGetNews >= numberToGet) {
                        break;
                    }
                }

                // Если количество новостей не совпало с нашим желаемым числом, переходим на следующую страницу
                queryNum++;
                // Обновляем query параметры для page
                queryStr = Integer.toString(queryNum);

                // Получаем HTML-документ со страницы
                // Подключаемся к url с query параметрами category=1&page=queryStr
                // Переменная queryStr позволяет делать постраничную навигацию при получения 9 новостей с текущей
                // userAgent: Устанавливаем User-Agent
                // timeout: Устанавливаем таймаут случайным значением
                doc = Jsoup.connect(URL).data("category", "1").data("page", queryStr).userAgent("Mozilla").timeout(getTimeout()).get();
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int getTimeout() {
        // Генерируем случайное число для таймаута
        Random random = new Random();

        return Math.abs(random.nextInt(2000) + 5000);
    }

    // Функция для вывода новостей в консоль
    private static void consoleOut(int id, String text, String data) {
        System.out.println("Id: " + id);
        System.out.println("Дата: " + data);
        System.out.println("Текст: " + text);
        System.out.println("\n");
    }

    public ArrayList<News> getNewsList() {
        return newsList;
    }

    public String getTitle() {
        return title;
    }
}
