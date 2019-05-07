/** 
	 * Makes the pool of ByteBuffer
	 * @since 1.4.5
	 */
private void makeClientPool(PoolConfig opConfig) throws Exception {
    logger.finer("Creating ClientThread pool");
    ThreadObjectFactory factory = new ThreadObjectFactory();
    ObjectPool objectPool = poolManager.makeClientPool(factory, opConfig);
    pool = new ClientPool(makeQSObjectPool(objectPool), opConfig);
    factory.setClientPool(pool);
    pool.setMaxThreadsForNioWrite(getBasicConfig().getAdvancedSettings().getMaxThreadsForNioWrite());
    poolManager.initPool(objectPool, opConfig);
}
