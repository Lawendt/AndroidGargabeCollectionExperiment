package com.example.luiza_000.garbagecollection;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private byte[] bytes;
    private byte[][] bytesArray;
    private int size = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Thread threadForget = new Thread(new Runnable() {
            @Override
            public void run() {
                int current = 0;

                while(current < size) {
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    bytes = new byte[1024 * 1024];
                    bytes = null;
                    current++;
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                finishAndRemoveTask();
            }
        });


        final Thread threadStore = new Thread(new Runnable() {
            @Override
            public void run() {
                int current = 0;
                bytesArray = new byte[size][];


                while(current < size) {
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    bytesArray[current] = new byte[1024*1024];
                    current++;
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                finishAndRemoveTask();
            }
        });

        Button bForget = findViewById(R.id.btn_forget);
        bForget.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                threadStore.start();
            }
        });


        Button bStore = findViewById(R.id.btn_store);
        bStore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                threadStore.start();
            }
        });






    }


}
