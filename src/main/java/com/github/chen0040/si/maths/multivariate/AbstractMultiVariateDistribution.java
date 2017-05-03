package com.github.chen0040.si.maths.multivariate;

import Jama.Matrix;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;


/**
 * Created by xschen on 18/8/15.
 */
public abstract class AbstractMultiVariateDistribution implements MultiVariateDistribution {

    protected int dimension;
    protected Matrix muRowVector;
    protected Matrix covarianceMatrix;

    public AbstractMultiVariateDistribution(){

    }

    public int getDimension(){
        return dimension;
    }

    @Override
    public Object clone(){
        throw new NotImplementedException();
    }

    public void copy(MultiVariateDistribution rhs) {

        AbstractMultiVariateDistribution rhs2 = (AbstractMultiVariateDistribution)rhs;
        dimension = rhs2.dimension;
        muRowVector = rhs2.muRowVector == null ? null : (Matrix)rhs2.muRowVector.clone();
        covarianceMatrix = rhs2.covarianceMatrix == null ? null : (Matrix)rhs2.covarianceMatrix.clone();

    }

    public Matrix getMu() {
        return muRowVector;
    }

    public Matrix getCovariance() {
        return covarianceMatrix;
    }

    public abstract double getProbabilityDensity(double[] x) ;

    public abstract void sample(List<double[]> values);

    public abstract boolean inPredictionInterval(double[] x, double f);

    public boolean isDegenerate(){
        return false;
    }

    public Matrix toColumnVector(double[] x){
        int m = x.length;
        Matrix v = new Matrix(m, 1);
        for(int i=0; i < m; ++i){
            v.set(i, 0, x[i]);
        }
        return v;
    }

    public Matrix columnVector(int n){
        return new Matrix(n, 1);
    }
}
