@Override
public void threadFinished() {
    log.debug("Thread Finished");
    closeThreadLocalConnections();
}
