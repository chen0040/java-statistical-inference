package com.github.chen0040.si.statistics;


import java.util.List;


/**
 * Created by xschen on 4/5/2017.
 */
public class Count {
   public static Interval quantileRange(List<Double> values, double p_lo, double p_hi) {
      int n = values.size();
      values.sort(Double::compare);

      double q_lo, q_hi;

      int marked = (int)(n * p_lo);

      if(marked >= n) {
         q_lo = values.get(n-1);
      } else {
         q_lo = values.get(marked);
      }

      marked = (int)(n * p_hi);

      if(marked >= n) {
         q_hi = values.get(n-1);
      } else {
         q_hi = values.get(marked);
      }

      return new Interval(q_lo, q_hi);
   }

   public static double inverseCumulativeProbability(List<Double> values, double percentage) {

      int n = values.size();
      values.sort(Double::compare);

      int marked = (int)(n * percentage);

      if(marked >= n) {
         return values.get(n-1);
      } else {
         return values.get(marked);
      }
   }

   public static double cumulativeProbability(List<Double> values, double quantile) {
      int n = values.size();
      values.sort(Double::compare);

      for(int i=0; i < n; ++i) {
         if(values.get(i) >= quantile) {
            return (i+1.0) / n;
         }
      }
      return 1.0;
   }
}
