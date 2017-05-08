package com.github.chen0040.si.dsl;


import com.github.chen0040.si.statistics.ConfidenceInterval;
import com.github.chen0040.si.statistics.SamplingDistributionOfSampleMean;


/**
 * Created by xschen on 8/5/2017.
 */
public class Mean {

   private final SamplingDistributionOfSampleMean samplingDistributionOfSampleMean;
   public Mean(SamplingDistributionOfSampleMean samplingDistributionOfSampleMean) {
      this.samplingDistributionOfSampleMean = samplingDistributionOfSampleMean;
   }
   public ConfidenceInterval confidenceInterval(double significanceLevel){
      return samplingDistributionOfSampleMean.confidenceInterval(significanceLevel);
   }
}
