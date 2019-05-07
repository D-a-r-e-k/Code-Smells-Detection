private void dumpClassInfo() throws IOException {
    out.writeShort(clazz.access_flags);
    out.writeShort(clazz.this_class);
    out.writeShort(clazz.super_class);
}
