/**
   * Sets the class path to use for starting the interpreter JVM. Must include the classes for the interpreter.
   * @param classPath Class path for the interpreter JVM
   */
public void setStartupClassPath(String classPath) {
    _startupClassPath = IOUtil.parsePath(classPath);
}
