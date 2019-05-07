/**
     * Returns a value as a String array.
     * The values are separated by commas with optional quotes and white space.
     */
public static String[] toQuotedAndCommaSeparatedArray(String str) throws IOException {
    LinkedList<String> values = new LinkedList<String>();
    StreamTokenizer tt = new StreamTokenizer(new StringReader(str));
    tt.wordChars('a', 'z');
    tt.wordChars('A', 'Z');
    tt.wordChars(128 + 32, 255);
    tt.whitespaceChars(0, ' ');
    tt.quoteChar('"');
    tt.quoteChar('\'');
    while (tt.nextToken() != StreamTokenizer.TT_EOF) {
        switch(tt.ttype) {
            case StreamTokenizer.TT_WORD:
            case '"':
            case '\'':
                values.add(tt.sval);
                break;
        }
    }
    return values.toArray(new String[values.size()]);
}
