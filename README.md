# java-statistical-inference
This package is a java implementation of an opinionated statistical inference engine with fluent api to make it easier for conducting statistical inference with little or no knowledge of statistical inference principles involved

[![Build Status](https://travis-ci.org/chen0040/java-statistical-inference.svg?branch=master)](https://travis-ci.org/chen0040/java-statistical-inference) [![Coverage Status](https://coveralls.io/repos/github/chen0040/java-statistical-inference/badge.svg?branch=master)](https://coveralls.io/github/chen0040/java-statistical-inference?branch=master) 

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

The code below shows how to load observed data about the variable "Amount" into the kie:

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

System.out.println("sample.mean: " + kie.getSampleMean());
System.out.println("sample.proportion: " + kie.getSampleProportion());
System.out.println("sample.sd: " + kie.getSampleSd());
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
