/**
	 * Returns {@link org.quickserver.util.pool.thread.ClientPool} class that 
	 * managing the pool of threads for handling clients.
	 * @exception IllegalStateException if pool is not created yet.
	 * @since 1.3
	 */
public ClientPool getClientPool() {
    if (pool == null)
        throw new IllegalStateException("No ClientPool available yet!");
    return pool;
}
