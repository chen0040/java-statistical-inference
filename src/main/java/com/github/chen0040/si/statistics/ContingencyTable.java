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

   public ContingencyTable(){

   }


   public ContingencyTable(Sample sample) {
      int count = sample.countByGroupId(null);
      for(int i=0; i < count; ++i){
         Observation observation = sample.get(i);
         String value1 = observation.getCategoricalValue();
         String value2 = observation.getGroupId();
         put(value1, value2, get(value1, value2)+1.0);
      }
   }


   public void put(String rowName, String colName, double value) {
      rowNames.add(rowName);
      colNames.add(colName);
      cells.put(key(rowName, colName), value);
   }

   public double get(String rowName, String colName) {
      return cells.getOrDefault(key(rowName, colName), 0.0);
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


   public String getSummary() {
      StringBuilder sb = new StringBuilder();
      for(String rowName : rowNames){
         for(String colName: colNames){
            double value = get(rowName, colName);
            sb.append("Cell.Count[").append(rowName).append("][").append(colName).append("]: ").append(value).append("\n");
         }
      }
      for(String rowName : rowNames){
         sb.append("Row.Count[").append(rowName).append("]: ").append(rowTotal(rowName)).append("\n");
      }
      for(String colName : colNames){
         sb.append("Col.Count[").append(colName).append("]: ").append(columnTotal(colName)).append("\n");
      }
      return sb.toString();
   }
}
