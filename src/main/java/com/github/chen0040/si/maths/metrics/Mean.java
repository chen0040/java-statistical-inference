package com.github.chen0040.si.maths.metrics;

/**
 * Created by xschen on 14/8/15.
 */
public class Mean {
    public static double apply(double[] values){
        int length = values.length;
        if(length==0) return Double.NaN;
        double sum = 0;
        for(int i=0; i < length; ++i){
            sum += values[i];
        }
        return sum / length;
    }
}
