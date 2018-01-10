package com.example.paulofelipeoliveirasouza.app_aguaz;

import android.content.Context;
import android.net.ConnectivityManager;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * Created by paulofelipeoliveirasouza on 07/01/2018.
 */

public class HttpCall {

    public static final int GET = 1;
    public static final int POST = 2;

    private String url;
    private int methodType;
    JSONObject params;

    public static int getGET() {
        return GET;
    }

    public static int getPOST() {return POST;}

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getMethodType() {
        return methodType;
    }

    public void setMethodType(int methodType) {
        this.methodType = methodType;
    }

    public JSONObject getParams() {
        return params;
    }

    public void setParams(JSONObject params) {this.params = params;}

}
