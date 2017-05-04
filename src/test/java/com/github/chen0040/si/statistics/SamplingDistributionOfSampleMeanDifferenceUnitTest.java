package com.github.chen0040.si.statistics;


import com.github.chen0040.si.enums.DistributionFamily;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.testng.Assert.*;


/**
 * Created by xschen on 4/5/2017.
 */
public class SamplingDistributionOfSampleMeanDifferenceUnitTest {
   private static Logger logger = LoggerFactory.getLogger(SamplingDistributionOfSampleMeanDifferenceUnitTest.class);

   // calculating the confidence interval of the difference between two population that are unknown
   @Test
   public void test_confidence_interval_small_sample(){

      double xHat1 = 52.1;
      double xHat2 = 27.1;
      double s1 = 45.1;
      double s2 = 26.4;
      int n2 = 22;
      int n1 = 22;
      String group2 = "group that eat biscuit while playing solitaire";
      String group1 = "group that DO NOT eat biscuit while playing solitaire";



      SamplingDistributionOfSampleMeanDifference differenceDistribution = new SamplingDistributionOfSampleMeanDifference(xHat1, xHat2, s1, s2, n1, n2, group1, group2);
      ConfidenceInterval ci = differenceDistribution.confidenceInterval(0.95);

      logger.info("confidence interval: {}", ci);
      logger.info("interpretation: {}", ci.getSummary());

      assertThat(differenceDistribution.getDistributionFamily()).isEqualTo(DistributionFamily.StudentT);


   }
}
