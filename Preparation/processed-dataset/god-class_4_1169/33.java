/** Sets the Interpreter to be in the given package.  Blocks until the interpreter is connected.
    * @param packageName Name of the package to enter.
    */
public boolean setPackageScope(String packageName) {
    InterpreterJVMRemoteI remote = _state.value().interpreter(false);
    if (remote == null) {
        return false;
    }
    try {
        remote.interpret("package " + packageName + ";");
        return true;
    } catch (RemoteException e) {
        _handleRemoteException(e);
        return false;
    }
}
