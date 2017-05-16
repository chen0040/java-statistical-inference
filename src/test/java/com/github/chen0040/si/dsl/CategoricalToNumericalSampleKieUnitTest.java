package com.github.chen0040.si.dsl;


import com.github.chen0040.data.frame.DataFrame;
import com.github.chen0040.data.frame.DataQuery;
import com.github.chen0040.si.statistics.ContingencyTable;
import com.github.chen0040.si.testing.Anova;
import com.github.chen0040.si.testing.ChiSquareTest;
import com.github.chen0040.si.utils.FileUtils;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;


/**
 * Created by xschen on 15/5/2017.
 */
public class CategoricalToNumericalSampleKieUnitTest {

   @Test
   public void test_multi_group_numerical_variable_load_file() throws IOException {


      Variable variable1 = new Variable("Age");
      Variable variable2 = new Variable("LiveChannel");

      CategoricalToNumericalSampleKie kie = variable1.multipleGroupNumericalSample(variable2);

      InputStream inputStream = FileUtils.getResource("contraception.csv");
      DataFrame dataFrame = DataQuery.csv(",")
              .from(inputStream)
              .skipRows(1)
              .selectColumn(5).asNumeric().asInput("Age")
              .selectColumn(4).asCategory().asInput("LiveChannel")
              .build();

      kie.addObservations(dataFrame);

      Anova test = kie.test4Independence();

      System.out.println(test.getSummary());



   }
}
