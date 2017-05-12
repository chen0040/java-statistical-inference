package com.github.chen0040.si.dsl;


/**
 * Created by xschen on 8/5/2017.
 */
public class VariablePair {
   private Variable variableOne;
   private Variable variableTwo;


   public VariablePair(Variable variableOne, Variable variableTwo) {
      this.variableOne = variableOne;
      this.variableTwo = variableTwo;
   }

   public PairedSampleKie numericalSample() {
      variableOne.setCategorical(false);
      variableTwo.setCategorical(false);
      return new PairedSampleKie(this);
   }
}
