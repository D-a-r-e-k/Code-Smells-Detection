/**
     * Create a ByteStore out of an InputStream
     */
public static ByteStore getBinaryFromIS(InputStream in, int nr_bytes_to_read) {
    byte[] s = new byte[nr_bytes_to_read + 100];
    int count = 0;
    int lastread = 0;
    // System.err.print("Reading ... "); 
    if (in != null) {
        synchronized (in) {
            while (count < s.length) {
                try {
                    lastread = in.read(s, count, nr_bytes_to_read - count);
                } catch (EOFException ex) {
                    System.err.println(ex.getMessage());
                    lastread = 0;
                } catch (Exception z) {
                    System.err.println(z.getMessage());
                    lastread = 0;
                }
                count += lastread;
                // System.err.print(lastread+" "); 
                if (lastread < 1)
                    break;
            }
        }
        // System.err.println(); 
        byte[] s2 = new byte[count + 1];
        for (int i = 0; i < count + 1; i++) {
            s2[i] = s[i];
        }
        //System.err.println("new byte-array, size "+s2.length); 
        ByteStore d = new ByteStore(s2);
        return d;
    } else {
        return null;
    }
}
