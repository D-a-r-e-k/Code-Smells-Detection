private void dumpMethods() throws IOException {
    Method method = null;
    out.writeShort(clazz.methods_count);
    for (int i = 0; i < clazz.methods_count; i++) {
        method = clazz.methods[i];
        out.writeShort(method.access_flags);
        out.writeShort(method.name_index);
        out.writeShort(method.descriptor_index);
        out.writeShort(method.attributes_count);
        for (int j = 0; j < method.attributes_count; j++) {
            dumpAttribute(method.attributes[j]);
        }
    }
}
