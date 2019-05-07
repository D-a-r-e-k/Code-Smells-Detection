private void clear() {
    slots = new LinkedList<RPSlot>();
    addedSlots = new LinkedList<String>();
    deletedSlots = new LinkedList<String>();
    events = new LinkedList<RPEvent>();
    links = new LinkedList<RPLink>();
    addedLinks = new LinkedList<String>();
    deletedLinks = new LinkedList<String>();
    maps = new HashMap<String, RPObject>();
    addedMaps = new LinkedList<String>();
    deletedMaps = new LinkedList<String>();
    modified = false;
    container = null;
    containerSlot = null;
    hidden = false;
    storable = false;
}
