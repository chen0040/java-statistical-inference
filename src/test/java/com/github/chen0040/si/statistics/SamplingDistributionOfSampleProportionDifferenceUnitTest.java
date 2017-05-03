package com.github.chen0040.si.statistics;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import static org.testng.Assert.*;


/**
 * Created by xschen on 3/5/2017.
 */
public class SamplingDistributionOfSampleProportionDifferenceUnitTest {

   private static Logger logger = LoggerFactory.getLogger(SamplingDistributionOfSampleProportionDifferenceUnitTest.class);

   // calculating the confidence interval of the difference between two population that are unknown
   @Test
   public void test_confidence_interval(){

      double p2 = 0.25;
      double p1 = 0.71;
      int n2 = 1028;
      int n1 = 83;
      String group2 = "US";
      String group1 = "Coursera";

      SamplingDistributionOfSampleProportionDifference differenceDistribution = new SamplingDistributionOfSampleProportionDifference(p1, p2, n1, n2, group1, group2);
      Interval ci = differenceDistribution.confidenceInterval(0.95);

      logger.info("confidence interval: {}", ci);

   }

}
