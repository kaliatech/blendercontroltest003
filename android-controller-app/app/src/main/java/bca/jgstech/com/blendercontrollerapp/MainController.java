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
    private Sensor mSensorAccel;
    private Sensor mSensorMag;
    private TextView tfX;

    private MessageSender sender;

    private Vector3D prevRotation = new Vector3D(0, 0, 0);


    private final float[] mAccelerometerReading = new float[3];
    private final float[] mMagnetometerReading = new float[3];

    private final float[] mRotationMatrix = new float[9];
    private final float[] mOrientationAngles = new float[3];

    public MainController(Activity activity, MessageSender sender) {
        //mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mSensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        mSensorAccel = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorMag = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        tfX = (TextView) activity.findViewById(R.id.tfX);
        tfX.setText("...");

        this.sender = sender;
    }

    @Override
    public void onFlushCompleted(Sensor sensor) {
        Log.d(tag, "onFlushCompleted");
    }

    private boolean receivedOne = false;

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(sensorEvent.values, 0, mAccelerometerReading,
                             0, mAccelerometerReading.length);
            receivedOne = true;
        }
        else if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(sensorEvent.values, 0, mMagnetometerReading,
                             0, mMagnetometerReading.length);
            if (receivedOne) {
                receivedOne = false;
                updateOrientationAngles();
            }
        }

    }


    // Compute the three orientation angles based on the most recent readings from
    // the device's accelerometer and magnetometer.
    public void updateOrientationAngles() {
        // Update rotation matrix, which is needed to update orientation angles.
        mSensorManager.getRotationMatrix(mRotationMatrix, null,
                                         mAccelerometerReading, mMagnetometerReading);

        // "mRotationMatrix" now has up-to-date information.

        mSensorManager.getOrientation(mRotationMatrix, mOrientationAngles);

        // "mOrientationAngles" now has up-to-date information.


        String vals = TextUtils.join(",", ArrayUtils.toObject(mOrientationAngles));
        Log.d(tag, "values:" + vals);
        tfX.setText(vals);

        Vector3D currRotation = new Vector3D(mOrientationAngles[0],
                                             mOrientationAngles[1],
                                             mOrientationAngles[2]);

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

        // Get updates from the accelerometer and magnetometer at a constant rate.
        // To make batch operations more efficient and reduce power consumption,
        // provide support for delaying updates to the application.
        //
        // In this example, the sensor reporting delay is small enough such that
        // the application receives an update before the system checks the sensor
        // readings again.
//        mSensorManager.registerListener(this, mSensorAccel,
//                                        SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
//        mSensorManager.registerListener(this, mSensorMag,
//                                        SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);

        mSensorManager.registerListener(this, mSensorAccel,
                                        SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mSensorMag,
                                        SensorManager.SENSOR_DELAY_NORMAL);


    }

    protected void onPause() {
        Log.d(tag, "onPause");
        log.debug("onPause2");
        mSensorManager.unregisterListener(this);
    }

}
