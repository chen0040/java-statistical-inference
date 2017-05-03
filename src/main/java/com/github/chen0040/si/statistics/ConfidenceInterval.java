package com.github.chen0040.si.statistics;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;


/**
 * Created by xschen on 3/5/2017.
 */
@Getter
@Setter
public class ConfidenceInterval extends Interval {

   @Setter(AccessLevel.NONE)
   private final double confidenceLevel;

   @Setter(AccessLevel.NONE)
   private final String summary;

   public ConfidenceInterval(Interval interval, double confidenceLevel, String summary) {
      super(interval.getLo(), interval.getHi());
      this.confidenceLevel = confidenceLevel;
      this.summary = summary;
   }
}
