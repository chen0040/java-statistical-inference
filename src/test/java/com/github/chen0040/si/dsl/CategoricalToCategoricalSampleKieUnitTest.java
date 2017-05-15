package com.github.chen0040.si.dsl;


import com.github.chen0040.data.frame.DataFrame;
import com.github.chen0040.data.frame.DataQuery;
import com.github.chen0040.si.testing.ChiSquareTest;
import com.github.chen0040.si.utils.FileUtils;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;


/**
 * Created by xschen on 15/5/2017.
 */
public class CategoricalToCategoricalSampleKieUnitTest {

   @Test
   public void test_multi_group_categorical_variable_load_file() throws IOException {


      Variable variable1 = new Variable("UseContraceptive");
      Variable variable2 = new Variable("LiveChannel");

      CategoricalToCategoricalSampleKie kie = variable1.multipleGroupCategoricalSample(variable2);

      InputStream inputStream = FileUtils.getResource("contraception.csv");
      DataFrame dataFrame = DataQuery.csv(",")
              .from(inputStream)
              .selectColumn(3).asCategory().asInput("UseContraceptive")
              .selectColumn(4).asCategory().asInput("LiveChannel")
              .build();

      kie.addObservations(dataFrame);

      ChiSquareTest test = kie.test4Independence();

      System.out.println(test.getSummary());



   }
}
