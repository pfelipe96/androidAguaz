package com.example.paulofelipeoliveirasouza.app_aguaz;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private TextView mSendCadastrar;
    private Button mButtonLogin;
    private EditText mEmail, mPassword;
    private String mEmailString, mPasswordString;
    private boolean mVerification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.edit_text_email_login);
        mPassword = findViewById(R.id.edit_text_password_login);
        mSendCadastrar = findViewById(R.id.send_cadastrar);
        mSendCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
        mButtonLogin = findViewById(R.id.button_entrar);
        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mEmailString = mEmail.getText().toString();
                    mPasswordString = mPassword.getText().toString();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                mVerification = VerificationFields(mEmailString, mPasswordString);
                if(mVerification){
                    sendBackEnd(mEmailString, mPasswordString);
                }
            }
        });
    }

    public boolean VerificationFields(String mEmail, String mPassword) {

        if (mEmail.trim().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "O campo do seu e-mail está vázio, por gentileza verifique!", Toast.LENGTH_LONG).show();
            return false;
        } else if (mPassword.trim().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "O campo da sua senha está vázio, por gentileza verifique!", Toast.LENGTH_LONG).show();
            return false;
        }

        if (mPassword.length() < 6) {
            Toast.makeText(getApplicationContext(), "Sua senha é muito curta", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;

    }

    public void sendBackEnd(String mEmail, String mPassword) {
        JSONObject mJsonObject = new JSONObject();
        try {
            mJsonObject.put("email", mEmail);
            mJsonObject.put("password", mPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpCall httpCallPost = new HttpCall();
        httpCallPost.setMethodType(HttpCall.POST);
        httpCallPost.setUrl("http://192.168.100.164:3000/usuario/login");
        httpCallPost.setParams(mJsonObject);
        try {
            new RequestHttp(httpCallPost, this) {
                @Override
                public void onGetResponse(String mResponseString, Response mResponse) throws JSONException {
                    super.onGetResponse(mResponseString, mResponse);
                    boolean isResponse = false;
                    String mMesssageResponse = null;
                    if(mResponse != null) {
                        JSONObject mJsonObject = null;
                        try {
                            mJsonObject = new JSONObject(mResponse.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        isResponse = mJsonObject.getBoolean("success");
                        mMesssageResponse = mJsonObject.getString("messange_info");
                    }
                    if(mResponseString.equals(RequestHttp.HTTP_OK) && isResponse == true) {
                        Toast.makeText(getApplicationContext(), mMesssageResponse , Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(), mMesssageResponse, Toast.LENGTH_LONG).show();
                    }
                }
            }.getRequestHttp();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
