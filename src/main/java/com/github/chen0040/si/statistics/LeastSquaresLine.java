package com.github.chen0040.si.statistics;


/**
 * Created by xschen on 5/5/2017.
 * Calculate the slope and intercept of two numerical variables x and y using least square line of the form y = b_0 + b_1 * x
 * x is known as the explanatory variable
 * y is known as the response variable
 */
public class LeastSquaresLine {

   /**
    * Estimate the slope b_0 in y = b_0 + x * b_1
    * @param sd_x sample standard deviation of numerical variable x
    * @param sd_y sample standard deviation of numerical variable y
    * @param correlation correlation between x and y
    * @return the slope b, where y = b * x + c
    */
   public double calculateSlope(double sd_x, double sd_y, double correlation){
      return sd_y * correlation / sd_x;
   }


   /**
    * Estimate the intercept b_0 = y - x * b_1
    * @param x_bar point estimate of sample mean for x
    * @param y_bar point estimate of sample mean for y
    * @param slope estimate of slow for y and x
    * @return
    */
   public double calculateIntercept(double x_bar, double y_bar, double slope) {
      return y_bar - x_bar * slope;
   }


   /**
    * Return the R^2 of the least sqaures fit of y = b_0 + x * b_1, R^2 is the explained variability
    * @param correlation
    * @return R^2, the explain variability, between 0 and 1
    */
   public double calculateR2(double correlation){

      return correlation * correlation;
   }

}
