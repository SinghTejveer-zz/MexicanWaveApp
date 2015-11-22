package com.maxicanwave.org;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.maxicanwave.manager.RequestManagerPost;
import com.maxicanwave.manager.ResponseHandler;
import com.maxicanwave.modal.Response;
import com.maxicanwave.modal.User;
import com.maxicanwave.util.Tool;
import com.maxicanwave.util.URLMaxicanWave;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

/**
 * Created by Dubey on 10/30/2015.
 */
public class RegisterActivity extends Activity implements ResponseHandler{
    Button btnRegistration;
    EditText edtFullName,edtUserName,edtPassword;
    Tool tool= new Tool();
    private Context context;
    private Button btnValidate;
    private boolean isAvailbilityChecked=false;
    TextView tx1;
    int counter = 3;
    String result = "";
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);
        context=this;

        btnValidate= (Button) findViewById(R.id.checkAvailability);
        edtFullName=(EditText)findViewById(R.id.txtfullname);
        edtUserName=(EditText)findViewById(R.id.txtuser);
        edtPassword=(EditText)findViewById(R.id.txtpassword);

        btnRegistration=(Button)findViewById(R.id.btn_user_registration);


        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(tool.isNetworkAvailable(context))
                {
                    String strFullName=edtFullName.getText().toString();
                    String strUserName=edtUserName.getText().toString();
                    String strPassword=edtPassword.getText().toString();
                    String strImeiNumber=tool.getIMEINumber(context);
                    if(!TextUtils.isEmpty(strFullName)||!TextUtils.isEmpty(strUserName)||!TextUtils.isEmpty(strPassword))
                    {
                        if(!(strUserName.length()<6)&&!(strPassword.length()<6))
                        {
                            User user=new User(strFullName,strImeiNumber,strUserName,strPassword);
                            Gson gson=new Gson();
                            String jsonString=gson.toJson(user);
                            mProgressDialog=new ProgressDialog(context);
                            mProgressDialog.setMessage("Processing");
                            mProgressDialog.show();;
                            RequestManagerPost loginRequest = new RequestManagerPost(context, URLMaxicanWave.USER_REGISTRATION,jsonString,"UserRegistration");
                            loginRequest.execute();
                        }
                        else
                        {
                            Toast.makeText(context, "User name and password must have atleast 6 digits", Toast.LENGTH_LONG).show();
                        }

                    }

                    else
                    {
                        Toast.makeText(context, "All fields are mandatory", Toast.LENGTH_LONG).show();

                    }


                }
                else
                {
                    Toast.makeText(context,"It seems you're not connected to internet",Toast.LENGTH_LONG).show();
                }


            }
        });

        btnValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(edtUserName.getText()))
                {
                if(tool.isNetworkAvailable(context)) {
                    btnValidate.setText("Wait");

                    JSONObject json = new JSONObject();

                    try {
                        mProgressDialog=new ProgressDialog(context);
                        mProgressDialog.setMessage("Processing");
                        mProgressDialog.show();;
                        json.accumulate("user", edtUserName.getText().toString());
                        RequestManagerPost requestManagerPost = new RequestManagerPost(context, URLMaxicanWave.USER_VALIDATE, json.toString(),"UserValidate");
                        requestManagerPost.execute();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                }


            }
        });






    }


    @Override
    public void onResponse(String result,String Method) {
       // this.result = result;
        mProgressDialog.cancel();
        System.out.println(result + " : " + Method);
        Gson gson = new Gson();
        Response response = gson.fromJson(result,Response.class);
        if(Method.equalsIgnoreCase("UserValidate"))
        {
            Toast.makeText(context,result,Toast.LENGTH_LONG).show();
        }else if(Method.equalsIgnoreCase("UserRegistration"))
        {
            Intent intent=new Intent(context,HomeActivity.class);
            intent.putExtra("response", response);
            startActivity(intent);
            finish();
        }

    }
}
