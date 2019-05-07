/**
   * Stops the store thread.
   */
public void stopStoreThread() {
    if (storeThread != null) {
        storeThread.setStop(true);
    }
}
