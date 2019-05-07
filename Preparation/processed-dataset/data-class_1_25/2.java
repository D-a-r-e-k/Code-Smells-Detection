/**
	 * dump the magic, minor and major version
	 * 
	 * @throws IOException
	 */
private void dumpClassHeader() throws IOException {
    out.writeInt(clazz.magic);
    out.writeShort(clazz.minor_version);
    out.writeShort(clazz.major_version);
}
