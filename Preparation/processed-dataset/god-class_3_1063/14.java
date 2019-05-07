/** 
     * Accomodate any changes in settings.
     * 
     * @see org.archive.crawler.framework.Frontier#kickUpdate()
     */
public void kickUpdate() {
    super.kickUpdate();
    int target = (Integer) getUncheckedAttribute(null, ATTR_TARGET_READY_QUEUES_BACKLOG);
    if (target < 1) {
        target = 1;
    }
    this.targetSizeForReadyQueues = target;
    try {
        initCostPolicy();
    } catch (FatalConfigurationException fce) {
        throw new RuntimeException(fce);
    }
    // The rules for a 'retired' queue may have changed; so, 
    // unretire all queues to 'inactive'. If they still qualify 
    // as retired/overbudget next time they come up, they'll 
    // be re-retired; if not, they'll get a chance to become 
    // active under the new rules. 
    Object key = this.retiredQueues.poll();
    while (key != null) {
        WorkQueue q = (WorkQueue) this.allQueues.get((String) key);
        if (q != null) {
            unretireQueue(q);
        }
        key = this.retiredQueues.poll();
    }
}
