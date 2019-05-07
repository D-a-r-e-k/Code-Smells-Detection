/** Sets up a JUnit test suite in the Interpreter JVM and finds which classes are really TestCase
    * classes (by loading them).  Blocks until the interpreter is connected and the operation completes.
    * @param classNames the class names to run in a test
    * @param files the associated file
    * @return the class names that are actually test cases
    */
public Option<List<String>> findTestClasses(List<String> classNames, List<File> files) {
    InterpreterJVMRemoteI remote = _state.value().interpreter(false);
    if (remote == null) {
        return Option.none();
    }
    try {
        return Option.some(remote.findTestClasses(classNames, files));
    } catch (RemoteException e) {
        _handleRemoteException(e);
        return Option.none();
    }
}
