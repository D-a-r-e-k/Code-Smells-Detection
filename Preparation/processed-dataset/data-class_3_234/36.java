/** Compact report of all nonempty queues (one queue per line)
     * 
     * @param writer
     */
private void allNonemptyReportTo(PrintWriter writer) {
    ArrayList<WorkQueue> inProcessQueuesCopy;
    synchronized (this.inProcessQueues) {
        // grab a copy that will be stable against mods for report duration  
        @SuppressWarnings("unchecked") Collection<WorkQueue> inProcess = this.inProcessQueues;
        inProcessQueuesCopy = new ArrayList<WorkQueue>(inProcess);
    }
    writer.print("\n -----===== IN-PROCESS QUEUES =====-----\n");
    queueSingleLinesTo(writer, inProcessQueuesCopy.iterator());
    writer.print("\n -----===== READY QUEUES =====-----\n");
    queueSingleLinesTo(writer, this.readyClassQueues.iterator());
    writer.print("\n -----===== SNOOZED QUEUES =====-----\n");
    queueSingleLinesTo(writer, this.snoozedClassQueues.iterator());
    writer.print("\n -----===== INACTIVE QUEUES =====-----\n");
    queueSingleLinesTo(writer, this.inactiveQueues.iterator());
    writer.print("\n -----===== RETIRED QUEUES =====-----\n");
    queueSingleLinesTo(writer, this.retiredQueues.iterator());
}
