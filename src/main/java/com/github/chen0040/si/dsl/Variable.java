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

   public String getName(){
      return name;
   }


   public VariablePair pair(Variable variableTwo) {
      categorical = false;
      variableTwo.categorical = false;
      return new VariablePair(this, variableTwo);
   }

   public TwoGroupNumericalSampleKie twoGroupNumericalSample(String group1, String group2){
      categorical = false;
      return new TwoGroupNumericalSampleKie(this, group1, group2);
   }

   public TwoGroupCategoricalSampleKie twoGroupCategoricalSampleKie(String group1, String group2) {
      categorical = true;
      return new TwoGroupCategoricalSampleKie(this, group1, group2);
   }

   public NumericalSampleKie numericalSample(){
      categorical = false;
      return new NumericalSampleKie(this);
   }

   public CategoricalSampleKie categoricalSample() {
      categorical = true;
      return new CategoricalSampleKie(this);
   }

   public CategoricalToNumericalSampleKie moreThanTwoGroupNumericalSample(){
      categorical = false;
      return new CategoricalToNumericalSampleKie(this);
   }

   public CategoricalToCategoricalSampleKie moreThanTwoGroupCategoricalSample(){
      categorical = false;
      return new CategoricalToCategoricalSampleKie(this);
   }

   public XYSampleKie regression(Variable y){
      categorical = false;
      return new XYSampleKie(this, y);
   }



}
