/**
   * Initialize syntax colorizing modes.
   */
private static void initModes() {
    StringTokenizer _tok = new StringTokenizer(getProperty("jext.modes"), " ");
    Mode _mode;
    modes = new ArrayList(_tok.countTokens());
    modesFileFilters = new ArrayList(_tok.countTokens());
    for (; _tok.hasMoreTokens(); ) {
        modes.add(_mode = new Mode(_tok.nextToken()));
        modesFileFilters.add(new ModeFileFilter(_mode));
    }
}
