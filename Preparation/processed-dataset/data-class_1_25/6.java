private void dumpFields() throws IOException {
    Field field = null;
    out.writeShort(clazz.fields_count);
    for (int i = 0; i < clazz.fields_count; i++) {
        field = clazz.fields[i];
        out.writeShort(field.access_flags);
        out.writeShort(field.name_index);
        out.writeShort(field.descriptor_index);
        out.writeShort(field.attributes_count);
        for (int j = 0; j < field.attributes_count; j++) {
            dumpAttribute(field.attributes[j]);
        }
    }
}
