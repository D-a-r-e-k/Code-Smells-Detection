/**
     *  Parses a list of patterns and returns a Collection of compiled Pattern
     *  objects.
     *
     * @param source
     * @param list
     * @return A Collection of the Patterns that were found from the lists.
     */
private Collection<Pattern> parseWordList(WikiPage source, String list) {
    ArrayList<Pattern> compiledpatterns = new ArrayList<Pattern>();
    if (list != null) {
        StringTokenizer tok = new StringTokenizer(list, " \t\n");
        while (tok.hasMoreTokens()) {
            String pattern = tok.nextToken();
            try {
                compiledpatterns.add(m_compiler.compile(pattern));
            } catch (MalformedPatternException e) {
                log.debug("Malformed spam filter pattern " + pattern);
                source.setAttribute("error", "Malformed spam filter pattern " + pattern);
            }
        }
    }
    return compiledpatterns;
}
