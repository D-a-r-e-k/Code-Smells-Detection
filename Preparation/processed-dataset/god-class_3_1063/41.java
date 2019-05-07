/**
     * Append queue report to general Frontier report.
     * @param w StringBuffer to append to.
     * @param iterator An iterator over 
     * @param total
     * @param max
     */
protected void appendQueueReports(PrintWriter w, Iterator<?> iterator, int total, int max) {
    Object obj;
    WorkQueue q;
    for (int count = 0; iterator.hasNext() && (count < max); count++) {
        obj = iterator.next();
        if (obj == null) {
            continue;
        }
        q = (obj instanceof WorkQueue) ? (WorkQueue) obj : (WorkQueue) this.allQueues.get((String) obj);
        if (q == null) {
            w.print("WARNING: No report for queue " + obj);
        }
        q.reportTo(w);
    }
    if (total > max) {
        w.print("...and " + (total - max) + " more.\n");
    }
}
