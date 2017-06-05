package com.example.csaper6.finalp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.peak.salut.Callbacks.SalutCallback;
import com.peak.salut.Callbacks.SalutDataCallback;
import com.peak.salut.Callbacks.SalutDeviceCallback;
import com.peak.salut.Salut;
import com.peak.salut.SalutDataReceiver;
import com.peak.salut.SalutDevice;
import com.peak.salut.SalutServiceData;

public class SalutActivity extends AppCompatActivity implements SalutDataCallback{
    private Salut network;

    public static final String TAG = SalutActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salut_try);


        SalutDataReceiver dataReceiver = new SalutDataReceiver(SalutActivity.this, SalutActivity.this);
        SalutServiceData serviceData = new SalutServiceData("sas", 50489, "hello");

        Salut network = new Salut(dataReceiver, serviceData, new SalutCallback() {
            @Override
            public void call() {
                Log.e(TAG, "Sorry, but this device does not support WiFi Direct.");
            }
        });





    }
    protected void hostNetwork()
    {
        network.startNetworkService(new SalutDeviceCallback() {
            @Override
            public void call(SalutDevice device) {
                Log.d(TAG, device.readableName + " has connected!");
                Toast.makeText(SalutActivity.this, "yay", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onDataReceived(Object o) {

    }
    protected void joinNetwork() {
        network.discoverNetworkServices(new SalutDeviceCallback() {
            @Override
            public void call(SalutDevice device) {
                Log.d(TAG, "A device has connected with the name " + device.deviceName);
                network.registerWithHost(device, new SalutCallback() {
                    @Override
                    public void call() {
                        Log.d(TAG, "We're now registered.");
                    }
                }, new SalutCallback() {
                    @Override
                    public void call() {
                        Log.d(TAG, "We failed to register.");
                    }
                });
            }
        }, false);
}}
