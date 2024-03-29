import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

// Класс для вывода новостей в файл
public class Output {
    public void toFile(String file, ArrayList<News> newsList, String title) {
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
