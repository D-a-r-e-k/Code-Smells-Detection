/**
   * Returns an input stream corresponding to selected language.
   * @param in The default stream
   * @param fileName The requested file
   */
public static InputStream getLanguageStream(InputStream in, String fileName) {
    ZipEntry entry;
    if (languagePack != null && (entry = languagePackContains(fileName)) != null) {
        try {
            return languagePack.getInputStream(entry);
        } catch (IOException ioe) {
            return in;
        }
    } else
        return in;
}
