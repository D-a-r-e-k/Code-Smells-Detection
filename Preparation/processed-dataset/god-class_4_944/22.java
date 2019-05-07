private static void saveGuiSettings() {
    // Store divier locations  
    Settings.setVerticalDividerPosition(jagGenerator.splitPane.getDividerLocation());
    Settings.setHorizontalDividerPosition(jagGenerator.desktopConsoleSplitPane.getDividerLocation());
    // Store window's maximized state and/or it's size and location  
    int extendedState = jagGenerator.getExtendedState();
    boolean isMaximized = ((extendedState & MAXIMIZED_BOTH) == MAXIMIZED_BOTH);
    if (!isMaximized) {
        // The current Window-dimensions are only valid if not maximized  
        Settings.setUserWindowBounds(jagGenerator.getBounds());
    }
    Settings.setMaximized(isMaximized);
}
