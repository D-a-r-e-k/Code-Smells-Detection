/**
   * Callback for when the slave JVM fails to either run or respond to {@link SlaveRemote#start}.
   * @param e  Exception that occurred during startup.
   */
protected void handleSlaveWontStart(Exception e) {
    debug.log("Slave won't start", e);
    _state.value().startFailed(e);
}
