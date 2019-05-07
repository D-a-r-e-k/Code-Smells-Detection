public void stop() {
    super.stop();
    running = false;
    lock.lock();
    try {
        credits_available.signalAll();
    } finally {
        lock.unlock();
    }
}
