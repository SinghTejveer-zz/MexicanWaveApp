package com.maxicanwave.org;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.maxicanwave.manager.RequestManagerPost;
import com.maxicanwave.manager.ResponseHandler;
import com.maxicanwave.modal.GroupMember;
import com.maxicanwave.modal.Response;
import com.maxicanwave.util.URLMaxicanWave;

/**
 * Created by Ashish Srivastava on 11/19/2015.
 */
public class TaskActivity extends Activity implements ResponseHandler {

    GroupMember grpm = new GroupMember();
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_finish);
        mProgressDialog=new ProgressDialog(this);
        mProgressDialog.setMessage("Processing");
        mProgressDialog.show();
        grpm = (GroupMember) getIntent().getExtras().getSerializable("group");
        grpm.setTask_status(3);
        grpm.setTask(1);
        Gson gson = new Gson();
        String taskJson = gson.toJson(grpm);
        RequestManagerPost taskComplete = new RequestManagerPost(this, URLMaxicanWave.TASK_UPDATE, taskJson, "Task Complete");
        taskComplete.execute();
        Log.d("Loop","Loop");

    }

    @Override
    public void onResponse(String result, String Method) {
        if (Method.equals("Task Complete")) {
            mProgressDialog.cancel();
            Intent intent = new Intent(this, ListSummaryGroup.class);
            Response response = new Response();
            Log.d("ID",""+grpm.getId());
            response.setId(grpm.getId());
            intent.putExtra("response", response);
            startActivity(intent);
        }
    }
}
