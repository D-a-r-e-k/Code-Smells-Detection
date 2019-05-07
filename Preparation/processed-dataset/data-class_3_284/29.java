/**
   * Refills the input buffer.
   *
   * @return      <code>false</code>, iff there was new input.
   * 
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
private boolean zzRefill() throws java.io.IOException {
    /* first: make room (if you can) */
    if (zzStartRead > 0) {
        System.arraycopy(zzBuffer, zzStartRead, zzBuffer, 0, zzEndRead - zzStartRead);
        /* translate stored positions */
        zzEndRead -= zzStartRead;
        zzCurrentPos -= zzStartRead;
        zzMarkedPos -= zzStartRead;
        zzPushbackPos -= zzStartRead;
        zzStartRead = 0;
    }
    /* is the buffer big enough? */
    if (zzCurrentPos >= zzBuffer.length) {
        /* if not: blow it up */
        char newBuffer[] = new char[zzCurrentPos * 2];
        System.arraycopy(zzBuffer, 0, newBuffer, 0, zzBuffer.length);
        zzBuffer = newBuffer;
    }
    /* finally: fill the buffer with new input */
    int numRead = zzReader.read(zzBuffer, zzEndRead, zzBuffer.length - zzEndRead);
    if (numRead < 0) {
        return true;
    } else {
        zzEndRead += numRead;
        return false;
    }
}
