/**
     *  This method peeks ahead in the stream until EOL and returns the result.
     *  It will keep the buffers untouched.
     *
     *  @return The string from the current position to the end of line.
     */
// FIXME: Always returns an empty line, even if the stream is full. 
private String peekAheadLine() throws IOException {
    String s = readUntilEOL().toString();
    if (s.length() > PUSHBACK_BUFFER_SIZE) {
        log.warn("Line is longer than maximum allowed size (" + PUSHBACK_BUFFER_SIZE + " characters.  Attempting to recover...");
        pushBack(s.substring(0, PUSHBACK_BUFFER_SIZE - 1));
    } else {
        try {
            pushBack(s);
        } catch (IOException e) {
            log.warn("Pushback failed: the line is probably too long.  Attempting to recover.");
        }
    }
    return s;
}
