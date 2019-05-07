private synchronized void writeLastByteBuffer() throws IOException {
    int written = 0;
    while (lastByteBuffer.remaining() != 0) {
        java.nio.channels.SocketChannel sc = handler.getSocketChannel();
        if (sc != null && sc.isOpen()) {
            written = sc.write(lastByteBuffer);
            if (written == 0) {
                break;
            }
            if (logger.isLoggable(Level.FINEST)) {
                logger.finest("Written " + written + " bytes");
            }
        } else {
            throw new IOException("SocketChannel was closed.");
        }
    }
    if (lastByteBuffer.remaining() == 0) {
        returnBufferBack(lastByteBuffer);
        lastByteBuffer = null;
    }
}
