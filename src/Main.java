import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            // Получаем HTML-документ страницы
            Document doc = Jsoup.connect("http://example.com").get();

            // Заголовок страницы
            String title = doc.title();
            System.out.println("Title: " + title);

            // Получаем все ссылки на странице
            Elements links = doc.select("a[href]");

            // Перебор коллекции всех найденных элементов
            for (Element link : links) {
                // Выводим текст ссылки и саму ссылку
                System.out.println("\nТекст: " + link.text());
                System.out.println("Ссылка: " + link.attr("href"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}