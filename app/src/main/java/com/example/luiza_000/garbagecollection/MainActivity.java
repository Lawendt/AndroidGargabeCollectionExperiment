package com.example.luiza_000.garbagecollection;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;



public class MainActivity extends AppCompatActivity {
    public class ByteClass {
        public ByteClass() {
            aByte = 1;
        }
        public byte aByte;
    }

    private ByteClass bytes;
    private ByteClass[][] referenceByte;
    private int times = 10;
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

                    for(int i = 0; i < nBytes; i++){
                        bytes = new ByteClass();
                    }
                    current++;
                    Log.i("GCTest", current + " Allocted " + nBytes + " bytes");
                }
            }
        });




        final Thread threadStore = new Thread(new Runnable() {
            @Override
            public void run() {

                referenceByte = new ByteClass[times][];

                while(sharedCurrent < times) {
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    referenceByte[sharedCurrent] = new ByteClass[nBytes];
                    for(int i = 0; i < nBytes; i++){
                        referenceByte[sharedCurrent][i] = new ByteClass();
                    }

                    Log.i("GCTest", sharedCurrent + " Allocted " + nBytes + " bytes");
                    sharedCurrent++;
                }

            }
        });

        Button bForget = findViewById(R.id.btn_forget);
        bForget.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                getInfoFromText();
                threadForget.start();
            }
        });


        Button bStore = findViewById(R.id.btn_store);
        bStore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                getInfoFromText();
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
