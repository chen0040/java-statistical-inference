package com.github.chen0040.si.statistics;


import com.github.chen0040.si.enums.DistributionFamily;
import com.github.chen0040.si.exceptions.VariableWrongValueTypeException;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.exception.OutOfRangeException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;


/**
 * Created by xschen on 3/5/2017.
 * A sampling distribution is a theoretical distribution of the values that a specified statistic of a sample takes on in all of the possible samples of a specific size that can be made from a given population. We have not discussed sampling distributions before.
 * Note that we can have sampling distributions of sample means, of sample standard deviations, etc.
 */
public class SamplingDistributionOfSampleProportion {

   // point estimate of the sample proportion p_bar
   private final double sampleProportionPointEstimate;

   private final int sampleSize;

   private final DistributionFamily distributionFamily;

   // the standard error of the point estimate, which is the standard deviation of the sampling distribution of sample proportions
   private final double standardError;

   private final String groupId;

   // degrees of freedom of p_bar
   private final double df;

   public SamplingDistributionOfSampleProportion(SampleDistribution sampleDistribution) {
      if(!sampleDistribution.isNumeric()) {
         throw new VariableWrongValueTypeException("Sampling distribution for sample proportions is not defined for numeric variable");
      }

      double p = sampleDistribution.getProportion();
      this.sampleProportionPointEstimate = p;
      this.sampleSize = sampleDistribution.getSampleSize();

      this.df = sampleSize - 1;

      this.standardError = calculateStandardError(p, this.sampleSize);

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


      this.sampleProportionPointEstimate = p;
      this.sampleSize = sampleSize;

      this.df = sampleSize - 1;

      this.standardError = calculateStandardError(p, sampleSize);

      int successCount = (int)(this.sampleSize * p);
      int failureCount = (int)(this.sampleSize * (1-p));

      if(successCount < 10 || failureCount < 10) {
         distributionFamily = DistributionFamily.StudentT;
      } else {
         distributionFamily = DistributionFamily.Normal;
      }

      this.groupId = groupId;
   }

   private double calculateStandardError(double p, int sampleSize) {
      return Math.sqrt(p * (1 - p) / sampleSize);
   }

   public double getSampleProportionPointEstimate() {
      return sampleProportionPointEstimate;
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
      double p_bar = sampleProportionPointEstimate;

      if(distributionFamily == DistributionFamily.Normal) {
         NormalDistribution distribution = new NormalDistribution(0, 1);
         double p_lo = (1.0 - confidenceLevel) / 2;
         double p_hi = 1.0 - p_lo;
         double Z = distribution.inverseCumulativeProbability(p_hi);

         return makeCI(new Interval(p_bar - Z * standardError, p_bar + Z * standardError), confidenceLevel);
      } else if(distributionFamily == DistributionFamily.StudentT) {
         TDistribution distribution = new TDistribution(df);
         double p_lo = (1.0 - confidenceLevel) / 2;
         double p_hi = 1.0 - p_lo;
         double t_df = distribution.inverseCumulativeProbability(p_hi);
         return makeCI(new Interval(p_bar - t_df * standardError, p_bar + t_df * standardError), confidenceLevel);
      } else {
         throw new NotImplementedException();
      }
   }

   private ConfidenceInterval makeCI(Interval interval, double confidenceLevel) {


      StringBuilder sb = new StringBuilder();
      sb.append("We are ").append(confidenceLevel * 100).append("% confident that");
      sb.append(" the proportion of \"").append(groupId).append("\" is ");
      sb.append(interval);

      return new ConfidenceInterval(interval, confidenceLevel, sb.toString());
   }



}