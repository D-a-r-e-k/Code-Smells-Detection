private String peekLine(BufferedReader in) throws IOException {
    if (currentLine == null)
        currentLine = in.readLine();
    return currentLine;
}
