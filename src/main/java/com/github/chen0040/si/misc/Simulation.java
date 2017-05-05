package com.github.chen0040.si.misc;


import org.apache.commons.math3.distribution.BinomialDistribution;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by xschen on 4/5/2017.
 */
public class Simulation {

   public static List<Double> binomial(double null_p, int trials, double simulationCount) {

      List<Double> result = new ArrayList<>();
      BinomialDistribution distribution = new BinomialDistribution(trials, null_p);
      for(int k=0; k < simulationCount; ++k) {
         int successCount = distribution.sample();
         double p = (double)successCount / trials;
         result.add(p);
      }

      return result;
   }





}
