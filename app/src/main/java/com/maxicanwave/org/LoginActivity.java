package com.maxicanwave.org;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.maxicanwave.manager.RequestManagerPost;
import com.maxicanwave.manager.ResponseHandler;
import com.maxicanwave.modal.Response;
import com.maxicanwave.util.Tool;
import com.maxicanwave.util.URLMaxicanWave;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

public class LoginActivity extends Activity implements ResponseHandler{
    private EditText edtUserName;
    private EditText edtPassword;
    private Button btnLogin;
    private Context context;
    private TextView tvSignUp;
    Tool tool= new Tool();
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        context=this;
        edtUserName= (EditText) findViewById(R.id.edtUserName);
        edtPassword= (EditText) findViewById(R.id.edtPassword);
        tvSignUp= (TextView) findViewById(R.id.signUpTextView);
        btnLogin= (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(tool.isNetworkAvailable(context))
                {
                    String strUserName=edtUserName.getText().toString();
                    String strPassword=edtPassword.getText().toString();
                    if(!TextUtils.isEmpty(strUserName)||!TextUtils.isEmpty(strPassword))
                    {
                        getJsonObject(strUserName,strPassword);
                        mProgressDialog=new ProgressDialog(context);
                        mProgressDialog.setMessage("Processing");
                        mProgressDialog.show();
                        RequestManagerPost loginRequest = new RequestManagerPost(context,URLMaxicanWave.USER_LOGIN,getJsonObject(strUserName, strPassword),"UserLogin");
                        loginRequest.execute();
                    }
                    else
                    {
                        Toast.makeText(context,"User Name or password can not be blank.",Toast.LENGTH_LONG).show();

                    }


                }
                else
                {
                    Toast.makeText(context,"It seems you're not connected to internet",Toast.LENGTH_LONG).show();
                }

            }
        });


        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent= new Intent(context,RegisterActivity.class);
                startActivity(registerIntent);
            }
        });









    }


    private String getJsonObject(String usrName, String usrPass)
    {
        String json = "";

        try {

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("user", usrName);
            jsonObject.accumulate("pass", usrPass);
            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  json;
    }


    @Override
    public void onResponse(String result,String Method) {
        Gson gson = new Gson();
        Response response = gson.fromJson(result,Response.class);
        mProgressDialog.cancel();
        if(response.getStatus().equals("Success")) {
            Intent intent = new Intent(context, HomeActivity.class);
            intent.putExtra("response", response);
            startActivity(intent);
            finish();
        }
        else
        {
            Toast.makeText(context,"Login Failed",Toast.LENGTH_LONG).show();

        }

    }
}
