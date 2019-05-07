/**
   * This handles the event that the StoreInfo is removed from Pooka.
   */
public void remove() {
    // FIXME need to do a lot here. 
    try {
        disconnectStore();
    } catch (Exception e) {
    }
    cleanup();
}
