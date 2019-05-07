/**
     * Force logging, etc. of operator- deleted CrawlURIs
     * 
     * @see org.archive.crawler.framework.Frontier#deleted(org.archive.crawler.datamodel.CrawlURI)
     */
public synchronized void deleted(CrawlURI curi) {
    //treat as disregarded 
    controller.fireCrawledURIDisregardEvent(curi);
    log(curi);
    incrementDisregardedUriCount();
    curi.stripToMinimal();
    curi.processingCleanup();
}
