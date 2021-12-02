package edu.floridapoly.mobiledeviceapps.fall21.cinemapal;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpGetRequest extends CallbackTask<String> {
    AsyncTask<Void, Void, Void> task;

    public HttpGetRequest(String urlStr) {
        @SuppressWarnings("deprecation") @SuppressLint("StaticFieldLeak")
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Log.d("HTTP", "doing request at " + urlStr);
                try {
                    URL url = new URL(urlStr);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    int responseCode = connection.getResponseCode();
                    if(responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String output;
                        while((output = in.readLine()) != null) {
                            response.append(output);
                        }
                        in.close();

                        callOnSuccess(response.toString());
                    } else {
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                        StringBuilder err = new StringBuilder();
                        String output;
                        while((output = in.readLine()) != null) {
                            err.append(output);
                        }
                        in.close();

                        throw new IOException(err.toString());
                    }
                }
                catch(Exception e) {
                    if(onFailure != null) {
                        onFailure.onFailure(e);
                    }
                }

                return null;
            }
        };
        this.task = task;
    }

    @Override
    public void execute() {
        task.execute();
    }
}
