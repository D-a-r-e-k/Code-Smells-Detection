/** Called when the JUnitTestManager wants to open a file that is not currently open.
   * @param className the name of the class for which we want to find the file
   * @return the file associated with the given class
   */
public File getFileForClassName(String className) {
    return _junitModel.getFileForClassName(className);
}
