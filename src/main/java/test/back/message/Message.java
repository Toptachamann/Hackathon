package test.back.message;

import java.util.Date;
import java.text.*;

public class Message {
    private String author;
    private String date;
    private String text;

    public Message() {
        this.author = "null";
        this.date = new SimpleDateFormat("HH:mm:ss").format(new Date()).toString();
        this.text = "empty";
    }

    public Message(String author, String date, String text) {
        this.author = author;
        this.date = date;
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public String getText() {
        return text;
    }
}
