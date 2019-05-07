private String getLine(BufferedReader in) throws IOException {
    String line = currentLine;
    currentLine = null;
    if (line == null)
        in.readLine();
    return line;
}
