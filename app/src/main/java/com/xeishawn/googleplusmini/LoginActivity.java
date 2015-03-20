package com.xeishawn.googleplusmini;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;

public class LoginActivity extends Activity {
//    private static final String TAG = "gpm-login-activity";
    static final int REQUEST_CODE_PICK_ACCOUNT = 1000;
    private static final String SCOPE;
    private SignInButton sign_in_button;
    private static String[] scopes = new String[]{
            "https://www.googleapis.com/auth/plus.me",
            "https://www.googleapis.com/auth/plus.circles.read",
            "https://www.googleapis.com/auth/plus.profiles.read"
    };

    static {
        SCOPE = "oauth2: " + TextUtils.join(" ", scopes);
    }
    private GetUsernameTask user;
    String mEmail;
    static final int REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
//        Log.i(TAG, "onCreate");
        sign_in_button = (SignInButton) findViewById(R.id.sign_in_button);
        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUsername();
            }
        });
    }

    private void getUsername() {
//        Log.i(TAG, "getUsername");
        if (mEmail == null) {
            pickUserAccount();
        } else {
            if (isDeviceOnline()) {
                user = new GetUsernameTask(LoginActivity.this, mEmail, SCOPE);
                user.execute();
            } else {
                Toast.makeText(this, "not online", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void pickUserAccount() {
//        Log.i(TAG, "pickUserAccount");
        String[] accountTypes = new String[]{"com.google"};
        Intent intent = AccountPicker.newChooseAccountIntent(null, null, accountTypes, false, null, null, null, null);
        startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.i(TAG, "onActivityResult");
        if (requestCode == REQUEST_CODE_PICK_ACCOUNT) {
            if (resultCode == RESULT_OK) {
                mEmail = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                getUsername();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Please pick an account", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void handleException(final Exception e) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (e instanceof GooglePlayServicesAvailabilityException) {
                    int statusCode = ((GooglePlayServicesAvailabilityException)e).getConnectionStatusCode();
                    Dialog dialog = GooglePlayServicesUtil.getErrorDialog(statusCode, LoginActivity.this, REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
                    dialog.show();
                } else if (e instanceof UserRecoverableAuthException) {
                    Intent intent = ((UserRecoverableAuthException)e).getIntent();
                    startActivityForResult(intent, REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
                }
            }
        });
    }

    boolean isDeviceOnline() {
//        Log.i(TAG, "isDeviceOnline");
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean isWifiConn = networkInfo.isConnected();
        networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isMobileConn = networkInfo.isConnected();
        return isMobileConn || isWifiConn;
    }
}