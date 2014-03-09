package com.example.databaseandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
    public static final int ACCEPTED = 202;
    public static final int LOGIN_REQUEST_CODE = 0;
    public static final int LOGOUT_RESULT_CODE = 2;
    public static final int RECOVER_REQUEST_CODE = 1;
    public static final int REGISTER_REQUEST_CODE = 2;
    public static final int UNAUTHORIZED = 401;
    private Button bLogin;
    private EditText etEmailAddress, etPassword;
    private final Class<?> LOGIN_DESTINATION = Test.class;
    private SharedPreferences sharedPreferences;

    public void login() {
        this.sharedPreferences.edit().putBoolean("user_logged_in", true)
                .commit();
        this.sharedPreferences
                .edit()
                .putString("username", this.etEmailAddress.getText().toString())
                .commit();
        this.sharedPreferences.edit()
                .putString("password", this.etPassword.getText().toString())
                .commit();
        this.startActivityForResult(new Intent(MainActivity.this,
                this.LOGIN_DESTINATION), MainActivity.LOGIN_REQUEST_CODE);
    }

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        // get shared preferences
        this.sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);

        // check if user is logged in already
        if (this.sharedPreferences.getBoolean("user_logged_in", false)) {
            // user is logged in, bypass activity
            this.startActivityForResult(new Intent(MainActivity.this,
                    this.LOGIN_DESTINATION), MainActivity.LOGIN_REQUEST_CODE);
        }

        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_main);
        this.bLogin = (Button) this.findViewById(R.id.submit);
        this.etEmailAddress = (EditText) this.findViewById(R.id.username);
        this.etPassword = (EditText) this.findViewById(R.id.password);

        this.bLogin.setOnClickListener(new OnClickListener() {
            /*
             * (non-Javadoc)
             * @see android.view.View.OnClickListener#onClick(android.view.View)
             */
            @Override
            public void onClick(final View v) {
                new LoginTask(MainActivity.this).execute(
                        MainActivity.this.etEmailAddress.getText().toString(),
                        MainActivity.this.etPassword.getText().toString());
            }
        });
    }

    public void showLoginError(final String result) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(
                MainActivity.this);
        if (builder != null) {
            builder.setPositiveButton("ok",
                    new DialogInterface.OnClickListener() {
                        /*
                         * (non-Javadoc)
                         * @see
                         * android.content.DialogInterface.OnClickListener#onClick
                         * (android.content.DialogInterface, int)
                         */
                        @Override
                        public void onClick(DialogInterface dialog,
                                final int which) {
                            if (dialog != null) {
                                dialog.cancel();
                                dialog = null;
                            }
                        }
                    });
            builder.setMessage("Invalid Username or Password.");
            final AlertDialog alert = builder.create();
            if (alert != null) {
                alert.setCancelable(false);
                alert.show();
            }
        }
    }
}