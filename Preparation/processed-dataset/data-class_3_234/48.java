public long averageDepth() {
    int inProcessCount = inProcessQueues.uniqueSet().size();
    int readyCount = readyClassQueues.size();
    int snoozedCount = snoozedClassQueues.size();
    int activeCount = inProcessCount + readyCount + snoozedCount;
    int inactiveCount = inactiveQueues.size();
    int totalQueueCount = (activeCount + inactiveCount);
    return (totalQueueCount == 0) ? 0 : liveQueuedUriCount.get() / totalQueueCount;
}
