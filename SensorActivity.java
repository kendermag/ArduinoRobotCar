package com.example.test_emptybased_reboot_rc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/*
* mainly unused butusable in the application activity, leftover from the previous revision of the project
* this, the Accelerometer and Gyroscope classes are to be deleted
* */

public class SensorActivity extends AppCompatActivity {
    public static boolean AAAA = true;

    private static final int SERVERPORT = 80;

    private static String SERVER_IP = MainActivity.SERVER_IP;

    public String VISSZA_SERVER_IP = SERVER_IP;

    private Accelerometer accelerometer;

    private Gyroscope gyroscope;

    private Socket socket;

    public void handleButtonClicked(View paramView) throws IOException {
        if (((Button)paramView).getText().toString().equals("Back to main")) {
            Intent intent = new Intent((Context)this, MainActivity.class);
            onPause();
            this.socket.close();
            onPause();
            startActivity(intent);
        }
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_sensor);
        (new Thread(new ClientThread())).start();
        this.accelerometer = new Accelerometer((Context)this);
        Gyroscope gyroscope = new Gyroscope((Context)this);
        this.gyroscope = gyroscope;
        gyroscope.setListener(new Gyroscope.Listener() {
            public void onRotation(float param1Float1, float param1Float2, float param1Float3) throws IOException {
                if (param1Float2 > 2.7F) {
                    SensorActivity.this.socket.sendUrgentData(70);
                    SensorActivity.this.socket.sendUrgentData(87);
                } else if (param1Float2 < -2.7F) {
                    SensorActivity.this.socket.sendUrgentData(66);
                    SensorActivity.this.socket.sendUrgentData(87);
                }
            }
        });
    }

    protected void onPause() {
        super.onPause();
        this.accelerometer.unregister();
        this.gyroscope.unregister();
    }

    protected void onResume() {
        super.onResume();
        this.accelerometer.register();
        this.gyroscope.register();
    }

    // threaded client part
    class ClientThread implements Runnable {
        public void run() {
            InetAddress inetAddress = null;
            try {
                InetAddress inetAddress1 = InetAddress.getByName(SensorActivity.SERVER_IP);
                inetAddress = inetAddress1;
            } catch (UnknownHostException unknownHostException) {
                unknownHostException.printStackTrace();
            }
            try {
                SensorActivity sensorActivity = SensorActivity.this;
                Socket socket = new Socket(inetAddress, 80);

                sensorActivity.socket = socket;  //.access$002(sensorActivity, socket);
            } catch (IOException iOException) {
                iOException.printStackTrace();
            }
        }
    }
}