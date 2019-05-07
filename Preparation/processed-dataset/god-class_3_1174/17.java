public boolean isExtension() {
    final String namespace = _fname.getNamespace();
    return (namespace != null) && (namespace.equals(EXT_XSLTC));
}
