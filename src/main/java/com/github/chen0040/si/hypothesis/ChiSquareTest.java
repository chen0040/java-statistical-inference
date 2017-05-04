package com.github.chen0040.si.hypothesis;


import com.github.chen0040.si.statistics.ContingencyTable;
import com.github.chen0040.si.statistics.Sample;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.math3.distribution.ChiSquaredDistribution;

import java.util.List;


/**
 * Created by xschen on 4/5/2017.
 * ChiSqaure test
 * conducted between two categorical variables
 *
 * H_0: two categorical variables are independent
 *      namely, the value of one does not dependent on the value of the other
 *
 * H_1: they are dependent
 *      namely, the value of one depends on the value of the other
 */
@Getter
@Setter
public class ChiSquareTest {

   // test statistics
   private double chiSqaure;

   // degrees of freedom
   private double df;

   // probability of observed data or more extreme cases assuming the two categorical variables are independent
   private double pValue;

   private double significanceLevel;

   private boolean rejectH0 = false;

   public ChiSquareTest(Sample sample) {

   }

   public ChiSquareTest(){

   }

   public void run(ContingencyTable table, double significanceLevel) {

      List<String> rows = table.rows();
      List<String> cols = table.columns();

      chiSqaure = 0.0;

      double total = table.total();

      for(String rowName : rows) {
         for(String colName : cols){
            double expected = table.rowTotal(rowName) * table.columnTotal(colName) / total;
            double observed = table.get(rowName, colName);
            chiSqaure += Math.pow(observed - expected, 2.0) / expected;
         }
      }

      df = (rows.size()-1) * (cols.size()-1);

      ChiSquaredDistribution distribution = new ChiSquaredDistribution(df);
      double cp = distribution.cumulativeProbability(chiSqaure);
      pValue = 1 - cp;

      this.significanceLevel = significanceLevel;

      if(significanceLevel > 0){
         rejectH0 = pValue < significanceLevel;
      }
   }

   public String getSummary(){
      StringBuilder sb = new StringBuilder();

      sb.append("Chi^2: ").append(chiSqaure);
      sb.append("\ndegrees of freedom: ").append(df);
      sb.append("\np-value: ").append(pValue);

      if(significanceLevel > 0){
         sb.append("\nif the significance level is ").append(significanceLevel).append(", then ").append(rejectH0 ? "two categorical variables are dependent" : "two categorical variables are independent");
      }
      return sb.toString();
   }

   @Override
   public String toString(){
      return getSummary();
   }

   public void report(){
      System.out.println(toString());
   }

   public boolean willRejectH0(double significanceLevel){
      return pValue < significanceLevel;
   }


}
