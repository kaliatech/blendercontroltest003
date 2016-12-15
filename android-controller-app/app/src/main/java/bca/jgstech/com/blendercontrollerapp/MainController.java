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
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.io.IOException;

import bca.jgstech.com.blendercontrollerapp.msgsender.MessageSender;
import bca.jgstech.com.blendercontrollerapp.utils.MathUtils;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class MainController implements SensorEventListener2 {

    private static String tag = MainController.class.getSimpleName();

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private TextView tfX;

    private MessageSender sender;

    private Vector3D prevRotation = new Vector3D(0, 0, 0);

    public MainController(Activity activity, MessageSender sender) {
        //mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mSensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

        tfX = (TextView) activity.findViewById(R.id.tfX);
        tfX.setText("...xx");

        this.sender = sender;
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

        Vector3D currRotation = new Vector3D(sensorEvent.values[0],
                                             sensorEvent.values[1],
                                             sensorEvent.values[2]);

        Vector3D rot = MathUtils.lowPass(prevRotation, currRotation);
        try {
            sender.sendRotation(rot);
        } catch (IOException e) {
            Log.d(tag, "Error sending rotation", e);
        }
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
