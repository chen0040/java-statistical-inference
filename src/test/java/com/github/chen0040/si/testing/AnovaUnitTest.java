package com.github.chen0040.si.testing;


import com.github.chen0040.si.statistics.SampleDistribution;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.within;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.testng.Assert.*;


/**
 * Created by xschen on 4/5/2017.
 */
public class AnovaUnitTest {

   private SampleDistribution sampleDistributionTotal;
   private Map<String, SampleDistribution> sampleDistributionByGroupId = new HashMap<>();

   private AnovaTesting anova;



   @BeforeMethod
   public void setUp(){

      sampleDistributionTotal = Mockito.mock(SampleDistribution.class);

      String[] groups = new String[] { "lower class", "working class", "middle class", "upper class" };
      int[] sampleSizes = new int[] { 41, 407, 331, 16 };
      double[] sampleMeans = new double[] { 5.07, 5.75, 6.76, 6.19 };

      for(int i=0; i < groups.length; ++i) {
         String groupId = groups[i];
         int sampleSizeOfGroup = sampleSizes[i];
         double sampleMeanOfGroup = sampleMeans[i];

         SampleDistribution sampleDistribution = Mockito.mock(SampleDistribution.class);
         Mockito.when(sampleDistribution.getSampleSize()).thenReturn(sampleSizeOfGroup);
         Mockito.when(sampleDistribution.getSampleMean()).thenReturn(sampleMeanOfGroup);


         sampleDistributionByGroupId.put(groupId, sampleDistribution);

      }

      Mockito.when(sampleDistributionTotal.getSampleSize()).thenReturn(795);
      Mockito.when(sampleDistributionTotal.getSampleMean()).thenReturn(6.14);
      Mockito.when(sampleDistributionTotal.getSumOfSquares()).thenReturn(1.98 * 1.98 * 794);

   }

   @Test
   public void test_independence_between_numerical_and_categorical_variables() {
      AnovaTesting anova = new AnovaTesting();
      anova.run(sampleDistributionTotal, sampleDistributionByGroupId, 0.0001);

      assertThat(anova.getSumOfSquaresGroup()).isCloseTo(236.56, within(1.0));
      assertThat(anova.getSumOfSquaresError()).isCloseTo(2869.80, within(10.0));
      assertThat(anova.getSumOfSquaresTotal()).isCloseTo(3106.36, within(10.0));
      assertThat(anova.getF()).isCloseTo(21.735, within(0.1));

      double significanceLevel = 0.0001;
      assertThat(anova.getPValue()).isLessThan(significanceLevel);
      assertTrue(anova.willRejectH0(significanceLevel));

      anova.report();
   }

}
