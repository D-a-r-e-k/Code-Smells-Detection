private void handleCreditRequest(Address sender, boolean urgent) {
    boolean send_credit_response = false;
    received_lock.lock();
    try {
        num_credit_requests_received++;
        Long bytes = received.get(sender);
        if (log.isTraceEnabled())
            log.trace("received credit request from " + sender + " (total received: " + bytes + " bytes");
        if (bytes == null) {
            if (log.isErrorEnabled())
                log.error("received credit request from " + sender + ", but sender is not in received hashmap;" + " adding it");
            send_credit_response = true;
        } else {
            if (bytes.longValue() < max_credits && !urgent) {
                if (log.isTraceEnabled())
                    log.trace("adding " + sender + " to pending credit requesters");
                pending_requesters.add(sender);
            } else {
                send_credit_response = true;
            }
        }
        if (send_credit_response)
            received.put(sender, ZERO_CREDITS);
    } finally {
        received_lock.unlock();
    }
    if (send_credit_response) {
        sendCreditResponse(sender);
    }
}
