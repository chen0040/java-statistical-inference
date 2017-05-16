package com.github.chen0040.si.dsl;


import com.github.chen0040.data.frame.DataFrame;
import com.github.chen0040.data.frame.DataQuery;
import com.github.chen0040.si.statistics.ConfidenceInterval;
import com.github.chen0040.si.testing.TestingOnValue;
import com.github.chen0040.si.utils.FileUtils;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;


/**
 * Created by xschen on 14/5/2017.
 */
public class PairedSampleKieUnitTest {

   @Test
   public void test_paired_variables_load_file() throws IOException {
      Variable variable1 = new Variable("Begin");
      Variable variable2 = new Variable("End");

      InputStream inputStream = FileUtils.getResource("calcium-paired.dat");
      DataFrame dataFrame = DataQuery.csv().from(inputStream)
              .skipRows(1)
              .selectColumn(1).asNumeric().asInput("Begin")
              .selectColumn(2).asNumeric().asInput("End")
              .build();

      PairedSampleKie kie = variable2.pair(variable1).numericalSample();
      kie.addObservations(dataFrame);

      Mean mean = kie.difference();


      ConfidenceInterval confidenceInterval = mean.confidenceInterval(0.95);
      TestingOnValue test = kie.testDifferenceEqualTo(0.5);

      System.out.println("sample.mean: " + kie.getSampleDifferenceMean());
      System.out.println("sample.sd: " + kie.getSampleDifferenceSd());
      System.out.println("sample.size: " + kie.getSampleSize());
      System.out.println("sample.median: " + kie.getSampleMedian());
      System.out.println("sample.max: " + kie.getSampleMax());
      System.out.println("sample.min: " + kie.getSampleMin());
      System.out.println("sample.1st.quartile: " + kie.getSampleFirstQuartile());
      System.out.println("sample.3rd.quartile: " + kie.getSampleThirdQuartile());

      System.out.println("sampling distribution: " + kie.getSamplingDistribution());

      System.out.println("95% confidence interval: " + confidenceInterval);

      System.out.println("========================================================");

      System.out.println(confidenceInterval.getSummary());
      System.out.println(test.getSummary());
   }
}
