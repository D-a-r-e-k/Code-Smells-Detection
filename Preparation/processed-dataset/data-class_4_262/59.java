//}}}  
//{{{ markersChanged() method  
/**
	 * Return true when markers have changed and the markers file needs
	 * to be updated
	 * @since jEdit 4.3pre7
	 */
public boolean markersChanged() {
    return getFlag(MARKERS_CHANGED);
}
