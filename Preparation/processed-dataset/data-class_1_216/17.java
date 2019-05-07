//------------------------------------------------------------------------ 
/**
     * Utility method to copy an input stream to an output stream.
     * Wraps both streams in buffers. Ensures right numbers of bytes copied.
     */
public static void copyInputToOutput(InputStream input, OutputStream output, long byteCount) throws IOException {
    int bytes;
    long length;
    BufferedInputStream in = new BufferedInputStream(input);
    BufferedOutputStream out = new BufferedOutputStream(output);
    byte[] buffer;
    buffer = new byte[8192];
    for (length = byteCount; length > 0; ) {
        bytes = (int) (length > 8192 ? 8192 : length);
        try {
            bytes = in.read(buffer, 0, bytes);
        } catch (IOException ex) {
            try {
                in.close();
                out.close();
            } catch (IOException ex1) {
            }
            throw new IOException("Reading input stream, " + ex.getMessage());
        }
        if (bytes < 0)
            break;
        length -= bytes;
        try {
            out.write(buffer, 0, bytes);
        } catch (IOException ex) {
            try {
                in.close();
                out.close();
            } catch (IOException ex1) {
            }
            throw new IOException("Writing output stream, " + ex.getMessage());
        }
    }
    try {
        in.close();
        out.close();
    } catch (IOException ex) {
        throw new IOException("Closing file streams, " + ex.getMessage());
    }
}
