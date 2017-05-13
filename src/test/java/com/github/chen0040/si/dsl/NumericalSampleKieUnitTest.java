package com.github.chen0040.si.dsl;


import com.github.chen0040.data.frame.DataFrame;
import com.github.chen0040.data.frame.DataQuery;
import com.github.chen0040.si.statistics.ConfidenceInterval;
import com.github.chen0040.si.testing.TestingOnValue;
import com.github.chen0040.si.utils.FileUtils;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.testng.Assert.*;


/**
 * Created by xschen on 13/5/2017.
 */
public class NumericalSampleKieUnitTest {

   @Test
   public void test_single_numerical_variable_load_file() throws IOException {

      String variableName = "Decrease";
      InputStream inputStream = FileUtils.getResource("calcium.dat");
      DataFrame dataFrame = DataQuery.csv().from(inputStream)
              .selectColumn(3).asNumeric().asInput(variableName).build();

      Variable variable = new Variable(variableName);
      NumericalSampleKie kie = variable.numericalSample();
      kie.addObservations(dataFrame);
      Mean mean = kie.mean();
      ConfidenceInterval confidenceInterval = mean.confidenceInterval(0.95);
      TestingOnValue test = kie.test4MeanEqualTo(0.5);

      System.out.println("sample.mean: " + kie.getSampleMean());
      System.out.println("sample.sd: " + kie.getSampleSd());
      System.out.println("sample.size: " + kie.getSampleSize());

      System.out.println("Confidence interval for the " + variableName + ": " + confidenceInterval);

      System.out.println("========================================================");

      System.out.println(confidenceInterval.getSummary());
      System.out.println(test.getSummary());
   }

   @Test
   public void test_single_numerical_variable() throws IOException {

      String variableName = "Amount";

      Variable variable = new Variable(variableName);
      NumericalSampleKie kie = variable.numericalSample();

      kie.addObservations(new double[] { 0.2, 0.4, 0.6, 0.12, 0.9, 0.13, -0.12, -0.55, 0.5});

      Mean mean = kie.mean();
      ConfidenceInterval confidenceInterval = mean.confidenceInterval(0.95);
      TestingOnValue test = kie.test4MeanEqualTo(0.5);

      System.out.println("sample.mean: " + kie.getSampleMean());
      System.out.println("sample.sd: " + kie.getSampleSd());
      System.out.println("sample.size: " + kie.getSampleSize());

      System.out.println("Confidence interval for the " + variableName + ": " + confidenceInterval);

      System.out.println("========================================================");

      System.out.println(confidenceInterval.getSummary());
      System.out.println(test.getSummary());
   }
}
