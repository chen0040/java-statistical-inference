package com.github.chen0040.si.dsl;


import com.github.chen0040.data.frame.DataFrame;
import com.github.chen0040.data.frame.DataQuery;
import com.github.chen0040.si.statistics.ConfidenceInterval;
import com.github.chen0040.si.testing.TestingOnProportionDifference;
import com.github.chen0040.si.utils.FileUtils;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;


/**
 * Created by xschen on 15/5/2017.
 */
public class TwoGroupCategoricalSampleKieUnitTest {

   @Test
   public void test_two_groups_categorical_variable_load_file() throws IOException {
      Variable variable_use = new Variable("UseContraceptive");
      Variable variable_urban = new Variable("IsUrban");

      InputStream inputStream = FileUtils.getResource("contraception.csv");
      DataFrame dataFrame = DataQuery.csv(",")
              .from(inputStream)
              .selectColumn(3).asCategory().asInput("UseContraceptive")
              .selectColumn(6).asCategory().asInput("IsUrban")
              .build();

      TwoGroupCategoricalSampleKie kie = variable_use.twoGroupCategoricalSampleKie(variable_urban, "Y", "N");

      kie.addObservations(dataFrame);

      ProportionDifference difference = kie.proportionDifference("Y");
      ConfidenceInterval confidenceInterval = difference.confidenceInterval(0.95);

      TestingOnProportionDifference test = kie.test4EqualProportions("Y");

      System.out.println("sample1.mean: " + kie.getGroup1SampleMean("Y"));
      System.out.println("sample1.proportion: " + kie.getGroup1SampleProportion("Y"));
      System.out.println("sample1.sd: " + kie.getGroup1SampleSd("Y"));
      System.out.println("sample1.size: " + kie.getGroup1SampleSize());

      System.out.println("sample2.mean: " + kie.getGroup2SampleMean("Y"));
      System.out.println("sample2.proportion: " + kie.getGroup2SampleProportion("Y"));
      System.out.println("sample2.sd: " + kie.getGroup2SampleSd("Y"));
      System.out.println("sample2.size: " + kie.getGroup2SampleSize());

      System.out.println("sampling distribution: " + kie.getSamplingDistribution("Y"));

      System.out.println("95% confidence interval: " + confidenceInterval);

      System.out.println("========================================================");

      System.out.println(confidenceInterval.getSummary());
      System.out.println(test.getSummary());

   }
}
