package com.epiccrown.me.note.noteme.Helpers;

import android.app.Dialog;
import android.app.DialogFragment;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.epiccrown.me.note.noteme.R;
import com.epiccrown.me.note.noteme.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class EditDialog extends DialogFragment {
    private Uri ENDPOINT;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        final String url = bundle.getString("url");
        String title = bundle.getString("title");
        String hint = bundle.getString("hint");
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.edit_dialog, null);
        EditText editText = v.findViewById(R.id.dialog_edittext);
        editText.setHint(hint);
        editText.setText(User.username);
        final String new_username = editText.getText().toString();
        return new android.app.AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setView(v)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!new_username.equals(User.username)) {
                            ENDPOINT = Uri.parse(url)
                                    .buildUpon()
                                    .appendQueryParameter("newusername", new_username)
                                    .appendQueryParameter("iduser", User.current_id)
                                    .build();
                            new UpdateUsername().execute();
                        }else{
                            Toast.makeText(getActivity(),R.string.account_username_change_error,Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();

    }

    private class UpdateUsername extends AsyncTask<Void,Void,String>{

        @Override
        protected String doInBackground(Void... voids) {
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

        @Override
        protected void onPostExecute(String s) {
            //super.onPostExecute(s);
            if(s.equals("Updated successfully")){
                Toast.makeText(getActivity(),R.string.account_username_change_success,Toast.LENGTH_SHORT).show();
            }
        }
    }
}
