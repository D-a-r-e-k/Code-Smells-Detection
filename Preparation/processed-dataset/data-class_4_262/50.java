//}}}  
//{{{ addMarker() method  
/**
	 * Adds a marker to this buffer.
	 * @param pos The position of the marker
	 * @param shortcut The shortcut ('\0' if none)
	 * @since jEdit 3.2pre1
	 */
public void addMarker(char shortcut, int pos) {
    Marker markerN = new Marker(this, shortcut, pos);
    boolean added = false;
    // don't sort markers while buffer is being loaded  
    if (isLoaded()) {
        setFlag(MARKERS_CHANGED, true);
        markerN.createPosition();
        for (int i = 0; i < markers.size(); i++) {
            Marker marker = markers.get(i);
            if (shortcut != '\0' && marker.getShortcut() == shortcut)
                marker.setShortcut('\0');
            if (marker.getPosition() == pos) {
                markers.removeElementAt(i);
                i--;
            }
        }
        for (int i = 0; i < markers.size(); i++) {
            Marker marker = markers.get(i);
            if (marker.getPosition() > pos) {
                markers.insertElementAt(markerN, i);
                added = true;
                break;
            }
        }
    }
    if (!added)
        markers.addElement(markerN);
    if (isLoaded() && !getFlag(TEMPORARY)) {
        EditBus.send(new BufferUpdate(this, null, BufferUpdate.MARKERS_CHANGED));
    }
}
