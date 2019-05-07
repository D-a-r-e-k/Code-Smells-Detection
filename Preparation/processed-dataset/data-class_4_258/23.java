protected byte[] getFullFont() throws IOException {
    RandomAccessFileOrArray rf2 = null;
    try {
        rf2 = new RandomAccessFileOrArray(rf);
        rf2.reOpen();
        byte b[] = new byte[rf2.length()];
        rf2.readFully(b);
        return b;
    } finally {
        try {
            if (rf2 != null) {
                rf2.close();
            }
        } catch (Exception e) {
        }
    }
}
