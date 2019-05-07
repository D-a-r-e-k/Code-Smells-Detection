/**
	 * Serialization support.
	 */
private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
    s.defaultReadObject();
    // Read the hidden mapping 
    Map map = (Map) s.readObject();
    hiddenMapping = new WeakHashMap(map);
}
