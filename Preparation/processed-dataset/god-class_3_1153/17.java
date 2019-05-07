// ------------------- End of management information ---------------------- 
public Object down(Event evt) {
    switch(evt.getType()) {
        case Event.MSG:
            Message msg = (Message) evt.getArg();
            Address dest = msg.getDest();
            if (dest != null && !dest.isMulticastAddress())
                // only handle multicast messages 
                break;
            boolean send_credit_request = false;
            lock.lock();
            try {
                while (curr_credits_available <= 0 && running) {
                    if (log.isTraceEnabled())
                        log.trace("blocking (current credits=" + curr_credits_available + ")");
                    try {
                        num_blockings++;
                        // will be signalled when we have credit responses from all members 
                        boolean rc = credits_available.await(max_block_time, TimeUnit.MILLISECONDS);
                        if (rc || (curr_credits_available <= 0 && running)) {
                            if (log.isTraceEnabled())
                                log.trace("returned from await but credits still unavailable (credits=" + curr_credits_available + ")");
                            long now = System.currentTimeMillis();
                            if (now - last_blocked_request >= max_block_time) {
                                last_blocked_request = now;
                                lock.unlock();
                                // send the credit request without holding the lock 
                                try {
                                    sendCreditRequest(true);
                                } finally {
                                    lock.lock();
                                }
                            }
                        } else {
                            // reset the last_blocked_request stamp so the 
                            // next timed out block will for sure send a request 
                            last_blocked_request = 0;
                        }
                    } catch (InterruptedException e) {
                    }
                }
                // when we get here, curr_credits_available is guaranteed to be > 0 
                int len = msg.getLength();
                num_bytes_sent += len;
                curr_credits_available -= len;
                // we'll block on insufficient credits on the next down() call 
                if (curr_credits_available <= 0) {
                    pending_creditors.clear();
                    synchronized (members) {
                        pending_creditors.addAll(members);
                    }
                    send_credit_request = true;
                }
            } finally {
                lock.unlock();
            }
            // we don't need to protect send_credit_request because a thread above either (a) decrements the credits 
            // by the msg length and sets send_credit_request to true or (b) blocks because there are no credits 
            // available. So only 1 thread can ever set send_credit_request at any given time 
            if (send_credit_request) {
                if (log.isTraceEnabled())
                    log.trace("sending credit request to group");
                start = System.nanoTime();
                // only 1 thread is here at any given time 
                Object ret = down_prot.down(evt);
                // send the message before the credit request 
                sendCreditRequest(false);
                // do this outside of the lock 
                return ret;
            }
            break;
        case Event.VIEW_CHANGE:
            handleViewChange((View) evt.getArg());
            break;
        case Event.SUSPECT:
            handleSuspect((Address) evt.getArg());
            break;
        case Event.CONFIG:
            Map<String, Object> map = (Map<String, Object>) evt.getArg();
            handleConfigEvent(map);
            break;
    }
    return down_prot.down(evt);
}
