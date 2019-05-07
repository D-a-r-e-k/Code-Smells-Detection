protected Class resolveClass(ObjectStreamClass os) throws IOException, ClassNotFoundException {
    if (loader == null)
        return super.resolveClass(os);
    else
        return loader.loadClass(os.getName());
}
