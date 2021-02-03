package com.example.test_emptybased_reboot_rc;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class Accelerometer {
    private Listener listener;

    private Sensor sensor;

    private SensorEventListener sensorEventListener;

    private SensorManager sensorManager;

    Accelerometer(Context paramContext) {
        SensorManager sensorManager = (SensorManager)paramContext.getSystemService(Context.SENSOR_SERVICE);
        this.sensorManager = sensorManager;
        this.sensor = sensorManager.getDefaultSensor(10);
        this.sensorEventListener = new SensorEventListener() {
            public void onAccuracyChanged(Sensor param1Sensor, int param1Int) {}

            public void onSensorChanged(SensorEvent param1SensorEvent) {
                if (Accelerometer.this.listener != null)
                    Accelerometer.this.listener.onTranslation(param1SensorEvent.values[0], param1SensorEvent.values[1], param1SensorEvent.values[2]);
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
        void onTranslation(float param1Float1, float param1Float2, float param1Float3);
    }
}
