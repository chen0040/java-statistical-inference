package com.github.chen0040.si.dsl;


import com.github.chen0040.data.frame.DataFrame;
import com.github.chen0040.data.frame.DataQuery;
import com.github.chen0040.data.frame.Sampler;
import com.github.chen0040.si.statistics.SampleLinearRegression;
import com.github.chen0040.si.testing.Anova4Regression;
import org.testng.annotations.Test;

import java.util.Random;

import static org.testng.Assert.*;


/**
 * Created by xschen on 15/5/2017.
 */
public class XYSampleKieUnitTest {

   @Test
   public void test_two_numerical_variables_load_file(){

      final Random random = new Random(System.currentTimeMillis());

      // regression: y ~ 25 + 5 * x
      Sampler.DataSampleBuilder builder = new Sampler().forColumn("x").generate((name, index) -> (double)index)
              .forColumn("y").generate((name, index) -> 25 + (index + random.nextDouble()) * 5 + random.nextDouble())
               .end();

      DataFrame dataFrame = DataQuery.blank()
              .newInput("x")
              .newOutput("y")
              .end().build();
      dataFrame = builder.sample(dataFrame, 100);

      System.out.println(dataFrame.head(10));

      Variable x = new Variable("x");
      XYSampleKie kie = x.regression(new Variable("y"));

      kie.addObservations(dataFrame);

      SampleLinearRegression model = kie.model();

      System.out.println("correlation between x and y: " + model.getCorrelation());
      System.out.println("y-intercept: " + model.getIntercept());
      System.out.println("slope: " + model.getSlope());
      System.out.println("R^2: " + model.getR2());
      System.out.println("SD(X): " + model.getSX());
      System.out.println("SD(Y): " + model.getSY());
      System.out.println("Mean(X): " + model.getXBar());
      System.out.println("Mean(Y): " + model.getYBar());

      Anova4Regression anova = kie.test4Independence();

      System.out.println(anova.getSummary());


   }
}
