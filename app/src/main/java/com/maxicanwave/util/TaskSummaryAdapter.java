package com.maxicanwave.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.maxicanwave.manager.RequestManagerPost;
import com.maxicanwave.manager.ResponseHandler;
import com.maxicanwave.modal.GroupMember;
import com.maxicanwave.modal.Response;
import com.maxicanwave.org.R;
import com.maxicanwave.org.TimerActivity;
import com.maxicanwave.util.URLMaxicanWave;

import java.util.ArrayList;

/**
 * Created by Tejveer on 11/13/2015.
 */
public class TaskSummaryAdapter extends ArrayAdapter<String>  implements ResponseHandler {
    GroupMember grpm = new GroupMember();
    ArrayList<String> groupname;
    ArrayList<Integer> group_id ;
    ArrayList<Integer> task ;
    ArrayList<Integer> task_status ;
    ArrayList<Integer> task_id ;
    int list_param;
    Response response;
    public TaskSummaryAdapter(Context context, int resource) {
        super(context, resource);
    }

    public TaskSummaryAdapter(Context context, int resource, ArrayList<String> group,ArrayList<Integer> group_id
            ,ArrayList<Integer> task,ArrayList<Integer> task_status,ArrayList<Integer> task_id,int list_param,Response response)
    {
        super(context, resource, group);
        this.groupname = group;
        this.group_id = group_id;
        this.task = task;
        this.task_status = task_status;
        this.task_id = task_id;
        this.list_param = list_param;
        this.response = response;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.row_join_group_summary, null);

        }
        TextView textView= (TextView) v.findViewById(R.id.tvGroupName);
        textView.setText(groupname.get(position));

        Button btnAccept= (Button) v.findViewById(R.id.btnJoin);
        Button btnDeny= (Button) v.findViewById(R.id.btnCancel);
        if(list_param==2)
        {
            btnDeny.setVisibility(View.INVISIBLE);
        }
        else {
            if (task_status.get(position).equals(2)) {
                btnAccept.setVisibility(View.INVISIBLE);
                btnDeny.setVisibility(View.VISIBLE);
                btnDeny.setText("Task Denied");
                btnDeny.setTextSize(10);
                btnDeny.setClickable(false);
                btnDeny.setBackgroundColor(Color.parseColor("#D3D3D3"));
            } else if (task_status.get(position).equals(3)) {
                btnAccept.setVisibility(View.INVISIBLE);
                btnDeny.setVisibility(View.VISIBLE);
                btnDeny.setText("Task Complete");
                btnDeny.setTextSize(10);
                btnDeny.setClickable(false);
                btnDeny.setBackgroundColor(Color.parseColor("#D3D3D3"));
            }else{
                btnDeny.setVisibility(View.VISIBLE);
                btnAccept.setVisibility(View.VISIBLE);
                btnAccept.setText("Accept Task");
                btnAccept.setBackgroundColor(Color.parseColor("#008000"));
                btnDeny.setText("Deny Task");
                btnAccept.setTextSize(10);
                btnDeny.setTextSize(10);
                btnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "ID : " + task_id.get(position) + " AND Group : " + groupname.get(position), Toast.LENGTH_LONG).show();

                    }
                });

                btnDeny.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "ID : " + task_id.get(position) + " AND Group : " + groupname.get(position), Toast.LENGTH_LONG).show();
                        grpm.setId(task_id.get(position));
                        grpm.setG_id(group_id.get(position));
                        grpm.setU_id(response.getId());
                        grpm.setTask(1);
                        grpm.setTask_status(2);
                        Gson gson = new Gson();
                        String taskJson = gson.toJson(grpm);
                        RequestManagerPost denyTask = new RequestManagerPost(getContext(), URLMaxicanWave.TASK_UPDATE, taskJson, "DenyTask");
                        denyTask.execute();
                    }
                });

                btnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        grpm.setId(task_id.get(position));
                        grpm.setG_id(group_id.get(position));
                        grpm.setU_id(response.getId());
                        grpm.setTask(1);
                        grpm.setTask_status(2);
                        Intent intent =  new Intent(getContext(), TimerActivity.class);
                        intent.putExtra("group",grpm);
                        getContext().startActivity(intent);

                    }
                });

            }
        }





        return v;
    }

    @Override
    public void onResponse(String result, String Method) {
        if(Method.equals("DenyTask"))
        {
            Toast.makeText(getContext(),"Task Cancel", Toast.LENGTH_LONG).show();

        }

    }
}
