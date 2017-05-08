package com.github.chen0040.si.dsl;


import com.github.chen0040.si.statistics.Observation;
import com.github.chen0040.si.statistics.Sample;
import com.github.chen0040.si.statistics.SampleDistribution;
import com.github.chen0040.si.statistics.SamplingDistributionOfSampleProportion;
import com.github.chen0040.si.testing.TestingOnProportion;


/**
 * Created by xschen on 8/5/2017.
 */
public class CategoricalSampleKie {
   private final Variable variable;
   private Sample sample;
   private double sampleProportion;
   private int sampleSize;
   private final String successLabel;

   public CategoricalSampleKie(Variable variable) {
      this.variable = variable;
      sample = new Sample();
      this.successLabel = null;
   }


   public CategoricalSampleKie(Variable variable, String successLabel, double sampleProportion, int sampleSize) {
      this.variable = variable;
      this.sampleProportion = sampleProportion;
      this.sampleSize = sampleSize;
      this.successLabel = successLabel;
   }


   public CategoricalSampleKie addObservation(String value){
      if(sample == null) {
         throw new RuntimeException("distribution is already provided for the categorical variable, cannot add observation");
      }
      Observation observation = new Observation();
      observation.setValue(value);
      sample.add(observation);
      return this;
   }

   private String groupId(){
      return variable.getName();
   }

   public Proportion proportion(String value){
      if(successLabel != null && !successLabel.equals(value)){
         throw new RuntimeException("distribution is already provided with the success label that is different that the parameter");
      }
      SampleDistribution sampleDistribution = new SampleDistribution(sample, value, groupId());
      SamplingDistributionOfSampleProportion sds = new SamplingDistributionOfSampleProportion(sampleDistribution);
      return new Proportion(sds);
   }

   public TestingOnProportion isProportionEqualTo(String value, double p) {
      if(successLabel != null && !successLabel.equals(value)){
         throw new RuntimeException("distribution is already provided with the success label that is different that the parameter");
      }

      if(sample != null) {
         TestingOnProportion test = new TestingOnProportion();
         SampleDistribution distribution =  new SampleDistribution(sample, value, groupId());
         double xHat = distribution.getSampleMean();
         double sd = distribution.getSampleSd();
         int n = distribution.getSampleSize();
         test.run(xHat, n, p);
         return test;
      } else {
         TestingOnProportion test = new TestingOnProportion();
         test.run(sampleProportion, sampleSize, p);
         return test;
      }
   }


}
