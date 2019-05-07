/** Validates before changing visibility.  Only runs in the event thread.
    * @param vis true if frame should be shown, false if it should be hidden.
    */
public void setVisible(boolean vis) {
    assert EventQueue.isDispatchThread();
    validate();
    // made modal for now 
    if (vis) {
        //      _mainFrame.hourglassOn(); 
        //      _mainFrame.installModalWindowAdapter(this, NO_OP, CANCEL); 
        enableChangeListeners();
        toFront();
    } else {
        //      _mainFrame.removeModalWindowAdapter(this); 
        //      _mainFrame.hourglassOff(); 
        disableChangeListeners();
        _mainFrame.toFront();
    }
    super.setVisible(vis);
}
