package com.example.luiza_000.garbagecollection;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private byte[] bytes;
    private byte[][] bytesArray;
    private int size = 1000;
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

        final Thread[] threadAccess = new Thread[2];
        threadAccess[0] = new Thread(new Runnable() {
            @Override
            public void run() {

                while(sharedCurrent < size) {

                    if(sharedCurrent > 1)
                    {
                        for(int i = 0; i < sharedCurrent-1; i++)
                        {
                            for(int j = 0; j < 1024*1024; j++)
                            {
                                bytesArray[i][j]++;
                            }
                        }
                    }
                }

                finishAndRemoveTask();
            }
        });
        threadAccess[1] = new Thread(new Runnable() {
            @Override
            public void run() {

                while(sharedCurrent < size) {

                    if(sharedCurrent > 1)
                    {
                        for(int i = sharedCurrent-2; i >= 0; i--)
                        {
                            for(int j = 0; j < 1024*1024; j++)
                            {
                                bytesArray[i][j]++;
                            }
                        }
                    }
                }

                finishAndRemoveTask();
            }
        });


        final Thread threadStore = new Thread(new Runnable() {
            @Override
            public void run() {

                bytesArray = new byte[size][];


                while(sharedCurrent < size) {
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    bytesArray[sharedCurrent] = new byte[1024*1024];
                    sharedCurrent++;
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



    }


}
