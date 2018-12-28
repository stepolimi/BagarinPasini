package com.data4help.data4help1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private String url = "http://192.168.0.143:8080/d4h-server-0.0.1-SNAPSHOT/api/users";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.loginButton);
        View registerLink = findViewById(R.id.registerLink);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject credential = new JSONObject();
                try {
                    credential.put("email", email.getText().toString());
                    credential.put("password", password.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                JsonObjectRequest jobReq = new JsonObjectRequest(Request.Method.GET, url, credential,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) { System.out.print("hi"); }},
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) { VolleyLog.e("Error: "+ volleyError.getMessage()); }}){
                    @Override
                    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                        switch (response.statusCode) {
                            case 200:
                                System.out.println("funziona!!!");
                                startActivity(new Intent(LoginActivity.this, MenuActivity.class));
                                break;
                            case 403:
                                System.out.println("The access has been denied. Try again.");
                                break;
                            case 401:
                                System.out.println("The given email is already in the DB. Change it or login.");
                                break;

                        }
                        finish();
                        return super.parseNetworkResponse(response);
                    }
                };
                queue.add(jobReq);
            }
        });


        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startActivity(new Intent(LoginActivity.this, RegistrationActivity.class)); }
        });
    }
}
