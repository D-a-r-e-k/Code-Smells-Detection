//}}}  
/** @return an MD5 hash of the contents of the buffer */
private byte[] calculateHash() {
    final byte[] dummy = new byte[1];
    if (!jEdit.getBooleanProperty("useMD5forDirtyCalculation"))
        return dummy;
    ByteBuffer bb = null;
    readLock();
    try {
        // Log.log(Log.NOTICE, this, "calculateHash()");  
        int length = getLength();
        bb = ByteBuffer.allocate(length * 2);
        // Chars are 2 bytes  
        CharBuffer cb = bb.asCharBuffer();
        cb.append(getSegment(0, length));
    } finally {
        readUnlock();
    }
    try {
        MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
        digest.update(bb);
        return digest.digest();
    } catch (NoSuchAlgorithmException nsae) {
        Log.log(Log.ERROR, this, "Can't Calculate MD5 hash!", nsae);
        return dummy;
    }
}
