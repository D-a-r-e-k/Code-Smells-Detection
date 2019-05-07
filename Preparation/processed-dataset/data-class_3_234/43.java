public void considerIncluded(UURI u) {
    this.alreadyIncluded.note(canonicalize(u));
    CrawlURI temp = new CrawlURI(u);
    temp.setClassKey(getClassKey(temp));
    getQueueFor(temp).expend(getCost(temp));
}
