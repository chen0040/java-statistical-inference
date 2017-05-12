package com.github.chen0040.si.dsl;


import com.github.chen0040.si.statistics.Observation;
import com.github.chen0040.si.statistics.Sample;
import com.github.chen0040.si.statistics.SampleLinearRegression;
import com.github.chen0040.si.testing.Anova4Regression;


/**
 * Created by xschen on 12/5/2017.
 */
public class XYSampleKie {
   private final Variable varX;
   private final Variable varY;
   private final Sample sample = new Sample();

   public XYSampleKie(Variable varX, Variable varY){
      this.varX = varX;
      this.varY = varY;
   }

   public XYSampleKie addObservation(double x, double y){
      Observation observation = new Observation();
      observation.setX(x);
      observation.setY(y);
      sample.add(observation);
      return this;
   }

   public SampleLinearRegression model(){
      return new SampleLinearRegression(sample);
   }

   public Anova4Regression test4Independence() {
      return new Anova4Regression(sample);
   }

}
