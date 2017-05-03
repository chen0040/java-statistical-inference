package com.github.chen0040.si.statistics;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * Created by xschen on 3/5/2017.
 */
public class Sample {
   private final List<Observation> observations = new ArrayList<>();
   private Optional<Boolean> isNumeric = Optional.empty();

   public void add(Observation observation) {
      if(isNumeric.isPresent()){
         boolean numericOnly = isNumeric.get();
         if(observation.isNumeric() != numericOnly) {
            throw new RuntimeException("sample should only contain ".concat(numericOnly ? "numeric" : "categorical").concat(" values"));
         }
      } else {
         isNumeric = Optional.of(observation.isNumeric());
      }

      observations.add(observation);
   }

   public boolean isNumeric(){
      if(!isNumeric.isPresent()){
         throw new RuntimeException("No observation is found in the sample");
      }
      return isNumeric.get();
   }

   public int size() {
      return observations.size();
   }

   public Observation get(int index) {
      return observations.get(index);
   }


   // compute the sample mean
   public double mean() {
      if(!isNumeric()){
         throw new RuntimeException("Mean can only be calculated on numeric variables");
      }

      return observations.stream().map(Observation::getNumericValue).reduce((a, b) -> a + b).get() / observations.size();
   }

   public double binomialMean(String successLabel) {

      if(!isBinomial()){
        throw new RuntimeException("Binomial mean can only be calculated on data for which variable has two categorical values");
      }
      if(!observations.stream().anyMatch(o -> o.getCategoricalValue().equals(successLabel))){
         throw new RuntimeException("Success label ".concat(successLabel).concat(" is not found in the sample data"));
      }
      return size() * proportion(successLabel);
   }

   public double proportion(String successLabel) {
      if(isNumeric()) {
         throw new RuntimeException("proportional can only be calculated on categorical variables");
      }
      return (double)observations.stream().filter(o -> o.getCategoricalValue().equals(successLabel)).count() / size();
   }

   private boolean isBinomial() {
      return observations.stream().map(Observation::getCategoricalValue).distinct().count() == 2;
   }

   // compute the sample standard deviation
   public double sd() {
      if(!isNumeric()) {
         throw new RuntimeException("Standard deviation can only be calculated on numeric variables");
      }

      final double sampleMean = mean();
      if(size() == 1) {
         return 0;
      }

      return Math.sqrt(observations.stream().map(o -> Math.pow(o.getNumericValue() - sampleMean, 2.0)).reduce((a, b) -> a + b).get() / (size()-1));
   }

}
