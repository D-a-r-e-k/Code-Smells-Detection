/** Require variable declarations to include an explicit type. The result is {@code false} if
   * the remote JVM is unavailable or if an exception occurs.  Blocks until the interpreter is connected.
   */
public boolean setRequireVariableType(boolean require) {
    InterpreterJVMRemoteI remote = _state.value().interpreter(false);
    if (remote == null) {
        return false;
    }
    try {
        remote.setRequireVariableType(require);
        return true;
    } catch (RemoteException e) {
        _handleRemoteException(e);
        return false;
    }
}
