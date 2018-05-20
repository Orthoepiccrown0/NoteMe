package com.epiccrown.me.note.noteme.Fingerprints;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.widget.TextView;

import com.epiccrown.me.note.noteme.Fragments.SecretsFragment;
import com.epiccrown.me.note.noteme.MainActivity;
import com.epiccrown.me.note.noteme.R;

@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    private Context context;

    // Constructor
    public FingerprintHandler(Context mContext) {
        context = mContext;
    }

    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
        CancellationSignal cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
        }
    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        this.update("" + errString);
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        this.update("" + helpString);
    }

    @Override
    public void onAuthenticationFailed() {
        this.update("Fingerprint Authentication failed.");
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        MainActivity act = (MainActivity) context;
        act.showFragment(new SecretsFragment());
        //act.finish();
    }

    private void update(String e) {
        TextView textView = (TextView) ((Activity) context).findViewById(R.id.errorText);
        textView.setText(e);
    }

}