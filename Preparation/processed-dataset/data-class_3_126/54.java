protected void selectAllLogLevels(boolean selected) {
    Iterator levels = getLogLevels();
    while (levels.hasNext()) {
        getMenuItem((LogLevel) levels.next()).setSelected(selected);
    }
}
