/** A helper to FlateDecode.
     * @param in the input data
     * @param strict <CODE>true</CODE> to read a correct stream. <CODE>false</CODE>
     * to try to read a corrupted stream
     * @return the decoded data
     */
public static byte[] FlateDecode(byte in[], boolean strict) {
    ByteArrayInputStream stream = new ByteArrayInputStream(in);
    InflaterInputStream zip = new InflaterInputStream(stream);
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    byte b[] = new byte[strict ? 4092 : 1];
    try {
        int n;
        while ((n = zip.read(b)) >= 0) {
            out.write(b, 0, n);
        }
        zip.close();
        out.close();
        return out.toByteArray();
    } catch (Exception e) {
        if (strict)
            return null;
        return out.toByteArray();
    }
}
