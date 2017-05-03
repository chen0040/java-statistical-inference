package com.github.chen0040.si.statistics;


import com.github.chen0040.si.exceptions.VariableWrongValueTypeException;


/**
 * Created by xschen on 3/5/2017.
 * A sampling distribution is a theoretical distribution of the values that a specified statistic of a sample takes on in all of the possible samples of a specific size that can be made from a given population. We have not discussed sampling distributions before.
 * Note that we can have sampling distributions of sample means, of sample standard deviations, etc.
 */
public class SamplingDistributionOfSampleMean {

   // the sample statistics
   private final double sampleMean;


   private final int sampleSize;

   // standard deviation, could be population standard deviation or sample stand deviation
   private final double sd;

   private final double standardError;


   public SamplingDistributionOfSampleMean(SampleDistribution sampleDistribution){
      if(sampleDistribution.isCategorical()){
         throw new VariableWrongValueTypeException("Sampling distribution of sample means are not defined for categorical variable");
      }
      this.sampleMean = sampleDistribution.getSampleMean();
      this.sd = sampleDistribution.getSampleSd();
      this.sampleSize = sampleDistribution.getSampleSize();
      this.standardError = calculateStandardError(sd, sampleSize);
   }

   public SamplingDistributionOfSampleMean(double sampleMean, int sampleSize, double sd) {


      this.sampleMean = sampleMean;
      this.sampleSize = sampleSize;
      this.sd = sd;
      this.standardError = calculateStandardError(sd, sampleSize);
   }

   private double calculateStandardError(double sd, int sampleSize) {
      return sd / Math.sqrt(sampleSize);
   }

   public double getSampleMean() {
      return sampleMean;
   }

   public double getStandardError() {
      return standardError;
   }

   public int getSampleSize(){
      return sampleSize;
   }
}
