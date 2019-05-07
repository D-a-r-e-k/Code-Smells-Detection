//}}}  
//{{{ getMarker() method  
/**
	 * Returns the marker with the specified shortcut.
	 * @param shortcut The shortcut
	 * @since jEdit 3.2pre2
	 */
public Marker getMarker(char shortcut) {
    for (Marker marker : markers) {
        if (marker.getShortcut() == shortcut)
            return marker;
    }
    return null;
}
