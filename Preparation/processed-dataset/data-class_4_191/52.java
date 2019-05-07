/**
   * This handles the distributions of any Connection events.
   *
   * As defined in interface net.suberic.pooka.event.MessageLoadedListener.
   */
public void fireConnectionEvent(ConnectionEvent e) {
    // from the EventListenerList javadoc, including comments. 
    // Guaranteed to return a non-null array 
    Object[] listeners = eventListeners.getListenerList();
    // Process the listeners last to first, notifying 
    // those that are interested in this event 
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
        if (listeners[i] == ConnectionListener.class) {
            ConnectionListener listener = (ConnectionListener) listeners[i + 1];
            if (e.getType() == ConnectionEvent.CLOSED)
                listener.closed(e);
            else if (e.getType() == ConnectionEvent.DISCONNECTED)
                listener.disconnected(e);
            else if (e.getType() == ConnectionEvent.OPENED)
                listener.opened(e);
        }
    }
}
