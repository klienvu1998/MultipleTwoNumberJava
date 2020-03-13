package com.example.multipletwonumberjava;

public class Calculate {
    private long number;
    private long loopNumber;
    private long result;

    public Calculate(long number, long loopNumber, long result) {
        this.number = number;
        this.loopNumber = loopNumber;
        this.result = result;
    }

    public long returnResult(){
        for(int i=0;i<loopNumber;i++){
            result += number;
        }
        return result;
    }
}
