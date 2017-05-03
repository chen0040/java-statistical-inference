package com.github.chen0040.si.maths.multivariate;

import Jama.Matrix;
import com.github.chen0040.si.maths.utils.MatrixService;


/**
 * Created by xschen on 19/8/15.
 */
public class FilterService {

    // X is matrix in which each row represent an observation (i.e. a shrinkedData record)
    // this method alleviate effect of the white noise in the X
    public static Matrix whiten(Matrix X){
        // X: m x n
        Matrix covXX = MatrixService.cov(X); //covXX: n x n
        Matrix meanXX = MatrixService.mean(X); //meanXX: 1 x n

        // yy = sqrtm(inv(cov(xx)))*(xx' - repmat(mean(xx), 1, size(xx, 1));

        Matrix part1 = MatrixService.sqrt(covXX.inverse()); // part1: n x n
        Matrix part2 = MatrixService.repmat(meanXX.transpose(), 1, X.getRowDimension()); // part2: n x m
        Matrix part3 = X.transpose().minus(part2); // part3 : n x m;
        Matrix part4 = part1.times(part3); // part4: n x m
        return part4;
    }

    // B = (A - repmat(mean(A),[m 1])) ./ repmat(std(A),[m 1])
    public static Matrix normalize(Matrix A){
        int m = A.getRowDimension();

        Matrix Amean = MatrixService.mean(A); // 1 x n
        Matrix Amean_repmat = MatrixService.repmat(Amean, m, 1); // m x n
        Matrix A_minus_Amean_repmat = A.minus(Amean_repmat); // m x n

        Matrix Astd = MatrixService.std(A); // 1 x n
        Matrix Astd_repmat = MatrixService.repmat(Astd, m, 1); // m x n

        Matrix B = A_minus_Amean_repmat.arrayRightDivide(Astd_repmat); // m x n
        return B;
    }


}
