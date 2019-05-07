/**
     *  Takes a MT-Blacklist -formatted blacklist and returns a list of compiled
     *  Pattern objects.
     *
     *  @param list
     *  @return The parsed blacklist patterns.
     */
private Collection<Pattern> parseBlacklist(String list) {
    ArrayList<Pattern> compiledpatterns = new ArrayList<Pattern>();
    if (list != null) {
        try {
            BufferedReader in = new BufferedReader(new StringReader(list));
            String line;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (line.length() == 0)
                    continue;
                // Empty line 
                if (line.startsWith("#"))
                    continue;
                // It's a comment 
                int ws = line.indexOf(' ');
                if (ws == -1)
                    ws = line.indexOf('\t');
                if (ws != -1)
                    line = line.substring(0, ws);
                try {
                    compiledpatterns.add(m_compiler.compile(line));
                } catch (MalformedPatternException e) {
                    log.debug("Malformed spam filter pattern " + line);
                }
            }
        } catch (IOException e) {
            log.info("Could not read patterns; returning what I got", e);
        }
    }
    return compiledpatterns;
}
