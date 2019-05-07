/**
	 * This method serialize the object with the given level of detail.
	 *
	 * @param out
	 *            the output serializer
	 * @param level
	 *            the level of Detail
	 */
@Override
public void writeObject(marauroa.common.net.OutputSerializer out, DetailLevel level) throws java.io.IOException {
    super.writeObject(out, level);
    if (level == DetailLevel.FULL) {
        /*
			 * Even if hidden and storable are server side only
			 * variables, we serialize then for database storage.
			 */
        out.write((byte) 1);
        out.write((byte) (hidden ? 1 : 0));
        out.write((byte) (storable ? 1 : 0));
    } else {
        out.write((byte) 0);
    }
    serializeRPSlots(out, level);
    /*
		 * We compute the amount of links to serialize first. We don't serialize
		 * hidden or private slots unless detail level is full.
		 */
    int size = 0;
    for (RPLink link : links) {
        if (shouldSerialize(DefinitionClass.RPLINK, link.getName(), level)) {
            size++;
        }
    }
    /*
		 * Now write it.
		 */
    out.write(size);
    for (RPLink link : links) {
        Definition def = getRPClass().getDefinition(DefinitionClass.RPLINK, link.getName());
        if (shouldSerialize(def, level)) {
            link.writeObject(out, level);
        }
    }
    /*
		 * we compute the amount of maps before serializing 
		 */
    if (out.getProtocolVersion() >= NetConst.FIRST_VERSION_WITH_MAP_SUPPORT) {
        size = 0;
        for (Entry<String, RPObject> entry : maps.entrySet()) {
            Definition def = getRPClass().getDefinition(DefinitionClass.ATTRIBUTE, entry.getKey());
            if (shouldSerialize(def, level)) {
                size++;
            }
        }
        out.write(size);
        /*
			 * now we write the maps in two steps
			 * 1. the names of the maps
			 * 2. the maps
			 */
        for (String map : maps.keySet()) {
            Definition def = getRPClass().getDefinition(DefinitionClass.ATTRIBUTE, map);
            if (shouldSerialize(def, level)) {
                out.write(map);
            }
        }
        for (String map : maps.keySet()) {
            Definition def = getRPClass().getDefinition(DefinitionClass.ATTRIBUTE, map);
            if (shouldSerialize(def, level)) {
                maps.get(map).writeObject(out, level);
            }
        }
    }
    /*
		 * We compute the amount of events to serialize first. We don't
		 * serialize hidden or private slots unless detail level is full.
		 */
    size = 0;
    for (RPEvent event : events) {
        if (shouldSerialize(DefinitionClass.RPEVENT, event.getName(), level)) {
            size++;
        }
    }
    /*
		 * Now write it too.
		 */
    out.write(size);
    for (RPEvent event : events) {
        Definition def = getRPClass().getDefinition(DefinitionClass.RPEVENT, event.getName());
        if (shouldSerialize(def, level)) {
            event.writeObject(out, level);
        }
    }
}
