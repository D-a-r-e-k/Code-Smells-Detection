# Code Smells Classifier Using Automatically Learned Source Code Features

<b>Data preparation</b>

Java project CodeAnalysis is used to transform Java classes & methods extracted from files into unified represenation records. Both classes and methods are represented as a list of methods. Method_0 is introduced to capture class'es context and it has all surrounding class'es fields.

dataset was adopted from http://essere.disco.unimib.it/wiki/research/mlcsd

<b>Classifier</b>

Feed forward neural network is trained based on features extracted from a processed dataset using LSTM (https://github.com/D-a-r-e-k/Source-Code-Modelling) / Code2Vec (https://github.com/tech-srl/code2vec)

This work is based on Master thesis "Code smells detection using automatic source code features learning"
Summary (in Lithuanian) is published in Vilnius University Proceedings „Lietuvos magistrantų informatikos ir IT tyrimai“ (11th page)
