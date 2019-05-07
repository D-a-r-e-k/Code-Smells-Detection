/** Sets the default interpreter to be the current one.  The result is "none" if
   * the remote JVM is unavailable or if an exception occurs.  Blocks until the interpreter is connected.
    * @return Status flags: whether the current interpreter changed, and whether it is busy; or "none" on an error
    */
public Option<Pair<Boolean, Boolean>> setToDefaultInterpreter() {
    InterpreterJVMRemoteI remote = _state.value().interpreter(false);
    if (remote == null) {
        return Option.none();
    }
    try {
        return Option.some(remote.setToDefaultInterpreter());
    } catch (RemoteException e) {
        _handleRemoteException(e);
        return Option.none();
    }
}
