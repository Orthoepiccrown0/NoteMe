package com.epiccrown.me.note.noteme.Helpers;

import android.app.Dialog;
import android.app.DialogFragment;

import android.content.DialogInterface;
import android.content.Intent;
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

public class ChangeUsernameDialog extends android.support.v4.app.DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        String title = bundle.getString("title");
        String hint = bundle.getString("hint");
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.edit_dialog, null);
        final EditText editText = v.findViewById(R.id.dialog_edittext);
        editText.setHint(hint);
        editText.setText(User.username);

        return new android.app.AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setView(v)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String new_username = editText.getText().toString();
                        if(!new_username.equals(User.username)&&new_username.length()>2) {
                            Intent intent = new Intent();
                            intent.putExtra("newusername",new_username);
                            getTargetFragment().onActivityResult(getTargetRequestCode(),0,intent);
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


}
