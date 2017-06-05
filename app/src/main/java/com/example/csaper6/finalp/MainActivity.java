package com.example.csaper6.finalp;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bluelinelabs.logansquare.LoganSquare;
import com.peak.salut.Callbacks.SalutCallback;
import com.peak.salut.Callbacks.SalutDataCallback;
import com.peak.salut.Callbacks.SalutDeviceCallback;
import com.peak.salut.Salut;
import com.peak.salut.SalutDataReceiver;
import com.peak.salut.SalutDevice;
import com.peak.salut.SalutServiceData;

import java.io.IOException;
import java.security.KeyPair;


public class MainActivity extends AppCompatActivity implements SalutDataCallback{

    private TextView encryptedMessageText, messageText;
    private EditText three;
    private Button button, buttonHost, buttonJoin,buttonSend;
    private String encryptedMessage, messageToSend, publicKey;

    private boolean isHost=false;
    public static final String TAG = MainActivity.class.getSimpleName();
    private Salut network;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        encryptedMessageText = (TextView) findViewById(R.id.one);
        messageText = (TextView) findViewById(R.id.two);
        three = (EditText) findViewById(R.id.three);
        button = (Button) findViewById(R.id.button);
        buttonHost = (Button) findViewById(R.id.button_host);
        buttonJoin = (Button) findViewById(R.id.button_join);
        buttonSend = (Button)findViewById(R.id.button_send);
        //setOnClickListeners();
        SalutDataReceiver dataReceiver = new SalutDataReceiver(MainActivity.this, MainActivity.this);
        SalutServiceData serviceData = new SalutServiceData("sas", 50489, "hello");

        network = new Salut(dataReceiver, serviceData, new SalutCallback() {
            @Override
            public void call() {
                Log.e(TAG, "Sorry, but this device does not support WiFi Direct.");
            }
        });



        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if(adapter != null){
            Toast.makeText(MainActivity.this, "done", Toast.LENGTH_SHORT).show();
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rsa hello = new rsa();
                KeyPair key = hello.MakeKeys();
                byte[] message = hello.enCrypt(key.getPublic(), three.getText().toString());
//                String inputMessage = hello.decipher(message, key.getPrivate());
//                messageText.setText(inputMessage);
                encryptedMessageText.setText(key.getPublic().toString());
                messageToSend = new String(message);
                publicKey = key.getPublic().toString();
//                salutTry newNetwork = new salutTry();
//                newNetwork.joinNetwork();






            }
        });
        buttonHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isHost = true;
                hostNetwork();
            }
        });
        buttonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isHost = false;
                joinNetwork();

            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                sendMessage();

            }
        });
    }

//    private void setOnClickListeners() {
//        buttonJoin.setOnClickListener((View.OnClickListener) this);
//        button.setOnClickListener((View.OnClickListener) this);
//        buttonHost.setOnClickListener((View.OnClickListener) this);
//        buttonSend.setOnClickListener((View.OnClickListener) this);
//    }


//    private void salut() {
//        setContentView(R.layout.activity_salut_try);
//
//        SalutDataReceiver dataReceiver = new SalutDataReceiver(MainActivity.this, MainActivity.this);
//        SalutServiceData serviceData = new SalutServiceData("sas", 50489, "hello");
//
//        Salut network = new Salut(dataReceiver, serviceData, new SalutCallback() {
//            @Override
//            public void call() {
//                Log.e(TAG, "Sorry, but this device does not support WiFi Direct.");
//            }
//        });
//
//
//    }


    @Override
    public void onDataReceived(Object data) {
        Log.d(TAG, "Received network data.");
        try
        {
            Message newMessage = LoganSquare.parse(data.toString(),Message.class);
            Log.d(TAG, newMessage.description);  //See you on the other side!
            //Do other stuff with data.
            encryptedMessage = newMessage.description;
        }
        catch (IOException ex)
        {
            Log.e(TAG, "Failed to parse network data.");
        }
    }


    public void sendMessage()
    {
        Message myMessage = new Message();
        myMessage.description = "Encrypted Message: " + messageToSend + " \n"
        + "Public key: ";

        network.sendToAllDevices(myMessage, new SalutCallback() {
            @Override
            public void call() {
                Log.e(TAG, "Oh no! The data failed to send.");
            }
        });

    }


    protected void hostNetwork()
    {
        network.startNetworkService(new SalutDeviceCallback() {
            @Override
            public void call(SalutDevice device) {
                Log.d(TAG, device.readableName + " has connected!");
            }
        });
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

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(MainActivity.this.isHost)
            network.stopNetworkService(true);
        else
            network.unregisterClient(Boolean.parseBoolean(null));
    }


}













//region Description
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                KeyPairGenerator kpg = null;
//                try {
//                    kpg = KeyPairGenerator.getInstance("RSA");
//                } catch (NoSuchAlgorithmException e) {
//                    e.printStackTrace();
//                }
//                kpg.initialize(1024);
//                KeyPair kp = kpg.genKeyPair();
//                Key publicKey = kp.getPublic();
//                Key privateKey = kp.getPrivate();
//
//                KeyFactory fact = null;
//                try {
//                    fact = KeyFactory.getInstance("RSA");
//                } catch (NoSuchAlgorithmException e) {
//                    e.printStackTrace();
//                }
//
//                RSAPublicKeySpec pub = null;
//                try {
//                    pub = fact.getKeySpec(publicKey, RSAPublicKeySpec.class);
//                } catch (InvalidKeySpecException e) {
//                    e.printStackTrace();
//                }
//
//                RSAPrivateKeySpec priv = null;
//                try {
//                    priv = fact.getKeySpec(privateKey, RSAPrivateKeySpec.class);
//                } catch (InvalidKeySpecException e) {
//                    e.printStackTrace();
//                }
//
//
//                // Get an instance of the Cipher for RSA encryption/decryption
//                Cipher c = null;
//                try {
//                    c = Cipher.getInstance("RSA");
//                } catch (NoSuchAlgorithmException e) {
//                    e.printStackTrace();
//                } catch (NoSuchPaddingException e) {
//                    e.printStackTrace();
//                }
//// Initiate the Cipher, telling it that it is going to Encrypt, giving it the public key
//                try {
//                    c.init(Cipher.ENCRYPT_MODE, publicKey);
//                } catch (InvalidKeyException e) {
//                    e.printStackTrace();
//                }
//
//                String myMessage = three.getText().toString();
//                SealedObject myEncryptedMessage= null;
//                try {
//                    myEncryptedMessage = new SealedObject( myMessage, c);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (IllegalBlockSizeException e) {
//                    e.printStackTrace();
//                }
//
//                Cipher dec = null;
//                try {
//                    dec = Cipher.getInstance("RSA");
//                } catch (NoSuchAlgorithmException e) {
//                    e.printStackTrace();
//                } catch (NoSuchPaddingException e) {
//                    e.printStackTrace();
//                }
//// Initiate the Cipher, telling it that it is going to Decrypt, giving it the private key
//                try {
//                    dec.init(Cipher.DECRYPT_MODE, privateKey);
//                } catch (InvalidKeyException e) {
//                    e.printStackTrace();
//                }
//                String message = null;
//                try {
//                    message = (String) myEncryptedMessage.getObject(dec);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IllegalBlockSizeException e) {
//                    e.printStackTrace();
//                } catch (BadPaddingException e) {
//                    e.printStackTrace();
//                }
//
//
//
//                encryptedMessageText.setText(priv.getPrivateExponent().toString());
//                messageText.setText(message);
//
//
//            }
//        });
//
//endregion

