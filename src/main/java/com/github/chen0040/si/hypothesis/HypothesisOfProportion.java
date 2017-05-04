package com.github.chen0040.si.hypothesis;


import com.github.chen0040.si.enums.DistributionFamily;
import com.github.chen0040.si.statistics.Count;
import org.apache.commons.math3.distribution.NormalDistribution;

import java.util.List;


/**
 * Created by xschen on 3/5/2017.
 */
public class HypothesisOfProportion {

   // point estimate of proportion (observed value)
   private double p_hat;

   // null value of proportion (value assumed to be the proportion in the null hypothesis)
   private double p;

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


   // p_hat: observed sample proportion
   // p: expected proportion proposed by the null hypothesis
   public HypothesisOfProportion(){

   }

   public void run(double p_hat, int sampleSize, double p){
      run(p_hat, sampleSize, p, -1);
   }

   public void run(double p_hat, int sampleSize, double p, double significanceLevel) {

      this.p_hat = p_hat;
      this.p = p;

      this.sampleSize = sampleSize;

      int successCount = (int)(sampleSize * p);
      int failureCount = (int)(sampleSize * (1-p));

      if(successCount < 10 || failureCount < 10) {
         distributionFamily = DistributionFamily.SimulationOnly;
         List<Double> proportions = Simulation.binomial(p, sampleSize, simulationCount);

         double cp = Count.cumulativeProbability(proportions, p_hat);
         pValueOneTail = 1 - cp;
         pValueTwoTails = pValueOneTail * 2;


      } else {
         // standard error of the sample proportion under null hypothesis
         double standardError = calculateStandardError(p, sampleSize);

         double Z = (p_hat - p) / standardError;

         NormalDistribution distribution = new NormalDistribution(0, 1.0);
         double cmp = distribution.cumulativeProbability(Z);
         pValueOneTail = 1 - cmp;
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



}
