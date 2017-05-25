package com.github.chen0040.si.statistics;


import com.github.chen0040.data.utils.TupleTwo;
import com.github.chen0040.si.exceptions.VariableWrongValueTypeException;
import lombok.Getter;
import lombok.Setter;


/**
 * Created by xschen on 8/5/2017.
 * Explore the linear relationship between two numerical variables x and y in the form of:
 * y = b_0 + x * b_1
 */
@Getter
@Setter
public class SampleLinearRegression {

   // sample mean of x
   private double xBar;

   // sample mean of y
   private double yBar;

   // sample standard deviation of x
   private double sX;
   // sample standard deviation of y
   private double sY;

   // correlation between x and y
   private double correlation;

   private double R2;

   // b_1 for y = b_0 + x * b_1
   private double slope;

   // b_0 for y = b_0 + x * b_1
   private double intercept;

   public SampleLinearRegression(Sample sample){


      int sampleSize = sample.size();

      if(!sample.containsTwoNumericalVariables()) {
         throw new VariableWrongValueTypeException("Sample 1 should contain numeric variable x and y");
      }

      TupleTwo<Double, Double> tuple = sample.getObservations().stream()
              .map(o -> new TupleTwo<>(o.getX(), o.getY()))
              .reduce((a, b) -> new TupleTwo<>(a._1() + b._1(), a._2() + b._2()))
              .get();

      xBar = tuple._1() / sampleSize;
      yBar = tuple._2() / sampleSize;

      double sum = 0;
      double sum_x = 0;
      double sum_y = 0;
      for(int i=0; i < sampleSize; ++i) {
         Observation o = sample.get(i);
         double x = o.getX();
         double y = o.getY();

         double x_d = x - xBar;
         double y_d = y - yBar;

         sum += x_d * y_d;
         sum_x += x_d * x_d;
         sum_y += y_d * y_d;
      }

      sX = sum_x / (sampleSize-1);
      sY = sum_y / (sampleSize-1);

      correlation = sum / (Math.sqrt(sum_x) * Math.sqrt(sum_y));

      R2 = correlation * correlation;

      slope = calculateSlope(sX, sY, correlation);

      intercept = calculateIntercept(xBar, yBar, slope);
   }

   /**
    * Estimate the slope b_0 in y = b_0 + x * b_1
    * @param sd_x sample standard deviation of numerical variable x
    * @param sd_y sample standard deviation of numerical variable y
    * @param correlation correlation between x and y
    * @return the slope b, where y = b * x + c
    */
   private double calculateSlope(double sd_x, double sd_y, double correlation){
      return sd_y * correlation / sd_x;
   }


   /**
    * Estimate the intercept b_0 = y - x * b_1
    * @param x_bar point estimate of sample mean for x
    * @param y_bar point estimate of sample mean for y
    * @param slope estimate of slow for y and x
    * @return
    */
   private double calculateIntercept(double x_bar, double y_bar, double slope) {
      return y_bar - x_bar * slope;
   }


}
