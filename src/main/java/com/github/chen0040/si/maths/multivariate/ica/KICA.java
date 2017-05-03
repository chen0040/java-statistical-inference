package com.github.chen0040.si.maths.multivariate.ica;

import Jama.Matrix;
import Jama.SingularValueDecomposition;
import com.github.chen0040.si.maths.multivariate.FilterService;
import com.github.chen0040.si.maths.utils.MatrixService;

import java.util.List;


/**
 * Created by xschen on 19/8/15.
 * ICA based on Kurtosis Maximization
 *
 * Kurtosis Maximization ICA:
 * % xx is a matrix in which each row is an observation
 * % cov(xx) is the covariance matrix of xx
 * % mean(xx) is a row vector, in which a single value in each column represents the mean of a column in xx
 *
 * % perform ICA:
 * [W,ss,vv] = svd((repmat(sum(yy.*yy,1),size(yy,1),1).*yy)*yy');
 */
public class KICA extends AbstractICA {

    @Override
    public ResultICA separateSources(List<double[]> X) {

        // XX: m x n
        Matrix XX = MatrixService.toMatrix(X);
        Matrix YY = FilterService.whiten(XX); // YY: n x m;

        Matrix SUM = MatrixService.sum(YY.arrayTimes(YY), 1); // SUM: 1 x m
        Matrix SUM_repmat = MatrixService.repmat(SUM, YY.getRowDimension(), 1); // SUM_repmat: n x m;
        Matrix ZZ = SUM_repmat.arrayTimes(YY).times(YY.transpose()); // ZZ: n x n

        SingularValueDecomposition svd = ZZ.svd();

        Matrix U = svd.getU(); // n x n
        Matrix S = svd.getS(); // n x n
        Matrix V = svd.getV(); // n x n

        Matrix X1 = XX.times(U); // m x n;
        Matrix X2 = XX.times(V); // m x n;

        return new ResultICA(MatrixService.toList(X1), MatrixService.toList(X2));
    }
}
