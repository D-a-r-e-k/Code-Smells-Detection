/** Require a semicolon at the end of statements. The result is {@code false} if
   * the remote JVM is unavailable or if an exception occurs.  Blocks until the interpreter is connected.
   */
public boolean setRequireSemicolon(boolean require) {
    InterpreterJVMRemoteI remote = _state.value().interpreter(false);
    if (remote == null) {
        return false;
    }
    try {
        remote.setRequireSemicolon(require);
        return true;
    } catch (RemoteException e) {
        _handleRemoteException(e);
        return false;
    }
}
