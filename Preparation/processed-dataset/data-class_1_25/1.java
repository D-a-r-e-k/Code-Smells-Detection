public void dump() throws IOException {
    out = new DataOutputStream(new FileOutputStream(destFile));
    dumpClassHeader();
    dumpConstantPool();
    dumpClassInfo();
    dumpInterfaces();
    dumpFields();
    dumpMethods();
    dumpClassAttributes();
    out.close();
}
