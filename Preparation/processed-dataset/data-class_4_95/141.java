/** 
	 * Makes the pool of ByteBuffer
	 * @since 1.4.5
	 */
private void makeByteBufferPool(PoolConfig opConfig) {
    logger.finer("Creating ByteBufferPool pool");
    int bufferSize = getBasicConfig().getAdvancedSettings().getByteBufferSize();
    boolean useDirectByteBuffer = getBasicConfig().getAdvancedSettings().getUseDirectByteBuffer();
    PoolableObjectFactory factory = new ByteBufferObjectFactory(bufferSize, useDirectByteBuffer);
    byteBufferPool = poolManager.makeByteBufferPool(factory, opConfig);
    poolManager.initPool(byteBufferPool, opConfig);
}
