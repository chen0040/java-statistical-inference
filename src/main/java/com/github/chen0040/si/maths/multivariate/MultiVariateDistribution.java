package com.github.chen0040.si.maths.multivariate;

import Jama.Matrix;

import java.util.List;


/**
 * Created by xschen on 18/8/15.
 */
public interface MultiVariateDistribution extends Cloneable {
    void copy(MultiVariateDistribution rhs);

    Matrix getMu();
    Matrix getCovariance();

    double getProbabilityDensity(double[] x);
    void sample(List<double[]> values);

    boolean isDegenerate();

    boolean inPredictionInterval(double[] x, double f);




}
