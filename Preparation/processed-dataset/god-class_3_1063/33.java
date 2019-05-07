/**
     * @param w Where to write to.
     */
public void singleLineReportTo(PrintWriter w) {
    if (this.allQueues == null) {
        return;
    }
    int allCount = allQueues.size();
    int inProcessCount = inProcessQueues.uniqueSet().size();
    int readyCount = readyClassQueues.size();
    int snoozedCount = snoozedClassQueues.size();
    int activeCount = inProcessCount + readyCount + snoozedCount;
    int inactiveCount = inactiveQueues.size();
    int retiredCount = retiredQueues.size();
    int exhaustedCount = allCount - activeCount - inactiveCount - retiredCount;
    w.print(allCount);
    w.print(" queues: ");
    w.print(activeCount);
    w.print(" active (");
    w.print(inProcessCount);
    w.print(" in-process; ");
    w.print(readyCount);
    w.print(" ready; ");
    w.print(snoozedCount);
    w.print(" snoozed); ");
    w.print(inactiveCount);
    w.print(" inactive; ");
    w.print(retiredCount);
    w.print(" retired; ");
    w.print(exhaustedCount);
    w.print(" exhausted");
    w.flush();
}
