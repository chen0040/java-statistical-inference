package com.github.chen0040.si.dsl;


import com.github.chen0040.data.frame.DataFrame;
import com.github.chen0040.data.frame.DataRow;
import com.github.chen0040.si.statistics.*;
import com.github.chen0040.si.testing.TestingOnValue;


/**
 * Created by xschen on 8/5/2017.
 * kie for sample which contains only one single numerical variable
 */
public class NumericalSampleKie {

   private Sample sample = new Sample();
   private double sampleMean;
   private double sampleSd;
   private int sampleSize;

   private SampleDistribution sampleDistribution = null;
   private SamplingDistributionOfSampleMean samplingDistributionOfSampleMean = null;

   private final Variable variable;

   public NumericalSampleKie(Variable variable) {
      this.variable = variable;
   }

   public NumericalSampleKie fromSampleDistribution(double sampleMean, double sampleSd, int sampleSize) {

      this.sample = null;
      this.sampleMean = sampleMean;
      this.sampleSize = sampleSize;
      this.sampleSd = sampleSd;

      sampleDistribution = new SampleDistribution(sampleMean, sampleSize, sampleSd, groupId());
      samplingDistributionOfSampleMean = new SamplingDistributionOfSampleMean(sampleMean, sampleSize, sampleSd, groupId());
      return this;
   }

   public NumericalSampleKie addObservation(double value){
      if(sample == null) {
         throw new RuntimeException("distribution is already provided, cannot add observation");
      }

      Observation observation = new Observation();
      observation.setX(value);
      observation.setGroupId(groupId());
      sample.add(observation);

      sampleDistribution = null;
      samplingDistributionOfSampleMean = null;

      return this;
   }

   public NumericalSampleKie addObservations(DataFrame dataFrame){
      for(int i=0; i < dataFrame.rowCount(); ++i){
         DataRow row = dataFrame.row(i);
         double value = row.getCell(variable.getName());
         addObservation(value);
      }
      return this;
   }

   private SampleDistribution getSampleDistribution(){
      if(sampleDistribution == null) {
         sampleDistribution = new SampleDistribution(sample, groupId());
      }
      return sampleDistribution;
   }

   private SamplingDistributionOfSampleMean getSamplingDistribution(){
      if(samplingDistributionOfSampleMean == null) {
         samplingDistributionOfSampleMean = new SamplingDistributionOfSampleMean(getSampleDistribution());
      }
      return samplingDistributionOfSampleMean;
   }

   public Mean mean(){
      return new Mean(getSamplingDistribution());
   }

   private String groupId(){
      return variable.getName();
   }

   public TestingOnValue test4MeanEqualTo(double mean) {
      if(sample != null) {
         TestingOnValue test = new TestingOnValue();
         SampleDistribution distribution = getSampleDistribution();
         double xHat = distribution.getSampleMean();
         double sd = distribution.getSampleSd();
         int n = distribution.getSampleSize();
         test.run(xHat, sd, n, mean);
         return test;
      } else {
         TestingOnValue test = new TestingOnValue();
         test.run(sampleMean, sampleSd, sampleSize, mean);
         return test;
      }
   }

   public double getSampleMean(){
      return getSampleDistribution().getSampleMean();
   }

   public double getSampleSd(){
      return getSampleDistribution().getSampleSd();
   }

   public double getSampleSize(){
      return getSampleDistribution().getSampleSize();
   }


   public void addObservations(double[] values) {
      for(Double value : values){
         addObservation(value);
      }
   }
}
