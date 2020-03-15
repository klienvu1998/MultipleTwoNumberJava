package com.example.multipletwonumberjava;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private EditText editTextNumber1,editTextNumber2;
    public static TextView textViewResult;
    private Button btnCalculate;

    public final static int COMPLETED_THREAD_POOL = 100;
    private final static int COREPOOLSIZE = 1;

    //1 thrad
    //Runtime.getRuntime().availableProcessors();
    // 30 thread

    private final static int MAXPOOLSIZE = Runtime.getRuntime().availableProcessors();
    private final static int QUENECAPACITY = 1;

    private ThreadPoolExecutor threadPoolExecutor;
    public static long result = -1;
    private long loopNumber;
    private long number;
    private ArrayList<Long> arrLong;
    private Calculate[] calculates;

    private int checkFinishTaskCount;
    private CalculateHandler calculateHandler;

    private long endTime = 0;
    private long startTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapping();
        calculateHandler = new CalculateHandler();
        threadPoolExecutor = new ThreadPoolExecutor(
                COREPOOLSIZE,
                MAXPOOLSIZE,
                100,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<Runnable>(QUENECAPACITY)
        );
        arrLong = new ArrayList<>(MAXPOOLSIZE);
        calculates = new Calculate[MAXPOOLSIZE];
        btnToCalculate();
    }


    private void btnToCalculate() {
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime = System.currentTimeMillis();
                arrLong.clear();
                result = 0;
                checkFinishTaskCount = 0;
                if(!editTextNumber1.getText().toString().equals("") && !editTextNumber2.getText().toString().equals("")) {
                    loopNumber = Long.parseLong(editTextNumber1.getText().toString());
                    number = Long.parseLong(editTextNumber2.getText().toString());

                    for(int i=0;i<MAXPOOLSIZE-1;i++){
                        arrLong.add(loopNumber/MAXPOOLSIZE);
                    }
                    arrLong.add(loopNumber - ((MAXPOOLSIZE-1)*(loopNumber/MAXPOOLSIZE)));

                    for(int i=0;i<MAXPOOLSIZE;i++){
                        calculates[i] = new Calculate(number,arrLong.get(i),result);
                    }
                    for (int i=0;i<MAXPOOLSIZE;i++){
                        threadPoolExecutor.execute(new ThreadToCalculate(calculates[i], result, new ThreadToCalculate.ListennerThread() {
                            @Override
                            public void getResult(long tResult) {
                                if(tResult!=-1){
                                    result += tResult;
                                    checkFinishTaskCount++;
                                    Message message = new Message();
                                    if(checkFinishTaskCount == MAXPOOLSIZE){
                                        message.what = COMPLETED_THREAD_POOL;
                                        endTime = System.currentTimeMillis();
                                        Log.d("MainActivity",String.valueOf(endTime-startTime));
                                        //1 thread : 2980 mili
                                        //Core of phone : 2218 mili
                                        //30 threads :  85071 mili
                                    }
                                    calculateHandler.sendMessage(message);
                                }
                            }
                        }));
                    }
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        threadPoolExecutor.shutdown();
    }

    private void mapping() {
        editTextNumber1 = findViewById(R.id.editText_Number1);
        editTextNumber2 = findViewById(R.id.editText_Number2);
        textViewResult = findViewById(R.id.textView_Result);
        btnCalculate = findViewById(R.id.btn_Calculate);
    }
}
