package com.github.chen0040.si.dsl;


import com.github.chen0040.data.frame.DataFrame;
import com.github.chen0040.data.frame.DataQuery;
import com.github.chen0040.si.statistics.ConfidenceInterval;
import com.github.chen0040.si.testing.TestingOnProportion;
import com.github.chen0040.si.utils.FileUtils;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;


/**
 * Created by xschen on 13/5/2017.
 */
public class CategoricalSampleKieUnitTest {

   @Test
   public void test_single_variable_load_file() throws IOException {

      String variableName = "type";
      InputStream inputStream = FileUtils.getResource("iris.data");
      DataFrame dataFrame = DataQuery.csv(",").from(inputStream)
              .selectColumn(4).asCategory().asInput(variableName).build();

      Variable variable = new Variable(variableName);
      CategoricalSampleKie kie = variable.categoricalSample();

      kie.addObservations(dataFrame);

      Proportion proportion = kie.proportion("Iris-setosa");
      TestingOnProportion test = kie.test4ProportionEqualTo("Iris-setosa", 0.5);

      ConfidenceInterval confidenceInterval = proportion.confidenceInterval(0.95);

      System.out.println("sample.mean: " + kie.getSampleMean("Iris-setosa"));
      System.out.println("sample.proportion: " + kie.getSampleProportion("Iris-setosa"));
      System.out.println("sample.sd: " + kie.getSampleSd("Iris-setosa"));
      System.out.println("sample.size: " + kie.getSampleSize());

      System.out.println("confidence interval for proportion of \"Iris-setosa\": " + confidenceInterval);
      System.out.println("sampling distribution: " + kie.getSamplingDistribution("Iris-setosa"));


      System.out.println(confidenceInterval.getSummary());
      System.out.println(test.getSummary());

   }

   @Test
   public void test_single_variable() {

      Variable variable = new Variable("accountType");
      CategoricalSampleKie kie = variable.categoricalSample();

      kie.addObservations(new String[] { "Asset", "Liability", "Equity", "Revenue", "Expense", "Liability", "Equity", "Revenue", "Asset", "Liability", "Equity" });

      Proportion proportion = kie.proportion("Liability");
      TestingOnProportion test = kie.test4ProportionEqualTo("Liability", 0.5);

      ConfidenceInterval confidenceInterval = proportion.confidenceInterval(0.95);

      System.out.println("sample.mean: " + kie.getSampleMean("Liability"));
      System.out.println("sample.proportion: " + kie.getSampleProportion("Liability"));
      System.out.println("sample.sd: " + kie.getSampleSd("Liability"));
      System.out.println("sample.size: " + kie.getSampleSize());

      System.out.println("confidence interval for proportion of \"Liability\": " + confidenceInterval);
      System.out.println("sampling distribution: " + kie.getSamplingDistribution("Liability"));


      System.out.println(confidenceInterval.getSummary());
      System.out.println(test.getSummary());

   }
}
