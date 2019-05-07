/**
   * The resource for the default display filters.
   */
protected String getDefaultDisplayFiltersResource() {
    if (getCacheHeadersOnly()) {
        return super.getDefaultDisplayFiltersResource();
    } else if (isSentFolder())
        return "CachingFolderInfo.sentFolderDefaultDisplayFilters";
    else
        return "CachingFolderInfo.defaultDisplayFilters";
}
