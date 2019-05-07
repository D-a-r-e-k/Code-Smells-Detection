/**
	 * Cleans all Object and Thread pools
	 * @since 1.3
	 */
public void clearAllPools() throws Exception {
    try {
        if (pool != null)
            getClientPool().clear();
        if (clientHandlerPool != null)
            getClientHandlerPool().clear();
        if (getClientDataPool() != null)
            getClientDataPool().clear();
        if (getDBPoolUtil() != null)
            getDBPoolUtil().clean();
        if (byteBufferPool != null)
            getByteBufferPool().clear();
    } catch (Exception e) {
        logger.warning("Error: " + e);
        throw e;
    }
}
