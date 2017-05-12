package com.github.chen0040.si.dsl;


import com.github.chen0040.si.statistics.ContingencyTable;
import com.github.chen0040.si.statistics.Observation;
import com.github.chen0040.si.statistics.Sample;
import com.github.chen0040.si.testing.ChiSquareTest;


/**
 * Created by xschen on 12/5/2017.
 */
public class CategoricalToCategoricalSampleKie {
   private final Variable variable;
   private Sample sample;
   private ContingencyTable contingencyTable;

   public CategoricalToCategoricalSampleKie(Variable variable){
      this.variable = variable;
   }

   public CategoricalToCategoricalSampleKie addObservation(String value, String groupId){
      if(sample == null){
         sample = new Sample();
      }
      Observation observation = new Observation();
      observation.setCategory(value);
      observation.setGroupId(groupId);
      sample.add(observation);
      return this;
   }

   public CategoricalToCategoricalSampleKie fromContingencyTable(ContingencyTable table){
      contingencyTable = table;
      return this;
   }

   public ChiSquareTest test4Independence(){
      if(sample != null){
         return new ChiSquareTest(sample);
      } else {
         return new ChiSquareTest().run(contingencyTable);
      }
   }
}
