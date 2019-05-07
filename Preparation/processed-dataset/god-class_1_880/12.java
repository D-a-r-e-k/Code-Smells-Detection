/** 
     * Format the segment for XML output.  
     *
     * @param type String whose value is appropriate for an XML element name.
     */
public String toXML(final String type) {
    return "<" + type + " index=\"" + index + "\" from=\"" + from + "\" to=\"" + to + "\">\n" + "  <visits>" + visits + "</visits>\n" + "</" + type + ">\n";
}
