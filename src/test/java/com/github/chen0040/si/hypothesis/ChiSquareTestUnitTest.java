package com.github.chen0040.si.hypothesis;


import com.github.chen0040.si.statistics.ContingencyTable;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.testng.Assert.*;


/**
 * Created by xschen on 4/5/2017.
 */
public class ChiSquareTestUnitTest {

   @Test
   public void test_two_categorical_variables_independence(){
      ContingencyTable table = new ContingencyTable();
      table.put("obese", "dating", 81);
      table.put("obese", "cohabiting", 103);
      table.put("obese", "married", 147);
      table.put("not obese", "dating", 359);
      table.put("not obese", "cohabiting", 326);
      table.put("not obese", "married", 277);

      ChiSquareTest test = new ChiSquareTest();

      double significanceLevel = 0.0001;
      test.run(table, significanceLevel);

      assertThat(test.getChiSqaure()).isCloseTo(31.69, within(1.0));
      assertThat(test.getDf()).isEqualTo(2.0);

      test.report();

   }
}
