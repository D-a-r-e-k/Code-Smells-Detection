/**
   * Get a "fresh" interpreter JVM.  Has the same effect as {@link #startInterpreterJVM} if no interpreter
   * is running.  If a currently-running JVM is already "fresh", it is still stopped and restarted when
   * {@code force} is true.
   */
public void restartInterpreterJVM(boolean force) {
    _state.value().restart(force);
}
