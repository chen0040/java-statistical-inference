# java-statistical-inference
This package is a java implementation of an opinionated statistical inference engine with fluent api to make it easier for conducting statistical inference with little or no knowledge of statistical inference principles involved

[![Build Status](https://travis-ci.org/chen0040/java-statistical-inference.svg?branch=master)](https://travis-ci.org/chen0040/java-statistical-inference) [![Coverage Status](https://coveralls.io/repos/github/chen0040/java-statistical-inference/badge.svg?branch=master)](https://coveralls.io/github/chen0040/java-statistical-inference?branch=master) 

# Features

* Confidence Interval for numerical variable and proportions (one group or two groups)
* Hypothesis Testing for Single Numerical Variable
* Hypothesis Testing for Single Categorical Variable (Proportion)
* Hypothesis Testing for Two Group Numerical Variable
* Hypothesis Testing for Two Group Categorical Variable (Proportion)
* ANOVA: Independence Test between a Numerical Variable and a Categorical Variable
* Chi-Square Test: Independence Test between a Categorical Variable and another Categorical Variable
* ANOVA for Regression: Independence Test between a Numerical Variable and another Numerical Variable
* Automatic change of sampling distribution based on sample size:

    * Normal distribution for large sample on categorical variable (one or two groups)
    * Bootstrap simulation for small sample on categorical variable (one or two groups)
    * Normal distribution for large sample on numerical variable (one or two groups)
    * Student-T distribution for small sample on numerical variable (one or two groups)

* Central Limit Theorem Conditions Check

# Install

Add the following dependency into your POM file:

```xml
<dependency>
  <groupId>com.github.chen0040</groupId>
  <artifactId>java-statistical-inference</artifactId>
  <version>1.0.3</version>
</dependency>
```

# Usage

### Single Numerical Variable 

The code below shows how to declare a single numerical variable kie (knowledge inference engine):

```java
Variable variable = new Variable("Amount");
NumericalSampleKie kie = variable.numericalSample();
```

The code below shows how to load observed data about the variable "Amount" into the kie:

```java
kie.addObservations(new double[] { 0.2, 0.4, 0.6, 0.12, 0.9, 0.13, -0.12, -0.55, 0.5});
```

Alternatively the observed data can be loaded from a data frame (please refer to [here](https://github.com/chen0040/java-data-frame) for more example on how to create a data frame)

```java

DataFrame dataFrame = DataQuery.csv().from(new FileInputStream("amount.csv"))
              .selectColumn(0).asNumeric().asInput("Amount").build();
kie.addObservations(dataFrame);
```

The code below shows the various statistics that can be obtained from the kie about the variable "Amount":

```java
Mean mean = kie.mean();
double confidenceLevel = 0.95;
ConfidenceInterval confidenceInterval = mean.confidenceInterval(confidenceLevel);

System.out.println("sample.mean: " + kie.getSampleMean());
System.out.println("sample.sd: " + kie.getSampleSd());
System.out.println("sample.size: " + kie.getSampleSize());
System.out.println("sample.median: " + kie.getSampleMedian());
System.out.println("sample.max: " + kie.getSampleMax());
System.out.println("sample.min: " + kie.getSampleMin());
System.out.println("sample.1st.quartile: " + kie.getSampleFirstQuartile());
System.out.println("sample.3rd.quartile: " + kie.getSampleThirdQuartile());

System.out.println("sampling distribution: " + kie.getSamplingDistribution());

System.out.println("confidence interval for Amount: " + confidenceInterval);
```

The kie also provides user friendly statement for the confidence interval:

```java
System.out.println(kie.mean().confidenceInterval(0.95).getSummary());
```

The code belows shows how to test the null hypothesis that "The population mean of Amount is 0.5", with significance level of 0.05:

```java
double expected_mean = 0.5;
TestingOnValue test = kie.test4MeanEqualTo(expected_mean);

System.out.println("sampling distribution: " + test.getDistributionFamily());
System.out.println("test statistic: " + test.getTestStatistic());
System.out.println("p-value (one-tail): " + test.getPValueOneTail());
System.out.println("p-value (two-tails): " + test.getPValueTwoTails());
```

The kie also provides user friendly statement for the null hypothesis test:

```java
TestingOnValue test = kie.test4MeanEqualTo(0.5);
System.out.println(test.getSummary());
```

### Single Categorical Variable 

The code below shows how to declare a single categorical variable kie (knowledge inference engine):

```java
Variable variable = new Variable("Type");
NumericalSampleKie kie = variable.categoricalSample();
```

The code below shows how to load observed data about the variable "Type" into the kie:

```java
kie.addObservations(new String[] { "Asset", "Liability", "Equity", "Revenue", "Expense", "Liability", "Equity", "Revenue", "Asset", "Liability", "Equity" });
```

Alternatively the observed data can be loaded from a data frame 

```java

InputStream inputStream = new FileInputStream("iris.data");
DataFrame dataFrame = DataQuery.csv(",").from(inputStream)
      .selectColumn(4).asCategory().asInput("Type").build();
kie.addObservations(dataFrame);
```

The code below shows the various statistics that can be obtained from the kie about the variable "Amount":

```java
Proportion proportion = kie.proportion("Liability");
double confidenceLevel = 0.95;
ConfidenceInterval confidenceInterval = proportion.confidenceInterval(confidenceLevel);

System.out.println("sample.mean: " + kie.getSampleMean("Liability"));
System.out.println("sample.proportion: " + kie.getSampleProportion("Liability"));
System.out.println("sample.sd: " + kie.getSampleSd("Liability"));
System.out.println("sample.size: " + kie.getSampleSize());

System.out.println("sampling distribution: " + kie.getSamplingDistribution());

System.out.println("confidence interval for Type == Liability: " + confidenceInterval);
```

The kie also provides user friendly statement for the confidence interval:

```java
System.out.println(kie.proportion("Liability").confidenceInterval(0.95).getSummary());
```

The code belows shows how to test the null hypothesis that "The population proportion of Type==Liability is 0.5", with significance level of 0.05:

```java
double expected_proportion = 0.5;
TestingOnValue test = kie.test4MeanEqualTo(expected_proportion);

System.out.println("sampling distribution: " + test.getDistributionFamily());
System.out.println("test statistic: " + test.getTestStatistic());
System.out.println("p-value (one-tail): " + test.getPValueOneTail());
System.out.println("p-value (two-tails): " + test.getPValueTwoTails());
```

The kie also provides user friendly statement for the null hypothesis test:

```java
TestingOnValue test = kie.test4ProportionEqualTo(0.5);
System.out.println(test.getSummary());
```

### Paired Sample for a Numerical Variable

The sample code below shows how to run statistical inference on the sample from a paired observations (e.g. before, after) for a numerical variable:

```java
Variable variable1 = new Variable("Begin");
Variable variable2 = new Variable("End");

InputStream inputStream = new FileInputStream("calcium-paired.dat");
DataFrame dataFrame = DataQuery.csv().from(inputStream)
      .selectColumn(1).asNumeric().asInput("Begin")
      .selectColumn(2).asNumeric().asInput("End")
      .build();

PairedSampleKie kie = variable2.pair(variable1).numericalSample();
kie.addObservations(dataFrame);

Mean mean = kie.difference();


ConfidenceInterval confidenceInterval = mean.confidenceInterval(0.95);
TestingOnValue test = kie.testDifferenceEqualTo(0.5);

System.out.println("sample.difference-mean: " + kie.getSampleDifferenceMean());
System.out.println("sample.difference-sd: " + kie.getSampleDifferenceSd());
System.out.println("sample.size: " + kie.getSampleSize());
System.out.println("sample.median: " + kie.getSampleMedian());
System.out.println("sample.max: " + kie.getSampleMax());
System.out.println("sample.min: " + kie.getSampleMin());
System.out.println("sample.1st.quartile: " + kie.getSampleFirstQuartile());
System.out.println("sample.3rd.quartile: " + kie.getSampleThirdQuartile());

System.out.println("sampling distribution (difference): " + kie.getSamplingDistribution());

System.out.println("95% confidence interval: " + confidenceInterval);

System.out.println("========================================================");

System.out.println(confidenceInterval.getSummary());
System.out.println(test.getSummary());
```

In the above codes, the "[calcium-paired.dat](https://github.com/chen0040/java-statistical-inference/blob/master/src/test/resources/calcium-paired.dat)" contains results of a randomized comparative experiment to investigate the effect of calcium on blood pressure in African-American men. A treatment group of 10 men received a calcium supplement for 12 weeks. All subjects had their blood pressure tested before and after the 12-week period.

### Compare Two Groups for a Numerical Variable

The sample below shows the statistical inference on samples from two different groups (e.g., from two different experiment setup) for a numerical variable:

```java
Variable variable = new Variable("Decrease");
TwoGroupNumericalSampleKie kie = variable.twoGroupNumericalSample(new Variable("Treatment"), "Calcium", "Placebo");

InputStream inputStream = new FileInputStream("calcium.dat");
DataFrame dataFrame = DataQuery.csv().from(inputStream)
      .skipRows(33)
      .selectColumn(0).asCategory().asInput("Treatment")
      .selectColumn(3).asNumeric().asInput("Decrease")
      .build();

kie.addObservations(dataFrame);

MeanDifference difference = kie.difference();
ConfidenceInterval confidenceInterval = difference.confidenceInterval(0.95);

TestingOnValueDifference test = kie.test4GroupDifference();

System.out.println("sample1.mean: " + kie.getGroup1SampleMean());
System.out.println("sample1.sd: " + kie.getGroup1SampleSd());
System.out.println("sample1.size: " + kie.getGroup1SampleSize());
System.out.println("sample1.median: " + kie.getGroup1SampleMedian());
System.out.println("sample1.max: " + kie.getGroup1SampleMax());
System.out.println("sample1.min: " + kie.getGroup1SampleMin());
System.out.println("sample1.1st.quartile: " + kie.getGroup1SampleFirstQuartile());
System.out.println("sample1.3rd.quartile: " + kie.getGroup1SampleThirdQuartile());

System.out.println("sample2.mean: " + kie.getGroup2SampleMean());
System.out.println("sample2.sd: " + kie.getGroup2SampleSd());
System.out.println("sample2.size: " + kie.getGroup2SampleSize());
System.out.println("sample2.median: " + kie.getGroup2SampleMedian());
System.out.println("sample2.max: " + kie.getGroup2SampleMax());
System.out.println("sample2.min: " + kie.getGroup2SampleMin());
System.out.println("sample2.2st.quartile: " + kie.getGroup2SampleFirstQuartile());
System.out.println("sample2.3rd.quartile: " + kie.getGroup2SampleThirdQuartile());

System.out.println("sampling distribution: " + kie.getSamplingDistribution());

System.out.println("95% confidence interval: " + confidenceInterval);

System.out.println("========================================================");

System.out.println(confidenceInterval.getSummary());
System.out.println(test.getSummary());
```

In the above codes, the "[calcium.dat](https://github.com/chen0040/java-statistical-inference/blob/master/src/test/resources/calcium.dat)" contains results of a randomized comparative experiment to investigate the effect of calcium on blood pressure in African-American men. A treatment group of 10 men received a calcium supplement for 12 weeks, and a control group of 11 men received a placebo during the same period. All subjects had their blood pressure tested before and after the 12-week period.

The "kie.test4GroupDifference()" can be used to test whether the numerical variable is independent of another categorical variable which has two levels (i.e. the "group" variable)

### Compare Two Groups for a Categorical Variable

The sample below shows the statistical inference on samples from two different groups (e.g., from two different experiment setup) for a categorical variable:

```java
Variable variable_use = new Variable("UseContraceptive");
Variable variable_urban = new Variable("IsUrban");

InputStream inputStream = new FileInputStream("contraception.csv");
DataFrame dataFrame = DataQuery.csv(",")
      .from(inputStream)
      .selectColumn(3).asCategory().asInput("UseContraceptive")
      .selectColumn(6).asCategory().asInput("IsUrban")
      .build();

TwoGroupCategoricalSampleKie kie = variable_use.twoGroupCategoricalSampleKie(variable_urban, "Y", "N");

kie.addObservations(dataFrame);

ProportionDifference difference = kie.proportionDifference("Y");
ConfidenceInterval confidenceInterval = difference.confidenceInterval(0.95);

TestingOnProportionDifference test = kie.test4GroupDifference("Y");

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
```

In the above codes, the "[contraception.csv](https://github.com/chen0040/java-statistical-inference/blob/master/src/test/resources/contraception.csv)" contains results of whether a person is from urban area and whether he/she uses contraception.

The "kie.test4GroupDifference('Y')" can be used to test whether the categorical variable is independent of another categorical variable which has two levels (i.e. the "group" variable)

### ANOVA: Independence Test for a Numerical variable and a Categorical Variable

The sample code belows show to test for the independence between a categorical variable (explanatory variable) a numerical variable (response variable):

```java
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
```

In the above codes, the "[contraception.csv](https://github.com/chen0040/java-statistical-inference/blob/master/src/test/resources/contraception.csv)" contains results of which channel the person watch (categorical) and what is his/her age (numeric).


### Chi-Square: Independence Test for two Categorical Variables

The sample code belows show to test for the independence between two categorical variables

```java
Variable variable1 = new Variable("UseContraceptive");
Variable variable2 = new Variable("LiveChannel");

CategoricalToCategoricalSampleKie kie = variable1.multipleGroupCategoricalSample(variable2);

InputStream inputStream = new FileInputStream("contraception.csv");
DataFrame dataFrame = DataQuery.csv(",")
      .from(inputStream)
      .skipRows(1)
      .selectColumn(3).transform(text -> text.equals("Y") ? "Use" : "DontUse").asInput("UseContraceptive")
      .selectColumn(4).asCategory().asInput("LiveChannel")
      .build();

kie.addObservations(dataFrame);

ChiSquareTest test = kie.test4Independence();

ContingencyTable contingencyTable = kie.getOrCreateContingencyTable();

System.out.println(contingencyTable.getSummary());

System.out.println(test.getSummary());
```

In the above codes, the "[contraception.csv](https://github.com/chen0040/java-statistical-inference/blob/master/src/test/resources/contraception.csv)" contains results of whether a person watch which live channel (categorical variable) and whether he/she uses contraception (another categorical variable).
