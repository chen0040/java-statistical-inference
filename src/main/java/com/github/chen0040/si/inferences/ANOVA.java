package com.github.chen0040.si.inferences;


import com.github.chen0040.si.statistics.Sample;
import com.github.chen0040.si.statistics.SampleDistribution;
import org.apache.commons.math3.distribution.FDistribution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;


/**
 * Created by xschen on 3/5/2017.
 *
 * ANOVA (Analysis of Variance) is used to find whether there is a correlation between a numerical variable and a categorical variable for which the categorical
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

   private double meanSquaresGroup;

   private double meanSquaresError;

   // F = ratio of (between-group-variability) / (within-group-variability)
   public double F;

   private double pValue;

   public ANOVA(Sample sample) {
      if(sample.isCategorical()) {
         logger.error("ANOVA can only be applied for sample that involves a numerical variable and a categorical variable");
         throw new NotImplementedException();
      }

      SampleDistribution sampleDistributionTotal = new SampleDistribution(sample, null);

      sumOfSquaresTotal = sampleDistributionTotal.getSumOfSquares();
      grandMean = sampleDistributionTotal.getSampleMean();

      List<String> groups = sample.groups();

      sumOfSquaresGroup = 0;
      for(int i=0; i < groups.size(); ++i){
         String groupId = groups.get(i);

         SampleDistribution sampleDistributionGroup = new SampleDistribution(sample, groupId);
         double groupMean = sampleDistributionGroup.getSampleMean();
         sumOfSquaresGroup += Math.pow(groupMean - grandMean, 2.0) * sample.size(groupId);
      }

      sumOfSquaresError  = sumOfSquaresTotal - sumOfSquaresGroup;

      dfTotal = sample.size(null) - 1;
      dfGroup = groups.size() - 1;
      dfError = dfTotal - dfGroup;

      meanSquaresGroup = sumOfSquaresGroup / dfGroup;
      meanSquaresError = sumOfSquaresError / dfError;

      F = meanSquaresGroup / meanSquaresError;

      FDistribution fDistribution = new FDistribution(dfGroup, dfError);

      pValue = 1 - fDistribution.cumulativeProbability(F);
   }
}
