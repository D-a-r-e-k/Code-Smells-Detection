/**
   * Stop the interpreter if it's current running.  (Note that, until {@link #startInterpreterJVM} is called
   * again, all methods that delegate to the interpreter JVM will fail, returning "false" or "none".)
   */
public void stopInterpreterJVM() {
    _state.value().stop();
}
