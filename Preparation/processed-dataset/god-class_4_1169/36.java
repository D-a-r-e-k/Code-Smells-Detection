//  /** Updates the security manager in slave JVM */ 
//  public void enableSecurityManager() throws RemoteException { 
//    _interpreterJVM().enableSecurityManager(); 
//  } 
//   
//  /** Updates the security manager in slave JVM */ 
//  public void disableSecurityManager() throws RemoteException{ 
//    _interpreterJVM().disableSecurityManager(); 
//  } 
/**
   * Adds a named interpreter to the list.  The result is {@code false} if the remote JVM is unavailable or
   * if an exception occurs.  Blocks until the interpreter is connected.
   * @param name the unique name for the interpreter
   * @throws IllegalArgumentException if the name is not unique
   */
public boolean addInterpreter(String name) {
    InterpreterJVMRemoteI remote = _state.value().interpreter(false);
    if (remote == null) {
        return false;
    }
    try {
        remote.addInterpreter(name);
        return true;
    } catch (RemoteException e) {
        _handleRemoteException(e);
        return false;
    }
}
