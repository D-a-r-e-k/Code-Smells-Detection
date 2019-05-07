/** 
	 * Makes the pool of ClientHandler
	 * @since 1.3
	 */
private void makeClientHandlerPool(PoolConfig opConfig) throws Exception {
    logger.finer("Creating ClientHandler pool");
    PoolableObjectFactory factory = new ClientHandlerObjectFactory(getBlockingMode());
    clientHandlerPool = poolManager.makeClientHandlerPool(factory, opConfig);
    poolManager.initPool(clientHandlerPool, opConfig);
    clientHandlerPool = makeQSObjectPool(clientHandlerPool);
    clientIdentifier.setClientHandlerPool((QSObjectPool) clientHandlerPool);
}
