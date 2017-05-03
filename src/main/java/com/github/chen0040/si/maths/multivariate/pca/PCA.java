package com.github.chen0040.si.maths.multivariate.pca;

import Jama.Matrix;
import Jama.SingularValueDecomposition;
import com.github.chen0040.si.maths.multivariate.FilterService;
import com.github.chen0040.si.maths.utils.MatrixService;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by xschen on 20/8/15.
 */
public class PCA {

    /// Principal Component Analysis for Feature Transformation
    public ResultPCA shrink(
            List<double[]> Xinput,
            int K)
    {
        Matrix X = MatrixService.toMatrix(Xinput);

        /// If you do not perform mean normalization and feature scaling, PCA will rotate the shrinkedData in a possibly undesired way.
        X = FilterService.normalize(X); // X: m x n

        int n = X.getColumnDimension();
        int m = X.getRowDimension();


        Matrix X_transpose = X.transpose(); // X.transpose: n x m
        Matrix XVariance = MatrixService.times(X_transpose.times(X), 1 / m); // Variance: n x n;

        SingularValueDecomposition svd = XVariance.svd();
        Matrix U = svd.getU(); // U: n x n
        Matrix S = svd.getS(); // S: n x n;

        Matrix U_reduce = new Matrix(n, K); // U.reduced: n x K
        for (int i = 0; i < n; ++i)
        {
            for (int j = 0; j < K; ++j)
            {
                U_reduce.set(i, j, U.get(i, j));
            }
        }


        double Skk = 0;
        double Smm = MatrixService.vecSum(S);
        for(int i=0; i < K; ++i)
        {
            Skk += S.get(i, i);
        }
        double variance_retained = Skk/Smm;

        Matrix Z = X.times(U_reduce); // Z: m x K

        List<double[]> Zoutput = new ArrayList<double[]>();
        for (int i = 0; i < m; ++i)
        {
            double[] z = new double[K];
            for(int j=0; j < K; ++j){
                z[j] = Z.get(i, j);
            }
            Zoutput.add(z);
        }

        return new ResultPCA(Zoutput, U_reduce, variance_retained);
    }

    /// Reconstruct the K-dimension input Zinput to its original N-dimension form using the compressed matrix U_reduce (obtained from CompressData method)
    public List<double[]> recover(List<double[]> Zinput, Matrix U_reduce)
    {
        Matrix Z = MatrixService.toMatrix(Zinput); // Z: m x K
        int m = Z.getRowDimension();
        int K = Z.getColumnDimension();

        Matrix X = Z.times(U_reduce.transpose()); // X: m x n (U.reduce: n x K)

        List<double[]> Xoutput = new ArrayList<double[]>();
        int n = X.getColumnDimension();

        for(int i=0; i < m; ++i){
            double[] x = new double[n];
            for(int j=0; j <n; ++j){
                x[j] = X.get(i, j);
            }
            Xoutput.add(x);
        }
        return Xoutput;
    }

    public ResultPCA shrink(List<double[]> Xinput, double variance_retained_threshold)
    {
        Matrix X = MatrixService.toMatrix(Xinput);

        /// If you do not perform mean normalization and feature scaling, PCA will rotate the shrinkedData in a possibly undesired way.
        X = FilterService.normalize(X); // X: m x n

        int n = X.getColumnDimension();
        int m = X.getRowDimension();


        Matrix X_transpose = X.transpose(); // X.transpose: n x m
        Matrix XVariance = MatrixService.times(X_transpose.times(X), 1 / m); // Variance: n x n;

        SingularValueDecomposition svd = XVariance.svd();
        Matrix U = svd.getU(); // U: n x n
        Matrix S = svd.getS(); // S: n x n;

        double Skk = 0;
        double Smm = MatrixService.vecSum(S);
        double variance_retained = 0;
        int K = 0;
        for(int i=0; i < n; ++i)
        {
            double deltaS = S.get(i, i)/Smm;
            if(variance_retained + deltaS > variance_retained_threshold){
                break;
            }
            variance_retained += deltaS;
            K++;
        }


        Matrix U_reduce = new Matrix(n, K); // U.reduced: n x K
        for (int i = 0; i < n; ++i)
        {
            for (int j = 0; j < K; ++j)
            {
                U_reduce.set(i, j, U.get(i, j));
            }
        }

        Matrix Z = X.times(U_reduce); // Z: m x K

        List<double[]> Zoutput = new ArrayList<double[]>();
        for (int i = 0; i < m; ++i)
        {
            double[] z = new double[K];
            for(int j=0; j < K; ++j){
                z[j] = Z.get(i, j);
            }
            Zoutput.add(z);
        }

        return new ResultPCA(Zoutput, U_reduce, variance_retained);
    }


}
