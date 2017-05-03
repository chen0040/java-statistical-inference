package com.github.chen0040.si.statistics;


import com.github.chen0040.si.exceptions.VariableMixedValueTypeException;
import com.github.chen0040.si.exceptions.NoObservationFoundException;
import com.github.chen0040.si.exceptions.VariableWrongValueTypeException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * Created by xschen on 3/5/2017.
 */
public class Sample {
   private final List<Observation> observations = new ArrayList<>();
   private Optional<Boolean> isNumeric = Optional.empty();


   private boolean randomlySampledOrAssigned = true;
   private boolean sampledWithReplacement = true;
   // true population size, usually unknown and only used when sampled without replacement
   private int truePopulationSize = -1;

   public void add(Observation observation) {
      if(isNumeric.isPresent()){
         boolean numericOnly = isNumeric.get();
         if(observation.isNumeric() != numericOnly) {
            throw new VariableMixedValueTypeException("sample should only contain ".concat(numericOnly ? "numeric" : "categorical").concat(" values"));
         }
      } else {
         isNumeric = Optional.of(observation.isNumeric());
      }

      observations.add(observation);
   }

   public boolean isNumeric(){
      if(!isNumeric.isPresent()){
         throw new NoObservationFoundException("No observation is found in the sample");
      }
      return isNumeric.get();
   }

   public boolean isCategorical() {
      return !isNumeric();
   }

   public int size(String groupId) {
      return (int)observations.stream().filter(o -> groupId == null || groupId.equals(o.getGroupId())).count();
   }

   public Observation get(int index) {
      return observations.get(index);
   }


   public double proportion(String successLabel, String groupId) {
      if(isNumeric()) {
         throw new VariableWrongValueTypeException("proportional can only be calculated on categorical variables");
      }
      return (double)observations.stream()
              .filter(o -> groupId == null || groupId.equals(o.getGroupId()))
              .filter(o -> o.getCategoricalValue().equals(successLabel)).count() / size(groupId);
   }


   public List<Observation> getObservations() {
      return observations;
   }

   public long groups(){
      return observations.stream().map(Observation::getGroupId).distinct().count();
   }

   public boolean isRandomlySampledOrAssigned() {
      return randomlySampledOrAssigned;
   }

   public boolean isSampledWithReplacement(){
      return sampledWithReplacement;
   }

   public int getTruePopulationSize(){
      return truePopulationSize;
   }

   public void setTruePopulationSize(int truePopulationSize) {
      this.truePopulationSize = truePopulationSize;
   }
}
