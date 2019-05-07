/**
	 * Serialization support.
	 */
private void writeObject(ObjectOutputStream s) throws IOException {
    s.defaultWriteObject();
    // Write out the hidden mapping 
    Map map = new Hashtable(hiddenMapping);
    s.writeObject(map);
}
