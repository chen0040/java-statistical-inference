package com.github.chen0040.si.statistics;


import java.util.*;
import java.util.stream.Collectors;


/**
 * Created by xschen on 4/5/2017.
 */
public class ContingencyTable {
   private Set<String> rowNames = new HashSet<>();
   private Set<String> colNames = new HashSet<>();
   private Map<String, Double> cells = new HashMap<>();

   public void put(String rowName, String colName, double value) {
      rowNames.add(rowName);
      colNames.add(colName);
      cells.put(key(rowName, colName), value);
   }

   public double get(String rowName, String colName) {
      return cells.get(key(rowName, colName));
   }

   private String key(String rowName, String colName) {
      return rowName.concat("-").concat(colName);
   }

   public List<String> rows(){
      return rowNames.stream().collect(Collectors.toList());
   }

   public List<String> columns() {
      return colNames.stream().collect(Collectors.toList());
   }

   public double rowTotal(String rowName) {
      double sum = 0;
      for(String colName : colNames) {
         sum += get(rowName, colName);
      }
      return sum;
   }

   public double columnTotal(String colName){
      double sum = 0;
      for(String rowName: rowNames) {
         sum += get(rowName, colName);
      }
      return sum;
   }

   public double total(){
      double sum = 0;
      for(Double val : cells.values()){
         sum += val;
      }
      return sum;
   }
}
