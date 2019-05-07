/*
   * === AbstractMasterJVM methods ===
   */
/**
   * Callback for when the slave JVM has connected, and the bidirectional communications link has been 
   * established.  Provides access to the newly-created slave JVM.
   */
protected void handleSlaveConnected(SlaveRemote newSlave) {
    InterpreterJVMRemoteI slaveCast = (InterpreterJVMRemoteI) newSlave;
    _state.value().started(slaveCast);
}
