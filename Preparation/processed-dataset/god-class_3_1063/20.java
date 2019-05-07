/**
     * Return the 'cost' of a CrawlURI (how much of its associated
     * queue's budget it depletes upon attempted processing)
     * 
     * @param curi
     * @return the associated cost
     */
private int getCost(CrawlURI curi) {
    int cost = curi.getHolderCost();
    if (cost == CrawlURI.UNCALCULATED) {
        cost = costAssignmentPolicy.costOf(curi);
        curi.setHolderCost(cost);
    }
    return cost;
}
