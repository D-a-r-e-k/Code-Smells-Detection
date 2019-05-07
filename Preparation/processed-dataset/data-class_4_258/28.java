/**
     * If this font file is using the Compact Font File Format, then this method
     * will return the raw bytes needed for the font stream. If this method is
     * ever made public: make sure to add a test if (cff == true).
     * @return	a byte array
     * @since	2.1.3
     */
protected byte[] readCffFont() throws IOException {
    RandomAccessFileOrArray rf2 = new RandomAccessFileOrArray(rf);
    byte b[] = new byte[cffLength];
    try {
        rf2.reOpen();
        rf2.seek(cffOffset);
        rf2.readFully(b);
    } finally {
        try {
            rf2.close();
        } catch (Exception e) {
        }
    }
    return b;
}
