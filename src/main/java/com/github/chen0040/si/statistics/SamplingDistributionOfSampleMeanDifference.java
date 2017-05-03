package com.github.chen0040.si.statistics;


import com.github.chen0040.si.enums.DistributionFamily;
import com.github.chen0040.si.exceptions.VariableWrongValueTypeException;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.exception.OutOfRangeException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;


/**
 * Created by xschen on 3/5/2017.
 * This is the sampling distribution of (x_bar_1 - x_bar_2), where
 * x_bar_1 is the sample mean of group 1
 * x_bar_2 is the sample mean of group 2
 *
 * The group id here is referred to as the exploratory variable and is a categorical variable
 *
 *
 *
 */
public class SamplingDistributionOfSampleMeanDifference {
   // point estimate of sample mean x_bar_1
   private final double sampleMean1PointEstimate;

   // point estimate of sample mean x_bar_2
   private final double sampleMean2PointEstimate;

   private final int sampleSize1;

   private final int sampleSize2;

   // standard deviation of x_1, which could be population standard deviation sigma or sample stand deviation s_bar
   private final double sd1;

   // standard deviation of x_2, which could be population standard deviation sigma or sample stand deviation s_bar
   private final double sd2;

   // the standard deviation of the sampling distribution of sample means
   private final double standardError;

   // degrees of freedom of x_bar_1 - x_bar_2
   private final double df;

   private final DistributionFamily distributionFamily;

   private final String groupId1;
   private final String groupId2;

   // if the CLT holds for the sample mean x_bar, then:
   // x_bar ~N(mu, SE)
   // SE = sigma / sqrt(n)
   // where mu is the true population mean
   //       SE is the standard error (which is the standard deviation of the sampling distribution)
   //       sigma is the population standard deviation (can be estimated by the sample standard deviation s_bar)
   //       n is the sample size
   public SamplingDistributionOfSampleMeanDifference(SampleDistribution sampleDistribution1, SampleDistribution sampleDistribution2){
      if(sampleDistribution1.isCategorical()){
         throw new VariableWrongValueTypeException("Sampling distribution of sample means are not defined for categorical variable");
      }
      this.sampleMean1PointEstimate = sampleDistribution1.getSampleMean();
      this.sampleMean2PointEstimate = sampleDistribution2.getSampleMean();

      this.sd1 = sampleDistribution1.getSampleSd();
      this.sd2 = sampleDistribution2.getSampleSd();

      this.sampleSize1 = sampleDistribution1.getSampleSize();
      this.sampleSize2 = sampleDistribution2.getSampleSize();

      this.standardError = calculateStandardError(sd1, sd2, sampleSize1, sampleSize2);
      this.df = Math.min(sampleSize1 - 1, sampleSize2 - 1);

      if(sampleSize1 < 30 || sampleSize2 < 30) {
         distributionFamily = DistributionFamily.StudentT;
      } else {
         distributionFamily = DistributionFamily.Normal;
      }

      this.groupId1 = sampleDistribution1.getGroupId();
      this.groupId2 = sampleDistribution2.getGroupId();
   }

   // if the CLT holds for the sample mean x_bar, then:
   // x_bar ~N(mu, SE)
   // SE = sigma / sqrt(n)
   // where mu is the true population mean
   //       SE is the standard error (which is the standard deviation of the sampling distribution)
   //       sigma is the population standard deviation (can be estimated by the sample standard deviation s_bar)
   //       n is the sample size
   public SamplingDistributionOfSampleMeanDifference(double sampleMean1PointEstimate, double sampleMean2PointEstimate, int sampleSize1, int sampleSize2, double sd1, double sd2, String groupId1, String groupId2) {
      this.sampleMean1PointEstimate = sampleMean1PointEstimate;
      this.sampleMean2PointEstimate = sampleMean2PointEstimate;

      this.sampleSize1 = sampleSize1;
      this.sampleSize2 = sampleSize2;

      this.sd1 = sd1;
      this.sd2 = sd2;

      this.standardError = calculateStandardError(this.sd1, this.sd2, this.sampleSize1, this.sampleSize2);

      this.df = Math.min(this.sampleSize1 - 1, this.sampleSize2 - 1);

      if(this.sampleSize1 < 30 || this.sampleSize2 < 30) {
         distributionFamily = DistributionFamily.StudentT;
      } else {
         distributionFamily = DistributionFamily.Normal;
      }

      this.groupId1 = groupId1;
      this.groupId2 = groupId2;
   }

   private double calculateStandardError(double sd1, double sd2, int sampleSize1, int sampleSize2) {
      return Math.sqrt(sd1 * sd1 / sampleSize1 + sd2 * sd2 / sampleSize2);
   }

   public double getSampleMean1PointEstimate() {
      return sampleMean1PointEstimate;
   }

   public double getSampleMean2PointEstimate(){
      return sampleMean2PointEstimate;
   }

   public double getSampleMeanDifferencePointEstimate(){
      return sampleMean1PointEstimate - sampleMean2PointEstimate;
   }

   public double getStandardError() {
      return standardError;
   }

   public int getSampleSize1(){
      return sampleSize1;
   }

   public int getSampleSize2(){
      return sampleSize2;
   }

   public Interval confidenceInterval(double confidenceLevel) {
      if(confidenceLevel < 0 || confidenceLevel > 1) {
         throw new OutOfRangeException(confidenceLevel, 0, 1);
      }
      double dx_bar = sampleMean1PointEstimate - sampleMean2PointEstimate;

      if(distributionFamily == DistributionFamily.Normal) {
         NormalDistribution distribution = new NormalDistribution(0, 1);
         double p_lo = (1.0 - confidenceLevel) / 2;
         double p_hi = 1.0 - p_lo;
         double Z = distribution.inverseCumulativeProbability(p_hi);
         return new Interval(dx_bar - Z * standardError, dx_bar + Z * standardError);
      } else if(distributionFamily == DistributionFamily.StudentT) {
         TDistribution distribution = new TDistribution(df);
         double p_lo = (1.0 - confidenceLevel) / 2;
         double p_hi = 1.0 - p_lo;
         double t_df = distribution.inverseCumulativeProbability(p_hi);
         return new Interval(dx_bar - t_df * standardError, dx_bar + t_df * standardError);
      } else {
         throw new NotImplementedException();
      }
   }
}
