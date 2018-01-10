package com.example.paulofelipeoliveirasouza.app_aguaz;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUpActivity extends AppCompatActivity {

    private EditText mName, mEmail, mPhone, mZipCode, mNumberHouse, mPassword, mRePassword;
    private TextView mMessage;
    private String mNameString, mEmailString, mPhoneString, mZipCodeString, mNumberHouseString, mPasswordString, mRePasswordString;
    private Button mButtonCadastro;
    private Boolean mVerification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mName = findViewById(R.id.edit_text_name);
        mEmail = findViewById(R.id.edit_text_email);
        mPhone = findViewById(R.id.edit_text_phone);
        mZipCode = findViewById(R.id.edit_text_zip_code);
        mNumberHouse = findViewById(R.id.edit_text_number_house);
        mPassword = findViewById(R.id.edit_text_password);
        mRePassword = findViewById(R.id.edit_text_repassword);
        mButtonCadastro = findViewById(R.id.button_cadastro);
        mMessage = findViewById(R.id.text_message);


        mButtonCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mNameString = mName.getText().toString();
                    mEmailString = mEmail.getText().toString();
                    mPhoneString = mPhone.getText().toString();
                    mZipCodeString = mZipCode.getText().toString();
                    mNumberHouseString = mNumberHouse.getText().toString();
                    mPasswordString = mPassword.getText().toString();
                    mRePasswordString = mRePassword.getText().toString();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                mVerification = VerificationFields(mNameString, mEmailString, mPhoneString, mZipCodeString, mNumberHouseString, mPasswordString, mRePasswordString);
                if (mVerification) {
                    sendBackEnd(mNameString, mEmailString, mPhoneString, mZipCodeString, mNumberHouseString, mPasswordString);
                }
            }
        });

    }

    public boolean VerificationFields(String mName, String mEmail, String mPhone, String mZipCode, String mNumberHouse, String mPassword, String mRePassword) {

        if (mName.trim().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "O campo do seu nome está vázio, por gentileza verifique!", Toast.LENGTH_LONG).show();
            return false;
        } else if (mEmail.trim().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "O campo do seu e-mail está vázio, por gentileza verifique!", Toast.LENGTH_LONG).show();
            return false;
        } else if (mPhone.trim().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "O campo do seu telefone está vázio, por gentileza verifique!", Toast.LENGTH_LONG).show();
            return false;
        } else if (mZipCode.trim().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "O campo do seu CEP está vázio, por gentileza verifique!", Toast.LENGTH_LONG).show();
            return false;
        } else if (mNumberHouse.trim().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "O campo do seu número residencial está vázio, por gentileza verifique!", Toast.LENGTH_LONG).show();
            return false;
        } else if (mPassword.trim().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "O campo da sua senha está vázio, por gentileza verifique!", Toast.LENGTH_LONG).show();
            return false;
        } else if (mRePassword.trim().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "O campo de confirmação da sua senha está vázio, por gentileza verifique!", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!mPassword.equals(mRePassword)) {
            Toast.makeText(getApplicationContext(), "As senhas não são iguais", Toast.LENGTH_LONG).show();
            return false;
        }

        if (mPassword.length() < 6) {
            Toast.makeText(getApplicationContext(), "Sua senha é muito curta", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;

    }

    public void sendBackEnd(String mName, String mEmail, String mPhone, String mZipCode, String mNumberHouse, String mPassword) {
        JSONObject mJsonObject = new JSONObject();
        try {
            mJsonObject.put("name", mName);
            mJsonObject.put("email", mEmail);
            mJsonObject.put("phone", mPhone);
            mJsonObject.put("zipcode", mZipCode);
            mJsonObject.put("numbehouse", mNumberHouse);
            mJsonObject.put("password", mPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpCall httpCallPost = new HttpCall();
        httpCallPost.setMethodType(HttpCall.POST);
        httpCallPost.setUrl("http://192.168.100.164:3000/usuario/criar");
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
                        Toast.makeText(getApplicationContext(), mMesssageResponse, Toast.LENGTH_LONG).show();
                        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
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
