/**
	 * Closes all Object and Thread pools
	 * @since 1.3
	 */
public void closeAllPools() throws Exception {
    if (pool == null && clientHandlerPool == null && getClientDataPool() == null && getDBPoolUtil() == null && byteBufferPool == null) {
        return;
    }
    logger.fine("Closing pools for " + getName());
    try {
        if (pool != null && PoolHelper.isPoolOpen(getClientPool().getObjectPool())) {
            logger.finer("Closing ClientThread pool.");
            getClientPool().close();
        }
        if (clientHandlerPool != null && PoolHelper.isPoolOpen(getClientHandlerPool())) {
            logger.finer("Closing ClientHandler pool.");
            getClientHandlerPool().close();
        }
        if (getClientDataPool() != null && PoolHelper.isPoolOpen(getClientDataPool())) {
            logger.finer("Closing ClientData pool.");
            getClientDataPool().close();
        }
        if (getDBPoolUtil() != null) {
            logger.finer("Closing DB pool.");
            getDBPoolUtil().clean();
        }
        if (byteBufferPool != null && PoolHelper.isPoolOpen(getByteBufferPool())) {
            logger.finer("Closing ByteBuffer pool.");
            getByteBufferPool().close();
        }
        logger.fine("Closed pools for " + getName());
    } catch (Exception e) {
        logger.warning("Error closing pools for " + getName() + ": " + e);
        throw e;
    }
}
