package com.github.chen0040.si.testing;


import org.testng.annotations.Test;

import static org.testng.Assert.*;


/**
 * Created by xschen on 4/5/2017.
 */
public class TestingOnProportionUnitTest {

   @Test
   public void test_small_sample() {
      // sample involves a categorical variable which has two values { GUESS_CORRECTLY, GUESS_WRONGLY }

      double p_hat = 1.0; // observed in real life, octopus Paul guess correctly in all 8 trials
      int n = 8; // number of trials (sample size)
      double p_null = 0.5; // under H0, octopus Paul is doing random guess

      TestingOnProportion test = new TestingOnProportion();
      test.run(p_hat, n, p_null);

      double significanceLevel = 0.05;
      boolean twoTails = true;
      assertTrue(test.willRejectH0(significanceLevel, twoTails));
      assertTrue(test.willRejectH0(significanceLevel, false));

      test.report();
   }
}
