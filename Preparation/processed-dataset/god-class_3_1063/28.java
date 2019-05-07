/**
     * Forget the given CrawlURI. This allows a new instance
     * to be created in the future, if it is reencountered under
     * different circumstances.
     *
     * @param curi The CrawlURI to forget
     */
protected void forget(CrawlURI curi) {
    logger.finer("Forgetting " + curi);
    alreadyIncluded.forget(canonicalize(curi.getUURI()), curi);
}
