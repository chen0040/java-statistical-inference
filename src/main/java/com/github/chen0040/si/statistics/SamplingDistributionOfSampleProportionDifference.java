package com.github.chen0040.si.statistics;


import com.github.chen0040.si.enums.DistributionFamily;
import com.github.chen0040.si.exceptions.VariableWrongValueTypeException;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.exception.OutOfRangeException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;


/**
 * Created by xschen on 3/5/2017.
 * This is the sampling distribution of (p_bar_1 - p_bar_2), where
 * p_bar_1 is the sample proportion of group 1
 * p_bar_2 is the sample proportion of group 2
 *
 * The group id here is referred to as the exploratory variable and is a categorical variable
 */
public class SamplingDistributionOfSampleProportionDifference {

   // p_hat_1: point estimate of the sample proportion p_bar_1
   private final double sampleProportion1PointEstimate;

   // p_hat_2: point estimate of the sample proportion p_bar_2
   private final double sampleProportion2PointEstimate;

   private final int sampleSize1;

   private final int sampleSize2;

   private final DistributionFamily distributionFamily;

   // the standard deviation of the sampling distribution of sample proportions
   private final double standardError;

   // degrees of freedom of p_bar_1 - p_bar_2
   private final double df;

   private final String groupId1;

   private final String groupId2;

   public SamplingDistributionOfSampleProportionDifference(SampleDistribution sampleDistribution1, SampleDistribution sampleDistribution2) {
      if(!sampleDistribution1.isNumeric()) {
         throw new VariableWrongValueTypeException("Sampling distribution for sample proportions is not defined for numeric variable");
      }

      double p1 = sampleDistribution1.getProportion();
      double p2 = sampleDistribution2.getProportion();

      this.sampleProportion1PointEstimate = p1;
      this.sampleProportion2PointEstimate = p2;

      this.sampleSize1 = sampleDistribution1.getSampleSize();
      this.sampleSize2 = sampleDistribution2.getSampleSize();

      this.standardError = calculateStandardError(p1, p2, this.sampleSize1, this.sampleSize2);

      int successCount1 = (int)(this.sampleSize1 * p1);
      int failureCount1 = (int)(this.sampleSize1 * (1-p1));

      int successCount2 = (int)(this.sampleSize2 * p2);
      int failureCount2 = (int)(this.sampleSize2 * (1-p2));

      this.df = Math.min(sampleSize1 - 1, sampleSize2 - 1);

      if(successCount1 < 10 || failureCount1 < 10 || successCount2 < 10 || failureCount2 < 10) {
         distributionFamily = DistributionFamily.StudentT;
      } else {
         distributionFamily = DistributionFamily.Normal;
      }

      this.groupId1 = sampleDistribution1.getGroupId();
      this.groupId2 = sampleDistribution2.getGroupId();
   }

   public SamplingDistributionOfSampleProportionDifference(double p1, double p2, int sampleSize1, int sampleSize2, String groupId1, String groupId2) {


      this.sampleProportion1PointEstimate = p1;
      this.sampleProportion2PointEstimate = p2;

      this.sampleSize1 = sampleSize1;
      this.sampleSize2 = sampleSize2;

      this.df = Math.min(sampleSize1 - 1, sampleSize2 - 1);

      this.standardError = calculateStandardError(p1, p2, sampleSize1, sampleSize2);

      int successCount1 = (int)(this.sampleSize1 * p1);
      int failureCount1 = (int)(this.sampleSize1 * (1-p1));

      int successCount2 = (int)(this.sampleSize2 * p2);
      int failureCount2 = (int)(this.sampleSize2 * (1-p2));

      if(successCount1 < 10 || failureCount1 < 10 || successCount2 < 10 || failureCount2 < 10) {
         distributionFamily = DistributionFamily.StudentT;
      } else {
         distributionFamily = DistributionFamily.Normal;
      }

      this.groupId1 = groupId1;
      this.groupId2 = groupId2;
   }

   private double calculateStandardError(double p1, double p2, int n1, int n2) {
      return Math.sqrt(p1 * (1 - p1) / n1 + p2 * (1 - p2) / n2);
   }

   public double getSampleProportion1PointEstimate() {
      return sampleProportion1PointEstimate;
   }

   public double getSampleProportion2PointEstimate() {
      return sampleProportion2PointEstimate;
   }

   public double getSampleProportionDifferencePointEstimate() {
      return sampleProportion1PointEstimate - sampleProportion2PointEstimate;
   }

   public double getStandardError() {
      return standardError;
   }

   public int getSampleSize1(){
      return sampleSize1;
   }

   public ConfidenceInterval confidenceInterval(double confidenceLevel) {
      if(confidenceLevel < 0 || confidenceLevel > 1) {
         throw new OutOfRangeException(confidenceLevel, 0, 1);
      }
      double dp_bar = sampleProportion1PointEstimate - sampleProportion2PointEstimate;

      if(distributionFamily == DistributionFamily.Normal) {
         NormalDistribution distribution = new NormalDistribution(0, 1);
         double p_lo = (1.0 - confidenceLevel) / 2;
         double p_hi = 1.0 - p_lo;
         double Z = distribution.inverseCumulativeProbability(p_hi);
         return makeCI(new Interval(dp_bar - Z * standardError, dp_bar + Z * standardError), confidenceLevel);
      } else if(distributionFamily == DistributionFamily.StudentT) {
         TDistribution distribution = new TDistribution(df);
         double p_lo = (1.0 - confidenceLevel) / 2;
         double p_hi = 1.0 - p_lo;
         double t_df = distribution.inverseCumulativeProbability(p_hi);
         return makeCI(new Interval(dp_bar - t_df * standardError, dp_bar + t_df * standardError), confidenceLevel);
      } else {
         throw new NotImplementedException();
      }
   }

   private ConfidenceInterval makeCI(Interval interval, double confidenceLevel) {


      StringBuilder sb = new StringBuilder();
      sb.append("We are ").append(confidenceLevel * 100).append("% confident that");
      sb.append(" the proportion of \"").append(groupId1).append("\" is ");
      sb.append(interval).append(" higher than the proportion of \"").append(groupId2).append("\"");

      return new ConfidenceInterval(interval, confidenceLevel, sb.toString());
   }
}
