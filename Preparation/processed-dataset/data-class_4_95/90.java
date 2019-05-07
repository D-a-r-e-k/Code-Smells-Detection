/** 
	 * Makes the pool of ClientData
	 * @since 1.3
	 */
private void makeClientDataPool(PoolableObjectFactory factory, PoolConfig opConfig) throws Exception {
    logger.finer("Creating ClientData pool");
    clientDataPool = poolManager.makeClientDataPool(factory, opConfig);
    poolManager.initPool(clientDataPool, opConfig);
    clientDataPool = makeQSObjectPool(clientDataPool);
}
