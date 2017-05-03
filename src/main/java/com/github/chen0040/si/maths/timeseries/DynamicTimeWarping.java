package com.github.chen0040.si.maths.timeseries;

import java.util.List;


/**
 * Created by xschen on 23/8/15.
 * Link:
 * https://en.wikipedia.org/wiki/Dynamic_time_warping
 */
public class DynamicTimeWarping {

    // basically similar formulation as derived levinsthein distance between string (or time series) s and t
    public static int DTWDistance(List<Integer> s, List<Integer> t) {
        int n = s.size();
        int m = t.size();

        int[][] DTW = new int[n+1][];
        for(int i = 1; i < n+1; ++i){
            DTW[i] = new int[m+1];
        }

        for(int i = 1; i < n+1; ++i) {
            DTW[i][0] = Integer.MAX_VALUE;
        }
        for(int i = 1; i < m+1; ++i){
            DTW[0][i] = Integer.MAX_VALUE;
        }

        DTW[0][0] = 0;

        for(int i = 1; i < n + 1; ++i) {
            for (int j = 1; j < m + 1; ++j) {
                int cost = difference(s.get(i), t.get(j));
                DTW[i][j] = cost + minimum(DTW[i - 1][j],    // insertion
                                            DTW[i][j - 1],    // deletion
                                            DTW[i - 1][j - 1]);    // match
            }
        }

        return DTW[n][m];
    }

    // DTWDistance with local constraint w, a window parameter
    public static int DTWDistance(List<Integer> s, List<Integer> t, int w) {
        int n = s.size();
        int m = t.size();

        int[][] DTW = new int[n+1][];
        for(int i = 1; i < n+1; ++i){
            DTW[i] = new int[m+1];
        }

        for(int i = 0; i < n+1; ++i) {
            for(int j = 0; j < m+1; ++j) {
                DTW[i][0] = Integer.MAX_VALUE;
            }
        }


        DTW[0][0] = 0;

        for(int i = 1; i < n+1; ++i) {
            for (int j = Math.max(1, i - w); j <= Math.min(m, i + w); ++j) {
                int cost = difference(s.get(i), t.get(j));
                DTW[i][j] = cost + minimum(DTW[i - 1][j],    // insertion
                                            DTW[i][j - 1],    // deletion
                                            DTW[i - 1][j - 1]);    // match
            }
        }

        return DTW[n][m];
    }

    private static int difference(int val1, int val2){
        return Math.abs(val1 - val2);
    }

    private static int minimum(int val1, int val2, int val3){
        return Math.min(val1, Math.min(val2, val3));
    }
}
