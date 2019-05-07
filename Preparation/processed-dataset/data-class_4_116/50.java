/**
	 * Fills this object with the data that has been serialized.
	 *
	 * @param in
	 *            the input serializer
	 */
@Override
public void readObject(marauroa.common.net.InputSerializer in) throws java.io.IOException {
    super.readObject(in);
    modified = true;
    if (in.readByte() == 1) {
        hidden = in.readByte() == 1;
        storable = in.readByte() == 1;
    }
    deserializeRPSlots(in);
    /*
		 * then we load links
		 */
    int size = in.readInt();
    if (size > TimeoutConf.MAX_ARRAY_ELEMENTS) {
        throw new IOException("Illegal request of an list of " + String.valueOf(size) + " size");
    }
    links = new LinkedList<RPLink>();
    for (int i = 0; i < size; ++i) {
        RPLink link = new RPLink(null, null);
        link.setOwner(this);
        link = (RPLink) in.readObject(link);
        links.add(link);
    }
    if (in.getProtocolVersion() >= NetConst.FIRST_VERSION_WITH_MAP_SUPPORT) {
        // get the number of maps 
        int numberOfMaps = in.readInt();
        // get the names of the maps and store them in a list 
        List<String> mapNames = new ArrayList<String>();
        for (int j = 0; j < numberOfMaps; j++) {
            mapNames.add(in.readString());
        }
        // get the map objects and put them into the internal maps 
        for (int k = 0; k < numberOfMaps; k++) {
            RPObject rpo = new RPObject();
            rpo = (RPObject) in.readObject(rpo);
            maps.put(mapNames.get(k), rpo);
        }
    }
    /*
		 * And now we load events
		 */
    size = in.readInt();
    if (size > TimeoutConf.MAX_ARRAY_ELEMENTS) {
        throw new IOException("Illegal request of an list of " + String.valueOf(size) + " size");
    }
    events = new LinkedList<RPEvent>();
    for (int i = 0; i < size; ++i) {
        RPEvent event = new RPEvent();
        event.setOwner(this);
        event = (RPEvent) in.readObject(event);
        events.add(event);
    }
}
