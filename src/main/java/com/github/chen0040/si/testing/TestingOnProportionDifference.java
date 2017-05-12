package com.github.chen0040.si.testing;


import com.github.chen0040.si.enums.DistributionFamily;
import org.apache.commons.math3.distribution.NormalDistribution;


/**
 * Created by xschen on 9/5/2017.
 * H_0: the proportion difference p_1 = p_2
 *
 * H_A: the proportion difference p_1 != p_2
 *
 */
public class TestingOnProportionDifference {

   private final String successLabel;
   private final double pHat1;
   private final double pHat2;
   private final int n1;
   private final int n2;


   // the "pooled" proportion assumes to be the true p under null assumption;
   private final double pPooled;

   // standard error of p_1 - p_2 under null assumption;
   private final double standardError;

   private final double Z;

   private final double pValueOneTail;
   private final double pValueTwoTails;

   private final DistributionFamily distributionFamily = DistributionFamily.Normal;

   public TestingOnProportionDifference(String successLabel, double pHat1, double pHat2, int sampleSize1, int sampleSize2) {

      this.successLabel = successLabel;
      this.pHat1 = pHat1;
      this.pHat2 = pHat2;
      this.n1 = sampleSize1;
      this.n2 = sampleSize2;

      int successCount1 = (int)(pHat1 * sampleSize1);
      int failureCount1 = (int)((1-pHat1) * sampleSize1);
      int successCount2 = (int)(pHat2 * sampleSize2);
      int failureCount2 = (int)((1-pHat2) * sampleSize2);

      if(successCount1 < 10 || successCount2 <10 || failureCount1 < 10 || failureCount2 < 10) {
         throw new RuntimeException("Sample size too small for the testing to proceed, suggesting to use bootstrap simulation instead");
      }

      pPooled = (pHat1 + pHat2) / 2;

      standardError = Math.sqrt(pPooled * (1-pPooled) / sampleSize1 + pPooled * (1-pPooled) /sampleSize2);

      Z = (pHat1 - pHat2) / standardError;

      NormalDistribution distribution = new NormalDistribution(0.0, 1.0);
      double cp = distribution.cumulativeProbability(Z);

      pValueOneTail = 1 - cp;
      pValueTwoTails = pValueOneTail * 2;


   }

   public String getSummary(){
      StringBuilder sb = new StringBuilder();
      sb.append("group 1: sample proportion: ").append(pHat1).append(" sample size: ").append(n1);
      sb.append("\ngroup 2: sample proportion: ").append(pHat2).append(" sample size: ").append(n2);

      sb.append("\nSE of sample difference distribution: ").append(standardError);


      sb.append("\nDistribution is ").append(distributionFamily);
      sb.append("\ntest statistic: ").append(Z);
      sb.append("\np-value (one-tail): ").append(pValueOneTail);
      sb.append("\np-value (two-tail): ").append(pValueTwoTails);


      return sb.toString();
   }

   @Override
   public String toString(){
      return getSummary();
   }


   public void report() {
      System.out.println(toString());
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

}
