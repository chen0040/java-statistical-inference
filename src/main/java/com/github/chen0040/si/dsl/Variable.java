package com.github.chen0040.si.dsl;


/**
 * Created by xschen on 8/5/2017.
 */
public class Variable {
   private final String name;
   private boolean categorical;

   public Variable(String name) {
      this.name = name;
   }

   public Variable setCategorical(boolean categorical) {
      this.categorical = categorical;
      return this;
   }

   public String getName(){
      return name;
   }


   public VariablePair pair(Variable variableTwo) {
      return new VariablePair(this, variableTwo);
   }

   public NumericalSampleKie addObservation(double value){
      return new NumericalSampleKie(this).addObservation(value);
   }

   public NumericalSampleKie fromSampleDistribution(double sampleMean, double sampleSd, int sampleSize) {
      categorical = false;
      return new NumericalSampleKie(this, sampleMean, sampleSd, sampleSize);
   }

   public CategoricalSampleKie addObservation(String value) {
      return new CategoricalSampleKie(this).addObservation(value);
   }

   public CategoricalSampleKie fromSampleDistribution(String successLabel, double sampleProportion, int sampleSize) {
      categorical = true;
      return new CategoricalSampleKie(this, successLabel, sampleProportion, sampleSize);
   }

}
