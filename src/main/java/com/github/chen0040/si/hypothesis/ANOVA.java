package com.github.chen0040.si.hypothesis;


import com.github.chen0040.si.statistics.Sample;
import com.github.chen0040.si.statistics.SampleDistribution;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.math3.distribution.FDistribution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by xschen on 3/5/2017.
 *
 * ANOVA (Analysis of Variance)
 * Conducted between one numerical variable and one categorical variable
 *
 * it is used to find whether there is a correlation between a numerical variable and a categorical variable for which the categorical
 * variable has more than two levels
 *
 * Suppose the sample mean of the numerical variables grouped by the categorical variables are:
 *
 * x_bar_1, x_bar_2, ..., x_bar_i
 *
 * where j \in (1, 2, i, ...) are the levels in the categorical variable
 *
 * Then the null hypothesis can be formulated as:
 * H_0: The mean outcome is the same across all categories, namely mu_1 = mu_2 = ... = mu_i = ...
 *      In other words, the numerical variable is independent of the categorical variable
 *
 * The alternative hypothesis can be formulated as:
 * H_A: There exists (j, k) where j, k are two levels in the categorical variable, such that mu_j != mu_k
 *
 * The numerical variable is called response variable while the categorical variable is called explanatory variable
 */
@Getter
@Setter
public class ANOVA {

   private static final Logger logger = LoggerFactory.getLogger(ANOVA.class);

   // SST: sum of squares total
   // calculated as \sum^n_{i=1} (y_i - y_bar)^2
   //  where y_i is the value of the numerical variable for each observation in the sample
   //        y_bar is the grand mean of the variable
   private double sumOfSquaresTotal;

   private double grandMean;

   // between group variability
   // explained variability (variability that can be explained by the categorical variable)
   private double sumOfSquaresGroup;

   // within group variability
   // unexplained variability (variability that cannot be explained by the categorical variable)
   private double sumOfSquaresError;

   // total degrees of freedom
   private double dfTotal;

   // degrees of freedom for groups
   private double dfGroup;

   // degrees of freedom for error
   private double dfError;

   // between group variability
   // explained variability (variability that can be explained by the categorical variable)
   private double meanSquaresGroup;

   // within group variability
   // unexplained variability (variability that cannot be explained by the categorical variable)
   private double meanSquaresError;

   private boolean rejectH0 = false;

   private double significanceLevel = -1;

   // F = ratio of (between-group-variability) / (within-group-variability)
   private double F;

   private double pValue;

   public ANOVA(Sample sample) {
      if(sample.isCategorical()) {
         logger.error("ANOVA can only be applied for sample that involves a numerical variable and a categorical variable");
         throw new NotImplementedException();
      }

      SampleDistribution sampleDistributionTotal = new SampleDistribution(sample, null);
      List<String> groups = sample.groups();

      Map<String, SampleDistribution> sampleDistributionByGroupId = new HashMap<>();
      for(int i=0; i < groups.size(); ++i){
         String groupId = groups.get(i);

         SampleDistribution sampleDistributionGroup = new SampleDistribution(sample, groupId);
         sampleDistributionByGroupId.put(groupId, sampleDistributionGroup);
      }

      run(sampleDistributionTotal, sampleDistributionByGroupId, -1);
   }

   public ANOVA() {

   }

   public void run(SampleDistribution sampleDistributionTotal, Map<String, SampleDistribution> sampleDistributionByGroupId){
      run(sampleDistributionTotal, sampleDistributionByGroupId, -1);
   }

   public void run(SampleDistribution sampleDistributionTotal, Map<String, SampleDistribution> sampleDistributionByGroupId, double significanceLevel){

      this.significanceLevel = significanceLevel;

      sumOfSquaresTotal = sampleDistributionTotal.getSumOfSquares();
      grandMean = sampleDistributionTotal.getSampleMean();


      sumOfSquaresGroup = 0;

      for(Map.Entry<String, SampleDistribution> entry : sampleDistributionByGroupId.entrySet()){
         String groupId = entry.getKey();

         SampleDistribution sampleDistributionGroup = entry.getValue();
         double groupMean = sampleDistributionGroup.getSampleMean();
         sumOfSquaresGroup += Math.pow(groupMean - grandMean, 2.0) * sampleDistributionGroup.getSampleSize();
      }

      sumOfSquaresError  = sumOfSquaresTotal - sumOfSquaresGroup;

      dfTotal = sampleDistributionTotal.getSampleSize() - 1;
      dfGroup = sampleDistributionByGroupId.size() - 1;
      dfError = dfTotal - dfGroup;

      meanSquaresGroup = sumOfSquaresGroup / dfGroup;
      meanSquaresError = sumOfSquaresError / dfError;

      F = meanSquaresGroup / meanSquaresError;

      FDistribution fDistribution = new FDistribution(dfGroup, dfError);

      pValue = 1 - fDistribution.cumulativeProbability(F);
      rejectH0 = pValue < significanceLevel;
   }

   public String getSummary() {
      StringBuilder sb = new StringBuilder();
      sb.append("null hypothesis: numerical variable (response) is independent of the categorical variable (explanatory)");
      sb.append("alternative hypothesis: numerical variable is correlated to the categorical variable");

      sb.append("SST (sum of squares total): ").append(sumOfSquaresTotal);
      sb.append("\nSSG (sum of squares group): ").append(sumOfSquaresGroup);
      sb.append("\nSSE (sum of squares error): ").append(sumOfSquaresError);
      sb.append("\ndf (total): ").append(dfTotal);
      sb.append("\ndf (group): ").append(dfGroup);
      sb.append("\ndf (error): ").append(dfError);
      sb.append("\nMSG (mean squares group): ").append(meanSquaresGroup);
      sb.append("\nMSG (mean squares error): ").append(meanSquaresError);
      sb.append("\nF-statistic: ").append(F);
      sb.append("\np-value: ").append(pValue);

      if(significanceLevel > 0) {
         sb.append("\nIf the significance level is ").append(significanceLevel).append(", the null hypothesis is ").append(rejectH0 ? "rejected as p-value is smaller than that" : "failed to be rejected");
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

   public boolean willRejectH0(double significanceLevel){
      return pValue < significanceLevel;
   }

}
