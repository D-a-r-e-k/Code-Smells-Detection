/** Lets the model know if any exceptions occur while communicating with the Interpreter JVM. */
private void _handleRemoteException(RemoteException e) {
    if (e instanceof UnmarshalException) {
        /* Interpreter JVM has disappeared (perhaps reset); just ignore the error. */
        if (e.getCause() instanceof EOFException)
            return;
        /* Deals with bug 2688586: Reset during debugging throws UnmarshalException
       * We may want to extend this to all kinds of SocketExceptions. */
        if ((e.getCause() instanceof SocketException) && (e.getCause().getMessage().equals("Connection reset")))
            return;
    }
    DrJavaErrorHandler.record(e);
}
