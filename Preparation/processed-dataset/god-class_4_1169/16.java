/** Called when a full suite of tests has finished running. Forwards from the other JVM to the local JUnit model.
   * @param errors The array of errors from all failed tests in the suite.
   */
public void testSuiteEnded(JUnitError[] errors) {
    _junitModel.testSuiteEnded(errors);
}
