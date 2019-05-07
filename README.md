# Code Smells Classifier Using Automatically Learned Source Code Features

<b>Data preparation</b>

Java project CodeAnalysis is used to transform Java classes & methods extracted from files into unified represenation records. Both classes and methods are represented as a list of methods. Method_0 is introduced to capture class'es context and it has all surrounding class'es fields.

<b>Classifier</b>

Feed forward neural network is trained based on features extracted from a processed dataset using LSTM (https://github.com/D-a-r-e-k/Source-Code-Modelling) / Code2Vec (https://github.com/tech-srl/code2vec)
