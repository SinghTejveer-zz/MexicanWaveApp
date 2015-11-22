package com.maxicanwave.manager;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.maxicanwave.modal.Response;
import com.maxicanwave.util.Tool;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by Ashish Srivastava on 10/31/2015.
 */
public class RequestManagerPost extends AsyncTask<String, Void, String> {

    private String result;
    private String url;
    private String json;
    Tool tool = new Tool();
    private Context context;
    private ResponseHandler res;
    private String Method;

    public RequestManagerPost(Context con, String url, String json,String Method) {
        this.url = url;
        this.json = json;
        this.context = con;
        this.res = (ResponseHandler) con;
        this.Method = Method;
    }


    @Override
    protected String doInBackground(String... params) {
        System.out.println(json);
        InputStream inputStream = null;
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        // 4. convert JSONObject to JSON to String

        StringEntity se = null;

        // 5. set json to StringEntity
        try {
            se = new StringEntity(json);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 6. set httpPost Entity
        httpPost.setEntity(se);

        // 7. Set some headers to inform server about the type of the content
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        // 8. Execute POST request to the given URL
        HttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpPost);
            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if (inputStream != null)
                result = tool.convertInputStreamToString(inputStream);
            else
                result = "Did not work!";
        } catch (IOException e) {
            e.printStackTrace();
        }


        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        //this.result = result;
        Log.e("Response", result.toString());
        res.onResponse(result,Method);


    }
}


