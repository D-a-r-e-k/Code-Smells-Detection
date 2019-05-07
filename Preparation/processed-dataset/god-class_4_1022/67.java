public void removeMessageCountListener(MessageCountListener oldListener) {
    eventListeners.remove(MessageCountListener.class, oldListener);
}
