/* (non-Javadoc)
     * @see org.archive.crawler.framework.Frontier#isEmpty()
     */
public synchronized boolean isEmpty() {
    return liveQueuedUriCount.get() == 0 && alreadyIncluded.pending() == 0;
}
