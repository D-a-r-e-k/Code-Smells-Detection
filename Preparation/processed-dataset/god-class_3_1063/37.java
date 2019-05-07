/** Compact report of all nonempty queues (one queue per line)
     * 
     * @param writer
     */
private void allQueuesReportTo(PrintWriter writer) {
    queueSingleLinesTo(writer, allQueues.keySet().iterator());
}
