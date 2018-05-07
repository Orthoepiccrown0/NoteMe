package com.epiccrown.me.note.noteme;

/**
 * Created by Epiccrown on 29.04.2018.
 */

public class Note {
    private String id;
    private String content;
    private String header;
    private String text_color;

    public Note() {}

    public Note(String id, String content, String header, String text_color) {
        this.id = id;
        this.content = content;
        this.header = header;
        this.text_color = text_color;
    }



    public String getId() {
        return id;
    }

    public Note setId(String id) {
        this.id = id;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Note setContent(String content) {
        this.content = content;
        return this;
    }

    public String getHeader() {
        return header;
    }

    public Note setHeader(String header) {
        this.header = header;
        return this;
    }

    public String getText_color() {
        return text_color;
    }

    public Note setText_color(String text_color) {
        this.text_color = text_color;
        return this;
    }

    public static Note Builder(){
        return new Note();
    }

}
