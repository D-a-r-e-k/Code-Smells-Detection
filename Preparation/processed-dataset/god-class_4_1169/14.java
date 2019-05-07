/** Called when a particular test is started.  Forwards from the slave JVM to the local JUnit model.
   * @param testName The name of the test being started.
   */
public void testStarted(String testName) {
    _junitModel.testStarted(testName);
}
