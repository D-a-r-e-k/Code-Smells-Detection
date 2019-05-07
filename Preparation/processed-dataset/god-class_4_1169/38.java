/** Sets the current interpreter to the one specified by name.  The result is "none" if
   * the remote JVM is unavailable or if an exception occurs.  Blocks until the interpreter is connected.
    * @param name the unique name of the interpreter to set active
    * @return Status flags: whether the current interpreter changed, and whether it is busy; or "none" on an error
    */
public Option<Pair<Boolean, Boolean>> setActiveInterpreter(String name) {
    InterpreterJVMRemoteI remote = _state.value().interpreter(false);
    if (remote == null) {
        return Option.none();
    }
    try {
        return Option.some(remote.setActiveInterpreter(name));
    } catch (RemoteException e) {
        _handleRemoteException(e);
        return Option.none();
    }
}
