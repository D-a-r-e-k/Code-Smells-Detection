/**
	 * close every connection to the jdbc-source and remove the PooleElements 
	 */
public void shutdown() {
    synchronized (this.pool) {
        for (int i = 0; i < pool.length; i++) {
            if (pool[i] == null) {
                continue;
            }
            pool[i].cleanup();
            pool[i] = null;
        }
    }
}
