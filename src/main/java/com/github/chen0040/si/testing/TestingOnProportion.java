package com.github.chen0040.si.testing;


import com.github.chen0040.si.enums.DistributionFamily;
import com.github.chen0040.si.misc.Simulation;
import com.github.chen0040.si.statistics.Count;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.math3.distribution.NormalDistribution;

import java.util.List;


/**
 * Created by xschen on 3/5/2017.
 * H_0: p_null = p_null
 *    that is proportion p_null is p_null
 *
 * H_A: p_null != p_null
 *    that is the true proportion p_null is not equal p_null
 */
@Getter
@Setter
public class TestingOnProportion {

   // point estimate of proportion (observed value)
   private double pHat;

   // null value of proportion (value assumed to be the true proportion in the null hypothesis)
   private double pNull;

   // sample size
   private int sampleSize;

   // test statistic under null hypothesis, defined only if normal distribution holds for the sample proportion under CLT
   private double testStatistic;

   // probability of observed event or more extreme cases assuming the null hypothesis is true
   private double pValueOneTail;
   private double pValueTwoTails;

   private DistributionFamily distributionFamily;

   private double significanceLevel;

   private int simulationCount = 100;


   // pHat: observed sample proportion
   // p_null: expected proportion proposed by the null hypothesis
   public TestingOnProportion(){

   }

   public void run(double pHat, int sampleSize, double p){
      run(pHat, sampleSize, p, -1);
   }

   public void run(double pHat, int sampleSize, double pNull, double significanceLevel) {

      this.pHat = pHat;
      this.pNull = pNull;

      this.sampleSize = sampleSize;

      int successCount = (int)(sampleSize * pNull);
      int failureCount = (int)(sampleSize * (1-pNull));

      if(successCount < 10 || failureCount < 10) {
         distributionFamily = DistributionFamily.SimulationOnly;
         List<Double> proportions = Simulation.binomial(pNull, sampleSize, simulationCount);

         double cp = Count.cumulativeProbability(proportions, pHat);
         System.out.println(cp);
         pValueOneTail = 1 - cp;
         pValueTwoTails = pValueOneTail * 2;


      } else {
         // standard error of the sample proportion under null hypothesis
         double standardError = calculateStandardError(pNull, sampleSize);

         double Z = (pHat - pNull) / standardError;

         NormalDistribution distribution = new NormalDistribution(0, 1.0);
         double cp = distribution.cumulativeProbability(Z);
         pValueOneTail = 1 - cp;
         pValueTwoTails = pValueOneTail * 2;

         distributionFamily = DistributionFamily.Normal;
         testStatistic = Z;
      }

      this.significanceLevel = significanceLevel;
   }

   private double calculateStandardError(double p, double n ){
      return Math.sqrt(p * (p-1) / n);
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

      sb.append("Sample proportion: ").append(pHat).append(" Sample size: ").append(sampleSize);
      sb.append("\nDistribution: ").append(distributionFamily);
      sb.append("\np-value (one-tail): ").append(pValueOneTail);
      sb.append("\np-value (two-tails): ").append(pValueTwoTails);

      if(significanceLevel > 0) {
         sb.append("\nSuppose significance level is ").append(significanceLevel);
         sb.append("\n\tpopulation proportion is ").append(pValueOneTail < significanceLevel ? "!=" : "==").append(" ").append(pNull).append(" under one-tail test");
         sb.append("\n\tpopulation proportion is ").append(pValueTwoTails < significanceLevel ? "!=" : "==").append(" ").append(pNull).append(" under two-tails test");
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
