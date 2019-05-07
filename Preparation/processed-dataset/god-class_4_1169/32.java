/** Returns the current class path of the interpreter as a list of unique entries.  The result is "none"
   * if the remote JVM is unavailable or if an exception occurs.  Blocks until the interpreter is connected.
    */
public Option<Iterable<File>> getClassPath() {
    InterpreterJVMRemoteI remote = _state.value().interpreter(false);
    if (remote == null) {
        return Option.none();
    }
    try {
        return Option.some(remote.getClassPath());
    } catch (RemoteException e) {
        _handleRemoteException(e);
        return Option.none();
    }
}
