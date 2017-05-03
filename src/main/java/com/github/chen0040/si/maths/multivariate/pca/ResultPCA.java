package com.github.chen0040.si.maths.multivariate.pca;

import Jama.Matrix;

import java.util.List;


/**
 * Created by xschen on 20/8/15.
 */
public class ResultPCA {
    public List<double[]> shrinkedData;
    public Matrix U_reduce;
    public double variance_retained;
    public int K;

    public ResultPCA(List<double[]> data, Matrix U_reduce, double variance_retained){
        this.shrinkedData = data;
        this.U_reduce = U_reduce;
        this.variance_retained = variance_retained;
    }
}
