/**
   * Callback for when the slave JVM has quit.
   * @param status The exit code returned by the slave JVM.
   */
protected void handleSlaveQuit(int status) {
    debug.logValue("Slave quit", "status", status);
    _state.value().stopped(status);
}
