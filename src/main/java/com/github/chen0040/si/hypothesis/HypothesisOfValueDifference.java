package com.github.chen0040.si.hypothesis;


import com.github.chen0040.si.enums.DistributionFamily;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.TDistribution;


/**
 * Created by xschen on 4/5/2017.
 *
 * Hypothesis for difference between two difference population using samples from them
 * H_0: mu_1 == mu_2
 *   in other words values are not different for group1 and group 2
 *
 * H_A: mu_1 != mu_2
 *   in other words values are different for group1 and group 2
 */
@Getter
@Setter
public class HypothesisOfValueDifference {

   // point estimate of sample mean in group 1
   private double xHat1;

   // point estimate of sample mean in group 2
   private double xHat2;

   // sample standard deviation in group 1
   private double s1;

   // sample standard deviation in group 2
   private double s2;

   // sample size for group 1
   private int n1;

   // sample size for group 2
   private int n2;

   // test statistic
   private double testStatistic;

   // the standard error of the sampling distribution for the sample difference
   private double SE;

   // the probability of observed or more extreme cases assume the null hypothesis is true
   private double pValueOneTail;
   private double pValueTwoTail;

   private double df;

   private DistributionFamily distributionFamily;

   public HypothesisOfValueDifference(){

   }

   public void run(double xHat1,double xHat2, double s1, double s2, int n1, int n2){
      this.xHat1 = xHat1;
      this.xHat2 = xHat2;
      this.s1 = s1;
      this.s2 = s2;
      this.n1 = n1;
      this.n2 = n2;

      SE = Math.sqrt(s1 * s1 / n1 + s2 * s2 / n2);

      df = Math.min(n1-1, n2-1);

      double nullValue = 0;
      if(n1 < 30 || n2 < 30) {

         double t_df = ((xHat1 - xHat2) - nullValue) / SE;

         TDistribution distribution = new TDistribution(df);
         double cp = distribution.cumulativeProbability(t_df);
         pValueOneTail = 1 - cp;
         pValueTwoTail = pValueOneTail * 2;
         testStatistic = t_df;
         distributionFamily = DistributionFamily.StudentT;
      } else {
         double Z = ((xHat1 - xHat2) - nullValue) / SE;
         NormalDistribution distribution = new NormalDistribution(0, 1.0);
         double cp = distribution.cumulativeProbability(Z);
         pValueOneTail = 1 - cp;
         pValueTwoTail = pValueOneTail * 2;
         testStatistic = Z;
         distributionFamily = DistributionFamily.Normal;
      }

   }

   public String getSummary(){
      StringBuilder sb = new StringBuilder();
      sb.append("group 1: sample mean: ").append(xHat1).append(" sample sd: ").append(s1).append(" sample size: ").append(n1);
      sb.append("\ngroup 2: sample mean: ").append(xHat2).append(" sample sd: ").append(s2).append(" sample size: ").append(n2);

      sb.append("\nSE of sample difference distribution: ").append(SE);

      if(distributionFamily == DistributionFamily.StudentT) {
         sb.append("\ndegrees of freedom: ").append(df);
      }

      sb.append("\nDistribution is ").append(distributionFamily);
      sb.append("\ntest statistic: ").append(testStatistic);
      sb.append("\np-value (one-tail): ").append(pValueOneTail);
      sb.append("\np-value (two-tail): ").append(pValueTwoTail);


      return sb.toString();
   }

   @Override
   public String toString(){
      return getSummary();
   }


   public void report() {
      System.out.println(toString());
   }

   public boolean willRejectH0(double signficanceLevel, boolean twoTails) {
      if(twoTails){
         return pValueTwoTail < signficanceLevel;
      } else {
         return pValueOneTail < signficanceLevel;
      }
   }
}
