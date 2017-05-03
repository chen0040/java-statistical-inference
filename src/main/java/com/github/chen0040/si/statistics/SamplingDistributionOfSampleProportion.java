package com.github.chen0040.si.statistics;


import com.github.chen0040.si.enums.DistributionFamily;
import com.github.chen0040.si.exceptions.VariableWrongValueTypeException;


/**
 * Created by xschen on 3/5/2017.
 * A sampling distribution is a theoretical distribution of the values that a specified statistic of a sample takes on in all of the possible samples of a specific size that can be made from a given population. We have not discussed sampling distributions before.
 * Note that we can have sampling distributions of sample means, of sample standard deviations, etc.
 */
public class SamplingDistributionOfSampleProportion {

   // the sample statistics
   private final double sampleProportion;

   private final int sampleSize;

   private final DistributionFamily distributionFamily;

   // the standard deviation of the sampling distribution of sample proportions
   private final double standardError;

   private final String groupId;

   public SamplingDistributionOfSampleProportion(SampleDistribution sampleDistribution) {
      if(!sampleDistribution.isNumeric()) {
         throw new VariableWrongValueTypeException("Sampling distribution for sample proportions is not defined for numeric variable");
      }

      double p = sampleDistribution.getProportion();
      this.sampleProportion = p;
      this.sampleSize = sampleDistribution.getSampleSize();

      this.standardError = calculateStandardError(p);

      int successCount = (int)(this.sampleSize * p);
      int failureCount = (int)(this.sampleSize * (1-p));

      if(successCount < 10 || failureCount < 10) {
         distributionFamily = DistributionFamily.StudentT;
      } else {
         distributionFamily = DistributionFamily.Normal;
      }
      this.groupId = sampleDistribution.getGroupId();
   }

   public SamplingDistributionOfSampleProportion(double p, int sampleSize, String groupId) {


      this.sampleProportion = p;
      this.sampleSize = sampleSize;

      this.standardError = calculateStandardError(p);

      int successCount = (int)(this.sampleSize * p);
      int failureCount = (int)(this.sampleSize * (1-p));

      if(successCount < 10 || failureCount < 10) {
         distributionFamily = DistributionFamily.StudentT;
      } else {
         distributionFamily = DistributionFamily.Normal;
      }

      this.groupId = groupId;
   }

   private double calculateStandardError(double p) {
      return p * (1 - p);
   }

   public double getSampleProportion() {
      return sampleProportion;
   }

   public double getStandardError() {
      return standardError;
   }

   public int getSampleSize(){
      return sampleSize;
   }
}
