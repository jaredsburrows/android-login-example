package com.example.databaseandroid;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class LoginTask extends AsyncTask<String, Void, Integer> {
    private final Context context;
    private ProgressDialog dialog;

    public LoginTask(final Context context) {
        this.context = context;
    }

    @Override
    protected Integer doInBackground(final String... arg0) {
        int responseCode = 0;
        try {
            final HttpClient client = new DefaultHttpClient();
            final HttpPost httppost = new HttpPost(
                    "http://www.burrowsapps.com/test/user.php");

            final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                    2);
            nameValuePairs.add(new BasicNameValuePair("username", arg0[0]));
            nameValuePairs.add(new BasicNameValuePair("password", arg0[1]));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            int executeCount = 0;
            HttpResponse response;
            do {
                this.dialog.setMessage("Please Wait..");
                executeCount++;
                response = client.execute(httppost);
                responseCode = response.getStatusLine().getStatusCode();
            } while ((executeCount < 5) && (responseCode == 408));

            final BufferedReader rd = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));

            String line;
            while ((line = rd.readLine()) != null) {
                line.trim();
            }
        } catch (final Exception e) {
            responseCode = 408;
        }

        return responseCode;
    }

    @Override
    protected void onPostExecute(final Integer headerCode) {
        if (headerCode == 202) {
            ((MainActivity) this.context).login();
        } else {
            ((MainActivity) this.context).showLoginError("");
        }

        if (this.dialog != null) {
            this.dialog.dismiss();
            this.dialog = null;
        }
    }

    @Override
    protected void onPreExecute() {
        this.dialog = new ProgressDialog(this.context);
        this.dialog.setTitle("Logging in...");
        this.dialog.setCancelable(false);
        this.dialog.show();
    }
}