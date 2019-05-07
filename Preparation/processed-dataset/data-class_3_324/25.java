private void handleCreditResponse(Address sender) {
    lock.lock();
    try {
        num_replenishments_received++;
        if (pending_creditors.remove(sender) && pending_creditors.isEmpty()) {
            curr_credits_available = max_credits;
            stop = System.nanoTime();
            long diff = (stop - start) / 1000000L;
            if (log.isTraceEnabled())
                log.trace("replenished credits to " + curr_credits_available + " (total blocking time=" + diff + " ms)");
            blockings.add(new Long(diff));
            total_block_time += diff;
            credits_available.signalAll();
        }
    } finally {
        lock.unlock();
    }
}
