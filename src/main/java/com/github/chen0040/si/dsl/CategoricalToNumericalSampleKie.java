package com.github.chen0040.si.dsl;


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
   private Sample sample;

   private SampleDistribution sampleDistributionTotal;
   private Map<String, SampleDistribution> sampleDistributionByGroupId;

   public CategoricalToNumericalSampleKie(Variable variable) {
      this.variable = variable;
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

   public Anova test4DifferenceInGroups(){
      if(sample != null){
         return new Anova(sample);
      } else {
         return new Anova().run(sampleDistributionTotal, sampleDistributionByGroupId);
      }
   }

}
