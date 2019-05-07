/**
   * Wraps a static classifier in enough source to test using the weka class
   * libraries.
   * 
   * @param classifier a Sourcable Classifier
   * @param className the name to give to the source code class
   * @return the source for a static classifier that can be tested with weka
   *         libraries.
   * @throws Exception if code-generation fails
   */
public static String wekaStaticWrapper(Sourcable classifier, String className) throws Exception {
    return weka.classifiers.evaluation.Evaluation.wekaStaticWrapper(classifier, className);
}
