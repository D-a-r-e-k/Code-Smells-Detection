/**
   * Dispose of the frame for the LogBrokerMonitor.
   */
public void dispose() {
    _logMonitorFrame.dispose();
    _isDisposed = true;
    if (_callSystemExitOnClose == true) {
        System.exit(0);
    }
}
