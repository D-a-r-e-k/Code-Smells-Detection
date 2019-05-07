//}}}  
//{{{ removeAllMarkers() method  
/**
	 * Removes all defined markers.
	 * @since jEdit 2.6pre1
	 */
public void removeAllMarkers() {
    setFlag(MARKERS_CHANGED, true);
    for (int i = 0; i < markers.size(); i++) markers.get(i).removePosition();
    markers.removeAllElements();
    if (isLoaded()) {
        EditBus.send(new BufferUpdate(this, null, BufferUpdate.MARKERS_CHANGED));
    }
}
