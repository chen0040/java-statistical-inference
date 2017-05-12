package com.github.chen0040.si.dsl;


import com.github.chen0040.data.frame.DataFrame;
import com.github.chen0040.data.frame.DataRow;
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
   private String successLabel;

   public CategoricalSampleKie(Variable variable) {
      this.variable = variable;
      sample = new Sample();
      this.successLabel = null;
   }


   public CategoricalSampleKie fromSampleDistribution(String successLabel, double sampleProportion, int sampleSize) {
      this.sampleProportion = sampleProportion;
      this.sampleSize = sampleSize;
      this.successLabel = successLabel;
      return this;
   }


   public CategoricalSampleKie addObservation(String value){
      if(sample == null) {
         throw new RuntimeException("distribution is already provided for the categorical variable, cannot add observation");
      }
      Observation observation = new Observation();
      observation.setCategory(value);
      sample.add(observation);
      return this;
   }

   public CategoricalSampleKie addObservations(DataFrame dataFrame){
      for(int i=0; i < dataFrame.rowCount(); ++i){
         DataRow row = dataFrame.row(i);
         String value = row.getCategoricalCell(variable.getName());
         addObservation(value);
      }
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
         double pHat = distribution.getProportion();
         int n = distribution.getSampleSize();
         test.run(pHat, n, p);
         return test;
      } else {
         TestingOnProportion test = new TestingOnProportion();
         test.run(sampleProportion, sampleSize, p);
         return test;
      }
   }


}
