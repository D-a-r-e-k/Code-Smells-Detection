public ObjectInputStreamSupport(InputStream is, ClassLoader loader) throws Exception {
    super(is);
    this.loader = loader;
}
