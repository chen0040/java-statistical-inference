package com.github.chen0040.si.dsl;


import com.github.chen0040.si.statistics.ConfidenceInterval;
import com.github.chen0040.si.statistics.SamplingDistributionOfSampleProportion;


/**
 * Created by xschen on 8/5/2017.
 */
public class Proportion {
   private final SamplingDistributionOfSampleProportion samplingDistributionOfSampleProportion;
   public Proportion(SamplingDistributionOfSampleProportion samplingDistributionOfSampleProportion){
      this.samplingDistributionOfSampleProportion = samplingDistributionOfSampleProportion;
   }

   public ConfidenceInterval confidenceInterval(double confidenceLevel){
      return samplingDistributionOfSampleProportion.confidenceInterval(confidenceLevel);
   }
}
