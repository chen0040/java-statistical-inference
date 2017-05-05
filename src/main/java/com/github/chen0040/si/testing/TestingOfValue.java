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
public class TestingOfValue {

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

   private int sampleSize;

   public TestingOfValue() {}

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

   public boolean willRejectH0(double signficanceLevel, boolean twoTails) {
      if(twoTails){
         return pValueTwoTails < signficanceLevel;
      } else {
         return pValueOneTail < signficanceLevel;
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
