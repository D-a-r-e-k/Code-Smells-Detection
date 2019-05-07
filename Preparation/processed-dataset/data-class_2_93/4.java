public void flush() throws IOException {
    if (bufferList.size() != 0 || lastByteBuffer != null) {
        handler.registerWrite();
    } else {
        return;
    }
    while (bufferList.size() >= 5) {
        handler.waitTillFullyWritten();
    }
}
