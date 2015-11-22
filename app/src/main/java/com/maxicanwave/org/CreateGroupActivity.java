package com.maxicanwave.org;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.maxicanwave.manager.RequestManagerPost;
import com.maxicanwave.manager.ResponseHandler;
import com.maxicanwave.modal.Group;
import com.maxicanwave.util.Tool;
import com.maxicanwave.util.URLMaxicanWave;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tejveer on 11/3/2015.
 */
public class CreateGroupActivity extends Activity implements ResponseHandler{
    private Context context;
    private EditText edtGroupName;
    private Button btnCreate;
    Tool tool=new Tool();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_group_layout);
        context=this;
        edtGroupName= (EditText) findViewById(R.id.edtCreateGroup);
        btnCreate= (Button) findViewById(R.id.btnCreateGroup);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tool.isNetworkAvailable(context))
                {
                    String strGroupName=edtGroupName.getText().toString();
                    if(strGroupName.toString().length()<6)
                    {
                        Toast.makeText(context,"Group name must contain atleast 6 digits",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                            Group group=new Group();
                            group.setGroupname(strGroupName);

                        Gson gson=new Gson();
                        String jsonStr=gson.toJson(group);
                        RequestManagerPost creatGroupRequest = new RequestManagerPost(context, URLMaxicanWave.GROUP_VALIDATE,jsonStr,"GroupValidation");
                        creatGroupRequest.execute();


                    }
                }
                else
                {
                    Toast.makeText(context,"It seems you don't have Internet connection",Toast.LENGTH_LONG).show();

                }
            }
        });
    }


   private void createGroup()
   {
       String strGroupName=edtGroupName.getText().toString();
       Group group=new Group();
       group.setGroupname(strGroupName);
       group.setU_id("1");
       Gson gson=new Gson();
       String jsonStr=gson.toJson(group);
       RequestManagerPost creatGroupRequest = new RequestManagerPost(context, URLMaxicanWave.GROUP_CREATE,jsonStr,"GroupCreation");
       creatGroupRequest.execute();
   }




    @Override
    public void onResponse(String result, String Method) {
        try {
            JSONObject jsonObject=new JSONObject(result);
            String status="";
            switch (Method)
            {
                case "GroupValidation":
                    status=jsonObject.getString("status");
                    if(status.equalsIgnoreCase("Success"))
                    {
                        createGroup();
                    }else
                    {
                        Toast.makeText(context,"This Group Name is already exits",Toast.LENGTH_LONG).show();
                    }

                    break;
                case "GroupCreation":

                    status=jsonObject.getString("status");
                    if(status.equalsIgnoreCase("Success"))
                    {
                        Toast.makeText(context,"Group Created Successfully",Toast.LENGTH_LONG).show();
                    }else
                    {
                        Toast.makeText(context,"Group not created",Toast.LENGTH_LONG).show();
                    }


                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }
}
