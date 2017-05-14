package com.github.chen0040.si.dsl;


import com.github.chen0040.data.frame.DataFrame;
import com.github.chen0040.data.frame.DataRow;
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
   private SampleDistribution sampleDistribution;
   private SamplingDistributionOfSampleMean samplingDistribution;

   public PairedSampleKie(VariablePair variablePair) {
      this.variablePair = variablePair;
   }

   public PairedSampleKie addObservation(double value1, double value2) {
      Observation observation = new Observation();
      observation.setX(value1 - value2);
      observation.setGroupId(groupId());
      sample.add(observation);
      sampleDistribution = null;
      samplingDistribution = null;
      return this;
   }

   private String groupId(){
      return variablePair.variable1().getName() + " - " + variablePair.variable2().getName();
   }

   public Mean difference(){
      SamplingDistributionOfSampleMean sds = getSamplingDistribution();
      return new Mean(sds);
   }

   public SampleDistribution getSampleDistribution(){
      if(sampleDistribution == null) {
         sampleDistribution = new SampleDistribution(sample, groupId());
      }
      return sampleDistribution;
   }

   public SamplingDistributionOfSampleMean getSamplingDistribution(){
      if(samplingDistribution == null) {
         SampleDistribution distribution = getSampleDistribution();
         samplingDistribution = new SamplingDistributionOfSampleMean(distribution);
      }
      return samplingDistribution;
   }

   public TestingOnValue testDifferenceEqualTo(double mean) {
      TestingOnValue test = new TestingOnValue();
      SampleDistribution distribution = getSampleDistribution();
      double xHat = distribution.getSampleMean();
      double sd = distribution.getSampleSd();
      int n = distribution.getSampleSize();
      test.run(xHat, sd, n, mean);
      return test;
   }


   public void addObservations(DataFrame dataFrame) {
      for(int i=0; i < dataFrame.rowCount(); ++i){
         DataRow row = dataFrame.row(i);
         addObservation(row.getCell(variablePair.variable1().getName()), row.getCell(variablePair.variable2().getName()));
      }
   }


   public double getSampleDifferenceMean() {
      return getSampleDistribution().getSampleMean();
   }

   public double getSampleDifferenceSd(){
      return getSampleDistribution().getSampleSd();
   }

   public int getSampleSize(){
      return getSampleDistribution().getSampleSize();
   }


}
