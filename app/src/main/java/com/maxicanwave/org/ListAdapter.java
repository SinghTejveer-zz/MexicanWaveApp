package com.maxicanwave.org;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.maxicanwave.manager.RequestManagerPost;
import com.maxicanwave.manager.ResponseHandler;
import com.maxicanwave.modal.GroupMember;
import com.maxicanwave.modal.Response;
import com.maxicanwave.util.Tool;
import com.maxicanwave.util.URLMaxicanWave;

import java.util.ArrayList;

/**
 * Created by Tejveer on 10/20/2015.
 */
public class ListAdapter extends ArrayAdapter<String> implements ResponseHandler{
    private ArrayList<String> list;
    private ArrayList<Integer> listId;
    private ArrayList<Integer> listUid;
    private Context context;
    Tool tool = new Tool();
    ProgressDialog mProgressDialog;
    Gson gson = new Gson();
    int Group_id =0,User_id=0;

    GroupMember grpm = new GroupMember();
    public ListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ListAdapter(Context context, int resource, ArrayList<String> items,ArrayList<Integer> idlist,ArrayList<Integer> uidList) {
        super(context, resource, items);
        this.context = context;
        this.list = items;
        this.listId=idlist;
        this.listUid=uidList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.row_join_group, null);
        }
        TextView tt1 = (TextView) v.findViewById(R.id.tvGroupName);
        tt1.setText(list.get(position));
       // TextView tvdate = (TextView) v.findViewById(R.id.tvDate);

        Button btnJoin= (Button) v.findViewById(R.id.btnJoin);
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Group_id = listId.get(position);
                User_id = listUid.get(position);
                Toast.makeText(context,"ID : "+Group_id+" AND UID : "+User_id,Toast.LENGTH_LONG).show();

                if(tool.isNetworkAvailable(context))
                {
                    mProgressDialog=new ProgressDialog(context);
                    mProgressDialog.setMessage("Processing");
                    mProgressDialog.show();
                    grpm.setG_id(Group_id);
                    grpm.setU_id(User_id);
                    grpm.setTask(0);
                    grpm.setTask_status(1);
                    String json = gson.toJson(grpm);
                    RequestManagerPost loginRequest = new RequestManagerPost(context, URLMaxicanWave.GROUP_JOIN,json,"GROUP_JOIN");
                    loginRequest.execute();

                }
                else
                {
                    Toast.makeText(context,"It seems you don't have Internet connection",Toast.LENGTH_LONG).show();

                }
            }
        });





        return v;
    }

    @Override
    public void onResponse(String result, String Method) {
        Response response = gson.fromJson(result,Response.class);
        mProgressDialog.cancel();
        Toast.makeText(context,result,Toast.LENGTH_LONG);
    }
}