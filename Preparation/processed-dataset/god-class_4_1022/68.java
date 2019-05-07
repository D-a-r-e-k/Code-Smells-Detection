public void fireMessageCountEvent(MessageCountEvent mce) {
    // from the EventListenerList javadoc, including comments. 
    // Guaranteed to return a non-null array 
    Object[] listeners = eventListeners.getListenerList();
    // Process the listeners last to first, notifying 
    // those that are interested in this event 
    if (mce.getType() == MessageCountEvent.ADDED) {
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == MessageCountListener.class) {
                ((MessageCountListener) listeners[i + 1]).messagesAdded(mce);
            }
        }
    } else if (mce.getType() == MessageCountEvent.REMOVED) {
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == MessageCountListener.class) {
                ((MessageCountListener) listeners[i + 1]).messagesRemoved(mce);
            }
        }
    }
}
