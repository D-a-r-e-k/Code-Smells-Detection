/**
     * @param reader
     * @return true if end of file reached
     */
private boolean ignoreTransaction(BufferedReader reader) throws IOException {
    while (true) {
        String line = reader.readLine();
        if (line == null)
            return true;
        if (line.startsWith("-"))
            return false;
    }
}
