/**
   * Adds a mode to Jext's syntax colorizing modes list
   */
public static void addMode(Mode mode) {
    modes.add(mode);
    modesFileFilters.add(new ModeFileFilter(mode));
}
