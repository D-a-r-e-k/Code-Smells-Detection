/**
	 * Returns the number of attributes and events this object is made of.
	 */
@Override
public int size() {
    try {
        int total = super.size();
        total += events.size();
        for (RPSlot slot : slots) {
            for (RPObject object : slot) {
                total += object.size();
            }
        }
        for (RPLink link : links) {
            total += link.getObject().size();
        }
        return total;
    } catch (Exception e) {
        logger.error("Cannot determine size", e);
        return -1;
    }
}
