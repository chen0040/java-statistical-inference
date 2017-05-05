package com.github.chen0040.si.testing;


import com.github.chen0040.si.enums.DistributionFamily;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.TDistribution;


/**
 * Created by xschen on 3/5/2017.
 * H_0: mu = mu_null
 *     that is the true population mean is equal to mu_null
 *
 * H_A: mu != mu_null
 */
@Getter
@Setter
public class TestingOnValue {

   // point estimate of sample mean
   private double xHat;

   // null value that is assumed to be the true mean of the population given that H_0 is true
   private double xNull;

   // point estimate of sample deviation
   private double sampleSd;

   // standardError of sample mean
   private double standardError;

   // degrees of freedom;
   private double df;

   private DistributionFamily distributionFamily;

   private double testStatistic;

   private double significanceLevel;

   private double pValueOneTail;
   private double pValueTwoTails;

   // sample size
   private int sampleSize;

   public TestingOnValue() {}

   /**
    * The method computes the p-value for both one-tail and two-tails testing on the mean of a numerical variable for a population
    * @param xHat the point estimate of the sample mean
    * @param s the point estimate of the sample standard deviation
    * @param n the sample size
    * @param xNull the null value for the mean of the population under null hypothesis
    */
   public void run(double xHat, double s, int n, double xNull) {
      run(xHat, s, n, xNull, 0.05);
   }


   /**
    * The method computes the p-value for both one-tail and two-tails testing
    * @param xHat the point estimate of the sample mean
    * @param s the point estimate of the sample standard deviation
    * @param n the sample size
    * @param xNull the null value for the mean of the population under null hypothesis
    * @param significanceLevel the default significance level included
    */
   public void run(double xHat, double s, int n, double xNull, double significanceLevel) {

      sampleSize = n;
      this.xHat = xHat;
      this.sampleSd = s;
      this.xNull = xNull;

      if(n >= 30) {
         distributionFamily = DistributionFamily.Normal;
      } else {
         distributionFamily = DistributionFamily.StudentT;
      }

      standardError = s / Math.sqrt(n);

      df = n - 1;

      this.significanceLevel = significanceLevel;

      if(distributionFamily == DistributionFamily.Normal) {
         NormalDistribution distribution = new NormalDistribution(0.0, 1.0);
         double Z = (xHat - xNull) / standardError;

         double cp = distribution.cumulativeProbability(Z);
         pValueOneTail = 1 - cp;
         pValueTwoTails = pValueOneTail * 2;

         testStatistic = Z;
      } else {
         TDistribution distribution = new TDistribution(df);

         double t_df = (xHat - xNull) / standardError;

         double cp = distribution.cumulativeProbability(t_df);
         pValueOneTail = 1 - cp;
         pValueTwoTails = pValueOneTail * 2;
      }

   }


   /**
    * The method reject null hypothesis if the p-value calculated from the sample is smaller than the significance level
    * @param significanceLevel the significance level, usually about 0.05
    * @param twoTails true if the testing is two tails; false otherwise
    * @return true if the null hypothesis H_0 is rejected; false if H_0 fails to be rejected
    */
   public boolean willRejectH0(double significanceLevel, boolean twoTails) {
      if(twoTails){
         return pValueTwoTails < significanceLevel;
      } else {
         return pValueOneTail < significanceLevel;
      }
   }

   public String getSummary() {
      StringBuilder sb = new StringBuilder();

      sb.append("Sample mean: ").append(xHat).append(" Sample size: ").append(sampleSize);
      sb.append("\nDistribution: ").append(distributionFamily);
      sb.append("\np-value (one-tail): ").append(pValueOneTail);
      sb.append("\np-value (two-tails): ").append(pValueTwoTails);

      if(significanceLevel > 0) {
         sb.append("\nSuppose significance level is ").append(significanceLevel);
         sb.append("\n\tpopulation proportion is ").append(pValueOneTail < significanceLevel ? "!=" : "==").append(" ").append(xNull).append(" under one-tail test");
         sb.append("\n\tpopulation proportion is ").append(pValueTwoTails < significanceLevel ? "!=" : "==").append(" ").append(xNull).append(" under two-tails test");
      }

      return sb.toString();
   }

   @Override
   public String toString(){
      return getSummary();
   }

   public void report(){
      System.out.println(toString());
   }
}
