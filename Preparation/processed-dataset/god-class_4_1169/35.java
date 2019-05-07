/**
   * Runs the JUnit test suite already cached in the Interpreter JVM.  Blocks until the remote JVM is available.
   * Returns {@code false} if no test suite is cached, the remote JVM is unavailable, or an error occurs.
   */
public boolean runTestSuite() {
    InterpreterJVMRemoteI remote = _state.value().interpreter(true);
    if (remote == null) {
        return false;
    }
    try {
        return remote.runTestSuite();
    } catch (RemoteException e) {
        _handleRemoteException(e);
        return false;
    }
}
