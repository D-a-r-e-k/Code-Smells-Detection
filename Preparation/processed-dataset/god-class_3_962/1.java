//-------------------------------------------------------------------------- 
//   Public Methods: 
//-------------------------------------------------------------------------- 
/**
   * Show the frame for the LogBrokerMonitor. Dispatched to the
   * swing thread.
   */
public void show(final int delay) {
    if (_logMonitorFrame.isVisible()) {
        return;
    }
    // This request is very low priority, let other threads execute first. 
    SwingUtilities.invokeLater(new Runnable() {

        public void run() {
            Thread.yield();
            pause(delay);
            _logMonitorFrame.setVisible(true);
        }
    });
}
