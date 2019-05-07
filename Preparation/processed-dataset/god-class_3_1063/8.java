/* (non-Javadoc)
	 * @see org.archive.crawler.frontier.AbstractFrontier#asCrawlUri(org.archive.crawler.datamodel.CandidateURI)
	 */
protected CrawlURI asCrawlUri(CandidateURI caUri) {
    CrawlURI curi = super.asCrawlUri(caUri);
    // force cost to be calculated, pre-insert 
    getCost(curi);
    return curi;
}
