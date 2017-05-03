package com.github.chen0040.si.maths.univariate;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;


/**
 * Created by xschen on 12/8/15.
 */
public abstract class AbstractDistribution implements Distribution {
    protected double mu;
    protected double sigma;
    protected int sampleCount;
    protected double variance;

    public AbstractDistribution(){
        mu = 0;
        sigma = 0;
        sampleCount = 0;
    }

    @Override
    public Object clone(){
        throw new NotImplementedException();
    }

    public static double calculateSampleVariance(List<Double> values, double mu){
        double sum = 0;
        int count = values.size();
        if(count == 1) return 0;
        else if(count == 0) return Double.NaN;
        for(Double value : values){
            sum += (value - mu) * (value - mu);
        }
        return sum / (count-1);
    }

    public static double calculateSampleMean(List<Double> values){
        double sum = 0;
        int count = values.size();
        if(count == 0) return 0;
        for(Double value : values){
            sum += value;
        }
        return sum / count;
    }

    public double getMu() {
        return mu;
    }

    public void setMu(double mu) {
        this.mu = mu;
    }

    public void copy(Distribution rhs){
        if(rhs instanceof AbstractDistribution){
            AbstractDistribution cast_rhs = (AbstractDistribution)rhs;
            mu = cast_rhs.mu;
            sigma = cast_rhs.sigma;
            sampleCount = cast_rhs.sampleCount;
        }
    }

    public int getSampleCount() {
        return sampleCount;
    }

    public void setSampleCount(int sampleCount) {
        this.sampleCount = sampleCount;
    }

    public double getSigma() {
        return sigma;
    }

    public void setSigma(double sigma) {
        this.sigma = sigma;
        variance = sigma * sigma;
    }

    public double getVariance(){
        return variance;
    }

    public void setVariance(double variance){
        this.variance = variance;
        this.sigma = Math.sqrt(this.variance);
    }

    public abstract double getProbabilityDensity(double x);

    public abstract void sample(List<Double> values);
}
