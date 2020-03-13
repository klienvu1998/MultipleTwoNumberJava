package com.example.multipletwonumberjava;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private EditText editTextNumber1,editTextNumber2;
    private TextView textViewResult;
    private Button btnCalculate;

    private final static int COREPOOLSIZE = 2;
    private final static int MAXPOOLSIZE = Runtime.getRuntime().availableProcessors();
    private final static int QUENECAPACITY = 30;
    private ThreadPoolExecutor threadPoolExecutor;
    private long result = 0;
    private long loopNumber;
    private long number;
    private ArrayList<Long> arrLong;

    private Calculate[] calculates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        threadPoolExecutor = new ThreadPoolExecutor(
                COREPOOLSIZE,
                MAXPOOLSIZE,
                100,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<Runnable>(QUENECAPACITY)
        );
        arrLong = new ArrayList<>(MAXPOOLSIZE);
        calculates = new Calculate[MAXPOOLSIZE];
        mapping();
        btnToCalculate();

    }

    private void btnToCalculate() {
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result = 0;
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
                                if(loopNumber<MAXPOOLSIZE){
                                    result = tResult;
                                }
                                else {
                                    result += tResult;
                                }
                                textViewResult.setText(result+"");
                            }
                        }));
                    }
                    threadPoolExecutor.shutdown();
                }
            }
        });
    }


    private void mapping() {
        editTextNumber1 = findViewById(R.id.editText_Number1);
        editTextNumber2 = findViewById(R.id.editText_Number2);
        textViewResult = findViewById(R.id.textView_Result);
        btnCalculate = findViewById(R.id.btn_Calculate);
    }
}
