private void returnBufferBack(ByteBuffer byteBuffer) {
    try {
        handler.getServer().getByteBufferPool().returnObject(byteBuffer);
    } catch (Exception er) {
        logger.warning("Error while returning ByteBuffer to pool: " + er);
    }
}
