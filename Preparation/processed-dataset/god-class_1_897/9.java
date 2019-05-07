private void process(BufferedReader in, PrintWriter out, boolean ignoring) throws IOException {
    //    out.println("*** process ignore=" + ignoring + " : " + peekLine(in)); 
    while (peekLine(in) != null) {
        if (peekLine(in).trim().startsWith("#if")) {
            String line = getLine(in).trim();
            final boolean condition = evaluate(line.substring(3).trim());
            process(in, out, ignoring || !condition);
            if (peekLine(in) != null && peekLine(in).trim().startsWith("#else")) {
                getLine(in);
                // Discard the #else line 
                process(in, out, ignoring || condition);
            }
            line = getLine(in);
            if (line == null)
                throw new IOException("Missing \"#fi\"");
            if (!line.trim().startsWith("#fi"))
                throw new IOException("Expected \"#fi\", got: " + line);
        } else if (peekLine(in).trim().startsWith("#")) {
            break;
        } else {
            String line = getLine(in);
            if (!ignoring)
                write(out, line);
        }
    }
    out.flush();
}
