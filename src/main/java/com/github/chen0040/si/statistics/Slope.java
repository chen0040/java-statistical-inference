package com.github.chen0040.si.statistics;


/**
 * Created by xschen on 5/5/2017.
 * Calculate the slope of two numerical variables x and y
 * x is known as the explanatory variable
 * y is known as the response variable
 */
public class Slope {

   /**
    *
    * @param sd_x sample standard deviation of numerical variable x
    * @param sd_y sample standard deviation of numerical variable y
    * @param correlation correlation between x and y
    * @return the slope b, where y = b * x + c
    */
   public double calculateSlope(double sd_x, double sd_y, double correlation){
      return sd_y * correlation / sd_x;
   }

}
