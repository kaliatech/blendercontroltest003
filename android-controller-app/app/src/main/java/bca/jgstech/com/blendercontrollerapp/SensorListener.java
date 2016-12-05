package bca.jgstech.com.blendercontrollerapp;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class SensorListener implements SensorEventListener2 {

    private static String tag = SensorListener.class.getSimpleName();

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private TextView tfX;

    public SensorListener(Activity activity) {
        //mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mSensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

        tfX = (TextView) activity.findViewById(R.id.tfX);
        tfX.setText("...");

        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onFlushCompleted(Sensor sensor) {
        Log.d(tag, "onFlushCompleted");
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        String vals = TextUtils.join(",", ArrayUtils.toObject(sensorEvent.values));
        Log.d(tag, "onSensorChanged:" + vals);
        tfX.setText(vals);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        Log.d(tag, "onAccuracyChanged");
    }


    protected void onResume() {
        Log.d(tag, "onResume");
        log.debug("onResume2");
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        Log.d(tag, "onPause");
        log.debug("onPause2");
        mSensorManager.unregisterListener(this);
    }

}