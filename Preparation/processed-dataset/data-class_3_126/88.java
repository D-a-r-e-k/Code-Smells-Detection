//    protected void setView(String viewString, LogTable table) { 
//        if (STANDARD_VIEW.equals(viewString)) { 
//            table.setStandardView(); 
//        } else if (COMPACT_VIEW.equals(viewString)) { 
//            table.setCompactView(); 
//        } else if (DETAILED_VIEW.equals(viewString)) { 
//            table.setDetailedView(); 
//        } else { 
//            String message = viewString + "does not match a supported view."; 
//            throw new IllegalArgumentException(message); 
//        } 
//        _currentView = viewString; 
//    } 
protected void setView(String viewString, LogTable table) {
    if (DETAILED_VIEW.equals(viewString)) {
        table.setDetailedView();
    } else {
        String message = viewString + "does not match a supported view.";
        throw new IllegalArgumentException(message);
    }
    _currentView = viewString;
}
