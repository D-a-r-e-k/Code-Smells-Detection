//}}}  
//{{{ removeMarker() method  
/**
	 * Removes all markers at the specified line.
	 * @param line The line number
	 * @since jEdit 3.2pre2
	 */
public void removeMarker(int line) {
    for (int i = 0; i < markers.size(); i++) {
        Marker marker = markers.get(i);
        if (getLineOfOffset(marker.getPosition()) == line) {
            setFlag(MARKERS_CHANGED, true);
            marker.removePosition();
            markers.removeElementAt(i);
            i--;
        }
    }
    EditBus.send(new BufferUpdate(this, null, BufferUpdate.MARKERS_CHANGED));
}
