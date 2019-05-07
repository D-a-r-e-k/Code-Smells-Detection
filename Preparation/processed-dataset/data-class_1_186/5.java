/**
   * Gets a resource for reading.  pFileName could be a URL or a file name
   * or some similar identifier that the
   */
public java.io.InputStream getInputStream(String pFileName) throws java.io.IOException {
    try {
        URL url = new URL(pFileName);
        return url.openStream();
    } catch (MalformedURLException mue) {
        throw new IOException("Error opening URL:  " + mue.getMessage());
    }
}
