package com.example.negru.discolightsled;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private Button btnOn,btnG;
    private EditText RGBColor;
    private SeekBar sbRed,sbGreen,sbBlue;
    private ProgressDialog progress;
    private BluetoothAdapter myBluetooth = null;
    private BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private int RedValue,GreenValue,BlueValue;
    private String address = null;
    private Drawable thumbGreen,thumbBlue,thumbRed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //receive the address of the bluetooth device
        Intent newint = getIntent();
        address = newint.getStringExtra(DeviceList.EXTRA_ADDRESS);

        //view of the ledControl layout
        setContentView(R.layout.activity_main);
        //call the widgtes
        btnOn = (Button) findViewById(R.id.button2);
        btnG = (Button) findViewById(R.id.btnG);

        RGBColor = (EditText) findViewById(R.id.editText2);

        sbRed = (SeekBar) findViewById(R.id.seekBar2);
        sbGreen = (SeekBar) findViewById(R.id.seekBar3);
        sbBlue = (SeekBar) findViewById(R.id.seekBar4);

        new ConnectBT().execute();

        btnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btSocket != null) {
                    try {
                        btSocket.getOutputStream().write(("1").getBytes());
                        //btSocket.getOutputStream().write((RGBColor.getText().toString()).getBytes());
                    } catch (IOException e) {
                        msg("Error");
                    }

                }
            }
        });


        btnG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btSocket != null) {
                    try {
                        RedValue = sbRed.getProgress();
                        BlueValue = sbBlue.getProgress();
                        GreenValue = sbGreen.getProgress();
                        btSocket.getOutputStream().write(("2").getBytes());
                        //btSocket.getOutputStream().write((String.valueOf(RedValue) + ',' + String.valueOf(GreenValue) + ',' + String.valueOf(BlueValue)).getBytes());
                        //btSocket.getOutputStream().write((RGBColor.getText().toString()).getBytes());
                    } catch (IOException e) {
                        msg("Error");
                    }

                }
            }
        });
        sbRed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int lastProgress = 0;
            Timer endTimer = null;
            boolean timerExpired = false;

            class SeekBarTimerTask extends TimerTask {
                public void run() {
                    timerExpired = true;
                }
            }

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (timerExpired == false) {
                    // clean up the previous timer and start a new one
                    setTimer();
                } else {
                    // if the timer has expired, do not honor small variations of 2%
                    if (Math.abs(progress - lastProgress) <= seekBar.getMax() * 0.02) {
                        seekBar.setProgress(lastProgress);
                        return;
                    } else {
                        // The variation is not small. This means that the seekbar position change
                        // was intentional. Honor it and schedule the timer again.
                        setTimer();
                    }
                }


                lastProgress = progress;

            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                timerExpired = false;
                endTimer.cancel();
                endTimer.purge();
            }

            private void setTimer() {
                if (endTimer != null) {
                    endTimer.cancel();
                    endTimer.purge();
                }

                timerExpired = false;
                endTimer = new Timer();
                endTimer.schedule(new SeekBarTimerTask(), 300);
            }
        });


        sbBlue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int lastProgress = 0;
            Timer endTimer = null;
            boolean timerExpired = false;

            class SeekBarTimerTask extends TimerTask {
                public void run() {
                    timerExpired = true;
                }
            }

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (timerExpired == false) {
                    // clean up the previous timer and start a new one
                    setTimer();
                } else {
                    // if the timer has expired, do not honor small variations of 2%
                    if (Math.abs(progress - lastProgress) <= seekBar.getMax() * 0.02) {
                        seekBar.setProgress(lastProgress);
                        return;
                    } else {
                        // The variation is not small. This means that the seekbar position change
                        // was intentional. Honor it and schedule the timer again.
                        setTimer();
                    }
                }


                lastProgress = progress;

            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                timerExpired = false;
                endTimer.cancel();
                endTimer.purge();
            }

            private void setTimer() {
                if (endTimer != null) {
                    endTimer.cancel();
                    endTimer.purge();
                }

                timerExpired = false;
                endTimer = new Timer();
                endTimer.schedule(new SeekBarTimerTask(), 300);
            }
        });


        sbGreen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int lastProgress = 0;
            Timer endTimer = null;
            boolean timerExpired = false;

            class SeekBarTimerTask extends TimerTask {
                public void run() {
                    timerExpired = true;
                }
            }

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (timerExpired == false) {
                    // clean up the previous timer and start a new one
                    setTimer();
                } else {
                    // if the timer has expired, do not honor small variations of 2%
                    if (Math.abs(progress - lastProgress) <= seekBar.getMax() * 0.02) {
                        seekBar.setProgress(lastProgress);
                        return;
                    } else {
                        // The variation is not small. This means that the seekbar position change
                        // was intentional. Honor it and schedule the timer again.
                        setTimer();
                    }
                }


                lastProgress = progress;

             }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                timerExpired = false;
                endTimer.cancel();
                endTimer.purge();
            }

            private void setTimer() {
                if (endTimer != null) {
                    endTimer.cancel();
                    endTimer.purge();
                }

                timerExpired = false;
                endTimer = new Timer();
                endTimer.schedule(new SeekBarTimerTask(), 300);
            }
        });
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(MainActivity.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            }
            else
            {
                msg("Connected.");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }

    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

    private void Disconnect()
    {
        if (btSocket!=null) //If the btSocket is busy
        {
            try
            {
                btSocket.close(); //close connection
            }
            catch (IOException e)
            { msg("Error");}
        }
        finish(); //return to the first layout
    }

    private void turnOnLed()
    {
        if (btSocket!=null)
    {
        try
        {
            RedValue = sbRed.getProgress();
            BlueValue = sbBlue.getProgress();
            GreenValue = sbGreen.getProgress();
            btSocket.getOutputStream().write((String.valueOf(RedValue)+','+String.valueOf(GreenValue)+','+String.valueOf(BlueValue)).getBytes());
            //btSocket.getOutputStream().write((RGBColor.getText().toString()).getBytes());
        }
        catch (IOException e)
        {
            msg("Error");
        }
    }
    }

}
