package com.github.chen0040.si.dsl;


import com.github.chen0040.si.statistics.*;
import com.github.chen0040.si.testing.TestingOnProportionDifference;


/**
 * Created by xschen on 12/5/2017.
 */
public class TwoGroupCategoricalSampleKie {
   private final Variable variable;
   private final String group1Id;
   private final String group2Id;

   private Sample sample;

   private double sample1Proportion;
   private int sample1Size;

   private double sample2Proportion;
   private int sample2Size;

   private String successLabel;

   public TwoGroupCategoricalSampleKie(Variable variable, String group1Id, String group2Id) {
      this.variable = variable;
      sample = new Sample();
      this.successLabel = null;
      this.group1Id = group1Id;
      this.group2Id = group2Id;
   }


   public TwoGroupCategoricalSampleKie fromSampleDistributions(String successLabel, double sample1Proportion, double sample2Proportion, int sample1Size, int sample2Size) {
      this.sample1Proportion = sample1Proportion;
      this.sample2Proportion = sample2Proportion;
      this.sample1Size = sample1Size;
      this.sample2Size = sample2Size;
      this.successLabel = successLabel;
      return this;
   }


   public TwoGroupCategoricalSampleKie addObservation(String value, String groupId){
      if(sample == null) {
         throw new RuntimeException("distribution is already provided for the categorical variable, cannot add observation");
      }
      Observation observation = new Observation();
      observation.setValue(value);
      observation.setGroupId(groupId);
      sample.add(observation);
      return this;
   }

   private String group1Id(){
      return group1Id;
   }

   private String group2Id(){
      return group2Id;
   }

   public Proportion proportion(String value, String groupId){
      if(successLabel != null && !successLabel.equals(value)){
         throw new RuntimeException("distribution is already provided with the success label that is different that the parameter");
      }
      SampleDistribution sampleDistribution = new SampleDistribution(sample, value);
      SamplingDistributionOfSampleProportion sds = new SamplingDistributionOfSampleProportion(sampleDistribution);
      return new Proportion(sds);
   }

   public ProportionDifference proportionDifference(String value) {
      if(sample!=null) {
         SampleDistribution distribution1 =  new SampleDistribution(sample, value, group1Id());
         SampleDistribution distribution2 =  new SampleDistribution(sample, value, group2Id());
         SamplingDistributionOfSampleProportionDifference distributionOfSampleProportionDifference = new SamplingDistributionOfSampleProportionDifference(distribution1,distribution2);
         return new ProportionDifference(distributionOfSampleProportionDifference);
      } else {
         SamplingDistributionOfSampleProportionDifference distributionOfSampleProportionDifference = new SamplingDistributionOfSampleProportionDifference(sample1Proportion, sample2Proportion, sample1Size,sample2Size, group1Id, group2Id);
         return new ProportionDifference(distributionOfSampleProportionDifference);
      }
   }

   public TestingOnProportionDifference isProportionEqualTo(String value, double p) {
      if(successLabel != null && !successLabel.equals(value)){
         throw new RuntimeException("distribution is already provided with the success label that is different that the parameter");
      }

      if(sample != null) {
         TestingOnProportionDifference test = new TestingOnProportionDifference();
         SampleDistribution distribution1 =  new SampleDistribution(sample, value, group1Id());
         SampleDistribution distribution2 =  new SampleDistribution(sample, value, group2Id());
         double pHat1 = distribution1.getProportion();
         double pHat2 = distribution2.getProportion();
         int n1 = distribution1.getSampleSize();
         int n2 = distribution2.getSampleSize();
         test.run(successLabel, pHat1, pHat2, n1, n2);
         return test;
      } else {
         TestingOnProportionDifference test = new TestingOnProportionDifference();
         test.run(successLabel, sample1Proportion, sample2Proportion, sample1Size, sample2Size);
         return test;
      }
   }

}
