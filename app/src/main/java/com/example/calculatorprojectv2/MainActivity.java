package com.example.calculatorprojectv2;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    Button mainButton, nextButton;
    TextView instructionsLabel;
    private int stage = 0;
    private final UUID uuid = UUID.randomUUID();

    private static final int REQUEST_ENABLE_BT = 1;

    private final String[] stageLabels = {
            "You're bored in class one day, so you take our your calculator",
            "But you notice something's a little off about it...",
            "YOUR CALCULATOR IS BROKEN!",
            "But since you are the civilized person that you are",
            "Instead of panicking, you decide to take this opportunity to cure your boredom!",
            "Your goal is to get to the target button while staying under the button click limit.",
            "Press the Play Button to start!"
    };

    // Bluetooth is implemented by following
    // https://developer.android.com/guide/topics/connectivity/bluetooth
    // So there's a lot of stuff that I'm missing out on as I'm just copy pasting code
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainButton = findViewById(R.id.mainButton);
        nextButton = findViewById(R.id.nextButton);

        instructionsLabel = findViewById(R.id.instructionLabelOne);

        mainButton.setOnClickListener(view -> openSelectGameType());

        nextButton.setOnClickListener(view -> {
            instructionsLabel.setText(stageLabels[stage]);
            if (stage == stageLabels.length - 1) {
                nextButton.setVisibility(View.GONE);
                mainButton.setVisibility(View.VISIBLE);
            }
            stage++;
        });

        checkForBluetoothPermission();

        // Register for broadcasts when a device is discovered.


        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);

        AcceptThread acceptThread = new AcceptThread();
        acceptThread.start();
    }

    // TODO: LOOK THROUGH THIS: https://github.com/android/connectivity-samples

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
            }
        }
    };

//    public void openLevelOne(){
//        Intent intent = new Intent(this, LevelOneActivity.class);
//        startActivity(intent);
//    }

    public void openSelectGameType(){
        Intent intent = new Intent(this, GameTypeActivity.class);
        startActivity(intent);
    }

    //FIX CODE TO READ DATA TMRW!!! sds

//    private void handleLoadPrevScores(ActionEvent event) {
//        try{
//            FileReader reader = new FileReader("src/main/res/user_scores.txt");
//            Scanner in = new Scanner(reader);
//            while(in.hasNextLine())
//            {
//                String temp = in.nextLine();
//            }
//        } catch (FileNotFoundException ex) {
//            System.out.println("SOMETHING HAS GONE HORRIBLY WRONG WE'RE ALL GONNA DIE!");
//        }
//    }
//
//    private void handleSaveNewScore(ActionEvent event){
//        String outFile = "src/main/res/user_scores.txt";
//        try {
//            PrintWriter out = new PrintWriter(outFile);
//            out.close();
//
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
//            System.out.println("Something went wrong!");
//        }
//    }

    // TODO: Set up Bluetooth stuff
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                    Toast.makeText(getApplicationContext(),
                            "Thanks for turning on Bluetooth!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                    Toast.makeText(getApplicationContext(),
                            "You need Bluetooth (which requires location data) for the Collaborative Mode",
                            Toast.LENGTH_SHORT).show();
                }
            });

    public void checkForBluetoothPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            checkForPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            checkForPermission(Manifest.permission.BLUETOOTH);
        }
    }

    public void checkForPermission(String permission) {
        System.out.println("permission=: " + permission);
        if (ContextCompat.checkSelfPermission(getBaseContext(), permission) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            Toast.makeText(getApplicationContext(),
                    "Thanks for turning on " + permission + "!",
                    Toast.LENGTH_SHORT).show();
        } else {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            requestPermissionLauncher.launch(permission);
        }
    }

    public BluetoothAdapter bluetoothAdapter;

    public void setUpBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            Toast.makeText(getApplicationContext(),
                    "Your device doesn't support Bluetooth",
                    Toast.LENGTH_SHORT).show();
        }

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
            }
        }
        
        int requestCode = 1;
        Intent discoverableIntent =
                new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivityForResult(discoverableIntent, requestCode);
    }

    private class AcceptThread extends Thread {
        private static final String TAG = "ACCEPT_THREAD";
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            // Use a temporary object that is later assigned to mmServerSocket
            // because mmServerSocket is final.
            BluetoothServerSocket tmp = null;
            try {
                // MY_UUID is the app's UUID string, also used by the client code.
                tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord("CalculatorProjectServer",
                        uuid);
            } catch (IOException e) {
                Log.e(TAG, "Socket's listen() method failed", e);
            }
            mmServerSocket = tmp;
        }

        public void run() {
            BluetoothSocket socket = null;
            // Keep listening until exception occurs or a socket is returned.
            while (true) {
                try {
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    Log.e(TAG, "Socket's accept() method failed", e);
                    break;
                }

                if (socket != null) {
                    // A connection was accepted. Perform work associated with
                    // the connection in a separate thread.
                    manageMyConnectedSocket(socket);
                    try {
                        mmServerSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }

        // Closes the connect socket and causes the thread to finish.
        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }
    }

    public void manageMyConnectedSocket(BluetoothSocket socket) {
        System.out.println("socket = " + socket);
    }

    private class ConnectThread extends Thread {
        private static final String TAG = "CONNECT_THREAD";
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket
            // because mmSocket is final.
            BluetoothSocket tmp = null;
            mmDevice = device;

            try {
                // Get a BluetoothSocket to connect with the given BluetoothDevice.
                // MY_UUID is the app's UUID string, also used in the server code.
                tmp = device.createRfcommSocketToServiceRecord(uuid);
            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it otherwise slows down the connection.
            bluetoothAdapter.cancelDiscovery();

            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and return.
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    Log.e(TAG, "Could not close the client socket", closeException);
                }
                return;
            }

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            manageMyConnectedSocket(mmSocket);
        }

        // Closes the client socket and causes the thread to finish.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the client socket", e);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(receiver);
    }
}
