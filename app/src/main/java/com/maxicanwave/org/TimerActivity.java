package com.maxicanwave.org;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.TriggerEventListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.maxicanwave.manager.RequestManagerPost;
import com.maxicanwave.modal.GroupMember;
import com.maxicanwave.modal.Response;
import com.maxicanwave.util.URLMaxicanWave;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TimerActivity extends Activity implements SensorEventListener {

    private static final int SHAKE_THRESHOLD = 600;
    ArrayList<Float> heightlist = new ArrayList<Float>();
    ArrayList<Float> zSpeedList = new ArrayList<Float>();
    ArrayList<Float> heightMax =  new ArrayList<Float>();
    ArrayList<Double> motionMax =  new ArrayList<Double>();
    long timeInMillies = 0L;
    long timeSwap = 0L;
    long finalTime = 0L;
    private SensorManager senSensorManager;
    private Sensor senPressure, senMotion;
    private TriggerEventListener mTriggerEventListener;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private TextView textTimer;
    private long startTime = 0L;
    private Handler myHandler = new Handler();
    private GroupMember grpm = new GroupMember();
    private Response response = new Response();
    private Runnable updateTimerMethod = new Runnable() {

        public void run() {
            timeInMillies = SystemClock.uptimeMillis() - startTime;
            finalTime = timeSwap + timeInMillies;

            int seconds = (int) (finalTime / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            int milliseconds = (int) (finalTime % 1000);
            textTimer.setText("" + minutes + ":"
                    + String.format("%02d", seconds));

            myHandler.postDelayed(this, 0);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_activity);
        startTime = SystemClock.uptimeMillis();
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senPressure = senSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        senMotion = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senPressure, SensorManager.SENSOR_DELAY_NORMAL);
        senSensorManager.registerListener(this, senMotion, SensorManager.SENSOR_DELAY_NORMAL);
        textTimer = (TextView) findViewById(R.id.textView4);
        startTime = SystemClock.uptimeMillis();
        myHandler.postDelayed(updateTimerMethod, 0);
        grpm = (GroupMember) getIntent().getExtras().getSerializable("group");
        Log.d("User ID", "" + grpm.getU_id());
        response.setId(grpm.getU_id());


    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // when pressure value is changed, this method will be called.
        float pressure_value = 0.0f;
        float height = 0.0f;

        // if you use this listener as listener of only one sensor (ex, Pressure), then you don't need to check sensor type.
        if (Sensor.TYPE_PRESSURE == event.sensor.getType()) {
            pressure_value = event.values[0];
            height = SensorManager.getAltitude(SensorManager.PRESSURE_STANDARD_ATMOSPHERE, pressure_value);
            heightlist.add(height);
        }

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;
            }
            zSpeedList.add(z);


            validateStatusChange(heightlist, zSpeedList);


        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senPressure, SensorManager.SENSOR_DELAY_NORMAL);
        //senSensorManager.requestTriggerSensor(mTriggerEventListener, senMotion);
        senSensorManager.registerListener(this, senMotion, SensorManager.SENSOR_DELAY_NORMAL);

    }

    public void validateStatusChange(ArrayList<Float> height, ArrayList<Float> motion) {

        float max_motion = 0, min_motion = 0;
        float max_height = 0, min_height = 0;
        if (!motion.isEmpty() && motion.size() > 0) {
            min_motion = getMin(motion);
            max_motion = getMax(motion);
        }
        if (!height.isEmpty() && height.size() > 0) {
            max_height = getMax(height);
            min_height = getMin(height);
        }

        float heigth_diff = max_height - min_height;
        double motion_diff = max_motion - min_motion;
        heightMax.add(heigth_diff);
        motionMax.add(motion_diff);
        heightlist.clear();
        zSpeedList.clear();
        heightlist.add(max_height);
        heightlist.add(min_height);

        zSpeedList.add(max_motion);
        zSpeedList.add(min_height);
        timeSwap += timeInMillies;

            if (heigth_diff > 2 && motion_diff > min_motion) {
                myHandler.removeCallbacks(updateTimerMethod);
                onPause();
                updateTask(grpm);

            } else if (heigth_diff <= 0 && motion_diff > min_motion * 2) {
                myHandler.removeCallbacks(updateTimerMethod);
                onPause();
                updateTask(grpm);

            }
    }


    public double average(List<Float> list) {
        // 'average' is undefined if there are no elements in the list.
        if (list == null || list.isEmpty())
            return 0.0;
        // Calculate the summation of the elements in the list
        long sum = 0;
        int n = list.size();
        // Iterating manually is faster than using an enhanced for loop.
        for (int i = 0; i < n; i++)
            sum += list.get(i);
        // We don't want to perform an integer division, so the cast is mandatory.
        return ((double) sum) / n;
    }


    public float getMax(ArrayList<Float> arrayList) {
        return Collections.max(arrayList);
    }

    public float getMin(ArrayList<Float> arrayList) {
        return Collections.min(arrayList);
    }

    public void updateTask(GroupMember grpm)
    {
        grpm.setTask_status(3);
        grpm.setTask(1);
        Gson gson = new Gson();
        String taskJson = gson.toJson(grpm);
        Intent intent =  new Intent(this, TaskActivity.class);
        intent.putExtra("group", grpm);
        this.startActivity(intent);
    }
}
