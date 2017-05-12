package com.github.chen0040.si.statistics;


import java.io.Serializable;
import java.util.Optional;


/**
 * Created by xschen on 3/5/2017.
 */
public class Observation implements Serializable {

   private Optional<Double> x = Optional.empty();
   private Optional<Double> y = Optional.empty();
   private Optional<String> categoricalValue = Optional.empty();

   private String groupId = "";

   public void setCategory(String category) {
      if(isNumeric()) {
         throw new RuntimeException("Observation is already marked as numeric");
      }
      categoricalValue = Optional.of(category);
   }

   public void setX(double numeric) {
      if(isCategorical()) {
         throw new RuntimeException("Observation is already marked as categorical");
      }
      x = Optional.of(numeric);
   }

   public void setY(double numeric) {
      if(isCategorical()) {
         throw new RuntimeException("Observation is already marked as categorical");
      }
      y = Optional.of(numeric);
   }


   public void setGroupId(String groupId) {
      this.groupId = groupId;
   }

   public String getGroupId() {
      return this.groupId;
   }

   public boolean isNumeric(){
      return x.isPresent();
   }

   public boolean isCategorical() {
      return categoricalValue.isPresent();
   }

   public double getX() {
      return x.get();
   }

   public double getY() { return y.get(); }

   public String getCategoricalValue() {
      return categoricalValue.get();
   }

   public boolean containsTwoNumericalVariables(){
      return x.isPresent() && y.isPresent();
   }


}
