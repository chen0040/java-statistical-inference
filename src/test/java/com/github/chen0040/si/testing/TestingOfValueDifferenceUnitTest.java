package com.github.chen0040.si.testing;


import org.testng.annotations.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.testng.Assert.*;


/**
 * Created by xschen on 4/5/2017.
 */
public class TestingOfValueDifferenceUnitTest {

   @Test
   public void test_with_small_sample_size(){
      double xHat1 = 52.1;
      double xHat2 = 27.1;
      double s1 = 45.1;
      double s2 = 26.4;
      int n1 = 22;
      int n2 = 22;

      TestingOnValueDifference test = new TestingOnValueDifference();
      test.run(xHat1, xHat2, s1, s2, n1, n2);

      double significanceLevel = 0.05;
      assertThat(test.getPValueOneTail()).isLessThan(significanceLevel);

      boolean twoTails = true;
      assertTrue(test.willRejectH0(significanceLevel, twoTails));
      assertTrue(test.willRejectH0(significanceLevel, false));

      test.report();


   }
}
