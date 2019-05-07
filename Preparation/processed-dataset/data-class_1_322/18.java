/**
    * Closes all open sockets, the server socket and all threads waiting for incoming messages
    */
public void stop() {
    super.stop();
    serverStopping = true;
    if (reaper != null)
        reaper.stop();
    // Stop the main selector 
    if (m_acceptSelector != null)
        m_acceptSelector.wakeup();
    // Stop selector threads 
    if (m_readHandlers != null) {
        for (int i = 0; i < m_readHandlers.length; i++) {
            try {
                m_readHandlers[i].add(new Shutdown());
            } catch (InterruptedException e) {
                log.error("Thread (" + Thread.currentThread().getName() + ") was interrupted, failed to shutdown selector", e);
            }
        }
    }
    if (m_writeHandlers != null) {
        for (int i = 0; i < m_writeHandlers.length; i++) {
            try {
                m_writeHandlers[i].queue.put(new Shutdown());
                m_writeHandlers[i].selector.wakeup();
            } catch (InterruptedException e) {
                log.error("Thread (" + Thread.currentThread().getName() + ") was interrupted, failed to shutdown selector", e);
            }
        }
    }
    // Stop the callback thread pool 
    if (m_requestProcessors instanceof ThreadPoolExecutor)
        ((ThreadPoolExecutor) m_requestProcessors).shutdownNow();
    if (m_requestProcessors instanceof ThreadPoolExecutor) {
        try {
            ((ThreadPoolExecutor) m_requestProcessors).awaitTermination(Global.THREADPOOL_SHUTDOWN_WAIT_TIME, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
        }
    }
    // then close the connections 
    synchronized (conns) {
        Iterator it = conns.values().iterator();
        while (it.hasNext()) {
            Connection conn = (Connection) it.next();
            conn.destroy();
        }
        conns.clear();
    }
    while (!m_backGroundThreads.isEmpty()) {
        Thread t = m_backGroundThreads.remove(0);
        try {
            t.join();
        } catch (InterruptedException e) {
            log.error("Thread (" + Thread.currentThread().getName() + ") was interrupted while waiting on thread " + t.getName() + " to finish.");
        }
    }
    m_backGroundThreads.clear();
}
