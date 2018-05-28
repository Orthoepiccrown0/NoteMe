package com.epiccrown.me.note.noteme.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.epiccrown.me.note.noteme.Editor;
import com.epiccrown.me.note.noteme.Helpers.DataHelper;
import com.epiccrown.me.note.noteme.Helpers.MD5helper;
import com.epiccrown.me.note.noteme.R;
import com.epiccrown.me.note.noteme.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class ChangePasswordDialog extends DialogFragment {
    EditText oldpass;
    EditText newpass;
    EditText newpass_again;

    String old;
    String newp;
    String newp_a;

    private boolean secretpass = false;
    private ProgressDialog progressDialog;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        String type = bundle.getString("type");
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.change_pass_layout,null);
        oldpass = v.findViewById(R.id.oldpass_editbox);
        newpass = v.findViewById(R.id.newpass_editbox);
        newpass_again = v.findViewById(R.id.newpass_again_editbox);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Setting up the Death Star");
        progressDialog.setTitle("Wait a moment");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(true);

        String title = getResources().getString(R.string.newpass_dialog_title_acc);
        if(type.equals("secret")){
            oldpass.setInputType(InputType.TYPE_CLASS_NUMBER);
            newpass.setInputType(InputType.TYPE_CLASS_NUMBER);
            newpass_again.setInputType(InputType.TYPE_CLASS_NUMBER);
            secretpass = true;
            title = getResources().getString(R.string.newpass_dialog_title_sec);
        }
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(title)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        old = oldpass.getText().toString().trim();
                        newp = newpass.getText().toString().trim();
                        newp_a = newpass_again.getText().toString().trim();

                        if(!old.isEmpty()&&!newp.isEmpty()&&!newp_a.isEmpty()&&newp.length()>=4){
                            old = MD5helper.getMD5string(old);
                            newp = MD5helper.getMD5string(newp);
                            newp_a = MD5helper.getMD5string(newp_a);

                            if(newp.equals(newp_a)) {
                                if(!secretpass) {
                                    new ChangeAccPassword().execute();
                                    progressDialog.show();
                                }else{
                                    SQLiteDatabase db = DataHelper.getDB(getActivity());
                                    if(DataHelper.changeSecretPassword(db,newp,old))
                                        Toast.makeText(getActivity(),"Success!",Toast.LENGTH_SHORT).show();
                                    else
                                        Toast.makeText(getActivity(),"Something gone wrong",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }else{
                            Toast.makeText(getActivity(),"Check password length",Toast.LENGTH_SHORT).show();
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

    public class ChangeAccPassword extends AsyncTask<Void,Void,String>{

        @Override
        protected String doInBackground(Void... voids) {

            Uri ENDPOINT = Uri.parse("https://msg.altervista.org/note_me_rest/changepass.php");
                ENDPOINT = ENDPOINT
                        .buildUpon()
                        .appendQueryParameter("oldp", old)
                        .appendQueryParameter("newp", newp)
                        .appendQueryParameter("id", User.current_id)
                        .build();
                return execURL(ENDPOINT);

        }

        private String execURL(Uri ENDPOINT){
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
//            super.onPostExecute(s);
            progressDialog.dismiss();
            if (s != null)
                if (s.equals("Wrong pass"))
                    Toast.makeText(getContext(), "Your old password is wrong", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getContext(), "Something gone wrong", Toast.LENGTH_SHORT).show();
        }
    }
}
