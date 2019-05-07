protected JCheckBoxMenuItem getMenuItem(LogLevel level) {
    JCheckBoxMenuItem result = (JCheckBoxMenuItem) (_logLevelMenuItems.get(level));
    if (result == null) {
        result = createMenuItem(level);
        _logLevelMenuItems.put(level, result);
    }
    return result;
}
