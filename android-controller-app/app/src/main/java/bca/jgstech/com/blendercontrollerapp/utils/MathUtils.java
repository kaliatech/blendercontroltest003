package bca.jgstech.com.blendercontrollerapp.utils;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class MathUtils {


    /**
     * Time smoothing constant for low-pass filter
     * 0 ≤ alpha ≤ 1 ; a smaller value basically means more smoothing
     * See: http://en.wikipedia.org/wiki/Low-pass_filter#Discrete-time_realization
     */
    static final float ALPHA = 0.15f;

    /**
     * @see http://en.wikipedia.org/wiki/Low-pass_filter#Algorithmic_implementation
     * @see http://developer.android.com/reference/android/hardware/SensorEvent.html#values
     */
    public static Vector3D lowPass(Vector3D prev, Vector3D curr) {
        double x = curr.getX() + ALPHA * (prev.getX() - curr.getX());
        double y = curr.getY() + ALPHA * (prev.getY() - curr.getY());
        double z = curr.getZ() + ALPHA * (prev.getZ() - curr.getZ());

        return new Vector3D(x, y, z);
    }
}
