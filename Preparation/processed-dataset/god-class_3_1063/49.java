public float congestionRatio() {
    int inProcessCount = inProcessQueues.uniqueSet().size();
    int readyCount = readyClassQueues.size();
    int snoozedCount = snoozedClassQueues.size();
    int activeCount = inProcessCount + readyCount + snoozedCount;
    int inactiveCount = inactiveQueues.size();
    return (float) (activeCount + inactiveCount) / (inProcessCount + snoozedCount);
}
