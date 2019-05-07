//}}}  
//{{{ getMarkerInRange() method  
/**
	 * Returns the first marker within the specified range.
	 * @param start The start offset
	 * @param end The end offset
	 * @since jEdit 4.0pre4
	 */
public Marker getMarkerInRange(int start, int end) {
    for (int i = 0; i < markers.size(); i++) {
        Marker marker = markers.get(i);
        int pos = marker.getPosition();
        if (pos >= start && pos < end)
            return marker;
    }
    return null;
}
