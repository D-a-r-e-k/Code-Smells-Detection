/** Get the content from a stream as it is without applying any filter.
     * @param stream the stream
     * @param file the location where the stream is
     * @throws IOException on error
     * @return the stream content
     */
public static byte[] getStreamBytesRaw(PRStream stream, RandomAccessFileOrArray file) throws IOException {
    PdfReader reader = stream.getReader();
    byte b[];
    if (stream.getOffset() < 0)
        b = stream.getBytes();
    else {
        b = new byte[stream.getLength()];
        file.seek(stream.getOffset());
        file.readFully(b);
        PdfEncryption decrypt = reader.getDecrypt();
        if (decrypt != null) {
            PdfObject filter = getPdfObjectRelease(stream.get(PdfName.FILTER));
            ArrayList<PdfObject> filters = new ArrayList<PdfObject>();
            if (filter != null) {
                if (filter.isName())
                    filters.add(filter);
                else if (filter.isArray())
                    filters = ((PdfArray) filter).getArrayList();
            }
            boolean skip = false;
            for (int k = 0; k < filters.size(); ++k) {
                PdfObject obj = getPdfObjectRelease(filters.get(k));
                if (obj != null && obj.toString().equals("/Crypt")) {
                    skip = true;
                    break;
                }
            }
            if (!skip) {
                decrypt.setHashKey(stream.getObjNum(), stream.getObjGen());
                b = decrypt.decryptByteArray(b);
            }
        }
    }
    return b;
}
