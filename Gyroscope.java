package com.example.test_emptybased_reboot_rc;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import java.io.IOException;

public class Gyroscope {
    private Listener listener;

    private Sensor sensor;

    private SensorEventListener sensorEventListener;

    private SensorManager sensorManager;

    Gyroscope(Context paramContext) {
        SensorManager sensorManager = (SensorManager)paramContext.getSystemService(Context.SENSOR_SERVICE);
        this.sensorManager = sensorManager;
        this.sensor = sensorManager.getDefaultSensor(4);
        this.sensorEventListener = new SensorEventListener() {
            public void onAccuracyChanged(Sensor param1Sensor, int param1Int) {}

            public void onSensorChanged(SensorEvent param1SensorEvent) {
                if (Gyroscope.this.listener != null)
                    try {
                        Gyroscope.this.listener.onRotation(param1SensorEvent.values[0], param1SensorEvent.values[1], param1SensorEvent.values[2]);
                    } catch (IOException iOException) {
                        iOException.printStackTrace();
                    }
            }
        };
    }

    public void register() {
        this.sensorManager.registerListener(this.sensorEventListener, this.sensor, 3);
    }

    public void setListener(Listener paramListener) {
        this.listener = paramListener;
    }

    public void unregister() {
        this.sensorManager.unregisterListener(this.sensorEventListener);
    }

    public static interface Listener {
        void onRotation(float param1Float1, float param1Float2, float param1Float3) throws IOException;
    }
}
