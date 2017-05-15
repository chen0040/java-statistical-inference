package com.github.chen0040.si.dsl;


import com.github.chen0040.data.frame.DataFrame;
import com.github.chen0040.data.frame.DataQuery;
import com.github.chen0040.si.statistics.ConfidenceInterval;
import com.github.chen0040.si.testing.TestingOnValue;
import com.github.chen0040.si.testing.TestingOnValueDifference;
import com.github.chen0040.si.utils.FileUtils;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.testng.Assert.*;


/**
 * Created by xschen on 14/5/2017.
 */
public class TwoGroupNumericalSampleKieUnitTest {

   @Test
   public void test_single_variable_two_groups_load_file() throws IOException {
      Variable variable = new Variable("Decrease");
      TwoGroupNumericalSampleKie kie = variable.twoGroupNumericalSample(new Variable("Treatment"), "Calcium", "Placebo");

      InputStream inputStream = FileUtils.getResource("calcium.dat");
      DataFrame dataFrame = DataQuery.csv().from(inputStream)
              .skipRows(33)
              .selectColumn(0).asCategory().asInput("Treatment")
              .selectColumn(3).asNumeric().asInput("Decrease")
              .build();

      kie.addObservations(dataFrame);

      MeanDifference difference = kie.difference();
      ConfidenceInterval confidenceInterval = difference.confidenceInterval(0.95);

      TestingOnValueDifference test = kie.test4GroupDifference();

      System.out.println("sample1.mean: " + kie.getGroup1SampleMean());
      System.out.println("sample1.sd: " + kie.getGroup1SampleSd());
      System.out.println("sample1.size: " + kie.getGroup1SampleSize());

      System.out.println("sample2.mean: " + kie.getGroup2SampleMean());
      System.out.println("sample2.sd: " + kie.getGroup2SampleSd());
      System.out.println("sample2.size: " + kie.getGroup2SampleSize());

      System.out.println("sampling distribution: " + kie.getSamplingDistribution());

      System.out.println("95% confidence interval: " + confidenceInterval);

      System.out.println("========================================================");

      System.out.println(confidenceInterval.getSummary());
      System.out.println(test.getSummary());
   }
}
