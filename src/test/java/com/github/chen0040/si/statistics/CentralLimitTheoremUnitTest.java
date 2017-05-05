package com.github.chen0040.si.statistics;


import com.github.chen0040.si.exceptions.VariableWrongValueTypeException;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


/**
 * Created by xschen on 4/5/2017.
 */
public class CentralLimitTheoremUnitTest {

   private Sample numericalSample;
   private Sample categoricalSample;

   @BeforeMethod
   public void setUp(){
      numericalSample = Mockito.mock(Sample.class);
      categoricalSample = Mockito.mock(Sample.class);

      Mockito.when(numericalSample.isNumeric()).thenReturn(true);
      Mockito.when(numericalSample.isCategorical()).thenReturn(false);

      Mockito.when(categoricalSample.isCategorical()).thenReturn(true);
      Mockito.when(categoricalSample.isNumeric()).thenReturn(false);


      Mockito.when(categoricalSample.isRandomlySampledOrAssigned()).thenReturn(true);

   }

   @Test
   public void test_clt_numerical_variable_random_sample_failed() {
      Mockito.when(numericalSample.isRandomlySampledOrAssigned()).thenReturn(false);
      assertThat(CentralLimitTheorem.isHeld4SampleMean(numericalSample, null)).isEqualTo(false);
   }

   @Test
   public void test_clt_numerical_variable_sampling_without_replacement_failed() {
      Mockito.when(numericalSample.isRandomlySampledOrAssigned()).thenReturn(true);

      Mockito.when(numericalSample.isSampledWithReplacement()).thenReturn(false);
      Mockito.when(numericalSample.getTruePopulationSize()).thenReturn(100);
      Mockito.when(numericalSample.countByGroupId(null)).thenReturn(11);
      assertThat(CentralLimitTheorem.isHeld4SampleMean(numericalSample, null)).isEqualTo(false);
   }

   @Test
   public void test_clt_numerical_variable_min_sample_size_failed() {
      Mockito.when(numericalSample.isRandomlySampledOrAssigned()).thenReturn(true);

      Mockito.when(numericalSample.isSampledWithReplacement()).thenReturn(false);
      Mockito.when(numericalSample.getTruePopulationSize()).thenReturn(100);
      Mockito.when(numericalSample.countByGroupId(null)).thenReturn(9);
      assertThat(CentralLimitTheorem.isHeld4SampleMean(numericalSample, null)).isEqualTo(false);
   }

   @Test
   public void test_clt_numerical_variable_sampling_without_replacement_success() {
      Mockito.when(numericalSample.isRandomlySampledOrAssigned()).thenReturn(true);

      Mockito.when(numericalSample.isSampledWithReplacement()).thenReturn(false);
      Mockito.when(numericalSample.getTruePopulationSize()).thenReturn(1000);
      Mockito.when(numericalSample.countByGroupId(null)).thenReturn(90);
      assertThat(CentralLimitTheorem.isHeld4SampleMean(numericalSample, null)).isEqualTo(true);
   }

   @Test
   public void test_clt_numerical_variable_sampling_with_replacement_success() {
      Mockito.when(numericalSample.isRandomlySampledOrAssigned()).thenReturn(true);

      Mockito.when(numericalSample.isSampledWithReplacement()).thenReturn(true);
      Mockito.when(numericalSample.countByGroupId(null)).thenReturn(30);
      assertThat(CentralLimitTheorem.isHeld4SampleMean(numericalSample, null)).isEqualTo(true);
   }

   @Test(expectedExceptions = VariableWrongValueTypeException.class)
   public void test_clt_numerical_variable_throw_exception() {
      CentralLimitTheorem.isHeld4SampleProportion(numericalSample, "some label", null);
   }

   @Test
   public void test_clt_categorical_variable_random_sample_failed() {
      Mockito.when(categoricalSample.isRandomlySampledOrAssigned()).thenReturn(false);
      assertThat(CentralLimitTheorem.isHeld4SampleProportion(categoricalSample, "some label", null)).isEqualTo(false);
   }

   @Test
   public void test_clt_categorical_variable_sampling_without_replacement_failed() {
      Mockito.when(categoricalSample.isRandomlySampledOrAssigned()).thenReturn(true);

      Mockito.when(categoricalSample.isSampledWithReplacement()).thenReturn(false);
      Mockito.when(categoricalSample.getTruePopulationSize()).thenReturn(100);
      Mockito.when(categoricalSample.countByGroupId(null)).thenReturn(11);
      assertThat(CentralLimitTheorem.isHeld4SampleProportion(categoricalSample, "some label",  null)).isEqualTo(false);
   }

   @Test
   public void test_clt_categorical_variable_min_sample_size_failed() {
      Mockito.when(categoricalSample.isRandomlySampledOrAssigned()).thenReturn(true);

      Mockito.when(categoricalSample.isSampledWithReplacement()).thenReturn(false);
      Mockito.when(categoricalSample.getTruePopulationSize()).thenReturn(100);
      Mockito.when(categoricalSample.countByGroupId(null)).thenReturn(9);
      Mockito.when(categoricalSample.proportion("some label", null)).thenReturn(0.5);
      assertThat(CentralLimitTheorem.isHeld4SampleProportion(categoricalSample,"some label",  null)).isEqualTo(false);
   }

   @Test
   public void test_clt_categorical_variable_sampling_without_replacement_success() {
      Mockito.when(categoricalSample.isRandomlySampledOrAssigned()).thenReturn(true);

      Mockito.when(categoricalSample.isSampledWithReplacement()).thenReturn(false);
      Mockito.when(categoricalSample.getTruePopulationSize()).thenReturn(1000);
      Mockito.when(categoricalSample.countByGroupId(null)).thenReturn(90);
      Mockito.when(categoricalSample.proportion("some label", null)).thenReturn(0.5);
      assertThat(CentralLimitTheorem.isHeld4SampleProportion(categoricalSample, "some label", null)).isEqualTo(true);
   }

   @Test
   public void test_clt_categorical_variable_sampling_with_replacement_success() {
      Mockito.when(categoricalSample.isRandomlySampledOrAssigned()).thenReturn(true);

      Mockito.when(categoricalSample.isSampledWithReplacement()).thenReturn(true);
      Mockito.when(categoricalSample.countByGroupId(null)).thenReturn(30);
      Mockito.when(categoricalSample.proportion("some label", null)).thenReturn(0.5);
      assertThat(CentralLimitTheorem.isHeld4SampleProportion(categoricalSample, "some label", null)).isEqualTo(true);
   }

   @Test(expectedExceptions = VariableWrongValueTypeException.class)
   public void test_clt_categorical_variable_throw_exception() {
      CentralLimitTheorem.isHeld4SampleMean(categoricalSample, null);
   }
   
   
}
