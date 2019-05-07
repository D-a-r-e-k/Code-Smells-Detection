/**
     * This method compiles a human readable report on the status of the frontier
     * at the time of the call.
     * @param name Name of report.
     * @param writer Where to write to.
     */
public synchronized void reportTo(String name, PrintWriter writer) {
    if (ALL_NONEMPTY.equals(name)) {
        allNonemptyReportTo(writer);
        return;
    }
    if (ALL_QUEUES.equals(name)) {
        allQueuesReportTo(writer);
        return;
    }
    if (name != null && !STANDARD_REPORT.equals(name)) {
        writer.print(name);
        writer.print(" unavailable; standard report:\n");
    }
    standardReportTo(writer);
}
