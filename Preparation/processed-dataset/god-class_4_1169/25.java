/* === Wrappers for InterpreterJVMRemoteI methods === */
/** Interprets string s in the remote JVM.  Blocks until the interpreter is connected and evaluation completes.
    * @return  {@code true} if successful; {@code false} if the subprocess is unavailable, the subprocess dies
    *          during the call, or an unexpected exception occurs.
    */
public boolean interpret(final String s) {
    InterpreterJVMRemoteI remote = _state.value().interpreter(true);
    if (remote == null) {
        return false;
    }
    try {
        debug.logStart("Interpreting " + s);
        InterpretResult result = remote.interpret(s);
        result.apply(resultHandler());
        debug.logEnd("result", result);
        return true;
    } catch (RemoteException e) {
        debug.logEnd();
        _handleRemoteException(e);
        return false;
    }
}
