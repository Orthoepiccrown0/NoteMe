package com.epiccrown.me.note.noteme.Helpers;

/**
 * Created by Epiccrown on 03.05.2018.
 */

public class AccountListItem {

    private int image;
    private String action;

    public AccountListItem(int image, String action) {
        this.image = image;
        this.action = action;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
