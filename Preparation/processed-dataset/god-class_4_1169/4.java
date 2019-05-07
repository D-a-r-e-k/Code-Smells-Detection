/**
   * Stop the interpreter JVM, do not restart it, and terminate the RMI server associated with this object.
   * May be useful when a number of different MainJVM objects are created (such as when running tests).
   */
public void dispose() {
    _state.value().dispose();
}
