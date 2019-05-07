/** Removes the interpreter with the given name, if it exists.  The result is {@code false} if
   * the remote JVM is unavailable or if an exception occurs.  Blocks until the interpreter is connected.
    * @param name Name of the interpreter to remove
    */
public boolean removeInterpreter(String name) {
    InterpreterJVMRemoteI remote = _state.value().interpreter(false);
    if (remote == null) {
        return false;
    }
    try {
        remote.removeInterpreter(name);
        return true;
    } catch (RemoteException e) {
        _handleRemoteException(e);
        return false;
    }
}
