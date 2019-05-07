//}}}  
//{{{ getMarkerAtLine() method  
/**
	 * Returns the first marker at the specified line, or <code>null</code>
	 * if there is none.
	 * @param line The line number
	 * @since jEdit 3.2pre2
	 */
public Marker getMarkerAtLine(int line) {
    for (int i = 0; i < markers.size(); i++) {
        Marker marker = markers.get(i);
        if (getLineOfOffset(marker.getPosition()) == line)
            return marker;
    }
    return null;
}
