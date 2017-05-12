package com.github.chen0040.si.testing;


import com.github.chen0040.si.exceptions.VariableWrongValueTypeException;
import com.github.chen0040.si.statistics.Sample;
import com.github.chen0040.si.statistics.SampleLinearRegression;
import org.apache.commons.math3.distribution.FDistribution;


/**
 * Created by xschen on 8/5/2017.
 */
public class Anova4Regression {

   // sum of squares total for y
   private double ySST;

   // sum of squares error for y
   private double ySSE;

   // sum of squares group for y
   private double ySSG;

   // mean square group for y
   private double yMSG;

   // mean square error for y
   private double yMSE;

   // degrees of freedom total
   private double dfT;

   // degrees of freedom group
   private double dfG;

   // degrees of freedom error
   private double dfE;

   // the probability of
   private double pValue;

   // test statistics based on fisher distribution
   private double F;

   public Anova4Regression(Sample sample) {
      if(!sample.containsTwoNumericalVariables()) {
         throw new VariableWrongValueTypeException("Sample 1 should contain numeric variable x and y");
      }

      SampleLinearRegression regression = new SampleLinearRegression(sample);

      run(regression, sample);
   }

   public Anova4Regression run(SampleLinearRegression regression, Sample sample){
      double yBar = regression.getYBar();
      double b_0 = regression.getIntercept();
      double b_1 = regression.getSlope();

      ySST = sample.getObservations().stream().map(o -> Math.pow(o.getY() - yBar, 2.0)).reduce((a, b) -> a + b).get();
      ySSE = sample.getObservations().stream().map(o -> Math.pow(o.getY() - o.getX() * b_1 + b_0, 2.0)).reduce((a,b) -> a + b).get();

      ySSG = ySST - ySSE;

      dfT = sample.countByGroupId(null) - 1;
      dfG = 1;
      dfE = dfT - dfG;

      yMSG = ySSG / dfG;
      yMSE = ySSE / dfE;

      FDistribution distribution = new FDistribution(dfG, dfE);

      F = yMSG / yMSE;

      pValue = distribution.cumulativeProbability(F);

      return this;
   }

   public String getSummary() {
      StringBuilder sb = new StringBuilder();
      sb.append("null hypothesis: numerical variable y (response) is independent of the numerical variable x (explanatory)");
      sb.append("alternative hypothesis: numerical variable y is correlated to the numerical variable x");

      sb.append("SST (sum of squares total): ").append(ySST);
      sb.append("\nSSG (sum of squares group): ").append(ySSG);
      sb.append("\nSSE (sum of squares error): ").append(ySSE);
      sb.append("\ndf (total): ").append(dfT);
      sb.append("\ndf (group): ").append(dfG);
      sb.append("\ndf (error): ").append(dfE);
      sb.append("\nMSG (mean squares group): ").append(yMSG);
      sb.append("\nMSG (mean squares error): ").append(yMSE);
      sb.append("\nF-statistic: ").append(F);
      sb.append("\np-value: ").append(pValue);

      double significanceLevel = 0.001;
      boolean rejectH0 = pValue < significanceLevel;
      sb.append("\nIf the significance level is ").append(significanceLevel).append(", the null hypothesis is ").append(rejectH0 ? "rejected as p-value is smaller than that" : "failed to be rejected");


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
