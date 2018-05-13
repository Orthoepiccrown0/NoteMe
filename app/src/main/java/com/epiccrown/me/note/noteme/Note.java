package com.epiccrown.me.note.noteme;

import java.io.Serializable;

/**
 * Created by Epiccrown on 29.04.2018.
 */

public class Note implements Serializable{
    private String id="";
    private String content="";
    private String header="";
    private String color_bg="";

    public Note() {}

    public Note(String id, String content, String header, String color_bg) {
        this.id = id;
        this.content = content;
        this.header = header;
        this.color_bg = color_bg;
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

    public String getColor_bg() {
        return color_bg;
    }

    public Note setColor_bg(String color_bg) {
        this.color_bg = color_bg;
        return this;
    }

    public static Note Builder(){
        return new Note();
    }

}
