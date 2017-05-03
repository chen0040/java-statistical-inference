package com.github.chen0040.si.maths.metrics;

/**
 * Created by xschen on 14/8/15.
 */
public class StdDev {
    public static double apply(double[] values, double mu){
        return Math.sqrt(Variance.apply(values, mu));
    }
}
