package com.github.chen0040.si.statistics;


import com.github.chen0040.si.exceptions.VariableValueNotMatchedException;
import com.github.chen0040.si.exceptions.VariableWrongValueTypeException;
import com.github.chen0040.si.utils.TupleTwo;


/**
 * Created by xschen on 8/5/2017.
 */
public class SampleCorrelation {
   public static double correlation(Sample sample1){


      int sampleSize1 = sample1.countByGroupId(null);

      if(!sample1.containsXY()) {
         throw new VariableWrongValueTypeException("Sample 1 should contain numeric variable x and y");
      }

      TupleTwo<Double, Double> tuple = sample1.getObservations().stream()
              .map(o -> new TupleTwo<>(o.getX(), o.getY()))
              .reduce((a, b) -> new TupleTwo<>(a._1() + b._1(), a._2() + b._2()))
              .get();

      double x_bar = tuple._1() / sampleSize1;
      double y_bar = tuple._2() / sampleSize1;

      double sum = 0;
      double sum_x = 0;
      double sum_y = 0;
      for(int i=0; i < sampleSize1; ++i) {
         Observation o = sample1.get(i);
         double x = o.getX();
         double y = o.getY();

         double x_d = x - x_bar;
         double y_d = y - y_bar;

         sum += x_d * y_d;
         sum_x += x_d * x_d;
         sum_y += y_d * y_d;
      }

      return sum / (Math.sqrt(sum_x) * Math.sqrt(sum_y));
   }
}
