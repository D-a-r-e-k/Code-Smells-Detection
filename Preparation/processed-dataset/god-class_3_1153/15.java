@ManagedOperation
public String printPendingRequesters() {
    received_lock.lock();
    try {
        return pending_requesters.toString();
    } finally {
        received_lock.unlock();
    }
}
