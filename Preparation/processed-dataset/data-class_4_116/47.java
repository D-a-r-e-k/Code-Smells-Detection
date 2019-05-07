/**
	 * This method returns a String that represent the object
	 *
	 * @return a string representing the object.
	 */
@Override
public String toString() {
    StringBuffer tmp = new StringBuffer("RPObject with ");
    tmp.append(super.toString());
    tmp.append(" with maps");
    for (Map.Entry<String, RPObject> map : maps.entrySet()) {
        tmp.append(" " + map.getKey());
        tmp.append("=[" + map.getValue().toAttributeString() + "]");
    }
    tmp.append(" and RPLink ");
    for (RPLink link : links) {
        tmp.append("[" + link.toString() + "]");
    }
    tmp.append(" and RPEvents ");
    for (RPEvent event : events) {
        tmp.append("[" + event.toString() + "]");
    }
    return tmp.toString();
}
