package com.github.chen0040.si.statistics;


import lombok.Getter;
import lombok.Setter;


/**
 * Created by xschen on 8/5/2017.
 */
@Getter
@Setter
public class SampleMetaData {

   private boolean randomlySampledOrAssigned = true;
   private boolean sampledWithReplacement = true;
   // true population size, usually unknown and only used when sampled without replacement
   private int truePopulationSize = -1;
}
