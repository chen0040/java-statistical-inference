package com.github.chen0040.si.statistics;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Optional;


/**
 * Created by xschen on 3/5/2017.
 */
public class Observation implements Serializable {

   private Optional<Double> numericValue = Optional.empty();
   private Optional<String> categoricalValue = Optional.empty();

   private String groupId = "";

   public void setValue(String category) {
      if(isNumeric()) {
         throw new RuntimeException("Observation is already marked as numeric");
      }
      categoricalValue = Optional.of(category);
   }

   public void setValue(double numeric) {
      if(isCategorical()) {
         throw new RuntimeException("Observation is already marked as categorical");
      }
      numericValue = Optional.of(numeric);
   }

   public void setGroupId(String groupId) {
      this.groupId = groupId;
   }

   public String getGroupId() {
      return this.groupId;
   }

   public boolean isNumeric(){
      return numericValue.isPresent();
   }

   public boolean isCategorical() {
      return categoricalValue.isPresent();
   }

   public double getNumericValue() {
      return numericValue.get();
   }

   public String getCategoricalValue() {
      return categoricalValue.get();
   }


}
