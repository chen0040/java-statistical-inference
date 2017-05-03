package com.github.chen0040.si.maths.multivariate.ica;

import java.util.List;


/**
 * Created by xschen on 19/8/15.
 */
public abstract class AbstractICA implements ICA {

    public abstract ResultICA separateSources(List<double[]> X) ;
}
