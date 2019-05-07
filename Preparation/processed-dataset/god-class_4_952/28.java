/**
	 * Return the linked object by the given link or null if the link doesn't exist.
	 * @param name the name of the link.
	 * @return the object linked by the given link.
	 */
public RPObject getLinkedObject(String name) {
    RPLink link = getLink(name);
    if (link != null) {
        return link.getObject();
    }
    return null;
}
