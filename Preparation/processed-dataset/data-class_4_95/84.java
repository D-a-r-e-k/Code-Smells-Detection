/**
	 * Initialise all Object and Thread pools.
	 * @since 1.3
	 */
public void initAllPools() throws Exception {
    logger.fine("Creating pools");
    if (getBlockingMode() == false) {
        makeByteBufferPool(getBasicConfig().getObjectPoolConfig().getByteBufferObjectPoolConfig());
    }
    makeClientPool(getBasicConfig().getObjectPoolConfig().getThreadObjectPoolConfig());
    makeClientHandlerPool(getBasicConfig().getObjectPoolConfig().getClientHandlerObjectPoolConfig());
    //check if client data is poolable  
    if (clientDataClass != null) {
        try {
            clientData = (ClientData) clientDataClass.newInstance();
            if (PoolableObject.class.isInstance(clientData) == true) {
                PoolableObject po = (PoolableObject) clientData;
                if (po.isPoolable() == true) {
                    makeClientDataPool(po.getPoolableObjectFactory(), getBasicConfig().getObjectPoolConfig().getClientDataObjectPoolConfig());
                } else {
                    clientDataPool = null;
                    logger.fine("ClientData is not poolable!");
                }
            }
        } catch (Exception e) {
            logger.warning("Error: " + e);
            throw e;
        }
    }
    try {
        makeDBObjectPool();
    } catch (Exception e) {
        logger.warning("Error in makeDBObjectPool() : " + e);
        logger.fine("StackTrace:\n" + MyString.getStackTrace(e));
        throw e;
    }
    logger.fine("Created pools");
}
