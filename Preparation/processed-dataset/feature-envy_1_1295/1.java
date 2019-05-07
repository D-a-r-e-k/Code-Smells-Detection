/**
   * Generate the output file.
   * @param out
   * @throws IOException
   */
public void generate(PrintWriter out) throws IOException {
    InputStream is = getClass().getResourceAsStream(templateName);
    if (is == null)
        throw new IOException("Invalid template name: " + templateName);
    BufferedReader in = new BufferedReader(new InputStreamReader(is));
    process(in, out, false);
}
