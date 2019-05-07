@ManagedOperation
public String printReceived() {
    received_lock.lock();
    try {
        return received.toString();
    } finally {
        received_lock.unlock();
    }
}
