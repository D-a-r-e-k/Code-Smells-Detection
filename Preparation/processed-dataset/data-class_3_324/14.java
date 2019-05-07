@ManagedOperation
public String printPendingCreditors() {
    lock.lock();
    try {
        return pending_creditors.toString();
    } finally {
        lock.unlock();
    }
}
