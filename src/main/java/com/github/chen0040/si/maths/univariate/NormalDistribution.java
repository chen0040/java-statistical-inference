package com.github.chen0040.si.maths.univariate;

import java.util.List;


/**
 * Created by xschen on 12/8/15.
 */
public class NormalDistribution extends AbstractDistribution {

    @Override
    public double getProbabilityDensity(double x){
        return Math.exp(-(x-mu) * (x-mu) / (2 * variance)) / Math.sqrt(2*Math.PI * variance);
    }

    @Override
    public void sample(List<Double> values){
        if(sampleCount == 0){
            sampleCount = values.size();
            mu = calculateSampleMean(values);
            setVariance(calculateSampleVariance(values, mu));
            return;
        }

        int n1 = getSampleCount();
        double variance1 = getVariance();
        double mu1 = getMu();

        int n2 = values.size();
        double mu2 = calculateSampleMean(values);
        double variance2 = calculateSampleVariance(values, mu2);
        double sigma2 = Math.sqrt(variance2);

        mu = (n1 * mu1 + n2 * mu2) / (n1+n2);

        setVariance((n1 * (variance1 + mu1 * mu1) + n2 * (variance2 + mu2 * mu2)) / (n1 + n2) - mu * mu);

        sampleCount = n1 + n2;

    }

    @Override
    public Object clone(){
        NormalDistribution clone = new NormalDistribution();
        clone.copy(this);
        return clone;
    }

}
