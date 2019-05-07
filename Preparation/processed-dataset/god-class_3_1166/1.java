public void addDocumentClosedListener(DocumentClosedListener l) {
    synchronized (_closedListeners) {
        _closedListeners.add(l);
    }
}
