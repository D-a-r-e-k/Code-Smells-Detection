//}}}  
//{{{ getMarkerNameString() method  
/**
	 * Returns a string of all set markers, used by the status bar
	 * (eg, "a b $ % ^").
	 * @since jEdit 4.2pre2
	 */
public String getMarkerNameString() {
    StringBuilder buf = new StringBuilder();
    for (int i = 0; i < markers.size(); i++) {
        Marker marker = markers.get(i);
        if (marker.getShortcut() != '\0') {
            if (buf.length() != 0)
                buf.append(' ');
            buf.append(marker.getShortcut());
        }
    }
    if (buf.length() == 0)
        return jEdit.getProperty("view.status.no-markers");
    else
        return buf.toString();
}
