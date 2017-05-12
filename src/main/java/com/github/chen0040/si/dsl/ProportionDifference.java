package com.github.chen0040.si.dsl;


import com.github.chen0040.si.statistics.ConfidenceInterval;
import com.github.chen0040.si.statistics.SamplingDistributionOfSampleProportionDifference;


/**
 * Created by xschen on 8/5/2017.
 */
public class ProportionDifference {
   private final SamplingDistributionOfSampleProportionDifference samplingDistributionOfSampleProportionDifference;
   public ProportionDifference(SamplingDistributionOfSampleProportionDifference samplingDistributionOfSampleProportionDifference){
      this.samplingDistributionOfSampleProportionDifference = samplingDistributionOfSampleProportionDifference;
   }

   public ConfidenceInterval confidenceInterval(double confidenceLevel){
      return samplingDistributionOfSampleProportionDifference.confidenceInterval(confidenceLevel);
   }
}
