/**
	 * dump interfaces implemented by this class
	 * 
	 * @throws IOException
	 */
private void dumpInterfaces() throws IOException {
    out.writeShort(clazz.interfaces_count);
    for (int i = 0; i < clazz.interfaces_count; i++) {
        out.writeShort(clazz.interfaces[i]);
    }
}
