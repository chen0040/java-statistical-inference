package com.github.chen0040.si.maths.multivariate.ica;

import java.util.List;


/**
 * Created by xschen on 19/8/15.
 */
public class ResultICA {

    private List<double[]> source1;
    private List<double[]> source2;

    public List<double[]> getSource1() {
        return source1;
    }

    public List<double[]> getSource2() {
        return source2;
    }

    public ResultICA(List<double[]> s1, List<double[]> s2){
        source1 = s1;
        source2 = s2;
    }
}
