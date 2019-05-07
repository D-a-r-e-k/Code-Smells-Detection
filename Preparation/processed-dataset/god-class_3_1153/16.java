@ManagedOperation
public void unblock() {
    lock.lock();
    try {
        curr_credits_available = max_credits;
        credits_available.signalAll();
    } finally {
        lock.unlock();
    }
}
