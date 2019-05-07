//{{{ finishLoading() method  
private void finishLoading() {
    updateHash();
    parseBufferLocalProperties();
    // AHA!  
    // this is probably the only way to fix this  
    FoldHandler oldFoldHandler = getFoldHandler();
    setMode();
    if (getFoldHandler() == oldFoldHandler) {
        // on a reload, the fold handler doesn't change, but  
        // we still need to re-collapse folds.  
        // don't do this on initial fold handler creation  
        invalidateFoldLevels();
        fireFoldHandlerChanged();
    }
    // Create marker positions  
    for (int i = 0; i < markers.size(); i++) {
        Marker marker = markers.get(i);
        marker.removePosition();
        int pos = marker.getPosition();
        if (pos > getLength())
            marker.setPosition(getLength());
        else if (pos < 0)
            marker.setPosition(0);
        marker.createPosition();
    }
}
