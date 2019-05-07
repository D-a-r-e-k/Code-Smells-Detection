//------------------------------------------------------------------------ 
public static void copyInputToOutput(InputStream input, OutputStream output) throws IOException {
    BufferedInputStream in = new BufferedInputStream(input);
    BufferedOutputStream out = new BufferedOutputStream(output);
    byte buffer[] = new byte[8192];
    for (int count = 0; count != -1; ) {
        count = in.read(buffer, 0, 8192);
        if (count != -1)
            out.write(buffer, 0, count);
    }
    try {
        in.close();
        out.close();
    } catch (IOException ex) {
        throw new IOException("Closing file streams, " + ex.getMessage());
    }
}
