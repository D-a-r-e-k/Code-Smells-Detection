/**
   * Shows the current status for this store and its thread.
   */
public void showStatus() {
    StringBuffer statusBuffer = new StringBuffer();
    getLogger().log(Level.INFO, "Status for store " + getStoreID());
    statusBuffer.append("Status for store " + getStoreID() + "\r\n");
    boolean infoIsConnected = isConnected();
    getLogger().log(Level.INFO, "Connected:  " + infoIsConnected);
    statusBuffer.append("Connected:  " + infoIsConnected + "\r\n");
    if (storeThread != null) {
        String currentAction = storeThread.getCurrentActionName();
        getLogger().log(Level.INFO, "Current Action:  " + currentAction);
        statusBuffer.append("Current Action:  " + currentAction + "\r\n");
        int queueSize = storeThread.getQueueSize();
        getLogger().log(Level.INFO, "Action Queue Size:  " + queueSize);
        statusBuffer.append("Action Queue Size:  " + queueSize + "\r\n");
        if (storeThread.getQueueSize() > 0) {
            System.out.println("Queue:");
            java.util.List queue = storeThread.getQueue();
            for (int i = 0; i < queue.size(); i++) {
                net.suberic.util.thread.ActionThread.ActionEventPair current = (net.suberic.util.thread.ActionThread.ActionEventPair) queue.get(i);
                String queueString = "  queue[" + i + "]:  ";
                String entryDescription = (String) current.action.getValue(javax.swing.Action.SHORT_DESCRIPTION);
                if (entryDescription == null)
                    entryDescription = (String) current.action.getValue(javax.swing.Action.NAME);
                if (entryDescription == null)
                    entryDescription = "Unknown action";
                queueString = queueString + entryDescription;
                System.out.println(queueString);
                statusBuffer.append(queueString + "\r\n");
            }
        }
        statusBuffer.append("Stack Trace:\r\n");
        StackTraceElement[] stackTrace = storeThread.getStackTrace();
        for (StackTraceElement stackLine : stackTrace) {
            statusBuffer.append("  " + stackLine + "\r\n");
        }
    } else {
        getLogger().log(Level.INFO, "No Action Thread.");
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement stackLine : stackTrace) {
            statusBuffer.append("  " + stackLine + "\r\n");
        }
    }
    Pooka.getUIFactory().showMessage(statusBuffer.toString(), "Status for " + getStoreID());
}
