package com.github.chen0040.si.statistics;


import com.github.chen0040.si.enums.DistributionFamily;
import com.github.chen0040.si.exceptions.VariableWrongValueTypeException;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.OutOfRangeException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;


/**
 * Created by xschen on 3/5/2017.
 * A sampling distribution is a theoretical distribution of the values that a specified statistic of a sample takes on in all of the possible samples of a specific size that can be made from a given population. We have not discussed sampling distributions before.
 * Note that we can have sampling distributions of sample means, of sample standard deviations, etc.
 */
public class SamplingDistributionOfSampleMean {

   // the sample statistics
   private final double sampleMean;


   private final int sampleSize;

   // standard deviation, which could be population standard deviation sigma or sample stand deviation s_bar
   private final double sd;

   // the standard deviation of the sampling distribution of sample means
   private final double standardError;

   // degrees of freedom
   private final double df;

   private final DistributionFamily distributionFamily;

   private final String groupId;

   // if the CLT holds for the sample mean x_bar, then:
   // x_bar ~N(mu, SE)
   // SE = sigma / sqrt(n)
   // where mu is the true population mean
   //       SE is the standard error (which is the standard deviation of the sampling distribution)
   //       sigma is the population standard deviation (can be estimated by the sample standard deviation s_bar)
   //       n is the sample size
   public SamplingDistributionOfSampleMean(SampleDistribution sampleDistribution){
      if(sampleDistribution.isCategorical()){
         throw new VariableWrongValueTypeException("Sampling distribution of sample means are not defined for categorical variable");
      }
      this.sampleMean = sampleDistribution.getSampleMean();
      this.sd = sampleDistribution.getSampleSd();
      this.sampleSize = sampleDistribution.getSampleSize();
      this.standardError = calculateStandardError(sd, sampleSize);
      this.df = sampleSize - 1;

      if(sampleSize < 30) {
         distributionFamily = DistributionFamily.StudentT;
      } else {
         distributionFamily = DistributionFamily.Normal;
      }

      this.groupId = sampleDistribution.getGroupId();
   }

   // if the CLT holds for the sample mean x_bar, then:
   // x_bar ~N(mu, SE)
   // SE = sigma / sqrt(n)
   // where mu is the true population mean
   //       SE is the standard error (which is the standard deviation of the sampling distribution)
   //       sigma is the population standard deviation (can be estimated by the sample standard deviation s_bar)
   //       n is the sample size
   public SamplingDistributionOfSampleMean(double sampleMean, int sampleSize, double sd, String groupId) {
      this.sampleMean = sampleMean;
      this.sampleSize = sampleSize;
      this.sd = sd;
      this.standardError = calculateStandardError(sd, sampleSize);
      this.df = sampleSize - 1;

      if(sampleSize < 30) {
         distributionFamily = DistributionFamily.StudentT;
      } else {
         distributionFamily = DistributionFamily.Normal;
      }

      this.groupId = groupId;
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

   public Interval confidenceInterval(double confidenceLevel) {
      if(confidenceLevel < 0 || confidenceLevel > 1) {
         throw new OutOfRangeException(confidenceLevel, 0, 1);
      }
      if(distributionFamily == DistributionFamily.Normal) {
         NormalDistribution distribution = new NormalDistribution(sampleMean, standardError);
         double p_lo = (1.0 - confidenceLevel) / 2;
         double p_hi = 1.0 - p_lo;
         double q_hi = distribution.inverseCumulativeProbability(p_hi);
         double q_lo = distribution.inverseCumulativeProbability(p_lo);
         return new Interval(q_lo, q_hi);
      } else if(distributionFamily == DistributionFamily.StudentT) {
         throw new NotImplementedException();
      } else {
         throw new NotImplementedException();
      }
   }
}
