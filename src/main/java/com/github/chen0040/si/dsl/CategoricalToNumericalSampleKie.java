package com.github.chen0040.si.dsl;


import com.github.chen0040.data.frame.DataFrame;
import com.github.chen0040.data.frame.DataRow;
import com.github.chen0040.si.statistics.Observation;
import com.github.chen0040.si.statistics.Sample;
import com.github.chen0040.si.statistics.SampleDistribution;
import com.github.chen0040.si.testing.Anova;

import java.util.Map;


/**
 * Created by xschen on 12/5/2017.
 */
public class CategoricalToNumericalSampleKie {
   private final Variable variable;
   private final Variable groupVariable;

   private Sample sample;

   private SampleDistribution sampleDistributionTotal;
   private Map<String, SampleDistribution> sampleDistributionByGroupId;

   public CategoricalToNumericalSampleKie(Variable variable, Variable groupVariable) {
      this.variable = variable;
      this.groupVariable = groupVariable;
   }

   public CategoricalToNumericalSampleKie addObservation(double value, String groupId) {
      if(sample == null){
         sample = new Sample();
      }
      Observation observation = new Observation();
      observation.setX(value);
      observation.setGroupId(groupId);
      sample.add(observation);
      return this;
   }

   public CategoricalToNumericalSampleKie fromSampleDistributions(SampleDistribution sampleDistributionTotal, Map<String, SampleDistribution> sampleDistributionByGroupId){
      this.sampleDistributionByGroupId = sampleDistributionByGroupId;
      this.sampleDistributionTotal = sampleDistributionTotal;

      return this;
   }

   public Anova test4Independence(){
      if(sample != null){
         return new Anova(sample);
      } else {
         return new Anova().run(sampleDistributionTotal, sampleDistributionByGroupId);
      }
   }


   public void addObservations(DataFrame dataFrame) {
      for(int i=0; i < dataFrame.rowCount(); ++i){
         DataRow row = dataFrame.row(i);
         double value = row.getCell(variable.getName());
         String groupId = row.getCategoricalCell(groupVariable.getName());
         addObservation(value, groupId);
      }
   }
}
