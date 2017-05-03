package com.github.chen0040.si.statistics;


import com.github.chen0040.si.exceptions.VariableWrongValueTypeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by xschen on 3/5/2017.
 * Central Limit Theorem
 * The implementation gives the sufficiently condition for two population statistics
 * 1. sample mean (for numerical variable)
 * 2. sample proportion (for categorical variable)
 */
public class CLT {

   private static Logger logger = LoggerFactory.getLogger(CLT.class);

   // CLT hold for proportion if
   // 1.
   // * observations int the samples are randomly sampled or assigned
   // * if sampling without replacement, the sample size should be less than 10% of the true population size
   // 2
   // * success-count >= 10 and failure-count >= 10 in each sample
   // * success-count and failure-count should be larger if the true population is skewed in distribution
   public boolean isHeld4SampleProportion(Sample sample, String successLabel, String groupId) {

      // observations in the sample must be randomly sampled or assigned
      if(!sample.isRandomlySampledOrAssigned()) {
         logger.warn("observations in the sample must be randomly sampled or assigned");
         return false;
      }

      int n = sample.size(groupId);
      // if sampling without replacement, then the sample size must be smaller than 10% of the true population under study
      if(sample.isSampledWithReplacement() && n >= sample.getTruePopulationSize() * 10 / 100){
         logger.warn("if sampled without replacement, then the sample size must be smaller than 10% of the true population under study");
         return false;
      }


      if(!sample.isNumeric()) {
         throw new VariableWrongValueTypeException("Only CTL for proportion works for categorical variable");
      }

      double p = sample.proportion(successLabel, groupId);
      int successCount = (int)(p * n);
      int failureCount = (int)((1-p) * n);

      return successCount >= 10 && failureCount >= 10;
   }

   // CLT hold for sample mean if
   // 1.
   // * observations int the samples are randomly sampled or assigned
   // * if sampling without replacement, the sample size should be less than 10% of the true population size
   // 2
   // * sample size >= 30 in each sample
   // * sample size should be larger if the true population is skewed in distribution
   public boolean isHeld4SampleMean(Sample sample, String groupId) {

      // observations in the sample must be randomly sampled or assigned
      if(!sample.isRandomlySampledOrAssigned()) {
         logger.warn("observations in the sample must be randomly sampled or assigned");
         return false;
      }

      int n = sample.size(groupId);
      // if sampling without replacement, then the sample size must be smaller than 10% of the true population under study
      if(sample.isSampledWithReplacement() && n >= sample.getTruePopulationSize() * 10 / 100){
         logger.warn("if sampled without replacement, then the sample size must be smaller than 10% of the true population under study");
         return false;
      }


      if(!sample.isNumeric()) {
         throw new VariableWrongValueTypeException("Only CTL for proportion works for categorical variable");
      }

      return sample.size(groupId) >= 30;
   }
}
