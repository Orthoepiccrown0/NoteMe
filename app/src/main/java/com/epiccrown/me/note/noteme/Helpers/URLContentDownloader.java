package com.epiccrown.me.note.noteme.Helpers;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class URLContentDownloader {

    public static String execURL(Uri ENDPOINT){
        try {

            URL url = new URL(ENDPOINT.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String str;
            String final_object = "";
            while ((str = in.readLine()) != null)
                final_object = str;
            return final_object;
        }catch (Exception ex){ex.printStackTrace();}
        return null;
    }

}
