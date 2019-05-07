/**
	 * Removes a link from this object and return it.
	 * @param name the name of the link to remove.
	 * @return the removed link or null if it was not found.
	 */
public RPLink removeLink(String name) {
    for (Iterator<RPLink> it = links.iterator(); it.hasNext(); ) {
        RPLink link = it.next();
        if (name.equals(link.getName())) {
            deletedLinks.add(name);
            modified = true;
            /* Remove and return it */
            it.remove();
            return link;
        }
    }
    return null;
}
