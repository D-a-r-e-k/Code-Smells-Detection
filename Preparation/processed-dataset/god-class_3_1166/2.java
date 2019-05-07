public void removeDocumentClosedListener(DocumentClosedListener l) {
    synchronized (_closedListeners) {
        _closedListeners.remove(l);
    }
}
