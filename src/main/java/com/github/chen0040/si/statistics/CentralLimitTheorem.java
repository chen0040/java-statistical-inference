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
public class CentralLimitTheorem {

   private static Logger logger = LoggerFactory.getLogger(CentralLimitTheorem.class);

   // CentralLimitTheorem hold for proportion if
   // 1.
   // * observations int the samples are randomly sampled or assigned
   // * if sampling without replacement, the sample size should be less than 10% of the true population size
   // 2
   // * success-count >= 10 and failure-count >= 10 in each sample
   // * success-count and failure-count should be larger if the true population is skewed in distribution
   public static boolean isHeld4SampleProportion(Sample sample, String successLabel, String groupId) {


      if(sample.isNumeric()) {
         logger.warn("0 (failed): only CTL for proportion works for categorical variable");
         throw new VariableWrongValueTypeException("Only CTL for proportion works for categorical variable");
      } else {
         logger.info("0 (success): sample contains categorical variable");
      }

      // observations in the sample must be randomly sampled or assigned
      if(!sample.isRandomlySampledOrAssigned()) {
         logger.warn("0 (failed): observations in the sample must be randomly sampled or assigned");
         return false;
      } else {
         logger.info("0 (success): randomly sampled or assigned");
      }

      int n = sample.countByGroupId(groupId);
      // if sampling without replacement, then the sample size must be smaller than 10% of the true population under study
      if(!sample.isSampledWithReplacement()){
         if(n >= sample.getTruePopulationSize() * 10 / 100) {
            logger.warn("if sampled without replacement, then the sample size must be smaller than 10% of the true population under study");
            return false;
         } else {
            logger.info("1 (success): sampling without replacement, the sample size is smaller than 10% of the population size");
         }
      } else {
         logger.info("1 (success): sampling with replacement");
      }

      double p = sample.proportion(successLabel, groupId);
      int successCount = (int)(p * n);
      int failureCount = (int)((1-p) * n);

      if(successCount >= 10 && failureCount >= 10){
         logger.info("2 (success): success and failures both greater than or equal to 10");
         return true;
      } else {
         logger.warn("2 (failed): success or failures smaller than 10");
         return false;
      }
   }

   // CentralLimitTheorem hold for sample mean if
   // 1.
   // * observations int the samples are randomly sampled or assigned
   // * if sampling without replacement, the sample size should be less than 10% of the true population size
   // 2
   // * sample size >= 30 in each sample
   // * sample size should be larger if the true population is skewed in distribution
   public static boolean isHeld4SampleMean(Sample sample, String groupId) {
      if(!sample.isNumeric()) {
         logger.error("0 (failed): only CTL for proportion works for categorical variable");
         throw new VariableWrongValueTypeException("Only CTL for proportion works for categorical variable");
      } else {
         logger.info("0 (success): numerical variable is present in the sample");
      }

      // observations in the sample must be randomly sampled or assigned
      if(!sample.isRandomlySampledOrAssigned()) {
         logger.warn("1 (failed): observations in the sample must be randomly sampled or assigned");
         return false;
      } else {
         logger.info("1 (success): sample is randomly sampled or assigned");
      }

      int n = sample.countByGroupId(groupId);
      // if sampling without replacement, then the sample size must be smaller than 10% of the true population under study
      if(!sample.isSampledWithReplacement()){
         if(n >= sample.getTruePopulationSize() * 10 / 100) {
            logger.warn("1 (failed): if sampled without replacement, then the sample size must be smaller than 10% of the true population under study");
            return false;
         } else {
            logger.info("1 (success): sampling without replacement, with sample size smaller than 10% of the population size");
         }
      } else {
         logger.info("1 (success): sampling with replacement");
      }




      if(sample.countByGroupId(groupId) >= 30){
         logger.info("2 (success): sample size > 30");
         return true;
      } else {
         logger.warn("2 (failed): sample must be greater or equal to 30 for CLT");
         return false;
      }
   }
}
