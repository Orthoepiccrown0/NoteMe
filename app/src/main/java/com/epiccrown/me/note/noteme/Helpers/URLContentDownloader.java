package com.epiccrown.me.note.noteme.Helpers;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class URLContentDownloader {

    private static Uri mUri;
    public static String getFinalString(Uri uri, Context context){
        mUri = uri;
        //String response = new URLExecuter().execute(context);

        return null;
    }

    private class URLExecuter extends AsyncTask<Context,Void,String>{

        Context mContext;

        @Override
        protected String doInBackground(Context... contexts) {
            for(Context context : contexts) mContext = context;

            try {

                URL url = new URL(mUri.toString());

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

}
