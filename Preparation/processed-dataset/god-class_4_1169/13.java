/** Called to indicate that a suite of tests has started running.
   * Forwards from the other JVM to the local JUnit model.
   * @param numTests The number of tests in the suite to be run.
   */
public void testSuiteStarted(int numTests) {
    _junitModel.testSuiteStarted(numTests);
}
