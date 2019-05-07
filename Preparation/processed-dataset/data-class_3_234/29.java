/**  (non-Javadoc)
     * @see org.archive.crawler.framework.Frontier#discoveredUriCount()
     */
public long discoveredUriCount() {
    return (this.alreadyIncluded != null) ? this.alreadyIncluded.count() : 0;
}
