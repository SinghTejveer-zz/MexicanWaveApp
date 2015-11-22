package com.maxicanwave.org;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.maxicanwave.modal.Response;

public class HomeActivity extends Activity {

    private Button btnJoin,btnCreateGroup,btnSummary;
    private  Context con;
    Response response;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);
        response = (Response) getIntent().getExtras().getSerializable("response");

        con=this;
        btnCreateGroup= (Button) findViewById(R.id.btn_create);
        btnJoin= (Button) findViewById(R.id.btn_join);
        btnSummary= (Button) findViewById(R.id.btn_summary);
        btnCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(con,CreateGroupActivity.class);
                i.putExtra("response",response);
                startActivity(i);
            }
        });

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(con,JoinGroupActivity.class);
                i.putExtra("response",response);
                startActivity(i);
            }
        });

        btnSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(con,ListSummaryGroup.class);
                i.putExtra("response",response);
                startActivity(i);
            }
        });

    }


}
