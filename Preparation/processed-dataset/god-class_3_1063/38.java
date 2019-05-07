/**
     * Write the single-line reports of all queues in the
     * iterator to the writer 
     * 
     * @param writer to receive report
     * @param iterator over queues of interest.
     */
private void queueSingleLinesTo(PrintWriter writer, Iterator<?> iterator) {
    Object obj;
    WorkQueue q;
    boolean legendWritten = false;
    while (iterator.hasNext()) {
        obj = iterator.next();
        if (obj == null) {
            continue;
        }
        q = (obj instanceof WorkQueue) ? (WorkQueue) obj : (WorkQueue) this.allQueues.get((String) obj);
        if (q == null) {
            writer.print(" ERROR: " + obj);
        }
        if (!legendWritten) {
            writer.println(q.singleLineLegend());
            legendWritten = true;
        }
        q.singleLineReportTo(writer);
    }
}
