public synchronized void write(byte[] b, int off, int len) throws IOException {
    if (len == 0) {
        return;
    }
    handler.isConnected();
    ByteBuffer byteBuffer = null;
    int remaining = 0;
    int toWrite = len;
    if (toWrite != 0 && bufferList.size() != 0) {
        byteBuffer = (ByteBuffer) bufferList.remove(bufferList.size() - 1);
        if (byteBuffer.remaining() == 0) {
            bufferList.add(byteBuffer);
            byteBuffer = null;
        }
    }
    while (toWrite != 0) {
        try {
            if (byteBuffer == null) {
                byteBuffer = (ByteBuffer) handler.getServer().getByteBufferPool().borrowObject();
            }
        } catch (Exception e) {
            logger.warning("Could not borrow ByteBufer from pool: " + e);
            throw new IOException(e.toString());
        }
        remaining = byteBuffer.remaining();
        if (remaining < toWrite) {
            byteBuffer.put(b, off, remaining);
            off = off + remaining;
            toWrite = toWrite - remaining;
        } else {
            byteBuffer.put(b, off, toWrite);
            toWrite = 0;
        }
        bufferList.add(byteBuffer);
        byteBuffer = null;
    }
}
