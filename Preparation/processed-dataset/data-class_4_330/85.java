/**
     * Forces an update of the <code>canSeeTiles</code>. This method should
     * be used to invalidate the current <code>canSeeTiles</code>. The method
     * {@link #resetCanSeeTiles} will be called whenever it is needed.
     */
public void invalidateCanSeeTiles() {
    synchronized (canSeeLock) {
        canSeeTiles = null;
    }
}
