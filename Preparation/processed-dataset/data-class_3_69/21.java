/**
     * Writes a line and jumps to a new one.
     */
private void writeln(BufferedWriter writer, String line) throws IOException {
    writer.write(line);
    writer.newLine();
}
