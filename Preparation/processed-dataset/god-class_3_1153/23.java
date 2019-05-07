private void handleMessage(Message msg, Address sender) {
    int len = msg.getLength();
    // we don't care about headers, this is faster than size() 
    Long new_val;
    boolean send_credit_response = false;
    received_lock.lock();
    try {
        Long credits = received.get(sender);
        if (credits == null) {
            new_val = MAX_CREDITS;
            received.put(sender, new_val);
        } else {
            new_val = credits.longValue() + len;
            received.put(sender, new_val);
        }
        // if(log.isTraceEnabled()) 
        //  log.trace("received " + len + " bytes from " + sender + ": total=" + new_val + " bytes"); 
        // see whether we have any pending credit requests 
        if (!pending_requesters.isEmpty() && pending_requesters.contains(sender) && new_val.longValue() >= max_credits) {
            pending_requesters.remove(sender);
            if (log.isTraceEnabled())
                log.trace("removed " + sender + " from credit requesters; sending credits");
            received.put(sender, ZERO_CREDITS);
            send_credit_response = true;
        }
    } finally {
        received_lock.unlock();
    }
    if (send_credit_response)
        // send outside of the monitor 
        sendCreditResponse(sender);
}
