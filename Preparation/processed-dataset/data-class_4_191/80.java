/**
   * The resource for the default display filters.
   */
protected String getDefaultDisplayFiltersResource() {
    if (isSentFolder())
        return "FolderInfo.sentFolderDefaultDisplayFilters";
    else
        return "FolderInfo.defaultDisplayFilters";
}
