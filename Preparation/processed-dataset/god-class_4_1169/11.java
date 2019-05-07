/** Called if JUnit is invoked on a non TestCase class.  Forwards from the other JVM to the local JUnit model.
   * @param isTestAll whether or not it was a use of the test all button
    * @param didCompileFail whether or not a compile before this JUnit attempt failed
   */
public void nonTestCase(boolean isTestAll, boolean didCompileFail) {
    _junitModel.nonTestCase(isTestAll, didCompileFail);
}
