package com.github.chen0040.si.dsl;


import com.github.chen0040.si.statistics.ConfidenceInterval;
import com.github.chen0040.si.statistics.SamplingDistributionOfSampleMeanDifference;


/**
 * Created by xschen on 8/5/2017.
 */
public class MeanDifference {

   private final SamplingDistributionOfSampleMeanDifference samplingDistributionOfSampleMeanDifference;
   public MeanDifference(SamplingDistributionOfSampleMeanDifference samplingDistributionOfSampleMeanDifference) {
      this.samplingDistributionOfSampleMeanDifference = samplingDistributionOfSampleMeanDifference;
   }
   public ConfidenceInterval confidenceInterval(double significanceLevel){
      return samplingDistributionOfSampleMeanDifference.confidenceInterval(significanceLevel);
   }
}
