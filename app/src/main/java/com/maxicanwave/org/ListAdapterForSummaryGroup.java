package com.maxicanwave.org;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.maxicanwave.util.Tool;
import com.maxicanwave.util.URLMaxicanWave;

import java.util.ArrayList;

/**
 * Created by Tejveer on 10/20/2015.
 */
public class ListAdapterForSummaryGroup extends ArrayAdapter<String> implements ResponseHandler{
    private ArrayList<String> list;
    private ArrayList<Integer> listId;
    private ArrayList<Integer> listUid;
    private Context context;
    Tool tool = new Tool();
    ProgressDialog mProgressDialog;
    Gson gson = new Gson();
    int Group_id =0,User_id=0;
    int list_param;

    GroupMember grpm = new GroupMember();
    public ListAdapterForSummaryGroup(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ListAdapterForSummaryGroup(Context context, int resource, ArrayList<String> items,int listParam) {
        super(context, resource, items);
        this.context = context;
        this.list = items;
        this.list_param=listParam;

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
        textView.setText(list.get(position));

        Button btnJoin= (Button) v.findViewById(R.id.btnJoin);
        Button btnCancel= (Button) v.findViewById(R.id.btnCancel);
        if(list_param==2)
        {
            btnCancel.setVisibility(View.INVISIBLE);
           // btnJoin.setVisibility(View.INVISIBLE);
        }
        else
        {
            btnCancel.setVisibility(View.VISIBLE);
            btnJoin.setVisibility(View.VISIBLE);
        }






        return v;
    }

    @Override
    public void onResponse(String result, String Method) {
        Response response = gson.fromJson(result,Response.class);
        mProgressDialog.cancel();
        Toast.makeText(context,result,Toast.LENGTH_LONG);
    }
}