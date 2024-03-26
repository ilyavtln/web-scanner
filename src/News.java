// Класс для хранения информации о новостях
public class News {
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