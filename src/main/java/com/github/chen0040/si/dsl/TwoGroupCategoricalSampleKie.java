package com.github.chen0040.si.dsl;


import com.github.chen0040.data.frame.DataFrame;
import com.github.chen0040.data.frame.DataRow;
import com.github.chen0040.si.statistics.*;
import com.github.chen0040.si.testing.TestingOnProportionDifference;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by xschen on 12/5/2017.
 */
public class TwoGroupCategoricalSampleKie {
   private final Variable variable;
   private final Variable groupVariable;

   private final String group1Id;
   private final String group2Id;

   private Sample sample;

   private double sample1Proportion;
   private int sample1Size;

   private double sample2Proportion;
   private int sample2Size;

   private String successLabel;

   private Map<String, SampleDistribution> sample1DistributionMap = new HashMap<>();
   private Map<String, SampleDistribution> sample2DistributionMap = new HashMap<>();

   public TwoGroupCategoricalSampleKie(Variable variable, Variable groupVariable, String group1Id, String group2Id) {
      this.variable = variable;
      this.groupVariable = groupVariable;

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
      observation.setCategory(value);
      observation.setGroupId(groupId);
      sample.add(observation);

      if(group1Id.equals(groupId)) {
         sample1DistributionMap.remove(value);
      } else if(group2Id.equals(groupId)){
         sample2DistributionMap.remove(value);
      }

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
      return new ProportionDifference(getSamplingDistribution(value));
   }

   public SamplingDistributionOfSampleProportionDifference getSamplingDistribution(String value){
      if(sample!=null) {
         SampleDistribution distribution1 =  getSampleDistribution1(value);
         SampleDistribution distribution2 =  getSampleDistribution2(value);
         return new SamplingDistributionOfSampleProportionDifference(distribution1,distribution2);

      } else {
         return new SamplingDistributionOfSampleProportionDifference(sample1Proportion, sample2Proportion, sample1Size,sample2Size, group1Id, group2Id);
      }
   }

   public SampleDistribution getSampleDistribution1(String value){
      if(sample1DistributionMap.containsKey(value)){
         return sample1DistributionMap.get(value);
      } else {
         SampleDistribution distribution = new SampleDistribution(sample, value, group1Id());
         sample1DistributionMap.put(value, distribution);
         return distribution;
      }
   }

   public SampleDistribution getSampleDistribution2(String value){
      if(sample2DistributionMap.containsKey(value)){
         return sample2DistributionMap.get(value);
      } else {
         SampleDistribution distribution = new SampleDistribution(sample, value, group2Id());
         sample2DistributionMap.put(value, distribution);
         return distribution;
      }
   }

   public TestingOnProportionDifference test4EqualProportions(String value) {
      if(successLabel != null && !successLabel.equals(value)){
         throw new RuntimeException("distribution is already provided with the success label that is different that the parameter");
      }

      if(sample != null) {
         TestingOnProportionDifference test = new TestingOnProportionDifference();
         SampleDistribution distribution1 =  getSampleDistribution1(value);
         SampleDistribution distribution2 =  getSampleDistribution2(value);
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


   public void addObservations(DataFrame dataFrame) {
      for(int i = 0; i < dataFrame.rowCount(); ++i){
         DataRow row = dataFrame.row(i);
         String value = row.getCategoricalCell(variable.getName());
         String groupId = row.getCategoricalCell(groupVariable.getName());
         addObservation(value, groupId);
      }
   }


   public double getGroup1SampleMean(String value) {
      return getSampleDistribution1(value).getSampleMean();
   }

   public double getGroup1SampleProportion(String value){
      return getSampleDistribution1(value).getProportion();
   }
   
   public double getGroup1SampleSd(String value){
      return getSampleDistribution1(value).getSampleSd();
   }
   
   public int getGroup1SampleSize(){
      if(sample != null){
         return sample.countByGroupId(group1Id);
      } else {
         return sample1Size;
      }
   }

   public double getGroup2SampleMean(String value) {
      return getSampleDistribution2(value).getSampleMean();
   }

   public double getGroup2SampleProportion(String value){
      return getSampleDistribution2(value).getProportion();
   }

   public double getGroup2SampleSd(String value){
      return getSampleDistribution2(value).getSampleSd();
   }

   public int getGroup2SampleSize(){
      if(sample != null){
         return sample.countByGroupId(group2Id);
      } else {
         return sample2Size;
      }
   }
}
