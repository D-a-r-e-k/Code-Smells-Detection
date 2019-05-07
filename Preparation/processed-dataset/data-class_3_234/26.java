private boolean includesRetireDirective(CrawlURI curi) {
    return curi.containsKey(A_FORCE_RETIRE) && (Boolean) curi.getObject(A_FORCE_RETIRE);
}
