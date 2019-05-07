/**
   * Performs a (stratified if class is nominal) cross-validation for a
   * classifier on a set of instances.
   * 
   * @param classifierString a string naming the class of the classifier
   * @param data the data on which the cross-validation is to be performed
   * @param numFolds the number of folds for the cross-validation
   * @param options the options to the classifier. Any options
   * @param random the random number generator for randomizing the data accepted
   *          by the classifier will be removed from this array.
   * @throws Exception if a classifier could not be generated successfully or
   *           the class is not defined
   */
public void crossValidateModel(String classifierString, Instances data, int numFolds, String[] options, Random random) throws Exception {
    m_delegate.crossValidateModel(classifierString, data, numFolds, options, random);
}
