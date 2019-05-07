void method0() { 
/** Number of slave startup failures allowed before aborting the startup process. */
private static final int MAX_STARTUP_FAILURES = 3;
/** Number of milliseconds to block while waiting for an InterpreterJVM stub. */
private static final int STARTUP_TIMEOUT = 10000;
/** Contains the current InterpreterJVM stub, or {@code null} if it is not running. */
private final StateMonitor<State> _state;
/** Instance of inner class to handle interpret result. */
private final ResultHandler _handler = new ResultHandler();
/** Listens to interactions-related events. */
private volatile InteractionsModelCallback _interactionsModel;
/** Listens to JUnit-related events. */
private volatile JUnitModelCallback _junitModel;
/** Listens to debug-related events */
private volatile DebugModelCallback _debugModel;
/* JVM execution options */
/** Whether to allow "assert" statements to run in the remote JVM. */
private volatile boolean _allowAssertions = false;
/** Class path to use for starting the interpreter JVM */
private volatile Iterable<File> _startupClassPath;
/** Working directory for slave JVM */
private volatile File _workingDir;
}
