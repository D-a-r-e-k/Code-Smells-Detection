/** Returns the debug port to use, as specified by the model. Returns -1 if no usable port could be found. */
private int _getDebugPort() {
    int port = -1;
    try {
        port = _interactionsModel.getDebugPort();
    } catch (IOException ioe) {
    }
    return port;
}
