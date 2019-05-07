//returns flag indicating if full write was done  
public synchronized boolean writeAllByteBuffer() throws IOException {
    if (lastByteBuffer != null) {
        writeLastByteBuffer();
        if (lastByteBuffer != null)
            return false;
    }
    if (bufferList.size() == 0) {
        if (toNotify != null) {
            synchronized (toNotify) {
                toNotify.notify();
                toNotify = null;
            }
        }
        return true;
    }
    while (bufferList.size() != 0) {
        lastByteBuffer = (ByteBuffer) bufferList.remove(0);
        lastByteBuffer.flip();
        writeLastByteBuffer();
        if (lastByteBuffer != null)
            return false;
    }
    if (toNotify != null) {
        synchronized (toNotify) {
            toNotify.notify();
            toNotify = null;
        }
    }
    return true;
}
