public void removeConnectionListener(ConnectionListener oldListener) {
    eventListeners.remove(ConnectionListener.class, oldListener);
}
