public void removeMessageChangedListener(MessageChangedListener oldListener) {
    eventListeners.remove(MessageChangedListener.class, oldListener);
}
