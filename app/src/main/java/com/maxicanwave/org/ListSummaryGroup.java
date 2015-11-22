package com.maxicanwave.org;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.maxicanwave.util.TaskSummaryAdapter;
import com.maxicanwave.manager.RequestManagerGet;
import com.maxicanwave.manager.ResponseHandler;
import com.maxicanwave.modal.Response;
import com.maxicanwave.util.Tool;
import com.maxicanwave.util.URLMaxicanWave;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ashish Srivastava on 11/7/2015.
 */
public class ListSummaryGroup extends Activity implements ResponseHandler{

    ArrayList<String> groupname = new ArrayList<String>();
    ArrayList<Integer> group_id = new ArrayList<Integer>();
    ArrayList<Integer> task = new ArrayList<Integer>();
    ArrayList<Integer> task_status = new ArrayList<Integer>();
    ArrayList<Integer> task_id = new ArrayList<Integer>();
    Response response;
    private ListView lv;
    ListAdapterForSummaryGroup la;
    ArrayList<String> listData=new ArrayList<String>();
    private Context context;
    Tool tool=new Tool();
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;

        setContentView(R.layout.layout_list_summary_group);
        response = (Response) getIntent().getExtras().getSerializable("response");
        lv= (ListView) findViewById(R.id.ls);
        getTaskList();

    }

    public void showList1(View v)
    {
        if(tool.isNetworkAvailable(context)) {
            groupname.clear();
            group_id.clear();
            task_id.clear();
            task.clear();
            task_status.clear();
            RequestManagerGet requestManagerGet = new RequestManagerGet(context, URLMaxicanWave.TASK_GET+"?id="+response.getId(),"GET_TASK");
            requestManagerGet.execute();

        }
        else{
            Toast.makeText(context, "It seems you don't have Internet connection", Toast.LENGTH_LONG).show();

        }
    }

    public void showList2(View v)
    {
//        if(tool.isNetworkAvailable(context)) {
//            mProgressDialog=new ProgressDialog(context);
//            mProgressDialog.setMessage("Processing");
//            mProgressDialog.show();;
//            RequestManagerGet requestManagerGet = new RequestManagerGet(context, URLMaxicanWave.TASK_GET+"?id="+response.getId(),"GET_TASK");
//            requestManagerGet.execute();
//
//        }
//        else{
//            Toast.makeText(context, "It seems you don't have Internet connection", Toast.LENGTH_LONG).show();
//
//        }
    }



    public void getTaskList()
    {
        if(tool.isNetworkAvailable(context)) {
            mProgressDialog=new ProgressDialog(context);
            mProgressDialog.setMessage("Processing");
            mProgressDialog.show();;
            RequestManagerGet requestManagerGet = new RequestManagerGet(context, URLMaxicanWave.TASK_GET+"?id="+response.getId(),"GET_TASK");
            requestManagerGet.execute();

        }
        else{
            Toast.makeText(context, "It seems you don't have Internet connection", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onResponse(String result, String Method) {
        if(Method.equals("GET_TASK"))
        {
            try {
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    group_id.add(jsonObject.getInt("g_id"));
                    groupname.add(jsonObject.getString("groupName"));
                    task.add(jsonObject.getInt("task"));
                    task_status.add(jsonObject.getInt("task_status"));
                    task_id.add(jsonObject.getInt("id"));
                }
                TaskSummaryAdapter taskSummaryAdapter = new TaskSummaryAdapter(context,R.id.tvGroupName,groupname,group_id,task,task_status,task_id,1,response);
                lv.setAdapter(taskSummaryAdapter);
                mProgressDialog.cancel();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
