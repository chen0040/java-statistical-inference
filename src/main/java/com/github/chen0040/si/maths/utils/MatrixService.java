package com.github.chen0040.si.maths.utils;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;


/**
 * Created by xschen on 19/8/15.
 */
public class MatrixService {


    public static Matrix cov(Matrix X){
        return MatrixService.divide(X.transpose().times(X), X.getRowDimension());
    }

    // returns a row vector, in which a single value in each column is the mean of a column in X
    public static Matrix mean(Matrix X){
        int dimension = X.getColumnDimension();
        Matrix muRowVector = new Matrix(1, dimension);

        int m = X.getRowDimension();
        for(int k=0; k < dimension; ++k){
            double sum = 0;
            for(int i=0; i < m; ++i){
                sum += X.get(i, k);
            }
            muRowVector.set(0, k, sum / m);
        }

        return muRowVector;
    }

    public static Matrix std(Matrix X){
        Matrix Xmean = mean(X);

        int dimension = X.getColumnDimension();
        Matrix muRowVector = new Matrix(1, dimension);

        int m = X.getRowDimension();
        for(int k=0; k < dimension; ++k){
            double sum = 0;
            for(int i=0; i < m; ++i){
                sum += Math.pow(X.get(i, k) - Xmean.get(0, k), 2);
            }

            muRowVector.set(0, k, Math.sqrt(sum / (m-1)));
        }

        return muRowVector;

    }

    public static Matrix mean(Matrix X, int dimension){
        if(dimension == 2){
            int rowCount = X.getRowDimension();

            Matrix muColVector = new Matrix(rowCount, 1);

            int n = X.getColumnDimension();
            for(int k=0; k < rowCount; ++k){
                double sum = 0;
                for(int i=0; i < n; ++i){
                    sum+=X.get(k, i);
                }
                muColVector.set(k, 0, sum/n);
            }

            return muColVector;
        }else{
            return mean(X);
        }
    }

    private static double computeCovariance(List<double[]> X, Matrix MU, int j, int k){
        int N = X.size();
        double sum = 0;
        for(int i=0; i < N; ++i) {
            sum += (X.get(i)[j] - MU.get(j, 0)) * (X.get(i)[k] - MU.get(k, 0));
        }
        return sum / (1-N);
    }

    // return a matrix, whose row is a double[] record in X
    public static Matrix toMatrix(List<double[]> X){
        int m = X.size();
        int n = X.get(0).length;
        Matrix XX = new Matrix(m, n);
        for(int i=0; i < m; ++i){
            for(int j=0; j < n; ++j){
                XX.set(i, j, X.get(i)[j]);
            }
        }

        return XX;
    }

    // reverse of toMatrix
    public static List<double[]> toList(Matrix X){
        int m = X.getRowDimension();
        int n = X.getColumnDimension();

        List<double[]> XX = new ArrayList<double[]>();

        for(int i = 0; i < m; ++i){
            double[] x = new double[n];
            for(int j=0; j < n; ++j){
                x[j] = X.get(i, j);
            }
            XX.add(x);
        }
        return XX;
    }

    public static Matrix zeros(int row, int col){
        return new Matrix(row, col);

    }

    public static Matrix zeros(int m){
        return zeros(m, m);
    }

    public static Matrix sum(Matrix matrix){
        int rows = matrix.getRowDimension();
        int cols = matrix.getColumnDimension();
        Matrix rowVec = new Matrix(1, cols);
        for(int j=0; j < cols; ++j){
            double sum = 0;
            for(int i=0; i < rows; ++i){
                sum += matrix.get(i, j);
            }
            rowVec.set(0, j, sum);
        }
        return rowVec;
    }

    public static Matrix sum(Matrix matrix, int dimension){
        if(dimension==2) {
            int rows = matrix.getRowDimension();
            int cols = matrix.getColumnDimension();
            Matrix columnVec = new Matrix(rows, 1);
            for (int i = 0; i < rows; ++i) {
                double sum = 0;
                for (int j = 0; j < cols; ++j) {
                    sum += matrix.get(i, j);
                }
                columnVec.set(i, 0, sum);
            }
            return columnVec;
        }
        else{
            return sum(matrix);
        }
    }

    public static Matrix repmat(Matrix A, int row_repeat, int col_repeat){
        int m = A.getRowDimension();
        int n = A.getColumnDimension();
        int m2 = m * row_repeat;
        int n2 = n * col_repeat;
        Matrix B = new Matrix(m2, n2);
        for(int i=0; i < row_repeat; ++i){
            for(int j=0; j < col_repeat; ++j){
                for(int i2 = 0; i2 < m; ++i2){
                    for(int j2 = 0; j2 < n; ++j2){
                        B.set(i * m + i2, j * n + j2, A.get(i2, j2));
                    }
                }
            }
        }

        return B;
    }

    public static double vecSum(Matrix vector){
        int rows = vector.getRowDimension();
        int cols = vector.getColumnDimension();
        double sum = 0;
        for(int j=0; j < cols; ++j){
            for(int i=0; i < rows; ++i){
                sum += vector.get(i, j);
            }
        }
        return sum;
    }

    public static Matrix column(Matrix matrix, int colIndex){
        int rows = matrix.getRowDimension();
        Matrix column = new Matrix(rows, 1);
        for(int i=0; i< rows; ++i){
            column.set(i, 0, matrix.get(i, colIndex));
        }
        return column;
    }

    public static Matrix row(Matrix matrix, int rowIndex){
        int cols = matrix.getColumnDimension();
        Matrix row = new Matrix(1, cols);
        for(int i=0; i < cols; ++i){
            row.set(0, i, matrix.get(rowIndex, i));
        }
        return row;
    }

    public static Matrix pow(Matrix matrix, double power){
        int rows = matrix.getRowDimension();
        int cols = matrix.getColumnDimension();

        Matrix result = new Matrix(rows, cols);
        for(int i=0; i < rows; ++i){
            for(int j=0; j < cols; ++j){
                result.set(i, j, Math.pow(matrix.get(i, j), power));
            }
        }

        return result;
    }

    private static Random random = new Random();

    public static double rand(){
        return random.nextDouble();
    }

    public static Matrix bsxfun(BiFunction<Double, Double, Double> operation, Matrix A, Matrix B){
        int rowCountA = A.getRowDimension();
        int colCountA = A.getColumnDimension();
        int rowCountB = B.getRowDimension();
        int colCountB = B.getColumnDimension();

        int rowCountC = Math.max(rowCountA, rowCountB);
        int colCountC = Math.max(colCountA, colCountB);

        Matrix C = new Matrix(rowCountC,  colCountC);

        for(int i=0; i < rowCountC; ++i){
            int rowIndexA = i < rowCountA ? i : rowCountA - 1;
            int rowIndexB = i < rowCountB ? i : rowCountB - 1;
            for(int j=0; j < colCountC; ++j){
                int colIndexA = j < colCountA ? j : colCountA - 1;
                int colIndexB = j < colCountB ? j : colCountB - 1;
                double cellA = A.get(rowIndexA, colIndexA);
                double cellB = B.get(rowIndexB, colIndexB);
                C.set(i, j, operation.apply(cellA, cellB));
            }
        }

        return C;
    }

    public static Matrix bsxfun_plus(Matrix A, Matrix B){
        return bsxfun(new BiFunction<Double, Double, Double>() {
            public Double apply(Double a, Double b) {
                return a + b;
            }
        }, A, B);
    }

    public static Matrix bsxfun_minus(Matrix A, Matrix B){
        return bsxfun(new BiFunction<Double, Double, Double>() {
            public Double apply(Double a, Double b) {
                return a - b;
            }
        }, A, B);
    }

    public static Matrix bsxfun_plus(Matrix A, double b){
        Matrix B = new Matrix(1, 1);
        B.set(0, 0, b);
        return bsxfun_plus(A, B);
    }

    public static Matrix bsxfun_times(Matrix A, Matrix B){
        return bsxfun(new BiFunction<Double, Double, Double>() {
            public Double apply(Double a, Double b) {
                return a * b;
            }
        }, A, B);
    }

    public static Matrix pow(double val, Matrix P){
        int rowCount = P.getRowDimension();
        int colCount = P.getColumnDimension();
        Matrix C = new Matrix(rowCount, colCount);

        for(int i=0; i < rowCount; ++i){
            for(int j=0; j < colCount; ++j){
                C.set(i, j, Math.pow(val, P.get(i, j)));
            }
        }

        return C;

    }

    public static Matrix filterRows(Matrix matrix, List<Integer> selectedRowIndices){
        int colCount = matrix.getColumnDimension();
        int rowCount = selectedRowIndices.size();
        Matrix result = new Matrix(rowCount, colCount);
        for(int i = 0; i < rowCount; ++i){
            int rowIndex = selectedRowIndices.get(i);
            for(int j=0; j < colCount; ++j){
                result.set(i, j, matrix.get(rowIndex, j));
            }
        }
        return result;
    }

    public static Matrix plus(Matrix A, double b){
        int colCount = A.getColumnDimension();
        int rowCount = A.getRowDimension();

        Matrix C = new Matrix(rowCount, colCount);

        for(int i=0; i < rowCount; ++i){
            for(int j=0; j < colCount; ++j){
                C.set(i, j, A.get(i, j)+b);
            }
        }

        return C;
    }

    public static Matrix times(Matrix A, double b){
        int colCount = A.getColumnDimension();
        int rowCount = A.getRowDimension();

        Matrix C = new Matrix(rowCount, colCount);

        for(int i=0; i < rowCount; ++i){
            for(int j=0; j < colCount; ++j){
                C.set(i, j, A.get(i, j) * b);
            }
        }

        return C;
    }

    public static Matrix divide(Matrix A, double b){
        int colCount = A.getColumnDimension();
        int rowCount = A.getRowDimension();

        Matrix C = new Matrix(rowCount, colCount);

        for(int i=0; i < rowCount; ++i){
            for(int j=0; j < colCount; ++j){
                C.set(i, j, A.get(i, j) / b);
            }
        }

        return C;
    }

    // return the principal square root of X
    public static Matrix sqrt(Matrix X) {

        final EigenvalueDecomposition evd = X.eig();
        final Matrix v = evd.getV();
        final Matrix d = evd.getD();

        for (int r = 0; r < d.getRowDimension(); r++)
            for (int c = 0; c < d.getColumnDimension(); c++)
                d.set(r, c, Math.sqrt(d.get(r, c)));

        final Matrix a = v.inverse();
        final Matrix b = v.times(d).inverse();
        return a.solve(b).inverse();
    }

}
