/**
     * Returns a bitset representing the PageMode and PageLayout viewer preferences.
     * Doesn't return any information about the ViewerPreferences dictionary.
     * @return an int that contains the Viewer Preferences.
     */
public int getSimpleViewerPreferences() {
    return PdfViewerPreferencesImp.getViewerPreferences(catalog).getPageLayoutAndMode();
}
