public synchronized void close() {
    if (lastByteBuffer != null) {
        returnBufferBack(lastByteBuffer);
    }
}
