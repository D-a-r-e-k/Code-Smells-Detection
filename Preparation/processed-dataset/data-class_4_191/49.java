/**
   * This handles the MessageLoadedEvent.
   *
   * As defined in interface net.suberic.pooka.event.MessageLoadedListener.
   */
public void fireMessageChangedEvent(MessageChangedEvent mce) {
    // from the EventListenerList javadoc, including comments. 
    // Guaranteed to return a non-null array 
    Object[] listeners = eventListeners.getListenerList();
    // Process the listeners last to first, notifying 
    // those that are interested in this event 
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
        if (listeners[i] == MessageChangedListener.class) {
            ((MessageChangedListener) listeners[i + 1]).messageChanged(mce);
        }
    }
}
