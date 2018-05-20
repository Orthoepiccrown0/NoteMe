package com.epiccrown.me.note.noteme.Fingerprints;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.epiccrown.me.note.noteme.Fragments.SecretsFragment;
import com.epiccrown.me.note.noteme.Helpers.DataHelper;
import com.epiccrown.me.note.noteme.Helpers.MD5helper;
import com.epiccrown.me.note.noteme.MainActivity;
import com.epiccrown.me.note.noteme.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class SecureNotes extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.secure_notes_fingerprints,container,false);
        TextView passwordType = v.findViewById(R.id.secure_password_insert);
        passwordType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataHelper dataHelper = new DataHelper(getActivity());
                SQLiteDatabase database = dataHelper.getReadableDatabase();

                Cursor cursor = database.query("SecureNotes",null,null,null,null,null,null);
                if(cursor.moveToFirst()){
                    final String password =  cursor.getString(1);

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                    alertDialog.setTitle("Insert Password");

                    final EditText input = new EditText(getActivity());
                    input.setInputType(InputType.TYPE_CLASS_NUMBER);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    lp.setMargins(4,0,4,0);
                    input.setLayoutParams(lp);
                    alertDialog.setView(input);

                    alertDialog.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                   if(password.equals(MD5helper.getMD5string(input.getText().toString()))){
                                       dialog.dismiss();
                                       FragmentManager fragmentManager = getFragmentManager();
                                       assert fragmentManager != null;
                                       fragmentManager.beginTransaction()
                                               .replace(R.id.main_holder,new SecretsFragment())
                                               .disallowAddToBackStack()
                                               .commit();
                                   }else{
                                       Toast.makeText(getActivity(),"Password is wrong",Toast.LENGTH_SHORT).show();
                                   }
                                }
                            });

                    alertDialog.setNegativeButton("CANCEL",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                    alertDialog.show();
                }else{
                    //create new password
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                    alertDialog.setTitle("Insert a new password");

                    final EditText input = new EditText(getActivity());
                    input.setInputType(InputType.TYPE_CLASS_NUMBER);

                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    lp.setMargins(4,0,4,0);
                    input.setLayoutParams(lp);
                    alertDialog.setView(input);

                    alertDialog.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    if(!input.getText().toString().trim().equals("")){
                                        DataHelper helper = new DataHelper(getActivity());
                                        SQLiteDatabase db = helper.getWritableDatabase();
                                        String pass = MD5helper.getMD5string(input.getText().toString().trim());
                                        DataHelper.insertSecretPassword(db,pass);
                                    }
                                }
                            });

                    alertDialog.setNegativeButton("CANCEL",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                    alertDialog.show();
                }
                cursor.close();
                database.close();
            }
        });
        return v;
    }
}
