package com.epiccrown.me.note.noteme;

/**
 * Created by Epiccrown on 29.04.2018.
 */

public class Note {
    private String id;
    private String content;
    private String header;
    private String text_color;

    public Note(String id, String content, String header, String text_color) {
        this.id = id;
        this.content = content;
        this.header = header;
        this.text_color = text_color;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getText_color() {
        return text_color;
    }

    public void setText_color(String text_color) {
        this.text_color = text_color;
    }
}
