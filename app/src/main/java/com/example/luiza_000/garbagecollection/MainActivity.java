package com.example.luiza_000.garbagecollection;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private byte[] bytes;
    private byte[][] bytesArray;
    private int times = 100;
    private int nBytes = 1048576;
    private int sharedCurrent = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedCurrent = 0;

        final Thread threadForget = new Thread(new Runnable() {
            @Override
            public void run() {
                int current = 0;

                while(current < times) {
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    bytes = new byte[nBytes];
                    bytes = null;
                    current++;
                    Log.i("GCTest", "Allocted " + nBytes + " bytes");
                }

            }
        });

        final Thread[] threadAccess = new Thread[2];
        threadAccess[0] = new Thread(new Runnable() {
            @Override
            public void run() {

                while(sharedCurrent < times) {

                    if(sharedCurrent > 1)
                    {
                        for(int i = 0; i < sharedCurrent-1; i++)
                        {
                            for(int j = 0; j < nBytes; j++)
                            {
                                bytesArray[i][j]++;
                            }
                        }
                    }
                }

            }
        });
        threadAccess[1] = new Thread(new Runnable() {
            @Override
            public void run() {

                while(sharedCurrent < times) {

                    if(sharedCurrent > 1)
                    {
                        for(int i = sharedCurrent-2; i >= 0; i--)
                        {
                            for(int j = 0; j < nBytes; j++)
                            {
                                bytesArray[i][j]++;
                            }
                        }
                    }
                }

            }
        });


        final Thread threadStore = new Thread(new Runnable() {
            @Override
            public void run() {

                bytesArray = new byte[times][];


                while(sharedCurrent < times) {
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    bytesArray[sharedCurrent] = new byte[nBytes];
                    Log.i("GCTest", "Allocted " + nBytes + " bytes");
                    sharedCurrent++;
                }

            }
        });

        Button bForget = findViewById(R.id.btn_forget);
        bForget.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                threadForget.start();
            }
        });


        Button bStore = findViewById(R.id.btn_store);
        bStore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                threadStore.start();
            }
        });


        Button bAccess = findViewById(R.id.btn_access);
        bAccess.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                threadAccess[0].start();
                threadAccess[1].start();
                threadStore.start();
            }
        });


        Button bKill = findViewById(R.id.btn_kill);
        bKill.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finishAndRemoveTask();
            }
        });



    }

    public void getInfoFromText(){

        EditText txtBytes = findViewById(R.id.txtBytes);
        nBytes = Integer.parseInt(txtBytes.getText().toString());

        EditText txtTimes = findViewById(R.id.txtTimes);
        times = Integer.parseInt(txtTimes.getText().toString());


    }


}
