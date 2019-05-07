public synchronized void write(int b) throws IOException {
    handler.isConnected();
    ByteBuffer byteBuffer = null;
    if (bufferList.size() != 0) {
        byteBuffer = (ByteBuffer) bufferList.remove(bufferList.size() - 1);
        if (byteBuffer.remaining() == 0) {
            bufferList.add(byteBuffer);
            byteBuffer = null;
        }
    }
    try {
        if (byteBuffer == null) {
            byteBuffer = (ByteBuffer) handler.getServer().getByteBufferPool().borrowObject();
        }
    } catch (Exception e) {
        logger.warning("Could not borrow ByteBufer from pool: " + e);
        throw new IOException(e.toString());
    }
    byteBuffer.put((byte) b);
    bufferList.add(byteBuffer);
}
