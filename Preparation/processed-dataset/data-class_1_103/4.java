/**
	 * Gets the next available and valid poolelement or tryes to create a new one
	 * @return the valid and available PoolElement
	 * @throws Exception If creation of PoolElement failes
	 */
private PoolElement getPoolElement(ConnectionBuffer cb) throws Exception {
    for (int i = 0; i < dbProps.poolsize; i++) {
        if ((cb != null && !cb.isValid()) || Thread.currentThread().isInterrupted())
            throw new CanceledRequestException("ConnectionBuffer has been invalidated");
        if (!Server.srv.isRunning()) {
            StringBuffer sb = new StringBuffer("Created no new Connetion - Server shutting down");
            Server.log("ConnectionPool", sb.toString(), Server.MSG_AUTH, Server.LVL_VERBOSE);
            return null;
        }
        p++;
        if (p > dbProps.poolsize - 1)
            p = 0;
        synchronized (this.pool) {
            if (pool[p] == null) {
                pool[p] = createPoolElement();
            }
            switch(pool[p].grab()) {
                case PoolElement.INVALID:
                    pool[p].cleanup();
                    pool[p] = createPoolElement();
                case PoolElement.IDLE:
                    return pool[p];
                case PoolElement.ACTIVE:
                    continue;
            }
        }
    }
    return null;
}
