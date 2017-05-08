package com.github.chen0040.si.dsl;


import com.github.chen0040.si.statistics.Observation;
import com.github.chen0040.si.statistics.Sample;
import com.github.chen0040.si.statistics.SampleDistribution;
import com.github.chen0040.si.statistics.SamplingDistributionOfSampleMean;
import com.github.chen0040.si.testing.TestingOnValue;


/**
 * Created by xschen on 8/5/2017.
 */
public class PairedSampleKie {
   private Sample sample = new Sample();
   private VariablePair variablePair;

   public PairedSampleKie(VariablePair variablePair) {
      this.variablePair = variablePair;
   }

   public PairedSampleKie addObservation(double value1, double value2) {
      Observation observation = new Observation();
      observation.setX(value1 - value2);
      sample.add(observation);
      return this;
   }

   public Mean difference(){
      SampleDistribution distribution = new SampleDistribution(sample, null);
      SamplingDistributionOfSampleMean sds = new SamplingDistributionOfSampleMean(distribution);
      return new Mean(sds);
   }

   public TestingOnValue isDifferenceEqualTo(double mean) {
      TestingOnValue test = new TestingOnValue();
      SampleDistribution distribution = new SampleDistribution(sample, null);
      double xHat = distribution.getSampleMean();
      double sd = distribution.getSampleSd();
      int n = distribution.getSampleSize();
      test.run(xHat, sd, n, mean);
      return test;
   }
}
