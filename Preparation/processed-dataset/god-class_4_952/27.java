/**
	 * Returns the link with given name or null if not found.
	 * @param name the name of the link to find.
	 * @return the link with given name or null if not found.
	 */
public RPLink getLink(String name) {
    for (RPLink link : links) {
        if (name.equals(link.getName())) {
            return link;
        }
    }
    return null;
}
