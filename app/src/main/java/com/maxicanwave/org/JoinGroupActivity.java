package com.maxicanwave.org;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.maxicanwave.manager.RequestManagerGet;
import com.maxicanwave.manager.ResponseHandler;
import com.maxicanwave.modal.Response;
import com.maxicanwave.util.Tool;
import com.maxicanwave.util.URLMaxicanWave;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JoinGroupActivity extends Activity implements ResponseHandler{


    private TextView tv,tv2;
    private Context context;
    private ListView lv;
    Tool tool=new Tool();
    private ArrayList<String> groupNameList=new ArrayList<String>();
    private ArrayList<Integer> idList=new ArrayList<Integer>();
    private ArrayList<Integer> uIdList=new ArrayList<Integer>();
    Response response;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_group);
        response = (Response)getIntent().getExtras().getSerializable("response");
        context=this;
            tv= (TextView) findViewById(R.id.textview);
        lv= (ListView) findViewById(R.id.ls);

        getAllGroups();




    }

    public void getAllGroups()
    {
        if(tool.isNetworkAvailable(context))
        {
              RequestManagerGet getAllGroupRequest = new RequestManagerGet(context, URLMaxicanWave.GROUP_GET_ALL+"?id="+response.getId(),"FetchAllGroup");
            getAllGroupRequest.execute();



        }
        else
        {
            Toast.makeText(context,"It seems you don't have Internet connection",Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onResponse(String result, String Method) {


        try {

            if("FetchAllGroup".equals(Method)) {
                JSONArray jsonArray = new JSONArray(result);
                String status = "";
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    groupNameList.add(jo.getString("groupname"));
                    idList.add(jo.getInt("id"));
                    uIdList.add(jo.getInt("u_id"));

                    ListAdapter la = new ListAdapter(context, R.id.tvGroupName, groupNameList, idList, uIdList);
                    lv.setAdapter(la);
                }

            }
            else if("GROUP_JOIN".equals(Method))
            {
                Gson gson = new Gson();
                Response response = gson.fromJson(result,Response.class);
                Intent intent =  new Intent(context,ListSummaryGroup.class);
                intent.putExtra("response", response);
                startActivity(intent);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }



    }
}
