package com.example.test_emptybased_reboot_rc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends Activity {
    private static final int SERVERPORT = 80;

    // hardcoded IP, this should be set to the IP that the ESP32 sets gives itself
    public static String SERVER_IP = "192.168.43.225";

    private static boolean flag = true;

    private static boolean flag_ = true;

    private Accelerometer accelerometer;

    private Gyroscope gyroscope;

    public static Socket socket;


    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_main);

        

        if (flag) {

            // using this, the IP can be manually set
            /*
            AlertDialog.Builder builder = new AlertDialog.Builder((Context)this);
            builder.setTitle("IP");
            builder.setMessage("Please specify the IP to connect to");
            final EditText input_ = new EditText((Context)this);
            EditText editText = null;
            builder.setView((View)input_); // this either needs to be created here or this is an id in the mainactivity
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                    MainActivity.SERVER_IP = input_.getText().toString();

                }
            });
            builder.show();
            */

            // without this some errors showed up, i left it this way
            (new CountDownTimer(1000L, 1000L) {
                public void onFinish() {
                    (new Thread(new MainActivity.ClientThread())).start();
                }

                public void onTick(long param1Long) {}
            }).start();
            flag = false;
        }


        /*
        * the basic mechanism is simply using some Listeners on each of the buttons,
        * and then using the Socket's sendUrgentData method, which is one byte
        *
        * then at the receiving arduino end, corresponding action takes place
        * according to the send UrgentData
        *
        * java yelled at me when not using try-catch  */

        Button forwardButton = (Button) findViewById(R.id.Forward);
        Button backwardButton = (Button) findViewById(R.id.Backward);

        Button leftButton = (Button) findViewById(R.id.Left);
        Button rightButton = (Button) findViewById(R.id.Right);

        Button disconnectButton = (Button) findViewById(R.id.Disconnect);
        Button sensormodeButton = (Button) findViewById(R.id.Sensormode);

        forwardButton.setOnTouchListener(new OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    try {
                        MainActivity.socket.sendUrgentData(70);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        MainActivity.socket.sendUrgentData(87);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    try {
                        MainActivity.socket.sendUrgentData(88);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        MainActivity.socket.sendUrgentData(90);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        });

        backwardButton.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    try {
                        MainActivity.socket.sendUrgentData(66);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        MainActivity.socket.sendUrgentData(87);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    try {
                        MainActivity.socket.sendUrgentData(88);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        MainActivity.socket.sendUrgentData(90);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        });

        // 76, 84
        leftButton.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    try {
                        MainActivity.socket.sendUrgentData(76);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        MainActivity.socket.sendUrgentData(84);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    try {
                        MainActivity.socket.sendUrgentData(77);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        MainActivity.socket.sendUrgentData(68);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        });

        // 82, 84
        rightButton.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    try {
                        MainActivity.socket.sendUrgentData(82);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        MainActivity.socket.sendUrgentData(84);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    try {
                        MainActivity.socket.sendUrgentData(77);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        MainActivity.socket.sendUrgentData(68);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        });

        disconnectButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    try {
                        MainActivity.socket.sendUrgentData(68);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        MainActivity.socket.sendUrgentData(67);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        sensormodeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    /* this creates a new intent which starts the SensorActivity
                    *  pauses this, closes the socket here
                    *  and the SensorActivity will start another socket*/
                    Intent intent = new Intent(MainActivity.this, SensorActivity.class);
                    try {
                        MainActivity.socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    onPause();
                    startActivity(intent);
                }
            }
        });

        // some color changing based on the tilting of the phone, unused
        /*
        this.accelerometer = new Accelerometer((Context)this);
        this.gyroscope = new Gyroscope((Context)this);
        final ConstraintLayout linearLayout = (ConstraintLayout)findViewById(R.id.linearLayout);
        linearLayout.setBackgroundColor(-3355444);
        ((ConstraintLayout)findViewById(R.id.linearLayout)).setBackgroundColor(110);
        this.gyroscope.setListener(new Gyroscope.Listener() {
            public void onRotation(float param1Float1, float param1Float2, float param1Float3) throws IOException {
                if (param1Float2 > 1.7F) {
                    linearLayout.setBackgroundColor(-16711681);
                    MainActivity.socket.sendUrgentData(103);
                    MainActivity.socket.sendUrgentData(103);
                    MainActivity.socket.sendUrgentData(103);
                } else if (param1Float2 < -1.7F) {
                    linearLayout.setBackgroundColor(-12303292);
                    MainActivity.socket.sendUrgentData(121);
                    MainActivity.socket.sendUrgentData(121);
                    MainActivity.socket.sendUrgentData(121);
                }
            }
        });
         */

        // could be done with "flag" i guess, this okay for now
        if (flag_) {
            flag_ = false;
        } else {
            (new Thread(new ClientThread())).start();
        }


    }

    protected void onPause() {
        super.onPause();
        //this.accelerometer.unregister();
        //this.gyroscope.unregister();
    }

    protected void onResume() {
        super.onResume();
        //this.accelerometer.register();
        //this.gyroscope.register();
    }

    // the threaded client part
    /*
    * the inetAddress gets the global MainActivity.SERVER_IP
    * and using that ip, and port 80 it starts a simple socket server
    * */
    class ClientThread implements Runnable {
        public void run() {
            InetAddress inetAddress = null;
            try {
                InetAddress inetAddress1 = InetAddress.getByName(MainActivity.SERVER_IP);
                inetAddress = inetAddress1;
            } catch (UnknownHostException unknownHostException) {
                unknownHostException.printStackTrace();
            }
            try {
                MainActivity mainActivity = MainActivity.this;
                Socket socket = new Socket(inetAddress, 80);

                mainActivity.socket = socket;
            } catch (IOException iOException) {
                iOException.printStackTrace();
            }
        }
    }
}
