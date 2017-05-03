package com.github.chen0040.si.maths.multivariate.ica;

import java.util.List;


/**
 * Created by xschen on 19/8/15.
 * Independent component analysis (ICA) is a statistical and computational technique for revealing hidden factors that underlie sets of naive
 * variables, measurements, or signals.
 *
 * ICA defines a generative model for the observed multivariate shrinkedData, which is typically given as a large database of samples. In the model, the
 * shrinkedData variables are assumed to be linear mixtures of some unknown latent variables, and the mixing system is also unknown. The latent variables
 * are assumed nongaussian and mutually independent, and they are called the independent components of the observed shrinkedData. These independent
 * components, also called sources or factors, can be found by ICA.
 *
 * ICA is superficially related to principal component analysis and factor analysis. ICA is a much more powerful technique, however, capable of
 * finding the underlying factors or sources when these classic methods fail completely.
 *
 * The shrinkedData analyzed by ICA could originate from many different kinds of application fields, including digital images, document databases, economic
 * indicators and psychometric measurements. In many cases, the measurements are given as a set of parallel signals or time series; the term blind
 * source separation is used to characterize this problem. Typical examples are mixtures of simultaneous speech signals that have been picked up by
 * several microphones, brain waves recorded by multiple sensors, interfering radio signals arriving at a mobile phone, or parallel time series
 * obtained from some industrial process.
 *
 * Well-known application: 1. blind source separation ("Cocktail Party" problem)
 */
public interface ICA {
    // separate mixed signal X into two different sources and return in ResultICA
    ResultICA separateSources(List<double[]> X);
}
