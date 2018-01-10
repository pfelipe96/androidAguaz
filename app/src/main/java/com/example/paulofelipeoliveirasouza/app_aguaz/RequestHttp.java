package com.example.paulofelipeoliveirasouza.app_aguaz;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.StrictMode;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by paulofelipeoliveirasouza on 06/01/2018.
 */


public class RequestHttp {

    public static String HTTP_OK = "success";
    public static String HTTP_FAILED = "failed";
    private HttpCall httpCall;
    private Activity mActivity;
    private Response mResponse;
    private Call mCall;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public RequestHttp(HttpCall httpCall, Activity mActivity) {
        this.httpCall = httpCall;
        this.mActivity = mActivity;
    }

    public void getRequestHttp() {
        if (isOnline()) {
            String requestURL = httpCall.getUrl();
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            OkHttpClient client = new OkHttpClient();
            Request request;

            if (httpCall.getMethodType() == HttpCall.POST) {
                request = new Request.Builder()
                        .url(requestURL)
                        .post(RequestBody.create(JSON, httpCall.getParams().toString()))
                        .build();
            } else {
                request = new Request.Builder()
                        .url(requestURL)
                        .get()
                        .build();
            }

            try {
                mResponse = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (mResponse.isSuccessful()) {
                try {
                    onGetResponse(HTTP_OK, mResponse);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    onGetResponse(HTTP_FAILED, mResponse);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            mResponse.close();
        } else {
            Toast.makeText(mActivity.getApplicationContext(), mActivity.getString(R.string.message_failed_is_online), Toast.LENGTH_LONG).show();

        }
    }

    public void onGetResponse(String mResponseString, Response mResponse) throws JSONException {

    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
