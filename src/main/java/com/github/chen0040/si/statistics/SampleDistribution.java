package com.github.chen0040.si.statistics;


import com.github.chen0040.si.exceptions.VariableWrongValueTypeException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;


/**
 * Created by xschen on 3/5/2017.
 * A sample distribution is an observed distribution of the values that a variable is observed to have for a sample of individuals. We have seen numerous sample distributions.
 */
@Getter
@Setter
public class SampleDistribution {

   private double sampleMeanPointEstimate;
   private double sampleSd;
   private double sampleVariance;
   private boolean isNumeric;
   private int sampleSize;

   @Getter(AccessLevel.NONE)
   @Setter(AccessLevel.NONE)
   private double proportionPointEstimate;

   @Getter(AccessLevel.NONE)
   @Setter(AccessLevel.NONE)
   private String successLabel;

   @Setter(AccessLevel.NONE)
   private final String groupId;

   public SampleDistribution(Sample sample, String groupId){
      if(!sample.isNumeric()){
         throw new VariableWrongValueTypeException("The constructor can only work on numeric variables");
      }

      this.groupId = groupId;

      isNumeric = true;

      sampleMeanPointEstimate = sample.getObservations().stream()
              .filter(o -> groupId == null || groupId.equals(o.getGroupId()))
              .map(Observation::getNumericValue)
              .reduce((a, b) -> a + b).get() / sample.size(groupId);

      sampleVariance = sample.getObservations().stream()
              .filter(o -> groupId == null || groupId.equals(o.getGroupId()))
              .map(o -> Math.pow(o.getNumericValue() - sampleMeanPointEstimate, 2.0))
              .reduce((a, b) -> a + b).get() / (sample.size(groupId)-1);

      sampleSd = Math.sqrt(sampleVariance);

      sampleSize = sample.size(groupId);
   }

   public SampleDistribution(Sample sample, String successLabel, String groupId) {
      if(sample.isNumeric()) {
         throw new VariableWrongValueTypeException("The constructor can only work on categorical variables");
      }

      this.groupId = groupId;

      isNumeric = false;

      sampleMeanPointEstimate = sample.size(groupId) * sample.proportion(successLabel, groupId);

      this.proportionPointEstimate = sample.proportion(successLabel, groupId);
      sampleVariance =  sample.size(groupId) * this.proportionPointEstimate * (1-this.proportionPointEstimate);

      sampleSd = Math.sqrt(sampleVariance);

      sampleSize = sample.size(groupId);

      this.successLabel = successLabel;
   }

   public double getProportionPointEstimate(){
      if(isNumeric()){
         throw new NotImplementedException();
      }
      return proportionPointEstimate;
   }

   public void setProportionPointEstimate(double p) {
      proportionPointEstimate = p;
   }

   public String getSuccessLabel(){
      if(isNumeric()) {
         throw new NotImplementedException();
      }

      return successLabel;
   }

   public void setSuccessLabel(String successLabel) {
      this.successLabel = successLabel;
   }


   public boolean isCategorical() {
      return !isNumeric();
   }
}
