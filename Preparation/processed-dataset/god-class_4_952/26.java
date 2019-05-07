/**
	 * Adds a new link to the object.
	 * @param link the link to add.
	 */
public void addLink(RPLink link) {
    if (hasLink(link.getName())) {
        throw new SlotAlreadyAddedException(link.getName());
    }
    link.setOwner(this);
    links.add(link);
    addedLinks.add(link.getName());
    modified = true;
}
