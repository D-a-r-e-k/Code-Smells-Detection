/** Called if the slave JVM encounters an illegal class file in testing.  Forwards from
   * the other JVM to the local JUnit model.
   * @param e the ClassFileError describing the error when loading the class file
   */
public void classFileError(ClassFileError e) {
    _junitModel.classFileError(e);
}
