public java.io.OutputStream getOutputStream(String pFileName) throws java.io.IOException {
    // no writing to streams in this one. 
    throw new IOException("Diskless mode:  no file modification available.");
}
