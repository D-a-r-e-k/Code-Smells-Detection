/**
   * Gets the string representation of the value of a variable in the current interpreter, or "none"
   * if the remote JVM is unavailable or an error occurs.  Blocks until the interpreter is connected.
   * @param var the name of the variable
   */
public Option<Pair<String, String>> getVariableToString(String var) {
    InterpreterJVMRemoteI remote = _state.value().interpreter(false);
    if (remote == null) {
        return Option.none();
    }
    try {
        return Option.some(remote.getVariableToString(var));
    } catch (RemoteException e) {
        _handleRemoteException(e);
        return Option.none();
    }
}
