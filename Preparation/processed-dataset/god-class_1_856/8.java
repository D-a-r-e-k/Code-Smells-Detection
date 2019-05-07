private void dumpClassAttributes() throws IOException {
    out.writeShort(clazz.attributes_count);
    for (int i = 0; i < clazz.attributes_count; i++) {
        dumpAttribute(clazz.attributes[i]);
    }
}
