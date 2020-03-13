package com.example.multipletwonumberjava;

public class ThreadToCalculate implements Runnable {

    private Calculate calculate;
    private long result;
    private ListennerThread listener;


    public ThreadToCalculate(Calculate calculate, long result, ListennerThread listener) {
        this.calculate = calculate;
        this.result = result;
        this.listener = listener;
    }

    @Override
    public void run() {
        result = calculate.returnResult();
        listener.getResult(result);
    }

    public interface ListennerThread{
        void getResult(long tResult);
    }
}
