/**
     * Whether queues should start inactive (only becoming active when needed
     * to keep the crawler busy), or if queues should start out ready.
     * 
     * @return true if new queues should held inactive
     */
private boolean holdQueues() {
    return ((Boolean) getUncheckedAttribute(null, ATTR_HOLD_QUEUES)).booleanValue();
}
