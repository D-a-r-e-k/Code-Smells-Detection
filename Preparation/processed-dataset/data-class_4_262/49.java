//}}}  
//{{{ addOrRemoveMarker() method  
/**
	 * If a marker is set on the line of the position, it is removed. Otherwise
	 * a new marker with the specified shortcut is added.
	 * @param pos The position of the marker
	 * @param shortcut The shortcut ('\0' if none)
	 * @since jEdit 3.2pre5
	 */
public void addOrRemoveMarker(char shortcut, int pos) {
    int line = getLineOfOffset(pos);
    if (getMarkerAtLine(line) != null)
        removeMarker(line);
    else
        addMarker(shortcut, pos);
}
