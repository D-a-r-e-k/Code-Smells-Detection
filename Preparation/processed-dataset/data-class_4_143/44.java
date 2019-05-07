/**
   * Returns the mode according to its name.
   */
public static Mode getMode(String modeName) {
    for (int i = 0; i < modes.size(); i++) {
        Mode _mode = (Mode) modes.get(i);
        if (_mode.getModeName().equalsIgnoreCase(modeName))
            return _mode;
    }
    return null;
}
