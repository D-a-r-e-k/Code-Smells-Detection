/*
   * === Startup and shutdown methods ===
   */
/** Starts the interpreter if it's not running already. */
public void startInterpreterJVM() {
    _state.value().start();
}
