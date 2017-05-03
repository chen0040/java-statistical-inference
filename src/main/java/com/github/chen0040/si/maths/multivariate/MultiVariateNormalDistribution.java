package com.github.chen0040.si.maths.multivariate;

import Jama.Matrix;
import com.github.chen0040.si.maths.utils.MatrixService;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;


/**
 * Created by xschen on 18/8/15.
 */
public class MultiVariateNormalDistribution extends AbstractMultiVariateDistribution {
    private double detCovarianceMatrix = 0;
    private double sqrt_2pi_power_k = 0;

    public MultiVariateNormalDistribution(){
        super();

    }

    @Override
    public void copy(MultiVariateDistribution rhs){
        super.copy(rhs);

        MultiVariateNormalDistribution rhs2 = (MultiVariateNormalDistribution)rhs;
        detCovarianceMatrix = rhs2.detCovarianceMatrix;
        sqrt_2pi_power_k = rhs2.sqrt_2pi_power_k;
    }

    @Override
    public Object clone() {
        MultiVariateNormalDistribution clone = new MultiVariateNormalDistribution();
        clone.copy(this);
        return clone;
    }

    @Override
    public double getProbabilityDensity(double[] x0) {
        if(isDegenerate()) return 0;

        Matrix x = toColumnVector(x0);
        Matrix x_minus_mu = x.minus(muRowVector.transpose());
        Matrix x_minus_mu_transpose = x_minus_mu.transpose();
        Matrix mat1 = x_minus_mu_transpose.times(covarianceMatrix.inverse()).times(x_minus_mu);

        return Math.exp(- mat1.get(0, 0) / 2) / (sqrt_2pi_power_k * Math.sqrt(detCovarianceMatrix));
    }

    @Override
    public void sample(List<double[]> values) {
        Matrix X = MatrixService.toMatrix(values);

        dimension = X.getColumnDimension();
        sqrt_2pi_power_k = Math.sqrt(Math.pow(2 * Math.PI, dimension));

        muRowVector = MatrixService.mean(X);

        covarianceMatrix = MatrixService.cov(X);
    }

    @Override
    public boolean isDegenerate(){
        return detCovarianceMatrix == 0;
    }



    private void updateModel(){
        detCovarianceMatrix = covarianceMatrix.det();

    }

    // Check the prediction interval in the link https://en.wikipedia.org/wiki/Multivariate_normal_distribution
    @Override
    public boolean inPredictionInterval(double[] x0, double p) {
        Matrix x = toColumnVector(x0);
        Matrix x_minus_mu = x.minus(muRowVector);
        Matrix x_minus_mu_transpose = x_minus_mu.transpose();
        Matrix mat1 = x_minus_mu_transpose.times(covarianceMatrix).times(x_minus_mu);

        return mat1.get(0, 0) <= ChiSquareQuantile(p, dimension);
    }

    protected double ChiSquareQuantile(double p, int k){
        throw new NotImplementedException();
    }


}
