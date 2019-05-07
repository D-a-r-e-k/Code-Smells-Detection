/**
	 * Adds a new link to the object.
	 * @param name the name of the link
	 * @param object the object to link.
	 */
public void addLink(String name, RPObject object) {
    if (hasLink(name)) {
        throw new SlotAlreadyAddedException(name);
    }
    RPLink link = new RPLink(name, object);
    link.setOwner(this);
    links.add(link);
    addedLinks.add(name);
    modified = true;
}
