public boolean isDataAvailableForWrite(Object toNotify) {
    if (lastByteBuffer != null) {
        if (this.toNotify != null) {
            throw new IllegalStateException("toNotify object was already set!");
        }
        this.toNotify = toNotify;
        return true;
    }
    if (bufferList.size() == 0) {
        return false;
    } else {
        if (this.toNotify != null) {
            throw new IllegalStateException("toNotify object was already set!");
        }
        this.toNotify = toNotify;
        return true;
    }
}
