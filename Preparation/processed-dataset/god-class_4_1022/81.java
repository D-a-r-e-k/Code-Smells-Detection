/**
   * This takes the FolderProperty.backendFilters and
   * FolderProperty.displayFilters properties and uses them to populate
   * the backendMessageFilters and displayMessageFilters arrays.
   */
public void createFilters() {
    BackendMessageFilter[] tmpBackendFilters = null;
    MessageFilter[] tmpDisplayFilters = null;
    Vector backendFilterNames = Pooka.getResources().getPropertyAsVector(getFolderProperty() + ".backendFilters", "");
    if (backendFilterNames != null && backendFilterNames.size() > 0) {
        tmpBackendFilters = new BackendMessageFilter[backendFilterNames.size()];
        for (int i = 0; i < backendFilterNames.size(); i++) {
            tmpBackendFilters[i] = new BackendMessageFilter(getFolderProperty() + ".backendFilters." + (String) backendFilterNames.elementAt(i));
        }
        backendFilters = tmpBackendFilters;
    }
    Vector foundFilters = new Vector();
    Vector defaultFilterNames = Pooka.getResources().getPropertyAsVector(getDefaultDisplayFiltersResource(), "");
    for (int i = 0; i < defaultFilterNames.size(); i++) {
        foundFilters.add(new MessageFilter("FolderInfo.defaultDisplayFilters." + (String) defaultFilterNames.elementAt(i)));
    }
    Vector displayFilterNames = Pooka.getResources().getPropertyAsVector(getFolderProperty() + ".displayFilters", "");
    for (int i = 0; i < displayFilterNames.size(); i++) {
        foundFilters.add(new MessageFilter(getFolderProperty() + ".displayFilters." + (String) displayFilterNames.elementAt(i)));
    }
    tmpDisplayFilters = new MessageFilter[foundFilters.size()];
    for (int i = 0; i < foundFilters.size(); i++) tmpDisplayFilters[i] = (MessageFilter) foundFilters.elementAt(i);
    displayFilters = tmpDisplayFilters;
    filterHeaders = new LinkedList();
    // update the fetch profile with the headers from the display filters. 
    for (int i = 0; i < tmpDisplayFilters.length; i++) {
        javax.mail.search.SearchTerm filterTerm = tmpDisplayFilters[i].getSearchTerm();
        if (filterTerm != null) {
            List headers = getHeaders(filterTerm);
            filterHeaders.addAll(headers);
        }
    }
    if (fetchProfile != null) {
        for (int i = 0; i < filterHeaders.size(); i++) {
            fetchProfile.add((String) filterHeaders.get(i));
        }
    }
}
