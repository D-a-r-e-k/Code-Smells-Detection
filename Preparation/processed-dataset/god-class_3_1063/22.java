/**
     * Replenish the budget of the given queue by the appropriate amount.
     * 
     * @param queue queue to replenish
     */
private void replenishSessionBalance(WorkQueue queue) {
    UURI contextUri = queue.getContextUURI(this);
    // TODO: consider confusing cross-effects of this and IP-based politeness 
    queue.setSessionBalance(((Integer) getUncheckedAttribute(contextUri, ATTR_BALANCE_REPLENISH_AMOUNT)).intValue());
    // reset total budget (it may have changed) 
    // TODO: is this the best way to be sensitive to potential mid-crawl changes 
    long totalBudget = ((Long) getUncheckedAttribute(contextUri, ATTR_QUEUE_TOTAL_BUDGET)).longValue();
    queue.setTotalBudget(totalBudget);
}
