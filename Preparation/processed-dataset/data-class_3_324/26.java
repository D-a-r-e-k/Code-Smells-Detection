private void handleViewChange(View view) {
    List<Address> mbrs = view != null ? view.getMembers() : null;
    if (mbrs != null) {
        synchronized (members) {
            members.clear();
            members.addAll(mbrs);
        }
    }
    lock.lock();
    try {
        // remove all members which left from pending_creditors 
        if (pending_creditors.retainAll(members) && pending_creditors.isEmpty()) {
            // the collection was changed and is empty now as a result of retainAll() 
            curr_credits_available = max_credits;
            if (log.isTraceEnabled())
                log.trace("replenished credits to " + curr_credits_available);
            credits_available.signalAll();
        }
    } finally {
        lock.unlock();
    }
    received_lock.lock();
    try {
        // remove left members 
        received.keySet().retainAll(members);
        // add new members with *full* credits (see doc/design/SimpleFlowControl.txt for reason) 
        for (Address mbr : members) {
            if (!received.containsKey(mbr))
                received.put(mbr, MAX_CREDITS);
        }
        // remove left members from pending credit requesters 
        pending_requesters.retainAll(members);
    } finally {
        received_lock.unlock();
    }
}
